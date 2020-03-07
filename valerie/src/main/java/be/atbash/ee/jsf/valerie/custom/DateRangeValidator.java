/*
 * Copyright 2014-2020 Rudy De Busscher
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
 */
package be.atbash.ee.jsf.valerie.custom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(DateRangeValidator.class);
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
        Class<?> clazz = object.getClass();

        MethodHandle handleStart = getHandle(clazz, start);
        MethodHandle handleEnd = getHandle(clazz, end);
        // FIXME handleStart, handleEnd can be null.
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
            LOGGER.warn("Unexpected exception: ", throwable);
            result = false;
        }

        return result;
    }

    private String getAccessorMethodName(String property) {
        return "get" + Character.toUpperCase(property.charAt(0)) +
                property.substring(1);
    }

    private MethodHandle getHandle(Class<?> target, String property) {

        try {
            return MethodHandles.lookup().findVirtual(target, getAccessorMethodName(property), DATE_GETTER);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            LOGGER.warn(String.format("Unable to find/access the Date property %s of class %s", property, target.getName()), e);
        }
        return null;
    }
}
