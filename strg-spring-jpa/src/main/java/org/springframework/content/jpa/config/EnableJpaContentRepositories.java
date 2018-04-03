package org.springframework.content.jpa.config;

import internal.org.springframework.content.jpa.config.JpaContentRepositoriesRegistrar;
import internal.org.springframework.content.jpa.config.JpaStoreConfiguration;
import internal.org.springframework.content.jpa.config.JpaStoreFactoryBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({JpaContentRepositoriesRegistrar.class, JpaStoreConfiguration.class})
public @interface EnableJpaContentRepositories {


	/**
	 * Alias for the {@link #basePackages()} attribute. Allows for more concise annotation declarations e.g.:
	 * {@code @EnableJpaRepositories("org.my.pkg")} instead of {@code @EnableJpaRepositories(basePackages="org.my.pkg")}.
	 * 
	 * @return base packages
	 */
	String[] value() default {};

	/**
	 * Base packages to scan for annotated components. {@link #value()} is an alias for (and mutually exclusive with) this
	 * attribute. Use {@link #basePackageClasses()} for a type-safe alternative to String-based package names.
	 * 
	 * @return base packages
	 */
	String[] basePackages() default {};

	/**
	 * Type-safe alternative to {@link #basePackages()} for specifying the packages to scan for annotated components. The
	 * package of each class specified will be scanned. Consider creating a special no-op marker class or interface in
	 * each package that serves no purpose other than being referenced by this attribute.
	 * 
	 * @return base package classes
	 */
	Class<?>[] basePackageClasses() default {};

	/**
	 * Returns the {@link FactoryBean} class to be used for each repository instance. Defaults to
	 * {@link JpaStoreFactoryBean}.
	 *
	 * @return jpa content repository factory bean
	 */
	Class<?> storeFactoryBeanClass() default JpaStoreFactoryBean.class;

}
