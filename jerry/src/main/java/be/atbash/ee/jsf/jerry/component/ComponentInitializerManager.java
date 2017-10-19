/*
 * Copyright 2014-2016 Rudy De Busscher
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
package be.atbash.ee.jsf.jerry.component;

import be.atbash.ee.jsf.jerry.storage.ComponentStorage;
import be.atbash.ee.jsf.jerry.utils.InvocationOrderedArtifactsProvider;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 *
 */
@ApplicationScoped
public class ComponentInitializerManager {

    private List<ComponentInitializer> initializers;

    @Inject
    private ComponentStorage componentStorage;

    @PostConstruct
    public void init() {
        initializers = InvocationOrderedArtifactsProvider.getComponentInitializers();
    }

    public void performInitialization(FacesContext facesContext, UIComponent uiComponent) {
        if (notAlreadyInitialized(uiComponent)) {
            performInit(facesContext, uiComponent);
            setInitialized(uiComponent);
        }
    }

    private void setInitialized(UIComponent uiComponent) {
        uiComponent.getAttributes().put(ComponentInitializer.class.getName(), Boolean.TRUE);
    }

    private boolean notAlreadyInitialized(UIComponent uiComponent) {
        return !uiComponent.getAttributes().containsKey(ComponentInitializer.class.getName());
    }

    private void performInit(FacesContext facesContext, UIComponent uiComponent) {
        String viewId = facesContext.getViewRoot().getViewId();
        String clientId = uiComponent.getClientId(facesContext);

        Map<String, Object> componentInfo = componentStorage.getComponentInfo(viewId, clientId);

        for (ComponentInitializer initializer : initializers) {
            if (initializer.isSupportedComponent(uiComponent)) {
                initializer.configureComponent(facesContext, uiComponent, componentInfo);
            }
        }
    }
}
