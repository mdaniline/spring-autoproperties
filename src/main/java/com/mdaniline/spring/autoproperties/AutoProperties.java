package com.mdaniline.spring.autoproperties;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated interface will be auto-implemented by looking at all getter methods
 * which are annotated with an {@link org.springframework.beans.factory.annotation.Value @Value} annotation.
 *
 * This functionality requires the annotated interface to be in a package that is defined in the
 * {@link EnableAutoProperties @EnableAutoProperties} configuration.
 * @since 1.0
 * @see EnableAutoProperties
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoProperties {
}
