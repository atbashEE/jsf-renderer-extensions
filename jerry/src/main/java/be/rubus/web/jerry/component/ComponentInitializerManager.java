package be.rubus.web.jerry.component;

import be.rubus.web.jerry.storage.ComponentStorage;
import be.rubus.web.jerry.utils.InvocationOrderedArtifactsProvider;

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
