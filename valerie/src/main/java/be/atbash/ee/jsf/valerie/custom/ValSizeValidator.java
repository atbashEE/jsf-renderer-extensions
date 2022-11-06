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

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 *
 */
public class ValSizeValidator implements ConstraintValidator<ValSize, Object> {
    private ValSize sizeAnnotation;

    @Override
    public void initialize(ValSize constraintAnnotation) {

        sizeAnnotation = constraintAnnotation;
        if (sizeAnnotation.min() < 0) {
            throw new ValSizeValidatorException(String.format("'min' property for @ValSize must be 0 or more but is %s", sizeAnnotation.min()));
        }
        if (sizeAnnotation.min() > sizeAnnotation.max()) {
            throw new ValSizeValidatorException(String.format("'min' property for @ValSize must be smaller or equal then 'max property; min = %s, max = %s", sizeAnnotation.min(), sizeAnnotation.max()));
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        boolean result = value != null || sizeAnnotation.min() == 0;
        if (result) {
            if (sizeAnnotation.min() > 0) {
                String data = value.toString();  // With validation in initialize value can never bu null here.
                int length = data.length();
                if (length < sizeAnnotation.min() || length > sizeAnnotation.max()) {
                    result = false;
                }
            }
        }
        return result;
    }
}
