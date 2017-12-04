/*
 * Copyright 2014-2017 Rudy De Busscher
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
        boolean result = value != null || sizeAnnotation.min() == 0;
        if (result) {
            if (sizeAnnotation.min() > 0) {
                String data = value.toString();
                int length = data.length();
                if (length < sizeAnnotation.min() || length > sizeAnnotation.max()) {
                    result = false;
                }
            }
        }
        return result;
    }
}
