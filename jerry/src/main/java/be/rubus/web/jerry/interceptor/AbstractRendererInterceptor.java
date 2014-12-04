package be.rubus.web.jerry.interceptor;

import be.rubus.web.jerry.interceptor.exception.SkipAfterInterceptorsException;
import be.rubus.web.jerry.interceptor.exception.SkipBeforeInterceptorsException;
import be.rubus.web.jerry.interceptor.exception.SkipRendererDelegationException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.render.Renderer;
import java.io.IOException;

/**
 * Empty base implementation which allows concrete implementations to override just the needed methods.
 */
public abstract class AbstractRendererInterceptor implements RendererInterceptor {

    protected AbstractRendererInterceptor() {
    }

    public String getInterceptorId() {
        return getClass().getName();
    }


    /*
    * before
    */

    /**
     * {@inheritDoc}
     */
    public void beforeDecode(FacesContext facesContext, UIComponent uiComponent, Renderer wrapped)
            throws SkipBeforeInterceptorsException, SkipRendererDelegationException {
    }

    /**
     * {@inheritDoc}
     */
    public void beforeEncodeBegin(FacesContext facesContext, UIComponent uiComponent, Renderer wrapped)
            throws IOException, SkipBeforeInterceptorsException, SkipRendererDelegationException {
    }

    /**
     * {@inheritDoc}
     */
    public void beforeEncodeChildren(FacesContext facesContext, UIComponent uiComponent, Renderer wrapped)
            throws IOException, SkipBeforeInterceptorsException, SkipRendererDelegationException {
    }

    /**
     * {@inheritDoc}
     */
    public void beforeEncodeEnd(FacesContext facesContext, UIComponent uiComponent, Renderer wrapped)
            throws IOException, SkipBeforeInterceptorsException, SkipRendererDelegationException {
    }

    /**
     * {@inheritDoc}
     */
    public void beforeGetConvertedValue(FacesContext facesContext, UIComponent uiComponent, Object o, Renderer wrapped)
            throws ConverterException, SkipBeforeInterceptorsException, SkipRendererDelegationException {
    }

    /*
     * after
     */

    /**
     * {@inheritDoc}
     */
    public void afterDecode(FacesContext facesContext, UIComponent uiComponent, Renderer wrapped)
            throws SkipAfterInterceptorsException {
    }

    /**
     * {@inheritDoc}
     */
    public void afterEncodeBegin(FacesContext facesContext, UIComponent uiComponent, Renderer wrapped)
            throws IOException, SkipAfterInterceptorsException {
    }

    /**
     * {@inheritDoc}
     */
    public void afterEncodeChildren(FacesContext facesContext, UIComponent uiComponent, Renderer wrapped)
            throws IOException, SkipAfterInterceptorsException {
    }

    /**
     * {@inheritDoc}
     */
    public void afterEncodeEnd(FacesContext facesContext, UIComponent uiComponent, Renderer wrapped)
            throws IOException, SkipAfterInterceptorsException {
    }

    /**
     * {@inheritDoc}
     */
    public void afterGetConvertedValue(FacesContext facesContext, UIComponent uiComponent, Object o, Renderer wrapped)
            throws ConverterException, SkipAfterInterceptorsException {
    }
}