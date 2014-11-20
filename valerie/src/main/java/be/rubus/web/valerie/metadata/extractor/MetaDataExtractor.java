/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package be.rubus.web.valerie.metadata.extractor;

import be.rubus.web.valerie.el.ELHelper;
import be.rubus.web.jerry.metadata.MetaDataEntry;
import be.rubus.web.valerie.property.DefaultPropertyInformation;
import be.rubus.web.valerie.property.PropertyDetails;
import be.rubus.web.valerie.property.PropertyInformation;
import be.rubus.web.valerie.property.PropertyInformationKeys;
import be.rubus.web.valerie.storage.MetaDataStorage;
import be.rubus.web.valerie.utils.AnnotationUtils;
import be.rubus.web.valerie.utils.ProxyUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;


/**
 * Default implementation which extracts meta-data (e.g. the annotations) of the value binding of a component.
 * It extracts the meta-data of the field and the property.
 * (Also the annotations of super classes and interfaces.)
 */
@ApplicationScoped
public class MetaDataExtractor  {

    @Inject
    private ELHelper elHelper;

    @Inject
    private MetaDataStorage storage;

    // TODO
    //protected final Logger logger = Logger.getLogger(getClass().getName());


    public PropertyInformation extract(FacesContext facesContext, Object object) {
        //should never occur
        if (!(object instanceof UIComponent)) {
            if (object != null) {
                //this.logger.warning(object.getClass() + " is no valid component");
            }
            return null;
        }

        UIComponent uiComponent = (UIComponent) object;

        //logger.finest("start extracting meta-data of " + uiComponent.getClass().getName());

        PropertyDetails propertyDetails = elHelper.getPropertyDetailsOfValueBinding(uiComponent);

        if (propertyDetails == null) {
            return null;
        }

        /*
         * get bean class and property name
         */
        Class entityClass = ProxyUtils.getUnproxiedClass(propertyDetails.getBaseObject().getClass());

        PropertyInformation propertyInformation = getPropertyInformation(entityClass, propertyDetails);

        //logger.finest("extract finished");

        return propertyInformation;
    }

    protected PropertyInformation getPropertyInformation(Class entityClass, PropertyDetails propertyDetails) {

        PropertyInformation propertyInformation = new DefaultPropertyInformation();

        if (isCached(storage, entityClass, propertyDetails.getProperty())) {
            //create
            propertyInformation.setInformation(PropertyInformationKeys.PROPERTY_DETAILS, propertyDetails);

            for (MetaDataEntry metaDataEntry : getCachedMetaData(storage, entityClass, propertyDetails.getProperty())) {
                propertyInformation.addMetaDataEntry(metaDataEntry);
            }
        } else {
            propertyInformation = AnnotationUtils.extractAnnotations(entityClass, propertyDetails);
            cacheMetaData(storage, propertyInformation);
        }
        return propertyInformation;
    }

    protected boolean isCached(MetaDataStorage storage, Class entityClass, String property) {
        return storage.containsMetaDataFor(entityClass, property);
    }

    protected void cacheMetaData(MetaDataStorage storage, PropertyInformation propertyInformation) {
        storage.storeMetaDataOf(propertyInformation);
    }

    protected MetaDataEntry[] getCachedMetaData(MetaDataStorage storage, Class entityClass, String property) {
        return storage.getMetaData(entityClass, property);
    }

}
