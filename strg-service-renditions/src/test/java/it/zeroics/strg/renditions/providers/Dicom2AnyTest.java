package it.zeroics.strg.renditions.providers;

import com.github.paulcwarren.ginkgo4j.Ginkgo4jConfiguration;
import com.github.paulcwarren.ginkgo4j.Ginkgo4jRunner;

import internal.org.springframework.content.commons.renditions.RenditionServiceImpl;
import internal.org.springframework.content.commons.utils.InputContentStream;
import it.zeroics.strg.model.Medium;
import it.zeroics.strg.renditions.utils.MimeHelper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.content.commons.renditions.RenditionCapability;
import org.springframework.content.commons.renditions.RenditionProvider;

import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.*;
import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(Ginkgo4jRunner.class)
@Ginkgo4jConfiguration(threads=1)
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
					assertThat(RendererTest.testDeprecated(provider), is(true)) ;
				});
			});
			Context("#isCapable", () -> {
				It("Must be capable only in some cases", () -> {
					MimeHelper mh = new MimeHelper(MimeHelper.METADATA_MIMETYPE);
					assertThat(provider.isCapable("what/the_fuck", mh.toString()).isBetterThan(RenditionCapability.NOT_CAPABLE), is(false));
					assertThat(provider.isCapable("image/dicom", mh.toString()).isBetterThan(RenditionCapability.NOT_CAPABLE), is(true));
					assertThat(provider.isCapable("image/dicom", mh.toString()).isBest(), is(true));
				});
			});
			Context("#convert", () -> {
				Context("#dcm to meta", () -> {
					It("Must convert single frame dcm to json", () -> {
						RendererTest c = new RendererTest();
						MimeHelper mh = new MimeHelper(MimeHelper.METADATA_MIMETYPE);
						assertThat(c.compareAsString(
								c.callConverterFromFileName("sample-singleframe-dcm.dcm","image/dicom", mh.toString(), provider),
								"sample-singleframe-dcm.dcm.meta"), is(true));
					});
					It("Must convert multi frame dcm to json", () -> {
						RendererTest c = new RendererTest();
						MimeHelper mh = new MimeHelper(MimeHelper.METADATA_MIMETYPE);
						assertThat(c.compareAsString(
								c.callConverterFromFileName("sample-multiframe-dcm.dcm","image/dicom",mh.toString(), provider),
								"sample-multiframe-dcm.dcm.meta"), is(true));
					});
				});
			});
        });
    }
}
