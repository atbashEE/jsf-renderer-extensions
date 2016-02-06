/*
 * Copyright 2014-2016 Rudy De Busscher
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

import be.rubus.web.valerie.custom.model.DateRangeModel1;
import be.rubus.web.valerie.custom.model.DateRangeModel2;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;


/**
 *
 */
public class DateRangeValidatorTest {

    private Date date1;
    private Date date2;
    private Date date3;

    private DateRangeValidator validator;

    @Before
    public void setup() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date1 = dateFormat.parse("14/09/2015");
        date2 = dateFormat.parse("15/09/2015");
        date3 = dateFormat.parse("14/09/2015");

        validator = new DateRangeValidator();

    }

    @Test
    public void testIsValid() {
        DateRangeModel1 model = new DateRangeModel1();
        model.setStartDate(date1);
        model.setEndDate(date2);

        validator.initialize(model.getClass().getAnnotation(DateRange.class));

        boolean valid = validator.isValid(model, null);
        assertThat(valid).isTrue();
    }

    @Test
    public void testIsValid_endIsEmpty() {
        DateRangeModel1 model = new DateRangeModel1();
        model.setStartDate(date1);

        validator.initialize(model.getClass().getAnnotation(DateRange.class));

        boolean valid = validator.isValid(model, null);
        assertThat(valid).isTrue();
    }

    @Test
    public void testIsValid_endBeforeStart() {
        DateRangeModel1 model = new DateRangeModel1();
        model.setStartDate(date2);
        model.setEndDate(date1);

        validator.initialize(model.getClass().getAnnotation(DateRange.class));

        boolean valid = validator.isValid(model, null);
        assertThat(valid).isFalse();
    }

    @Test
    public void testIsValid_sameDates() {
        DateRangeModel1 model = new DateRangeModel1();
        model.setStartDate(date1);
        model.setEndDate(date3);

        validator.initialize(model.getClass().getAnnotation(DateRange.class));

        boolean valid = validator.isValid(model, null);
        assertThat(valid).isFalse();
    }

    @Test
    public void testIsValid_equalsAllowed() {
        DateRangeModel2 model = new DateRangeModel2();
        model.setStartDate(date1);
        model.setEndDate(date2);

        validator.initialize(model.getClass().getAnnotation(DateRange.class));

        boolean valid = validator.isValid(model, null);
        assertThat(valid).isTrue();
    }

    @Test
    public void testIsValid_equalsAllowed_endIsEmpty() {
        DateRangeModel2 model = new DateRangeModel2();
        model.setStartDate(date1);

        validator.initialize(model.getClass().getAnnotation(DateRange.class));

        boolean valid = validator.isValid(model, null);
        assertThat(valid).isTrue();
    }

    @Test
    public void testIsValid_equalsAllowed_endBeforeStart() {
        DateRangeModel2 model = new DateRangeModel2();
        model.setStartDate(date2);
        model.setEndDate(date1);

        validator.initialize(model.getClass().getAnnotation(DateRange.class));

        boolean valid = validator.isValid(model, null);
        assertThat(valid).isFalse();
    }

    @Test
    public void testIsValid_equalsAllowed_sameDates() {
        DateRangeModel2 model = new DateRangeModel2();
        model.setStartDate(date1);
        model.setEndDate(date3);

        validator.initialize(model.getClass().getAnnotation(DateRange.class));

        boolean valid = validator.isValid(model, null);
        assertThat(valid).isTrue();
    }
}