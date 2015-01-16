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
package be.rubus.web.valerie.el;


import be.rubus.web.valerie.property.PropertyDetails;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * in order to centralize the jsf version dependency within the core
 * <p/>
 * this el-helper supports jsp and facelets (tested with 1.1.14)
 * <p/>
 * <p/>
 * there is a special facelets workaround for el-expressions of custom components
 * it's pluggable in order to support special mechanisms of different technologies (than jsp and facelets)
 * so you can plug in your own impl. which implements a custom workaround (like the facelets workaround of this impl.)
 */
@ApplicationScoped
public class ELHelper {


    //protected final boolean projectStageDevelopment = JsfProjectStage.is(JsfProjectStage.Development);

    public PropertyDetails getPropertyDetailsOfValueBinding(UIComponent uiComponent) {

        FacesContext facesContext = FacesContext.getCurrentInstance();

        RecordingELResolver elResolver =
                new RecordingELResolver(facesContext.getApplication().getELResolver(), true /* TODO this.projectStageDevelopment */);

        ValueExpression valueExpression = uiComponent.getValueExpression("value");

        if (valueExpression == null) {
            return null;
        }

        ELContext elContext = RecordingELResolver.createContextWrapper(facesContext.getELContext(), elResolver);

        try {
            valueExpression.setValue(elContext, null);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "error at binding: " + valueExpression.getExpressionString() +
                            " -- an el-resolver error occurred! maybe you used an invalid binding. ", e);
        }

        if (elResolver.getPath() == null || elResolver.getBaseObject() == null || elResolver.getProperty() == null) {
            return null;
        }

        return new PropertyDetails(elResolver.getPath(), elResolver.getBaseObject(), elResolver.getProperty());
    }

}
