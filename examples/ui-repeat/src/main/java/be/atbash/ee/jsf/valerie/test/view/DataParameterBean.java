/*
 * Copyright 2014-2018 Rudy De Busscher
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
package be.atbash.ee.jsf.valerie.test.view;

import be.atbash.ee.jsf.valerie.test.model.DataParameter;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Model
public class DataParameterBean {

    private List<DataParameter> parameters;

    private Map<String, String> parameterValues;

    @PostConstruct
    public void init() {
        parameters = new ArrayList<>();
        parameters.add(new DataParameter("required", "Required Value", true));
        parameters.add(new DataParameter("notRequired", "Optional Value", false));

        parameterValues = new HashMap<>();
        for (DataParameter parameter : parameters) {
            parameterValues.put(parameter.getKey(), "");
        }
    }

    public List<DataParameter> getParameters() {
        return parameters;
    }

    public Map<String, String> getParameterValues() {
        return parameterValues;
    }

    public String getStringValue() {
        return "AZERTY";
    }

    public int getIntValue() {
        return 123;
    }
}
