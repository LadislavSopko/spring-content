package it.zeroics.strg.renditions.providers;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.JustBeforeEach;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.content.commons.renditions.RenditionCapability;
import org.springframework.content.commons.renditions.RenditionProvider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.paulcwarren.ginkgo4j.Ginkgo4jConfiguration;
import com.github.paulcwarren.ginkgo4j.Ginkgo4jRunner;

import internal.org.springframework.content.commons.renditions.RenditionServiceImpl;
import it.zeroics.strg.renditions.providers.testsupport.RendererTest;
import it.zeroics.strg.renditions.utils.MimeHelper;

@RunWith(Ginkgo4jRunner.class)
@Ginkgo4jConfiguration(threads=1)
public class Any2ConversionsTest {
	private static final Log LOGGER = LogFactory.getLog(Any2ConversionsTest.class);

    private Any2Conversions provider;
	private RenditionServiceImpl renditionService;
    private Throwable t;
    
    public ExpectedException thrown = ExpectedException.none();

    {
        Describe("RenditionProviders: AntOffice2Pdf", () -> {
            JustBeforeEach(() -> {
                provider = new Any2Conversions();
            });
			Context("#deprecated", () -> {
				It("should throw exception", () -> {
					assertThat(RendererTest.testDeprecated(provider), is(true)) ;
				});
			});
			Context("#isCapable", () -> {
				It("Must be capable only in some cases", () -> {
					assertThat(provider.consumes(MimeHelper.CAPABILITY_MIMETYPE), is(false)) ;
					assertThat(provider.isCapable("application/vnd.oasis.opendocument.text", MimeHelper.CAPABILITY_MIMETYPE).isBetterThan(RenditionCapability.NOT_CAPABLE), is(true));
					assertThat(provider.isCapable("application/vnd.oasis.opendocument.text", MimeHelper.CAPABILITY_MIMETYPE).isBest(), is(true));

					assertThat(provider.consumes("what/the_fuck"), is(true)) ;
					assertThat(provider.isCapable("what/the_fuck", "text/plain").isBest(), is(false));
					assertThat(provider.isCapable("what/the_fuck", "application/pdf"), is(RenditionCapability.NOT_CAPABLE));
					MimeHelper mh = new MimeHelper(MimeHelper.METADATA_MIMETYPE);
					assertThat(provider.isCapable("what/the_fuck", mh.toString()), is(RenditionCapability.NOT_CAPABLE));
					assertThat(provider.consumes("image/dicom"), is(true)) ;
					assertThat(provider.isCapable("image/dicom", "application/pdf"), is(RenditionCapability.NOT_CAPABLE));
					assertThat(provider.consumes("application/msword"), is(true)) ;
					assertThat(provider.isCapable("application/msword", "application/pdf"), is(RenditionCapability.NOT_CAPABLE));
					assertThat(provider.consumes("application/vnd.ms-powerpoint"), is(true)) ;
					assertThat(provider.isCapable("application/vnd.ms-powerpoint", "application/pdf"), is(RenditionCapability.NOT_CAPABLE));
					assertThat(provider.consumes("application/vnd.ms-excel"), is(true)) ;
					assertThat(provider.isCapable("application/vnd.ms-excel", "application/pdf"), is(RenditionCapability.NOT_CAPABLE));
					assertThat(provider.consumes("application/vnd.oasis.opendocument.text"), is(true)) ;
					assertThat(provider.isCapable("application/vnd.oasis.opendocument.text", "application/pdf"), is(RenditionCapability.NOT_CAPABLE));
					assertThat(provider.consumes("application/vnd.oasis.opendocument.spreadsheet"), is(true)) ;
					assertThat(provider.isCapable("application/vnd.oasis.opendocument.spreadsheet", "application/pdf"), is(RenditionCapability.NOT_CAPABLE));
					assertThat(provider.consumes("application/vnd.oasis.opendocument.presentation"), is(true)) ;
					assertThat(provider.isCapable("application/vnd.oasis.opendocument.presentation", "application/pdf"), is(RenditionCapability.NOT_CAPABLE));
					assertThat(provider.consumes("application/vnd.openxmlformats-officedocument.wordprocessingml.document"), is(true)) ;
					assertThat(provider.isCapable("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/pdf"), is(RenditionCapability.NOT_CAPABLE));
					assertThat(provider.consumes("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"), is(true)) ;
					assertThat(provider.isCapable("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/pdf"), is(RenditionCapability.NOT_CAPABLE));
					assertThat(provider.consumes("application/vnd.openxmlformats-officedocument.presentationml.presentation"), is(true)) ;
					assertThat(provider.isCapable("application/vnd.openxmlformats-officedocument.presentationml.presentation", "application/pdf"), is(RenditionCapability.NOT_CAPABLE));
					assertThat(provider.consumes("application/vnd.openxmlformats-officedocument.presentationml.slideshow"), is(true)) ;
					assertThat(provider.isCapable("application/vnd.openxmlformats-officedocument.presentationml.slideshow", "application/pdf"), is(RenditionCapability.NOT_CAPABLE));
					
				});
				It("Must produce something expected", () -> {
					assertThat(Arrays.asList(provider.produces()).contains(MimeHelper.CAPABILITY_MIMETYPE), is(true)) ;
					assertThat(Arrays.asList(provider.produces()).contains("application/pdf"), is(false)) ;
					assertThat(Arrays.asList(provider.produces()).contains("text/plain"), is(false)) ;
					assertThat(Arrays.asList(provider.produces()).contains("image/dicom"), is(false)) ;
				});
			});
			Context("#convert", () -> {
				BeforeEach(() -> {
					renditionService = new RenditionServiceImpl();
					RenditionProvider provider = spy(new AnyOffice2Pdf());
					renditionService.setProviders(provider);
					provider = spy(new MostAny2Txt());
					renditionService.setProviders(provider);
					//provider = spy(new Dicom2Any());
					//renditionService.setProviders(provider);
					provider = spy(new AnyImage2Any());
					renditionService.setProviders(provider);
					provider = spy(new Any2Conversions());
					renditionService.setProviders(provider);
				});
				Context("#get service capabilities", () -> {
					It("Capabilityes for docx", () -> {
						RendererTest c = new RendererTest();
						/* Text extraction from PDF differs from text extraction from doc converted to PDF.
						 * Must use a different pattern file 
						 */
						ObjectMapper om = new ObjectMapper();
						
						JsonNode jn = om.readTree(c.callConverterFromFileName("sample-docx.docx.pdf", "application/pdf", MimeHelper.CAPABILITY_MIMETYPE, provider));
						Iterator<JsonNode> ijn  = jn.elements();
						ArrayList<String> capabilities = new ArrayList<String>() ;
						while (ijn.hasNext()) {
							JsonNode vjn = ijn.next();
							capabilities.add(vjn.textValue());
						}
						assertThat(capabilities.contains("text/plain"), is(true)) ;
					});
				});
			});
        });
    }
}
