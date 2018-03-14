package it.zeroics.strg.renditions.utils;

import com.github.paulcwarren.ginkgo4j.Ginkgo4jConfiguration;
import com.github.paulcwarren.ginkgo4j.Ginkgo4jRunner;
import org.junit.runner.RunWith;

import java.util.Map;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.*;
import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(Ginkgo4jRunner.class)
@Ginkgo4jConfiguration(threads=1)
public class MetadataTest {

    private Metadata meta;

    // private Exception e;

    {
        Describe("Metadata", () -> {
            JustBeforeEach(() -> {
                meta = new Metadata("txt", 1);
            });
            Context("#getMeta", () -> {
                JustBeforeEach(() -> {
                	meta.addMeta("one", "the-First");
                	meta.addMeta("two", "the-Second");
                	meta.addMeta("three", "the-Third");
                });
	            Context("#getMeta", () -> {
	                It("should be able to return a Map", () -> {
	                	Map<String,String> m = meta.getMeta();
	                    assertThat(m.get("one"), is("the-First"));
	                    assertThat(m.get("two"), is("the-Second"));
	                    assertThat(m.get("three"), is("the-Third"));
	                    assertThat(m.get("four"), nullValue());
	                });
	            });
	            Context("#toString", () -> {
	                It("should be able to return a JSON", () -> {
	                	String s = meta.toString();
	                	// Normalize a little.
	                	s = s.replaceAll("\r", "") ;
	                	s = s.replaceAll("\n", "") ;
	                	s = s.replaceAll(" ", "") ;
	                	assertThat(s, is("{\"one\":\"the-First\",\"two\":\"the-Second\",\"three\":\"the-Third\"}")) ;
	                });
	            });
            });
        });
    }
}
