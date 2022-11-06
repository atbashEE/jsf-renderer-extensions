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

import be.atbash.ee.jsf.valerie.provider.DateProducer;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
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
