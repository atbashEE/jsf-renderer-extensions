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
package be.atbash.ee.jsf.jerry.utils;

import javax.el.ValueExpression;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;

/**
 *
 */

public final class ComponentUtils {

    private ComponentUtils() {
    }

    /**
     * Return the {@code value} from the component ({@link ValueHolder}). It checks first the valueExpression, if not
     * present then the getter is executed for retrieving fixed values.
     *
     * @param component    JSF component to get the value from.
     * @param facesContext The FacesContext
     * @return The 'value' of the JSF Component
     */
    public static Object getValue(UIComponent component, FacesContext facesContext) {
        ValueExpression value = component.getValueExpression("value");
        if (value == null) {
            if (component instanceof ValueHolder) {

                return ((ValueHolder) component).getValue();
            } else {
                return null;
            }
        } else {
            return value.getValue(facesContext.getELContext());
        }
    }

    /**
     * Return the {@code required} info from the component ({@link EditableValueHolder}). It checks first the valueExpression, if not
     * present then the getter is executed for retrieving fixed values.
     *
     * @param component    JSF component to get the value from.
     * @param facesContext The FacesContext
     * @return The 'value' of the JSF Component
     */
    public static boolean isRequired(UIComponent component, FacesContext facesContext) {
        ValueExpression value = component.getValueExpression("required");
        if (value == null) {
            if (component instanceof EditableValueHolder) {

                return ((EditableValueHolder) component).isRequired();
            } else {
                return false; // default value when no attribute is specified
            }
        } else {
            Object result = value.getValue(facesContext.getELContext());
            if (result != null) {
                return (Boolean) result;
            }
            return false; // default value when no attribute is specified
        }
    }

    /**
     * Return the {@code style} from the component ({@link HtmlInputText} or {@link HtmlOutputLabel}). It checks first the valueExpression, if not
     * present then the getter is executed for retrieving fixed values.
     *
     * @param component    JSF component to get the value from.
     * @param facesContext The FacesContext
     * @return The 'value' of the JSF Component
     */
    // FIXME There are so many different classes with getStyle Method, reflection?!
    public static String getStyle(UIComponent component, FacesContext facesContext) {
        ValueExpression value = component.getValueExpression("style");
        if (value == null) {
            if (component instanceof HtmlInputText) {

                return ((HtmlInputText) component).getStyle();
            }
            if (component instanceof HtmlSelectOneMenu) {

                return ((HtmlSelectOneMenu) component).getStyle();
            }

            if (component instanceof HtmlOutputLabel) {
                return ((HtmlOutputLabel) component).getStyle();
            }

            return null;

        } else {
            return value.getValue(facesContext.getELContext()).toString();
        }
    }

    /**
     * Return the {@code styleClass} from the component ({@link HtmlInputText}, {@link HtmlOutputLabel} or {@link HtmlSelectOneMenu}). It checks first the valueExpression, if not
     * present then the getter is executed for retrieving fixed values.
     *
     * @param component    JSF component to get the value from.
     * @param facesContext The FacesContext
     * @return The 'value' of the JSF Component
     */
    // FIXME There are so many different classes with getStyle Method, reflection?!
    public static String getStyleClass(UIComponent component, FacesContext facesContext) {
        ValueExpression value = component.getValueExpression("styleClass");
        if (value == null) {
            if (component instanceof HtmlInputText) {

                return ((HtmlInputText) component).getStyleClass();
            }
            if (component instanceof HtmlSelectOneMenu) {

                return ((HtmlSelectOneMenu) component).getStyleClass();
            }
            if (component instanceof HtmlOutputLabel) {
                return ((HtmlOutputLabel) component).getStyleClass();
            }

            return null;

        } else {
            return value.getValue(facesContext.getELContext()).toString();
        }
    }

    public static int getMaxLength(UIComponent component, FacesContext facesContext) {
        ValueExpression value = component.getValueExpression("maxlength");
        if (value == null) {
            if (component instanceof HtmlInputText) {
                return ((HtmlInputText) component).getMaxlength();
            }
            return Integer.MIN_VALUE;
        }
        return Integer.valueOf(value.getValue(facesContext.getELContext()).toString());

    }
}
