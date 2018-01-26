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
import org.primefaces.component.selectonemenu.SelectOneMenu;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Map;

/**
 *
 */
@ApplicationScoped
public class DropdownInitializer implements ComponentInitializer {
    @Override
    public void configureComponent(FacesContext facesContext, UIComponent uiComponent, Map<String, Object> metaData) {
        SelectOneMenu selectOneMenu = (SelectOneMenu) uiComponent;
        String style = ComponentUtils.getStyleClass(selectOneMenu, facesContext);
        if (style == null) {
            style = "";
        }
        if (!style.contains("requiredDropdown")) {
            selectOneMenu.setStyleClass(style + " requiredDropdown");
        }
    }

    @Override
    public boolean isSupportedComponent(UIComponent uiComponent) {
        return uiComponent instanceof SelectOneMenu;
    }
}
