package it.zeroics.strg.renditions.dicom;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.runner.RunWith;
import org.springframework.util.MimeType;

import com.github.paulcwarren.ginkgo4j.Ginkgo4jConfiguration;
import com.github.paulcwarren.ginkgo4j.Ginkgo4jRunner;

import it.zeroics.strg.renditions.utils.MimeHelper;

@RunWith(Ginkgo4jRunner.class)
@Ginkgo4jConfiguration(threads = 1)
public class BasicRendererTest {
	private static final Log LOGGER = LogFactory.getLog(BasicRendererTest.class);

	{
		Describe("BasicRenderer", () -> {
			Context("#Match Meta", () -> {
				It("Must match request of metadata", () -> {
					try {
						MimeHelper.isMeta(MimeType.valueOf(""));
						fail("Invalid meta is recognized as good");
					} catch (Exception e) {
						assertThat(e, instanceOf(Exception.class));
					}

					assertThat(MimeHelper.isMeta(MimeType.valueOf("this/that")), is(false));
					assertThat(MimeHelper.isMeta(MimeType.valueOf("this/that;meta=true")), is(false));
					assertThat(MimeHelper.isMeta(MimeType.valueOf("application/pdf;meta=true")), is(false));

					assertThat(MimeHelper.isMeta(MimeType.valueOf("application/json")), is(false));
					assertThat(MimeHelper.isMeta(MimeType.valueOf("application/it.zeroics.meta")), is(true));
				});
			});
		});
	}
}
