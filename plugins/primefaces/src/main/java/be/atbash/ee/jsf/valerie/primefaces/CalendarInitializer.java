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
package be.atbash.ee.jsf.valerie.primefaces;

import be.atbash.ee.jsf.jerry.component.ComponentInitializer;
import be.atbash.ee.jsf.jerry.metadata.CommonMetaDataKeys;
import be.atbash.ee.jsf.jerry.ordering.InvocationOrder;
import org.primefaces.component.calendar.Calendar;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import java.util.Date;
import java.util.Map;

/**
 *
 */
@ApplicationScoped
@InvocationOrder(61)
public class CalendarInitializer implements ComponentInitializer {

    @Override
    public void configureComponent(FacesContext facesContext, UIComponent uiComponent, Map<String, Object> metaData) {

        Calendar calendar = (Calendar) uiComponent;

        if (metaData.containsKey(CommonMetaDataKeys.PAST.getKey())) {
            calendar.setMaxdate(new Date());
        }

        if (metaData.containsKey(CommonMetaDataKeys.FUTURE.getKey())) {
            calendar.setMindate(new Date());
        }

    }

    @Override
    public boolean isSupportedComponent(UIComponent uiComponent) {
        return uiComponent instanceof Calendar;
    }
}
