package be.rubus.web.jerry.component;

import be.rubus.web.jerry.interceptor.AbstractRendererInterceptor;
import be.rubus.web.jerry.interceptor.exception.SkipBeforeInterceptorsException;
import be.rubus.web.jerry.interceptor.exception.SkipRendererDelegationException;
import be.rubus.web.jerry.provider.BeanProvider;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
@ApplicationScoped
public class InterceptorComponentInitializer extends AbstractRendererInterceptor {

    private List<ComponentInitializer> initializers;

    @PostConstruct
    public void init() {
        initializers = BeanProvider.getContextualReferences(ComponentInitializer.class, true, false);
    }

    @Override
    public void beforeEncodeBegin(FacesContext facesContext, UIComponent uiComponent,
                                  Renderer wrapped) throws IOException, SkipBeforeInterceptorsException, SkipRendererDelegationException {
        for (ComponentInitializer initializer : initializers) {
            // TODO retrieve the metaData if Valerie is with us.
            initializer.configureComponent(facesContext, uiComponent, new HashMap<String, Object>());
        }

    }
}
