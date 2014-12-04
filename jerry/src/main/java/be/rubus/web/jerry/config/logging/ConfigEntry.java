package be.rubus.web.jerry.config.logging;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface ConfigEntry {
    String[] value() default {};

    Class<?> classResult() default Void.class;
}
