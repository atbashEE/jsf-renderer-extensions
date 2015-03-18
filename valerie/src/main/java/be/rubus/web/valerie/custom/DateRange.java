package be.rubus.web.valerie.custom;

import be.rubus.web.valerie.recording.RecordValue;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 */

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {DateRangeValidator.class})
@RecordValue
@Documented
public @interface DateRange {
    String start();

    String end();

    String message() default "{end} should be later than {start}";

    Class[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
