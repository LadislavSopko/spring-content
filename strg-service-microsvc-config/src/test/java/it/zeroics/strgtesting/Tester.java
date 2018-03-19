package it.zeroics.strgtesting;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;

import org.junit.runner.RunWith;

import com.github.paulcwarren.ginkgo4j.Ginkgo4jRunner;

@RunWith(Ginkgo4jRunner.class)
public class Tester {

	{
		Describe("Tester", () -> {
			Context("Test", () -> {
				BeforeEach(() -> {
					
				});
				
				It("Ok", () -> {
					
				});
			});
		});
	}
}
