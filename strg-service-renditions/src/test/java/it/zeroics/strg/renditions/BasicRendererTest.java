package it.zeroics.strg.renditions;

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
public class BasicRendererTest {
	private static final Log LOGGER = LogFactory.getLog(BasicRendererTest.class);

    {
        Describe("BasicRenderer", () -> {
			Context("#Match Meta", () -> {
				It("Must match request of metadata", () -> {
					try {
						BasicRenderer.justMeta(MimeType.valueOf("")) ;
						fail("Invalid meta is recognized as good");
					}
					catch(Exception e) {
						assertThat(e , instanceOf(Exception.class));
					}
					
					assertThat(BasicRenderer.justMeta(MimeType.valueOf("application/json")) , is(false));
					assertThat(BasicRenderer.justMeta(MimeType.valueOf("this/that")) , is(false));
					assertThat(BasicRenderer.justMeta(MimeType.valueOf("this/that;meta=true")) , is(false));
					assertThat(BasicRenderer.justMeta(MimeType.valueOf("application/pdf;meta=true")) , is(false));

					assertThat(BasicRenderer.justMeta(MimeType.valueOf("application/json;meta=true")) , is(true));
				});
			});
        });
    }
}
