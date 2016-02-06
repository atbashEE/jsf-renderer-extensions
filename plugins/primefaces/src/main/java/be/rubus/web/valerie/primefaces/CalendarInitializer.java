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
package be.rubus.web.valerie.primefaces;

import be.rubus.web.jerry.component.ComponentInitializer;
import be.rubus.web.jerry.component.ComponentInitializerManager;
import be.rubus.web.jerry.metadata.CommonMetaDataKeys;
import be.rubus.web.jerry.ordering.InvocationOrder;
import be.rubus.web.valerie.property.PropertyInformationManager;
import org.primefaces.component.calendar.Calendar;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.Date;
import java.util.Map;

/**
 *
 */
@ApplicationScoped
@InvocationOrder(61)
public class CalendarInitializer implements ComponentInitializer {

    @Inject
    private ComponentInitializerManager initializerManager;

    @Inject
    private PropertyInformationManager informationManager;

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
