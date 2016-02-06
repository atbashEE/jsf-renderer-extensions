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
package be.rubus.web.jerry.view;

import javax.enterprise.inject.Model;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 */
@Model
public class ValerieBean {

    @NotNull
    private String valueRequired;
    @Size(min = 1)
    private String valueMinSize;
    @Size(max = 30)
    private String valueMaxSize;

    public String getValueRequired() {
        return valueRequired;
    }

    public void setValueRequired(String valueRequired) {
        this.valueRequired = valueRequired;
    }

    public String getValueMinSize() {
        return valueMinSize;
    }

    public void setValueMinSize(String valueMinSize) {
        this.valueMinSize = valueMinSize;
    }

    public String getValueMaxSize() {
        return valueMaxSize;
    }

    public void setValueMaxSize(String valueMaxSize) {
        this.valueMaxSize = valueMaxSize;
    }
}
