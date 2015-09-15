/*
 * Copyright 2014-2015 Rudy De Busscher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
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
    private boolean equalsAllowed;

    public void initialize(DateRange dateRange) {
        start = dateRange.start();
        end = dateRange.end();
        equalsAllowed = dateRange.equalsAllowed();
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
                if (!result && equalsAllowed) {
                    result = endDate.equals(startDate);
                }
            }
        } catch (Throwable throwable) {
            // TODO proper logging
            throwable.printStackTrace();
            result = false;
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
        } catch (NoSuchMethodException | IllegalAccessException e) {
            // TODO proper logging
            e.printStackTrace();
        }
        return null;
    }
}
