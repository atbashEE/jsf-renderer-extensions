package be.rubus.web.jerry.example.validation.metadata.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 *
 */
@NotNull
@Size(min = 2, max = 14)
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface CombinedValidation {

    String message() default "Must be between 2 and 14 characters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
