package be.rubus.web.jerry.component;

import be.rubus.web.jerry.interceptor.AbstractRendererInterceptor;
import be.rubus.web.jerry.interceptor.exception.SkipBeforeInterceptorsException;
import be.rubus.web.jerry.interceptor.exception.SkipRendererDelegationException;
import be.rubus.web.jerry.ordering.InvocationOrder;
import be.rubus.web.jerry.storage.ComponentStorage;
import be.rubus.web.jerry.utils.InvocationOrderedArtifactsProvider;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * An instance of a {@link be.rubus.web.jerry.interceptor.RendererInterceptor} which executes all know
 * {@link be.rubus.web.jerry.component.ComponentInitializer}s found in the application.
 */
@ApplicationScoped
@InvocationOrder(100)
public class InterceptorComponentInitializer extends AbstractRendererInterceptor {

    private List<ComponentInitializer> initializers;

    @Inject
    private ComponentStorage componentStorage;


    @PostConstruct
    public void init() {
        initializers = InvocationOrderedArtifactsProvider.getComponentInitializers();
    }

    @Override
    public void beforeEncodeBegin(FacesContext facesContext, UIComponent uiComponent,
                                  Renderer wrapped) throws IOException, SkipBeforeInterceptorsException, SkipRendererDelegationException {
        String viewId = facesContext.getViewRoot().getViewId();
        String clientId = uiComponent.getClientId(facesContext);

        Map<String, Object> componentInfo = componentStorage.getComponentInfo(viewId, clientId);

        for (ComponentInitializer initializer : initializers) {
            initializer.configureComponent(facesContext, uiComponent, componentInfo);
        }

    }
}
