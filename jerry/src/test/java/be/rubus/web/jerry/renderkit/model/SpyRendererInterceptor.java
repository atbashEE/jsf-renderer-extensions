/*
 * Copyright 2014-2015 Rudy De Busscher
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
 *
 */
package be.rubus.web.jerry.renderkit.model;

import be.rubus.web.jerry.interceptor.RendererInterceptor;
import be.rubus.web.jerry.interceptor.exception.SkipAfterInterceptorsException;
import be.rubus.web.jerry.interceptor.exception.SkipBeforeInterceptorsException;
import be.rubus.web.jerry.interceptor.exception.SkipRendererDelegationException;
import be.rubus.web.jerry.ordering.InvocationOrder;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 */
@InvocationOrder
public class SpyRendererInterceptor implements RendererInterceptor {

    private Set<InterceptorCalls> callsPerformed = new HashSet<>();
    private Set<InterceptorCalls> throwException = new HashSet<>();
    private Set<InterceptorCalls> throwRendererException = new HashSet<>();
    private boolean skipOtherInterceptors;

    public void throwException(InterceptorCalls interceptorCalls) {
        throwException.add(interceptorCalls);
    }

    public void throwRendererException(InterceptorCalls interceptorCalls) {
        throwRendererException.add(interceptorCalls);
    }

    public boolean isCalled(InterceptorCalls interceptorCalls) {
        return callsPerformed.contains(interceptorCalls);
    }

    public void setSkipOtherInterceptors() {
        skipOtherInterceptors = true;
    }

    @Override
    public void beforeDecode(FacesContext facesContext, UIComponent uiComponent, Renderer renderer) throws SkipBeforeInterceptorsException, SkipRendererDelegationException {
        callsPerformed.add(InterceptorCalls.BEFORE_DECODE);
        if (throwRendererException.contains(InterceptorCalls.BEFORE_DECODE)) {
            throw new SkipRendererDelegationException(skipOtherInterceptors);
        }
        if (throwException.contains(InterceptorCalls.BEFORE_DECODE)) {
            throw new SkipBeforeInterceptorsException();
        }

    }

    @Override
    public void beforeEncodeBegin(FacesContext facesContext, UIComponent uiComponent,
                                  Renderer renderer) throws IOException, SkipBeforeInterceptorsException, SkipRendererDelegationException {
        callsPerformed.add(InterceptorCalls.BEFORE_ENCODE_BEGIN);
        if (throwRendererException.contains(InterceptorCalls.BEFORE_ENCODE_BEGIN)) {
            throw new SkipRendererDelegationException(skipOtherInterceptors);
        }
        if (throwException.contains(InterceptorCalls.BEFORE_ENCODE_BEGIN)) {
            throw new SkipBeforeInterceptorsException();
        }
    }

    @Override
    public void beforeEncodeChildren(FacesContext facesContext, UIComponent uiComponent,
                                     Renderer renderer) throws IOException, SkipBeforeInterceptorsException, SkipRendererDelegationException {
        callsPerformed.add(InterceptorCalls.BEFORE_ENCODE_CHILDREN);
        if (throwRendererException.contains(InterceptorCalls.BEFORE_ENCODE_CHILDREN)) {
            throw new SkipRendererDelegationException(skipOtherInterceptors);
        }
        if (throwException.contains(InterceptorCalls.BEFORE_ENCODE_CHILDREN)) {
            throw new SkipBeforeInterceptorsException();
        }

    }

    @Override
    public void beforeEncodeEnd(FacesContext facesContext, UIComponent uiComponent,
                                Renderer renderer) throws IOException, SkipBeforeInterceptorsException, SkipRendererDelegationException {
        callsPerformed.add(InterceptorCalls.BEFORE_ENCODE_END);
        if (throwRendererException.contains(InterceptorCalls.BEFORE_ENCODE_END)) {
            throw new SkipRendererDelegationException(skipOtherInterceptors);
        }
        if (throwException.contains(InterceptorCalls.BEFORE_ENCODE_END)) {
            throw new SkipBeforeInterceptorsException();
        }

    }

    @Override
    public void beforeGetConvertedValue(FacesContext facesContext, UIComponent uiComponent, Object submittedValue,
                                        Renderer renderer) throws ConverterException, SkipBeforeInterceptorsException, SkipRendererDelegationException {
        callsPerformed.add(InterceptorCalls.BEFORE_CONVERTED_VALUE);
        if (throwRendererException.contains(InterceptorCalls.BEFORE_CONVERTED_VALUE)) {
            throw new SkipRendererDelegationException(skipOtherInterceptors);
        }
        if (throwException.contains(InterceptorCalls.BEFORE_CONVERTED_VALUE)) {
            throw new SkipBeforeInterceptorsException();
        }

    }

    @Override
    public void afterDecode(FacesContext facesContext, UIComponent uiComponent, Renderer renderer) throws SkipAfterInterceptorsException {
        callsPerformed.add(InterceptorCalls.AFTER_DECODE);
        if (throwException.contains(InterceptorCalls.AFTER_DECODE)) {
            throw new SkipAfterInterceptorsException();
        }

    }

    @Override
    public void afterEncodeBegin(FacesContext facesContext, UIComponent uiComponent, Renderer renderer) throws IOException, SkipAfterInterceptorsException {
        callsPerformed.add(InterceptorCalls.AFTER_ENCODE_BEGIN);
        if (throwException.contains(InterceptorCalls.AFTER_ENCODE_BEGIN)) {
            throw new SkipAfterInterceptorsException();
        }

    }

    @Override
    public void afterEncodeChildren(FacesContext facesContext, UIComponent uiComponent, Renderer renderer) throws IOException, SkipAfterInterceptorsException {
        callsPerformed.add(InterceptorCalls.AFTER_ENCODE_CHILDREN);
        if (throwException.contains(InterceptorCalls.AFTER_ENCODE_CHILDREN)) {
            throw new SkipAfterInterceptorsException();
        }

    }

    @Override
    public void afterEncodeEnd(FacesContext facesContext, UIComponent uiComponent, Renderer renderer) throws IOException, SkipAfterInterceptorsException {
        callsPerformed.add(InterceptorCalls.AFTER_ENCODE_END);
        if (throwException.contains(InterceptorCalls.AFTER_ENCODE_END)) {
            throw new SkipAfterInterceptorsException();
        }

    }

    @Override
    public void afterGetConvertedValue(FacesContext facesContext, UIComponent uiComponent, Object submittedValue,
                                       Renderer renderer) throws ConverterException, SkipAfterInterceptorsException {
        callsPerformed.add(InterceptorCalls.AFTER_CONVERTED_VALUE);
        if (throwException.contains(InterceptorCalls.AFTER_CONVERTED_VALUE)) {
            throw new SkipAfterInterceptorsException();
        }

    }

    @InvocationOrder(1001)
    public static class Second extends SpyRendererInterceptor {

    }
}
