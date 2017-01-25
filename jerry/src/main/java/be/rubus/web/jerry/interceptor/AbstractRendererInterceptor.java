/*
 * Copyright 2014-2016 Rudy De Busscher
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
    public void afterGetConvertedValue(FacesContext facesContext, UIComponent uiComponent, Object submittedValue, Object convertedValue,
                                       Renderer wrapped)
            throws ConverterException, SkipAfterInterceptorsException {
    }
}