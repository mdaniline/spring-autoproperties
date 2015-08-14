package com.mdaniline.spring.autoproperties;

import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables automatic proxying of interfaces annotated with {@link AutoProperties @AutoProperties}.
 *
 * This interface should be applied to a Spring configuration class annotated with
 * {@link org.springframework.context.annotation.Configuration @Configuration}
 *
 * @since 1.0
 * @see AutoProperties
 * @see org.springframework.context.annotation.Configuration
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AutoPropertiesRegistrar.class)
public @interface EnableAutoProperties {

    @AliasFor("basePackages")
    String[] value() default {};

    @AliasFor("value")
    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};
}
