package be.rubus.web.jerry.interceptor;

import be.rubus.web.jerry.component.ComponentInitializer;
import be.rubus.web.jerry.interceptor.exception.SkipBeforeInterceptorsException;
import be.rubus.web.jerry.interceptor.exception.SkipRendererDelegationException;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Map;

/**
 *
 */
@ApplicationScoped
public class MarkerRendererInterceptor implements ComponentInitializer {
    @Override
    public void configureComponent(FacesContext facesContext, UIComponent uiComponent, Map<String, Object> metaData) {
        if (uiComponent instanceof HtmlInputText) {
            HtmlInputText input = (HtmlInputText) uiComponent;
            input.setStyleClass("inputField");
        }
    }


    @Override
    public boolean isSupportedComponent(UIComponent uiComponent) {
        return uiComponent instanceof HtmlInputText;
    }
}
