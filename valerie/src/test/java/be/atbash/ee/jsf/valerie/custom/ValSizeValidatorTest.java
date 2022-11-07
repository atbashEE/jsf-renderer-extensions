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

import be.atbash.ee.jsf.valerie.custom.model.ValSizeModel1;
import be.atbash.ee.jsf.valerie.custom.model.ValSizeModel2;
import be.atbash.ee.jsf.valerie.custom.model.ValSizeModel3;
import be.atbash.ee.jsf.valerie.custom.model.ValSizeModel4;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;

class ValSizeValidatorTest {

    private ValSizeValidator validator = new ValSizeValidator();

    @BeforeEach
    public void setup() throws ParseException {
        validator = new ValSizeValidator();

    }

    @Test
    void isValid_correct() throws NoSuchFieldException {
        ValSizeModel1 model = new ValSizeModel1();

        validator.initialize(model.getClass().getDeclaredField("value").getAnnotation(ValSize.class));

        boolean valid = validator.isValid("someValue", null);
        assertThat(valid).isTrue();

    }

    @Test
    void isValid_tooSmall() throws NoSuchFieldException {
        ValSizeModel1 model = new ValSizeModel1();

        validator.initialize(model.getClass().getDeclaredField("value").getAnnotation(ValSize.class));

        boolean valid = validator.isValid("short", null);
        assertThat(valid).isFalse();

    }

    @Test
    void isValid_tooLarge() throws NoSuchFieldException {
        ValSizeModel1 model = new ValSizeModel1();

        validator.initialize(model.getClass().getDeclaredField("value").getAnnotation(ValSize.class));

        boolean valid = validator.isValid("AVeryLongValue", null);
        assertThat(valid).isFalse();

    }

    @Test
    void isValid_null() throws NoSuchFieldException {
        ValSizeModel1 model = new ValSizeModel1();

        validator.initialize(model.getClass().getDeclaredField("value").getAnnotation(ValSize.class));

        boolean valid = validator.isValid(null, null);
        assertThat(valid).isFalse();

    }

    @Test
    void isValid_nullWithMin0() throws NoSuchFieldException {
        ValSizeModel2 model = new ValSizeModel2();

        validator.initialize(model.getClass().getDeclaredField("value").getAnnotation(ValSize.class));

        boolean valid = validator.isValid(null, null);
        assertThat(valid).isTrue();

    }

    @Test
    void invalidMinValue() {
        ValSizeModel3 model = new ValSizeModel3();

        Assertions.assertThatThrownBy(() -> validator.initialize(model.getClass().getDeclaredField("value").getAnnotation(ValSize.class)))
                .isInstanceOf(ValSizeValidatorException.class)
                .hasMessage("'min' property for @ValSize must be 0 or more but is -1");
    }

    @Test
    void invalidMinMaxValue(){
        ValSizeModel4 model = new ValSizeModel4();

        Assertions.assertThatThrownBy(() -> validator.initialize(model.getClass().getDeclaredField("value").getAnnotation(ValSize.class)))
                .isInstanceOf(ValSizeValidatorException.class)
                .hasMessage("'min' property for @ValSize must be smaller or equal then 'max property; min = 6, max = 5");

    }

}