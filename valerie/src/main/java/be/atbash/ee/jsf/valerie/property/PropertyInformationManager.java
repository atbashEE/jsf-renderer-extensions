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
package be.atbash.ee.jsf.valerie.property;

import be.atbash.ee.jsf.jerry.storage.ComponentStorage;
import be.atbash.ee.jsf.valerie.metadata.extractor.MetaDataExtractor;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

/**
 *
 */
@ApplicationScoped
public class PropertyInformationManager {

    @Inject
    private MetaDataExtractor extractor;

    @Inject
    private ComponentStorage componentStorage;

    public void determineInformation(FacesContext facesContext, UIComponent uiComponent) {

        String viewId = facesContext.getViewRoot().getViewId();
        String clientId = uiComponent.getClientId(facesContext);

        if (componentStorage.isEntryPossibleFor(viewId, clientId, PropertyInformation.class)) {

            if (!componentStorage.containsEntry(viewId, clientId, PropertyInformation.class)) {


                PropertyInformation info = extractor.extract(facesContext, uiComponent);
                if (info != null) {
                    componentStorage.storeEntry(viewId, clientId, info);
                } else {
                    componentStorage.setNotAvailable(viewId, clientId, PropertyInformation.class);
                }
            }
        }
    }

}
