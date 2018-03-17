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
package be.atbash.ee.jsf.jerry.renderkit;

import be.atbash.ee.jsf.jerry.interceptor.RendererInterceptor;
import be.atbash.ee.jsf.jerry.interceptor.exception.SkipAfterInterceptorsException;
import be.atbash.ee.jsf.jerry.interceptor.exception.SkipBeforeInterceptorsException;
import be.atbash.ee.jsf.jerry.interceptor.exception.SkipRendererDelegationException;
import be.atbash.ee.jsf.jerry.utils.InvocationOrderedArtifactsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

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
                interceptor.afterGetConvertedValue(facesContext, uiComponent, o, convertedObject, this.wrapped);
            }
        } catch (SkipAfterInterceptorsException e) {
            if (logger.isTraceEnabled()) {
                logger.trace("afterGetConvertedValue interceptors canceled", e);
            }
        }

        return convertedObject;
    }
}
