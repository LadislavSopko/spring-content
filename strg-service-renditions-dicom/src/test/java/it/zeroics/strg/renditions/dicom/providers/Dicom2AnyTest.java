package it.zeroics.strg.renditions.dicom.providers;

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

import it.zeroics.strg.renditions.utils.MimeHelper;

@RunWith(Ginkgo4jRunner.class)
@Ginkgo4jConfiguration(threads = 1)
public class Dicom2AnyTest {
	private static final Log LOGGER = LogFactory.getLog(Dicom2AnyTest.class);

	private Dicom2Any provider;

	public ExpectedException thrown = ExpectedException.none();

	{
		Describe("RenditionProviders: Dicom2Any", () -> {
			JustBeforeEach(() -> {
				provider = new Dicom2Any();
			});
			Context("#deprecated", () -> {
				It("should throw exception", () -> {
					assertThat(RendererTest.testDeprecated(provider), is(true));
				});
			});
			Context("#isCapable", () -> {
				It("Must be capable only in some cases", () -> {
					MimeHelper mh = new MimeHelper(MimeHelper.METADATA_MIMETYPE);
					assertThat(provider.consumes("what/the_fuck"), is(false));
					assertThat(provider.isCapable("what/the_fuck", mh.toString())
							.isBetterThan(RenditionCapability.NOT_CAPABLE), is(false));
					assertThat(provider.consumes("image/dicom"), is(true));
					assertThat(provider.isCapable("image/dicom", mh.toString())
							.isBetterThan(RenditionCapability.NOT_CAPABLE), is(true));
					assertThat(provider.isCapable("image/dicom", mh.toString()).isBest(), is(true));
					assertThat(provider.consumes(MimeHelper.CAPABILITY_MIMETYPE), is(false));
					assertThat(provider.isCapable("image/dicom", MimeHelper.CAPABILITY_MIMETYPE)
							.isBetterThan(RenditionCapability.NOT_CAPABLE), is(false));
				});
				It("Must produce something expected", () -> {
					assertThat(Arrays.asList(provider.produces()).contains(MimeHelper.METADATA_MIMETYPE), is(true));
					assertThat(Arrays.asList(provider.produces()).contains("text/plain"), is(false));
					assertThat(Arrays.asList(provider.produces()).contains("image/dicom"), is(false));
				});
			});
			Context("#convert", () -> {
				Context("#dcm to meta", () -> {
					It("Must convert single frame dcm to json", () -> {
						RendererTest c = new RendererTest();
						MimeHelper mh = new MimeHelper(MimeHelper.METADATA_MIMETYPE);
						assertThat(
								c.compareAsString(c.callConverterFromFileName("sample-singleframe-dcm.dcm",
										"image/dicom", mh.toString(), provider), "sample-singleframe-dcm.dcm.meta"),
								is(true));
					});
					It("Must convert multi frame dcm to json", () -> {
						RendererTest c = new RendererTest();
						MimeHelper mh = new MimeHelper(MimeHelper.METADATA_MIMETYPE);
						assertThat(
								c.compareAsString(c.callConverterFromFileName("sample-multiframe-dcm.dcm",
										"image/dicom", mh.toString(), provider), "sample-multiframe-dcm.dcm.meta"),
								is(true));
					});
				});
			});
		});
	}
}
