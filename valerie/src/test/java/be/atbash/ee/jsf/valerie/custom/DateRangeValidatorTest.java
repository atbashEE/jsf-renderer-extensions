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

import be.atbash.ee.jsf.valerie.custom.model.DateRangeModel1;
import be.atbash.ee.jsf.valerie.custom.model.DateRangeModel2;
import be.atbash.ee.jsf.valerie.custom.model.DateRangeModel3;
import be.atbash.ee.jsf.valerie.custom.model.DateRangeModel4;
import be.atbash.ee.jsf.valerie.utils.MethodHandleUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
class DateRangeValidatorTest {

    private Date date1;
    private Date date2;
    private Date date3;

    private DateRangeValidator validator;

    private TestLogger logger = TestLoggerFactory.getTestLogger(MethodHandleUtils.class);

    @BeforeEach
    public void setup() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date1 = dateFormat.parse("14/09/2015");
        date2 = dateFormat.parse("15/09/2015");
        date3 = dateFormat.parse("14/09/2015");

        validator = new DateRangeValidator();

    }

    @AfterEach
    public void teardown() {
        logger.clearAll();
    }

    @Test
    void testIsValid() {
        DateRangeModel1 model = new DateRangeModel1();
        model.setStartDate(date1);
        model.setEndDate(date2);

        validator.initialize(model.getClass().getAnnotation(DateRange.class));

        boolean valid = validator.isValid(model, null);
        Assertions.assertThat(valid).isTrue();
    }

    @Test
    void testIsValid_endIsEmpty() {
        DateRangeModel1 model = new DateRangeModel1();
        model.setStartDate(date1);

        validator.initialize(model.getClass().getAnnotation(DateRange.class));

        boolean valid = validator.isValid(model, null);
        Assertions.assertThat(valid).isTrue();
    }

    @Test
    void testIsValid_endBeforeStart() {
        DateRangeModel1 model = new DateRangeModel1();
        model.setStartDate(date2);
        model.setEndDate(date1);

        validator.initialize(model.getClass().getAnnotation(DateRange.class));

        boolean valid = validator.isValid(model, null);
        Assertions.assertThat(valid).isFalse();
    }

    @Test
    void testIsValid_sameDates() {
        DateRangeModel1 model = new DateRangeModel1();
        model.setStartDate(date1);
        model.setEndDate(date3);

        validator.initialize(model.getClass().getAnnotation(DateRange.class));

        boolean valid = validator.isValid(model, null);
        Assertions.assertThat(valid).isFalse();
    }

    @Test
    void testIsValid_equalsAllowed() {
        DateRangeModel2 model = new DateRangeModel2();
        model.setStartDate(date1);
        model.setEndDate(date2);

        validator.initialize(model.getClass().getAnnotation(DateRange.class));

        boolean valid = validator.isValid(model, null);
        Assertions.assertThat(valid).isTrue();
    }

    @Test
    void testIsValid_equalsAllowed_endIsEmpty() {
        DateRangeModel2 model = new DateRangeModel2();
        model.setStartDate(date1);

        validator.initialize(model.getClass().getAnnotation(DateRange.class));

        boolean valid = validator.isValid(model, null);
        Assertions.assertThat(valid).isTrue();
    }

    @Test
    void testIsValid_equalsAllowed_endBeforeStart() {
        DateRangeModel2 model = new DateRangeModel2();
        model.setStartDate(date2);
        model.setEndDate(date1);

        validator.initialize(model.getClass().getAnnotation(DateRange.class));

        boolean valid = validator.isValid(model, null);
        Assertions.assertThat(valid).isFalse();
    }

    @Test
    void testIsValid_equalsAllowed_sameDates() {
        DateRangeModel2 model = new DateRangeModel2();
        model.setStartDate(date1);
        model.setEndDate(date3);

        validator.initialize(model.getClass().getAnnotation(DateRange.class));

        boolean valid = validator.isValid(model, null);
        Assertions.assertThat(valid).isTrue();
    }

    @Test
    public void testIsValid_wrongStartDateProperty() {
        DateRangeModel3 model = new DateRangeModel3();
        model.setStartDate(date1);
        model.setEndDate(date3);

        validator.initialize(model.getClass().getAnnotation(DateRange.class));

        Assertions.assertThatThrownBy(() -> validator.isValid(model, null))
                .isInstanceOf(DateRangeValidatorPropertyException.class);

        Assertions.assertThat(logger.getLoggingEvents()).hasSize(1);
        Assertions.assertThat(logger.getLoggingEvents().get(0).getMessage()).isEqualTo("Unable to find/access the Date property 'wrongStartDateProperty' of class be.atbash.ee.jsf.valerie.custom.model.DateRangeModel3");
    }

    @Test
    public void testIsValid_wrongEndDateProperty() {
        DateRangeModel4 model = new DateRangeModel4();
        model.setStartDate(date1);
        model.setEndDate(date3);

        validator.initialize(model.getClass().getAnnotation(DateRange.class));

        Assertions.assertThatThrownBy(() -> validator.isValid(model, null))
                .isInstanceOf(DateRangeValidatorPropertyException.class);

        Assertions.assertThat(logger.getLoggingEvents()).hasSize(1);
        Assertions.assertThat(logger.getLoggingEvents().get(0).getMessage()).isEqualTo("Unable to find/access the Date property 'wrongEndDateProperty' of class be.atbash.ee.jsf.valerie.custom.model.DateRangeModel4");

    }
}