package it.zeroics.strg.model;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.annotation.Annotation;

import org.junit.runner.RunWith;
import org.springframework.content.commons.annotations.ContentId;
import org.springframework.content.commons.annotations.ContentLength;
import org.springframework.content.commons.annotations.ContentName;
import org.springframework.content.commons.annotations.MimeType;
import org.springframework.content.commons.utils.BeanUtils;
import org.springframework.data.mongodb.core.mapping.Document;

import com.github.paulcwarren.ginkgo4j.Ginkgo4jRunner;


@RunWith(Ginkgo4jRunner.class)
public class MediumTest {

	private Medium m = null;
	
	{
		Describe("Medium test", () -> {
			Context("medium must have specific anontated state", () -> {
				BeforeEach(() -> {
					m = new Medium();
				});
				It("should be @Document of [media-meta] collection",() -> {
					Document a = Medium.class.getAnnotation(Document.class);
					assertThat(a, is(notNullValue()));	
					assertThat(a.collection(), is("media-meta"));
				});
				
				It("must have @Id field", () -> {
					assertThat(BeanUtils.findFieldWithAnnotation(m, org.springframework.data.annotation.Id.class), is(notNullValue()));
				});
				
				It("must have @ContentName field", () -> {
					assertThat(BeanUtils.findFieldWithAnnotation(m, ContentName.class), is(notNullValue()));
				});
				
				It("must have @ContentId field", () -> {
					assertThat(BeanUtils.findFieldWithAnnotation(m, ContentId.class), is(notNullValue()));
				});
				
				It("must have @ContentLength field", () -> {
					assertThat(BeanUtils.findFieldWithAnnotation(m, ContentLength.class), is(notNullValue()));
				});
				
				It("must have @MimeType field", () -> {
					assertThat(BeanUtils.findFieldWithAnnotation(m, MimeType.class), is(notNullValue()));
				});
			});
		});
	}
}
