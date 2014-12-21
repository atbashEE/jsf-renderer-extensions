package be.rubus.web.jerry.config.logging;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Methods annotated with ConfigEntry will be shown in the log file during startup of the container.
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface ConfigEntry {
    /**
     * Define the value for the log entry because the method can't or shouldn't be executed just for logging purposes.
     *
     * @return Array of String values which are used to print out in the log.
     */
    String[] value() default {};

    /**
     * Special  case of the value attribute where the method returns a class.
     *
     * @return Type to be logged
     */
    Class<?> classResult() default Void.class;
}
