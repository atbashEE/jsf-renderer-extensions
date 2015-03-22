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

import be.rubus.web.valerie.custom.ValSize;

import javax.enterprise.inject.Model;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 */
@Model
public class RequiredBean {

    private String notRequired;

    @NotNull
    private String required;

    @Size(min = 1)
    private String defaultSize;

    @ValSize
    private String special;

    public String getNotRequired() {
        return notRequired;
    }

    public void setNotRequired(String notRequired) {
        this.notRequired = notRequired;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getDefaultSize() {
        return defaultSize;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }
}
