package be.rubus.web.valerie.custom;

import be.rubus.web.valerie.provider.DateProducer;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

/**
 *
 */
public class ValFutureValidator implements ConstraintValidator<ValFuture, Date> {
    @Override
    public void initialize(ValFuture constraintAnnotation) {

    }

    @Override
    public boolean isValid(Date value, ConstraintValidatorContext context) {
        boolean result = value != null;
        if (result) {
            Date now = DateProducer.now();
            result = value.after(now);
        }
        return result;
    }
}
