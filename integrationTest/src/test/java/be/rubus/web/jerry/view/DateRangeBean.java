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
package be.rubus.web.jerry.view;

import be.rubus.web.valerie.custom.DateRange;
import be.rubus.web.valerie.recording.RecordValue;

import javax.enterprise.inject.Model;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 *
 */
@Model
@DateRange(start = "startDate", end = "endDate")
public class DateRangeBean {

    @RecordValue
    private Date startDate;

    @RecordValue
    @NotNull
    private Date endDate;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {

    }
}
