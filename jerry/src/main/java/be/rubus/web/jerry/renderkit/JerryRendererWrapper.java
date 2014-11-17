package be.rubus.web.jerry.renderkit;

import javax.enterprise.inject.Typed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.render.Renderer;
import java.io.IOException;

/**
 *
 */
@Typed()
public class JerryRendererWrapper extends Renderer {

    protected Renderer wrapped;

    public JerryRendererWrapper(Renderer renderer) {
        this.wrapped = renderer;
    }

    @Override
    public final void decode(FacesContext facesContext, UIComponent uiComponent) {
        boolean delegateToWrappedRenderer = true;

        // TODO beforeDecode from the RendererInterceptor's
        /*
         * delegate
         */
        if (delegateToWrappedRenderer) {
            wrapped.decode(facesContext, uiComponent);
        }
        // TODO afterDecode from the RendererInterceptor's
    }

    @Override
    public final void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        boolean delegateToWrappedRenderer = true;

        // TODO beforeEncodeBegin from the RendererInterceptor's

        /*
         * delegate
         */
        if (delegateToWrappedRenderer) {
            wrapped.encodeBegin(facesContext, uiComponent);
        }

        // TODO afterEncodeBegin from the RendererInterceptor's

    }

    @Override
    public final void encodeChildren(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        boolean delegateToWrappedRenderer = true;

        // TODO beforeEncodeChildren from the RendererInterceptor's


        /*
         * delegate
         */
        if (delegateToWrappedRenderer) {
            wrapped.encodeChildren(facesContext, uiComponent);
        }

        // TODO afterEncodeChildren from the RendererInterceptor's


    }

    @Override
    public final void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        boolean delegateToWrappedRenderer = true;

        // TODO beforeEncodeEnd from the RendererInterceptor's


        /*
         * delegate
         */
        if (delegateToWrappedRenderer) {
            wrapped.encodeEnd(facesContext, uiComponent);
        }

        // TODO afterEncodeEnd from the RendererInterceptor's

    }

    @Override
    public final String convertClientId(FacesContext facesContext, String s) {
        return wrapped.convertClientId(facesContext, s);
    }

    @Override
    public final boolean getRendersChildren() {
        return wrapped.getRendersChildren();
    }

    @Override
    public final Object getConvertedValue(FacesContext facesContext, UIComponent uiComponent, Object o)
            throws ConverterException {
        boolean delegateToWrappedRenderer = true;
        Object convertedObject = null;

        // TODO beforeGetConvertedValue from the RendererInterceptor's



            /*
             * delegate
             */
        if (delegateToWrappedRenderer) {
            convertedObject = wrapped.getConvertedValue(facesContext, uiComponent, o);
        }

        // TODO afterGetConvertedValue from the RendererInterceptor's


        return convertedObject;
    }
}
