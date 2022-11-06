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
package be.atbash.ee.jsf.jerry.example.validation.metadata.view;

import be.atbash.ee.jsf.jerry.example.validation.metadata.validation.CombinedValidation;

import jakarta.enterprise.inject.Model;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 *
 */
@Model
public class Combined {

    private String value;

    @NotNull
    private String required;

    @Size(max = 14)
    private String maxLength;

    @CombinedValidation
    private String combined;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }

    public String getCombined() {
        return combined;
    }

    public void setCombined(String combined) {
        this.combined = combined;
    }
}
