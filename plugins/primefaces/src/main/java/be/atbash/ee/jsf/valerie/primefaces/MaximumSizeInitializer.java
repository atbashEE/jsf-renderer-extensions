/*
 * Copyright 2014-2017 Rudy De Busscher
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
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.inputtextarea.InputTextarea;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Map;

/**
 *
 */
@ApplicationScoped
@InvocationOrder(61)
public class MaximumSizeInitializer implements ComponentInitializer {

    @Override
    public void configureComponent(FacesContext facesContext, UIComponent uiComponent, Map<String, Object> metaData) {

        if (metaData.containsKey(CommonMetaDataKeys.SIZE.getKey())) {
            Integer maxSize = (Integer) metaData.get(CommonMetaDataKeys.SIZE.getKey());
            if (uiComponent instanceof InputText) {
                InputText inputText = (InputText) uiComponent;
                int maxLength = inputText.getMaxlength();
                if (maxSize < maxLength || maxLength == Integer.MIN_VALUE) {
                    inputText.setMaxlength(maxSize);
                }
            }
            if (uiComponent instanceof InputTextarea) {
                InputTextarea inputArea = (InputTextarea) uiComponent;
                int maxLength = inputArea.getMaxlength();
                if (maxSize < maxLength || maxLength == Integer.MIN_VALUE) {
                    inputArea.setMaxlength(maxSize);
                }
            }
        }

    }

    @Override
    public boolean isSupportedComponent(UIComponent uiComponent) {
        return uiComponent instanceof InputText || uiComponent instanceof InputTextarea;
    }
}
