package be.rubus.web.jerry.renderkit;

import be.rubus.web.jerry.interceptor.RendererInterceptor;
import be.rubus.web.jerry.interceptor.exception.SkipAfterInterceptorsException;
import be.rubus.web.jerry.interceptor.exception.SkipBeforeInterceptorsException;
import be.rubus.web.jerry.interceptor.exception.SkipRendererDelegationException;
import be.rubus.web.jerry.producer.LogProducer;
import be.rubus.web.jerry.utils.InvocationOrderedArtifactsProvider;
import org.slf4j.Logger;

import javax.enterprise.inject.Typed;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.List;

/**
 *
 */
public class JerryRendererWrapper extends Renderer {
    protected Logger logger = LogProducer.getLogger(this.getClass());

    protected Renderer wrapped;
    private List<RendererInterceptor> rendererInterceptors;

    public JerryRendererWrapper(Renderer renderer) {
        this.wrapped = renderer;
        rendererInterceptors = InvocationOrderedArtifactsProvider.getRendererInterceptors();
    }

    @Override
    public final void decode(FacesContext facesContext, UIComponent uiComponent) {
        boolean delegateToWrappedRenderer = true;

        try {
            for (RendererInterceptor interceptor : rendererInterceptors) {
                try {
                    interceptor.beforeDecode(facesContext, uiComponent, this.wrapped);
                } catch (SkipRendererDelegationException e) {

                    delegateToWrappedRenderer = false;

                    if (e.isSkipOtherInterceptors()) {
                        break;
                    }
                }
            }
        } catch (SkipBeforeInterceptorsException e) {
            if (logger.isTraceEnabled()) {
                logger.trace("beforeDecode interceptors canceled", e);
            }
        }

        /*
         * delegate
         */
        if (delegateToWrappedRenderer) {
            wrapped.decode(facesContext, uiComponent);
        }

        try {
            for (RendererInterceptor interceptor : rendererInterceptors) {
                interceptor.afterDecode(facesContext, uiComponent, this.wrapped);
            }
        } catch (SkipAfterInterceptorsException e) {
            if (logger.isTraceEnabled()) {
                logger.trace("afterDecode interceptors canceled", e);
            }
        }
    }

    @Override
    public final void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        boolean delegateToWrappedRenderer = true;

        try {
            for (RendererInterceptor interceptor : rendererInterceptors) {
                try {
                    interceptor.beforeEncodeBegin(facesContext, uiComponent, this.wrapped);
                } catch (SkipRendererDelegationException e) {

                    delegateToWrappedRenderer = false;

                    if (e.isSkipOtherInterceptors()) {
                        break;
                    }
                }
            }
        } catch (SkipBeforeInterceptorsException e) {
            if (logger.isTraceEnabled()) {
                logger.trace("beforeDecode interceptors canceled", e);
            }
        }

        /*
         * delegate
         */
        if (delegateToWrappedRenderer) {
            wrapped.encodeBegin(facesContext, uiComponent);
        }

        try {
            for (RendererInterceptor interceptor : rendererInterceptors) {
                interceptor.afterEncodeBegin(facesContext, uiComponent, this.wrapped);
            }
        } catch (SkipAfterInterceptorsException e) {
            if (logger.isTraceEnabled()) {
                logger.trace("afterEncodeBegin interceptors canceled", e);
            }
        }

    }

    @Override
    public final void encodeChildren(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        boolean delegateToWrappedRenderer = true;

        try {
            for (RendererInterceptor interceptor : rendererInterceptors) {
                try {
                    interceptor.beforeEncodeChildren(facesContext, uiComponent, this.wrapped);
                } catch (SkipRendererDelegationException e) {

                    delegateToWrappedRenderer = false;

                    if (e.isSkipOtherInterceptors()) {
                        break;
                    }
                }
            }
        } catch (SkipBeforeInterceptorsException e) {
            if (logger.isTraceEnabled()) {
                logger.trace("beforeDecode interceptors canceled", e);
            }
        }

        /*
         * delegate
         */
        if (delegateToWrappedRenderer) {
            wrapped.encodeChildren(facesContext, uiComponent);
        }

        try {
            for (RendererInterceptor interceptor : rendererInterceptors) {
                interceptor.afterEncodeChildren(facesContext, uiComponent, this.wrapped);
            }
        } catch (SkipAfterInterceptorsException e) {
            if (logger.isTraceEnabled()) {
                logger.trace("afterEncodeChildren interceptors canceled", e);
            }
        }


    }

    @Override
    public final void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        boolean delegateToWrappedRenderer = true;

        try {
            for (RendererInterceptor interceptor : rendererInterceptors) {
                try {
                    interceptor.beforeEncodeEnd(facesContext, uiComponent, this.wrapped);
                } catch (SkipRendererDelegationException e) {

                    delegateToWrappedRenderer = false;

                    if (e.isSkipOtherInterceptors()) {
                        break;
                    }
                }
            }
        } catch (SkipBeforeInterceptorsException e) {
            if (logger.isTraceEnabled()) {
                logger.trace("beforeDecode interceptors canceled", e);
            }
        }

        /*
         * delegate
         */
        if (delegateToWrappedRenderer) {
            wrapped.encodeEnd(facesContext, uiComponent);
        }

        try {
            for (RendererInterceptor interceptor : rendererInterceptors) {
                interceptor.afterEncodeEnd(facesContext, uiComponent, this.wrapped);
            }
        } catch (SkipAfterInterceptorsException e) {
            if (logger.isTraceEnabled()) {
                logger.trace("afterEncodeEnd interceptors canceled", e);
            }
        }

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

        try {
            for (RendererInterceptor interceptor : rendererInterceptors) {
                try {
                    interceptor.beforeGetConvertedValue(facesContext, uiComponent, o, this.wrapped);
                } catch (SkipRendererDelegationException e) {

                    delegateToWrappedRenderer = false;

                    if (e.isSkipOtherInterceptors()) {
                        break;
                    }
                }
            }
        } catch (SkipBeforeInterceptorsException e) {
            if (logger.isTraceEnabled()) {
                logger.trace("beforeDecode interceptors canceled", e);
            }
        }
            /*
             * delegate
             */
        if (delegateToWrappedRenderer) {
            convertedObject = wrapped.getConvertedValue(facesContext, uiComponent, o);
        }

        try {
            for (RendererInterceptor interceptor : rendererInterceptors) {
                interceptor.afterGetConvertedValue(facesContext, uiComponent, o, this.wrapped);
            }
        } catch (SkipAfterInterceptorsException e) {
            if (logger.isTraceEnabled()) {
                logger.trace("afterGetConvertedValue interceptors canceled", e);
            }
        }


        return convertedObject;
    }
}
