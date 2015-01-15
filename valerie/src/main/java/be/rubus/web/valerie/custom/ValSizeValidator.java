package be.rubus.web.valerie.custom;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 */
public class ValSizeValidator implements ConstraintValidator<ValSize, Object> {
    private ValSize sizeAnnotation;

    @Override
    public void initialize(ValSize constraintAnnotation) {

        sizeAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        boolean result = value != null;
        if (result) {
            String data = value.toString();
            int length = data.length();
            if (length < sizeAnnotation.min() || length > sizeAnnotation.max()) {
                result = false;
            }
        }
        return result;
    }
}
