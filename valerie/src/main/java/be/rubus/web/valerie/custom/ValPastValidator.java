package be.rubus.web.valerie.custom;

import be.rubus.web.valerie.provider.DateProducer;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

/**
 *
 */
public class ValPastValidator implements ConstraintValidator<ValPast, Date> {
    @Override
    public void initialize(ValPast constraintAnnotation) {

    }

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        boolean result = value != null;
        if (result) {
            Date now = DateProducer.now();
            result = value.before(now);
        }
        return result;
    }
}
