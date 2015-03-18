package be.rubus.web.valerie.custom;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Date;

/**
 *
 */
public class DateRangeValidator implements ConstraintValidator<DateRange, Object> {

    private static final MethodType DATE_GETTER = MethodType.methodType(Date.class);

    private String start;
    private String end;

    public void initialize(DateRange dateRange) {
        start = dateRange.start();
        end = dateRange.end();
    }

    public boolean isValid(Object object,
                           ConstraintValidatorContext constraintValidatorContext) {
        boolean result = true;
        Class clazz = object.getClass();

        MethodHandle handleStart = getHandle(clazz, start);
        MethodHandle handleEnd = getHandle(clazz, end);
        try {
            Date startDate = (Date) handleStart.invokeWithArguments(object);
            Date endDate = (Date) handleEnd.invokeWithArguments(object);
            if (startDate != null && endDate != null) {
                result = endDate.after(startDate);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }


        return result;
    }

    private String getAccessorMethodName(String property) {
        StringBuilder builder = new StringBuilder("get");
        builder.append(Character.toUpperCase(property.charAt(0)));
        builder.append(property.substring(1));
        return builder.toString();
    }

    private MethodHandle getHandle(Class<?> target, String property) {

        try {
            return MethodHandles.lookup().findVirtual(target, getAccessorMethodName(property), DATE_GETTER);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
