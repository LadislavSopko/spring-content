package it.zeroics.strg.renditions.providers;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.JustBeforeEach;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.io.InputStream;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.content.commons.renditions.RenditionCapability;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.github.paulcwarren.ginkgo4j.Ginkgo4jConfiguration;
import com.github.paulcwarren.ginkgo4j.Ginkgo4jRunner;

import it.zeroics.strg.renditions.RenditionsProperties;
import it.zeroics.strg.renditions.utils.MimeHelper;

@RunWith(Ginkgo4jRunner.class)
@Ginkgo4jConfiguration(threads=1)
public class AnyImage2AnyTest {
	private static final Log LOGGER = LogFactory.getLog(AnyImage2AnyTest.class);

    private AnyImage2Any provider;

    public ExpectedException thrown = ExpectedException.none();

    {
        Describe("RenditionProviders: AnyImage2Any", () -> {
            JustBeforeEach(() -> {
                provider = new AnyImage2Any();
            });
			Context("#deprecated", () -> {
				It("should throw exception", () -> {
					assertThat(RendererTest.testDeprecated(provider), is(true)) ;
				});
			});
			Context("#isCapable", () -> {
				It("Must be capable only in some cases", () -> {
					MimeHelper mh = new MimeHelper(MimeHelper.METADATA_MIMETYPE);
					assertThat(provider.consumes("what/the_fuck"), is(false)) ;
					assertThat(provider.isCapable("what/the_fuck", mh.toString()).isBetterThan(RenditionCapability.NOT_CAPABLE), is(false));
					assertThat(provider.consumes("image/dicom"), is(true)) ;
					assertThat(provider.isCapable("image/dicom", mh.toString()).isBetterThan(RenditionCapability.NOT_CAPABLE), is(false));
					assertThat(provider.isCapable("image/dicom", mh.toString()).isBest(), is(false));
					assertThat(provider.consumes(MimeHelper.CAPABILITY_MIMETYPE), is(false)) ;
					assertThat(provider.isCapable("image/jpg", MimeHelper.CAPABILITY_MIMETYPE).isBetterThan(RenditionCapability.NOT_CAPABLE), is(false));
				});
				It("Must produce something expected", () -> {
					assertThat(Arrays.asList(provider.produces()).contains(MimeHelper.METADATA_MIMETYPE), is(false)) ;
					assertThat(Arrays.asList(provider.produces()).contains("text/plain"), is(false)) ;
					assertThat(Arrays.asList(provider.produces()).contains("image/dicom"), is(false)) ;
					assertThat(Arrays.asList(provider.produces()).contains("image/jpg"), is(true)) ;
					assertThat(Arrays.asList(provider.produces()).contains("image/png"), is(true)) ;
					assertThat(Arrays.asList(provider.produces()).contains("image/gif"), is(true)) ;
					assertThat(Arrays.asList(provider.produces()).contains("image/tif"), is(true)) ;
					assertThat(Arrays.asList(provider.produces()).contains("image/x-tif"), is(true)) ;
					assertThat(Arrays.asList(provider.produces()).contains("image/x-rgb"), is(true)) ;
					assertThat(Arrays.asList(provider.produces()).contains("image/x-windows-bmp"), is(true)) ;
					assertThat(Arrays.asList(provider.produces()).contains("image/bmp"), is(true)) ;
					assertThat(Arrays.asList(provider.produces()).contains("image/x-portable-bitmap"), is(true)) ;
					assertThat(Arrays.asList(provider.produces()).contains("image/x-icon"), is(true)) ;
				});
			});
			Context("#convert", () -> {
				Context("#image to image", () -> {
					It("Must convert jpg to png", () -> {
						AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
						context.register(TestConfig.class, RenditionsProperties.class);
						context.refresh();
						RendererTest c = new RendererTest();
						MimeHelper mh = new MimeHelper("image/png");
						// Can't compare png. Same image can be compressed or not and a data withing changes. Must compare metadata.
						InputStream pngStream = c.callConverterFromFileName("sample-image.jpg","image/jpg", mh.toString(), provider);
						// assertThat(pngStream, is(not(nullValue())));

						MostAny2Txt metaProvider = new MostAny2Txt() ;
						/* Image conversion from jpg to png differs from the png itself. The resulting png contains the same image
						 * but format differs and an internal timestamp too. 
						 * Must match something else, as basic metadata 
						 */
						assertThat(c.compareAsString(
								c.callConverterFromInputStream(pngStream, "sample-image.jpg.png", "image/png", MimeHelper.METADATA_MIMETYPE, metaProvider), 
								"sample-image.png.json"), is(true));
						context.close();
					});
					It("Must convert png to jpg", () -> {
						AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
						context.register(TestConfig.class, RenditionsProperties.class);
						context.refresh();
						RendererTest c = new RendererTest();
						MimeHelper mh = new MimeHelper("image/jpg");
						assertThat(c.compareAsByteArray(
								c.callConverterFromFileName("sample-image.png","image/png", mh.toString(), provider),
								"sample-image.jpg"), is(true));
						context.close();
					});
				});
			});
        });
    }

	@PropertySource("classpath:/test.properties")
	@Configuration
	@AutoConfigurationPackage
	@EnableAutoConfiguration
	public static class TestConfig {
	}
}
