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
package be.rubus.web.jerry.component;

import be.rubus.web.jerry.ordering.InvocationOrderSupport;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Map;

/**
 * Allows to initialize components before
 * {@link javax.faces.component.UIComponent#encodeBegin(javax.faces.context.FacesContext)}.<br/>
 * e.g.: you can add information for client-side validation mechanisms,...
 * {@link be.rubus.web.jerry.metadata.MetaDataTransformer MetaDataTransformers}
 * are used to convert specific information of constraints to a generic representation.
 * A {@link ComponentInitializer} is just aware of
 * the generic data. E.g. a
 * {@link ComponentInitializer} doesn't have to care
 * if the information was provided by @Length or @Size. But a
 * {@link ComponentInitializer} has to be aware of
 * specific JSF component types. Typically there is one
 * {@link ComponentInitializer}
 * per JSF component lib.
 */
@InvocationOrderSupport
public interface ComponentInitializer {
    /**
     * Initialize components with information from the meta-data.
     *
     * @param facesContext The JSF Context
     * @param uiComponent  The component that should be initialized
     * @param metaData     Information from the {@link be.rubus.web.jerry.metadata.MetaDataEntry}
     *                     in an abstract form (independent of the concrete constraint implementations).
     */
    void configureComponent(FacesContext facesContext, UIComponent uiComponent, Map<String, Object> metaData);
}