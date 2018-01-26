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
package be.atbash.ee.jsf.jerry.example.interceptor;

import be.atbash.ee.jsf.jerry.component.ComponentInitializer;
import be.atbash.util.ComponentUtils;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.outputlabel.OutputLabel;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Map;

/**
 *
 */
@ApplicationScoped
public class DummyInitializer implements ComponentInitializer {
    @Override
    public void configureComponent(FacesContext facesContext, UIComponent uiComponent, Map<String, Object> metaData) {
        if (uiComponent instanceof OutputLabel) {
            OutputLabel label = (OutputLabel) uiComponent;
            Object value = ComponentUtils.getValue(label, facesContext);
            label.setValue((value == null ? "" : value.toString()) + "Hijacked");
        }

        if (uiComponent instanceof InputText) {
            InputText inputText = (InputText) uiComponent;
            inputText.setPlaceholder("Interceptor :)");

            if (inputText.isRequired()) {
                String style = ComponentUtils.getStyle(inputText, facesContext);
                if (style == null) {
                    style = "";
                }
                if (!style.contains("background-color: #B04A4A;")) {
                    inputText.setStyle(style + " background-color: #B04A4A;");
                }

            }
        }

    }

    @Override
    public boolean isSupportedComponent(UIComponent uiComponent) {
        return uiComponent instanceof OutputLabel || uiComponent instanceof InputText;
    }
}
