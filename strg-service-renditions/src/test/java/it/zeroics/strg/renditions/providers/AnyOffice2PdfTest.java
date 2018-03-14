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
import org.springframework.util.MimeType;

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
public class AnyOffice2PdfTest {
	private static final Log LOGGER = LogFactory.getLog(AnyOffice2PdfTest.class);

    private AnyOffice2Pdf provider;
    private Throwable t;
    
    public ExpectedException thrown = ExpectedException.none();

    {
        Describe("RenditionProviders: AntOffice2Pdf", () -> {
            JustBeforeEach(() -> {
                provider = new AnyOffice2Pdf();
            });
			Context("#deprecated", () -> {
				It("should throw exception", () -> {
					assertThat(RendererTest.testDeprecated(provider), is(true)) ;
				});
			});
			Context("#isCapable", () -> {
				It("Must be capable only in some cases", () -> {
					assertThat(provider.isCapable("what/the_fuck", "text/plain").isBest(), is(false));
					assertThat(provider.isCapable("what/the_fuck", "application/pdf"), is(RenditionCapability.NOT_CAPABLE));
					assertThat(provider.isCapable("what/the_fuck", "application/json;meta=true"), is(RenditionCapability.NOT_CAPABLE));
					assertThat(provider.isCapable("image/dicom", "application/pdf"), is(RenditionCapability.NOT_CAPABLE));
					assertThat(provider.isCapable("application/msword", "application/pdf").isBetterThan(RenditionCapability.NOT_CAPABLE), is(true));
					assertThat(provider.isCapable("application/vnd.ms-powerpoint", "application/pdf").isBetterThan(RenditionCapability.NOT_CAPABLE), is(true));
					assertThat(provider.isCapable("application/vnd.ms-excel", "application/pdf").isBetterThan(RenditionCapability.NOT_CAPABLE), is(true));
					assertThat(provider.isCapable("application/vnd.oasis.opendocument.text", "application/pdf").isBetterThan(RenditionCapability.NOT_CAPABLE), is(true));
					assertThat(provider.isCapable("application/vnd.oasis.opendocument.spreadsheet", "application/pdf").isBetterThan(RenditionCapability.NOT_CAPABLE), is(true));
					assertThat(provider.isCapable("application/vnd.oasis.opendocument.presentation", "application/pdf").isBetterThan(RenditionCapability.NOT_CAPABLE), is(true));
					assertThat(provider.isCapable("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/pdf").isBetterThan(RenditionCapability.NOT_CAPABLE), is(true));
					assertThat(provider.isCapable("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/pdf").isBetterThan(RenditionCapability.NOT_CAPABLE), is(true));
					assertThat(provider.isCapable("application/vnd.openxmlformats-officedocument.presentationml.presentation", "application/pdf").isBetterThan(RenditionCapability.NOT_CAPABLE), is(true));
					assertThat(provider.isCapable("application/vnd.openxmlformats-officedocument.presentationml.slideshow", "application/pdf").isBetterThan(RenditionCapability.NOT_CAPABLE), is(true));
				});
			});
			Context("#convert", () -> {
				Context("#docx to pdf", () -> {
					It("Must convert docx to pdf", () -> {
						RendererTest c = new RendererTest();
						InputStream pdfStream = c.callConverterFromFileName("sample-docx.docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document","application/pdf", provider);
						assertThat(pdfStream, is(not(nullValue())));

						MostAny2Txt txtProvider = new MostAny2Txt() ;
						/* Text extraction from PDF differs from text extraction from doc converted to PDF.
						 * Must use a different pattern file 
						 */
						assertThat(c.compareAsByteArray(
								c.callConverterFromInputStream(pdfStream, "sample-doc.docx.pdf", "application/pdf","text/plain", txtProvider), 
								"sample-docx.docx.pdf.txt"), is(true));
					});
				});
			});
        });
    }
}
