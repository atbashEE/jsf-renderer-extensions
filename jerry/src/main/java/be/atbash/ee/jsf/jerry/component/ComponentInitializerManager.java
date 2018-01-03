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
package be.atbash.ee.jsf.jerry.component;

import be.atbash.ee.jsf.jerry.storage.ComponentStorage;
import be.atbash.ee.jsf.jerry.utils.InvocationOrderedArtifactsProvider;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;

/**
 *
 */
@ApplicationScoped
public class ComponentInitializerManager {

    private static String REPEATED_COMPONENT = ComponentInitializerManager.class.getName() + ".REPEATED_COMPONENT";

    private List<ComponentInitializer> initializers;

    @Inject
    private ComponentStorage componentStorage;

    @PostConstruct
    public void init() {
        initializers = InvocationOrderedArtifactsProvider.getComponentInitializers();
    }

    public void performInitialization(FacesContext facesContext, UIComponent uiComponent) {
        if (notAlreadyInitialized(uiComponent)) {
            boolean hasInitializer = performInit(facesContext, uiComponent);
            if (!hasInitializer) {
                // No initializer who matches the component, don't try again.
                setInitialized(uiComponent);
            } else {
                if (!isRepeated(uiComponent)) {
                    // Initializer but not repeated, so only once is OK.
                    setInitialized(uiComponent);
                }
            }
        }
    }

    private boolean isRepeated(UIComponent uiComponent) {
        boolean result = checkRepeatableInitializer(uiComponent);
        if (!result) {
            // No RepeatableComponentInitializer used, check if it is within ui:repeat or UIData component.

            // See if we have determined the RepeatedComponent stuff before.
            result = checkRepeatedComponentFlag(uiComponent);
            if (!result) {
                // Determine the RepeatedComponent stuff
                result = checkRepeatedComponent(uiComponent);
                if (result) {
                    // Keep the flag for further reference.
                    setRepeatedComponentFlag(uiComponent);
                }
            }

        }
        return result;
    }

    private boolean checkRepeatedComponent(UIComponent uiComponent) {
        boolean result = uiComponent.getClass().getName().endsWith(".UIRepeat")
                || (uiComponent instanceof UIData);
        if (!result && uiComponent.getParent() != null) {
            result = checkRepeatedComponent(uiComponent.getParent());
        }
        return result;
    }

    private void setInitialized(UIComponent uiComponent) {
        uiComponent.getAttributes().put(ComponentInitializer.class.getName(), Boolean.TRUE);
    }

    private boolean notAlreadyInitialized(UIComponent uiComponent) {
        return !uiComponent.getAttributes().containsKey(ComponentInitializer.class.getName());
    }

    private boolean performInit(FacesContext facesContext, UIComponent uiComponent) {
        String viewId = facesContext.getViewRoot().getViewId();
        String clientId = uiComponent.getClientId(facesContext);

        Map<String, Object> componentInfo = componentStorage.getComponentInfo(viewId, clientId);

        boolean hasInitializer = false;
        for (ComponentInitializer initializer : initializers) {
            if (initializer.isSupportedComponent(uiComponent)) {
                initializer.configureComponent(facesContext, uiComponent, componentInfo);
                hasInitializer = true;
                if (initializer instanceof RepeatableComponentInitializer) {
                    setRepeatableInitializer(uiComponent);
                }
            }
        }
        return hasInitializer;
    }

    private void setRepeatableInitializer(UIComponent uiComponent) {
        uiComponent.getAttributes().put(RepeatableComponentInitializer.class.getName(), Boolean.TRUE);
    }

    private boolean checkRepeatableInitializer(UIComponent uiComponent) {
        return uiComponent.getAttributes().containsKey(RepeatableComponentInitializer.class.getName());
    }

    private void setRepeatedComponentFlag(UIComponent uiComponent) {
        uiComponent.getAttributes().put(REPEATED_COMPONENT, Boolean.TRUE);
    }

    private boolean checkRepeatedComponentFlag(UIComponent uiComponent) {
        return uiComponent.getAttributes().containsKey(REPEATED_COMPONENT);
    }
}
