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
package be.atbash.ee.jsf.valerie.test.initializer;

import be.atbash.ee.jsf.jerry.component.ComponentInitializer;
import be.atbash.ee.jsf.jerry.component.ComponentInitializerManager;
import be.atbash.ee.jsf.jerry.ordering.InvocationOrder;
import be.atbash.ee.jsf.valerie.property.PropertyInformationManager;
import be.atbash.util.ComponentUtils;
import org.slf4j.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.component.html.HtmlOutputLabel;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import java.util.Map;

/**
 *
 */
@ApplicationScoped
@InvocationOrder(1001)
public class RequiredMarkerInitializer implements ComponentInitializer {

    @Inject
    private ComponentInitializerManager initializerManager;

    @Inject
    private PropertyInformationManager informationManager;

    @Inject
    private Logger logger;

    @Override
    public void configureComponent(FacesContext facesContext, UIComponent uiComponent, Map<String, Object> metaData) {
        HtmlOutputLabel label = (HtmlOutputLabel) uiComponent;
        if (label.getFor() != null && !label.getFor().trim().isEmpty()) {
            UIComponent targetComponent = label.findComponent(label.getFor());

            if (targetComponent instanceof UIInput) {

                informationManager.determineInformation(facesContext, targetComponent);
                initializerManager.performInitialization(facesContext, targetComponent);

                boolean required = ComponentUtils.isRequired(targetComponent, facesContext);

                Object value = ComponentUtils.getValue(label, facesContext);

                if (required) {
                    label.setValue(value + " *");
                } else {
                    label.setValue(value);

                }
            } else {
                logger.warn("target component specified in for ('" + label.getFor() + "') not found from component " + label.getClientId(facesContext));
            }

        }
    }

    @Override
    public boolean isSupportedComponent(UIComponent uiComponent) {
        return uiComponent instanceof HtmlOutputLabel;
    }
}
