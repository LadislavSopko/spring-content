package it.zeroics.strg.renditions.providers;

import com.github.paulcwarren.ginkgo4j.Ginkgo4jConfiguration;
import com.github.paulcwarren.ginkgo4j.Ginkgo4jRunner;

import internal.org.springframework.content.commons.renditions.RenditionServiceImpl;
import internal.org.springframework.content.commons.utils.InputContentStream;
import it.zeroics.strg.model.Medium;

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
					assertThat(RendererTest.testDeprecated(provider), is(true)) ;
				});
			});
			Context("#isCapable", () -> {
				It("Must be capable only in some cases", () -> {
					assertThat(provider.isCapable("what/the_fuck", "text/plain").isBest(), is(true));
					assertThat(provider.isCapable("what/the_fuck", "application/json;meta=true").isBetterThan(RenditionCapability.NOT_CAPABLE), is(true));
					assertThat(provider.isCapable("what/the_fuck", "application/json;meta=true").isBest(), is(false));
					assertThat(provider.isCapable("image/dicom", "application/json;meta=true"), is(RenditionCapability.NOT_CAPABLE));
				});
			});
			Context("#convert", () -> {
				Context("#docx to text", () -> {
					It("Must convert docx to test", () -> {
						RendererTest c = new RendererTest();
						assertThat(c.compareAsString(
								c.callConverterFromFileName("sample-docx.docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document","text/plain", provider), 
								"sample-docx.docx.txt"), is(true));
					});
				});
				Context("#docx to meta", () -> {
					It("Must convert docx to json", () -> {
						RendererTest c = new RendererTest();
						assertThat(c.compareAsString(
								c.callConverterFromFileName("sample-docx.docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document","application/json;meta=true", provider),
								"sample-docx.docx.meta"), is(true));
					});
				});
			});
        });
    }
}
