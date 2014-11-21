package be.rubus.web.jerry.component;

import be.rubus.web.jerry.interceptor.AbstractRendererInterceptor;
import be.rubus.web.jerry.interceptor.exception.SkipBeforeInterceptorsException;
import be.rubus.web.jerry.interceptor.exception.SkipRendererDelegationException;
import be.rubus.web.jerry.ordering.InvocationOrder;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import javax.inject.Inject;
import java.io.IOException;

/**
 * An instance of a {@link be.rubus.web.jerry.interceptor.RendererInterceptor} which executes all know
 * {@link be.rubus.web.jerry.component.ComponentInitializer}s found in the application.
 */
@ApplicationScoped
@InvocationOrder(100)
public class InterceptorComponentInitializer extends AbstractRendererInterceptor {

    @Inject
    private ComponentInitializerManager manager;

    @Override
    public void beforeEncodeBegin(FacesContext facesContext, UIComponent uiComponent,
                                  Renderer wrapped) throws IOException, SkipBeforeInterceptorsException, SkipRendererDelegationException {
        manager.performInitialization(facesContext, uiComponent);

    }

}
