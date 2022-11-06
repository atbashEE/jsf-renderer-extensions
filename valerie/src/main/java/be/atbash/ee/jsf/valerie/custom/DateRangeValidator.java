/*
 * Copyright 2014-2022 Rudy De Busscher
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

import be.atbash.ee.jsf.valerie.utils.MethodHandleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.invoke.MethodHandle;
import java.util.Date;

/**
 *
 */
public class DateRangeValidator implements ConstraintValidator<DateRange, Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateRangeValidator.class);

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

        MethodHandle handleStart = MethodHandleUtils.getGetterHandle(clazz, start);
        if (handleStart == null) {
            throw new DateRangeValidatorPropertyException(String.format("Unknown object property defined for 'start' : %s", start));
        }
        MethodHandle handleEnd = MethodHandleUtils.getGetterHandle(clazz, end);
        if (handleEnd == null) {
            throw new DateRangeValidatorPropertyException(String.format("Unknown object property defined for 'end' : %s", end));
        }
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

}
