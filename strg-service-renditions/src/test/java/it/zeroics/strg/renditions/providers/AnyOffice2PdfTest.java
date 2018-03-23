package it.zeroics.strg.renditions.providers;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.JustBeforeEach;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.io.InputStream;
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
					assertThat(provider.consumes("what/the_fuck"), is(false)) ;
					assertThat(provider.isCapable("what/the_fuck", "text/plain").isBest(), is(false));
					assertThat(provider.isCapable("what/the_fuck", "application/pdf"), is(RenditionCapability.NOT_CAPABLE));
					MimeHelper mh = new MimeHelper(MimeHelper.METADATA_MIMETYPE);
					assertThat(provider.isCapable("what/the_fuck", mh.toString()), is(RenditionCapability.NOT_CAPABLE));
					assertThat(provider.consumes("image/dicom"), is(false)) ;
					assertThat(provider.isCapable("image/dicom", "application/pdf"), is(RenditionCapability.NOT_CAPABLE));
					assertThat(provider.consumes("application/msword"), is(true)) ;
					assertThat(provider.isCapable("application/msword", "application/pdf").isBetterThan(RenditionCapability.NOT_CAPABLE), is(true));
					assertThat(provider.consumes("application/vnd.ms-powerpoint"), is(true)) ;
					assertThat(provider.isCapable("application/vnd.ms-powerpoint", "application/pdf").isBetterThan(RenditionCapability.NOT_CAPABLE), is(true));
					assertThat(provider.consumes("application/vnd.ms-excel"), is(true)) ;
					assertThat(provider.isCapable("application/vnd.ms-excel", "application/pdf").isBetterThan(RenditionCapability.NOT_CAPABLE), is(true));
					assertThat(provider.consumes("application/vnd.oasis.opendocument.text"), is(true)) ;
					assertThat(provider.isCapable("application/vnd.oasis.opendocument.text", "application/pdf").isBetterThan(RenditionCapability.NOT_CAPABLE), is(true));
					assertThat(provider.consumes("application/vnd.oasis.opendocument.spreadsheet"), is(true)) ;
					assertThat(provider.isCapable("application/vnd.oasis.opendocument.spreadsheet", "application/pdf").isBetterThan(RenditionCapability.NOT_CAPABLE), is(true));
					assertThat(provider.consumes("application/vnd.oasis.opendocument.presentation"), is(true)) ;
					assertThat(provider.isCapable("application/vnd.oasis.opendocument.presentation", "application/pdf").isBetterThan(RenditionCapability.NOT_CAPABLE), is(true));
					assertThat(provider.consumes("application/vnd.openxmlformats-officedocument.wordprocessingml.document"), is(true)) ;
					assertThat(provider.isCapable("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/pdf").isBetterThan(RenditionCapability.NOT_CAPABLE), is(true));
					assertThat(provider.consumes("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"), is(true)) ;
					assertThat(provider.isCapable("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/pdf").isBetterThan(RenditionCapability.NOT_CAPABLE), is(true));
					assertThat(provider.consumes("application/vnd.openxmlformats-officedocument.presentationml.presentation"), is(true)) ;
					assertThat(provider.isCapable("application/vnd.openxmlformats-officedocument.presentationml.presentation", "application/pdf").isBetterThan(RenditionCapability.NOT_CAPABLE), is(true));
					assertThat(provider.consumes("application/vnd.openxmlformats-officedocument.presentationml.slideshow"), is(true)) ;
					assertThat(provider.isCapable("application/vnd.openxmlformats-officedocument.presentationml.slideshow", "application/pdf").isBetterThan(RenditionCapability.NOT_CAPABLE), is(true));
					assertThat(provider.consumes(MimeHelper.CAPABILITY_MIMETYPE), is(false)) ;
					assertThat(provider.isCapable("application/vnd.oasis.opendocument.text", MimeHelper.CAPABILITY_MIMETYPE).isBetterThan(RenditionCapability.NOT_CAPABLE), is(false));
				});
				It("Must produce something expected", () -> {
					assertThat(Arrays.asList(provider.produces()).contains("application/pdf"), is(true)) ;
					assertThat(Arrays.asList(provider.produces()).contains("text/plain"), is(false)) ;
					assertThat(Arrays.asList(provider.produces()).contains("image/dicom"), is(false)) ;
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
