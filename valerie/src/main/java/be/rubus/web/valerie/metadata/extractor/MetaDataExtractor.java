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
package be.rubus.web.valerie.metadata.extractor;

import be.rubus.web.jerry.utils.ProxyUtils;
import be.rubus.web.valerie.el.ELHelper;
import be.rubus.web.valerie.property.PropertyDetails;
import be.rubus.web.valerie.property.PropertyInformation;
import be.rubus.web.valerie.utils.AnnotationUtils;
import org.slf4j.Logger;

import javax.el.ValueExpression;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;


/**
 * Default implementation which extracts meta-data (e.g. the annotations) of the value binding of a component.
 * It extracts the meta-data of the field and the property.
 * (Also the annotations of super classes and interfaces.)
 */
@ApplicationScoped
public class MetaDataExtractor {

    @Inject
    private ELHelper elHelper;

    @Inject
    protected transient Logger logger;


    public PropertyInformation extract(FacesContext facesContext, Object object) {
        //should never occur
        if (!(object instanceof UIComponent)) {
            if (object != null) {
                logger.warn(object.getClass() + " is no valid component");
            }
            return null;
        }

        // TODO EXPERIMENTAL : Is it ok only descendants of this type to process
        if (!(object instanceof UIInput)) {
            return null;
        }
        UIComponent uiComponent = (UIComponent) object;

        if (logger.isTraceEnabled()) {
            logger.trace("start extracting meta-data of " + uiComponent.getClass().getName());
        }

        PropertyDetails propertyDetails = null;

        UIComponent compositeParent = findCompositeParent(uiComponent);
        if (compositeParent != null) {
            ValueExpression expression = (ValueExpression) compositeParent.getAttributes().get("Valerie" + uiComponent.getId());
            propertyDetails = elHelper.getPropertyDetailsOfExpression(facesContext, expression);

        } else {

            propertyDetails = elHelper.getPropertyDetailsOfValueBinding(facesContext, uiComponent);
        }

        if (propertyDetails == null) {
            return null;
        }

        /*
         * get bean class and property name
         */
        Class entityClass = ProxyUtils.getUnproxiedClass(propertyDetails.getBaseObject().getClass());

        PropertyInformation propertyInformation = getPropertyInformation(entityClass, propertyDetails);

        if (logger.isTraceEnabled()) {
            logger.trace("extract finished");
        }

        return propertyInformation;
    }

    private UIComponent findCompositeParent(UIComponent uiComponent) {
        UIComponent result = null;
        UIComponent parent = uiComponent;
        do {
            parent = parent.getParent();
            if (parent != null && UIComponent.isCompositeComponent(parent)) {
                result = parent;
            }
        } while (parent != null && result == null);
        return result;
    }

    protected PropertyInformation getPropertyInformation(Class entityClass, PropertyDetails propertyDetails) {

        return AnnotationUtils.extractAnnotations(entityClass, propertyDetails);

    }

}
