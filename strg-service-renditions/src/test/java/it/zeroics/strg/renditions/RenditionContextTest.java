package it.zeroics.strg.renditions;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.runner.RunWith;

import com.github.paulcwarren.ginkgo4j.Ginkgo4jConfiguration;
import com.github.paulcwarren.ginkgo4j.Ginkgo4jRunner;

import internal.org.springframework.content.commons.renditions.RenditionContext;

@RunWith(Ginkgo4jRunner.class)
@Ginkgo4jConfiguration(threads = 1)
public class RenditionContextTest {
	private static final Log LOGGER = LogFactory.getLog(RenditionContextTest.class);

	{
		Describe("Context", () -> {
			Context("#getInstance", () -> {
				It("Must obtain a valid instance", () -> {
					assertThat(RenditionContext.getInstance(), instanceOf(RenditionContext.class));
				});
			});
		});
	}
}
