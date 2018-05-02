package it.zeroics.strg.renditions.providers;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.JustBeforeEach;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.content.commons.renditions.RenditionCapability;

import com.github.paulcwarren.ginkgo4j.Ginkgo4jConfiguration;
import com.github.paulcwarren.ginkgo4j.Ginkgo4jRunner;

import it.zeroics.strg.renditions.providers.testsupport.RendererTest;
import it.zeroics.strg.renditions.utils.MimeHelper;

@RunWith(Ginkgo4jRunner.class)
@Ginkgo4jConfiguration(threads = 1)
public class MostAny2TxtTest {
	private static final Log LOGGER = LogFactory.getLog(MostAny2TxtTest.class);

	private MostAny2Txt provider;

	public ExpectedException thrown = ExpectedException.none();

	{
		Describe("RenditionProviders: MostAny2Txt", () -> {
			JustBeforeEach(() -> {
				provider = new MostAny2Txt();
			});
			Context("#deprecated", () -> {
				It("should throw exception", () -> {
					assertThat(RendererTest.testDeprecated(provider), is(true));
				});
			});
			Context("#isCapable", () -> {
				It("Must be capable only in some cases", () -> {
					assertThat(provider.consumes("what/the_fuck"), is(true)); // !!!
					assertThat(provider.isCapable("what/the_fuck", "text/plain").isBest(), is(true));
					MimeHelper mh = new MimeHelper(MimeHelper.METADATA_MIMETYPE);
					assertThat(provider.isCapable("what/the_fuck", mh.toString())
							.isBetterThan(RenditionCapability.NOT_CAPABLE), is(true));
					assertThat(provider.isCapable("what/the_fuck", mh.toString()).isBest(), is(false));
					assertThat(provider.consumes("image/dicom"), is(true)); // Comsumes dicom image to extract text but
																			// not metadata
					assertThat(provider.isCapable("image/dicom", mh.toString()), is(RenditionCapability.NOT_CAPABLE));
					assertThat(provider.consumes(MimeHelper.CAPABILITY_MIMETYPE), is(false));
					assertThat(provider.isCapable("application/pdf", MimeHelper.CAPABILITY_MIMETYPE)
							.isBetterThan(RenditionCapability.NOT_CAPABLE), is(false));
				});
				It("Must produce something expected", () -> {
					assertThat(Arrays.asList(provider.produces()).contains("text/plain"), is(true));
					assertThat(Arrays.asList(provider.produces()).contains(MimeHelper.METADATA_MIMETYPE), is(true));
					assertThat(Arrays.asList(provider.produces()).contains("application/pdf"), is(false));
					assertThat(Arrays.asList(provider.produces()).contains("image/dicom"), is(false));
				});
			});
			Context("#convert", () -> {
				Context("#docx to text", () -> {
					It("Must convert docx to test", () -> {
						RendererTest c = new RendererTest();
						assertThat(c.compareAsString(c.callConverterFromFileName("sample-docx.docx",
								"application/vnd.openxmlformats-officedocument.wordprocessingml.document", "text/plain",
								provider), "sample-docx.docx.txt"), is(true));
					});
				});
				Context("#docx to meta", () -> {
					It("Must convert docx to json", () -> {
						RendererTest c = new RendererTest();
						MimeHelper mh = new MimeHelper(MimeHelper.METADATA_MIMETYPE);
						assertThat(c.compareAsString(c.callConverterFromFileName("sample-docx.docx",
								"application/vnd.openxmlformats-officedocument.wordprocessingml.document",
								mh.toString(), provider), "sample-docx.docx.meta"), is(true));
					});
				});
			});
		});
	}
}
