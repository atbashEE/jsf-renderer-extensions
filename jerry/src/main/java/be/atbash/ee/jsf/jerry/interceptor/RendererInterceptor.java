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
package be.atbash.ee.jsf.jerry.interceptor;

import be.atbash.ee.jsf.jerry.interceptor.exception.SkipAfterInterceptorsException;
import be.atbash.ee.jsf.jerry.interceptor.exception.SkipBeforeInterceptorsException;
import be.atbash.ee.jsf.jerry.interceptor.exception.SkipRendererDelegationException;
import be.atbash.util.PublicAPI;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.render.Renderer;
import java.io.IOException;

/**
 * Allows to intercept renderer methods.<br/>
 * It's the base mechanism of Jerry which enables most of the concepts provided by the framework.
 * Furthermore, it allows to add custom concepts.
 */
@PublicAPI
public interface RendererInterceptor {

    /*
     * before
     */

    /**
     * Intercepts a method of the renderer. The name of the intercepted method is the name of this method without the
     * prefix ('before').
     *
     * @param facesContext The JSF Context
     * @param uiComponent  The current component
     * @param renderer     The intercepted renderer
     * @throws SkipBeforeInterceptorsException can be thrown to stop the execution of the subsequent interceptors
     * @throws SkipRendererDelegationException can be thrown to skip the invocation of the intercepted renderer method.
     */
    void beforeDecode(FacesContext facesContext, UIComponent uiComponent, Renderer renderer)
            throws SkipBeforeInterceptorsException, SkipRendererDelegationException;

    /**
     * Intercepts a method of the renderer. The name of the intercepted method is the name of this method without the
     * prefix ('before').
     *
     * @param facesContext The JSF Context
     * @param uiComponent  The current component
     * @param renderer     The intercepted renderer
     * @throws java.io.IOException             In case the response writer is accessed and there was an IO problem.
     * @throws SkipBeforeInterceptorsException can be thrown to stop the execution of the subsequent interceptors
     * @throws SkipRendererDelegationException can be thorwn to skip the invocation of the intercepted renderer method.
     */
    void beforeEncodeBegin(FacesContext facesContext, UIComponent uiComponent, Renderer renderer)
            throws IOException, SkipBeforeInterceptorsException, SkipRendererDelegationException;

    /**
     * Intercepts a method of the renderer. The name of the intercepted method is the name of this method without the
     * prefix ('before').
     *
     * @param facesContext The JSF Context
     * @param uiComponent  The current component
     * @param renderer     The intercepted renderer
     * @throws IOException                     In case the response writer is accessed and there was an IO problem.
     * @throws SkipBeforeInterceptorsException can be thrown to stop the execution of the subsequent interceptors
     * @throws SkipRendererDelegationException can be thorwn to skip the invocation of the intercepted renderer method.
     */
    void beforeEncodeChildren(FacesContext facesContext, UIComponent uiComponent, Renderer renderer)
            throws IOException, SkipBeforeInterceptorsException, SkipRendererDelegationException;

    /**
     * Intercepts a method of the renderer. The name of the intercepted method is the name of this method without the
     * prefix ('before').
     *
     * @param facesContext The JSF Context
     * @param uiComponent  The current component
     * @param renderer     The intercepted renderer
     * @throws IOException                     In case the response writer is accessed and there was an IO problem.
     * @throws SkipBeforeInterceptorsException can be thrown to stop the execution of the subsequent interceptors
     * @throws SkipRendererDelegationException can be thorwn to skip the invocation of the intercepted renderer method.
     */
    void beforeEncodeEnd(FacesContext facesContext, UIComponent uiComponent, Renderer renderer)
            throws IOException, SkipBeforeInterceptorsException, SkipRendererDelegationException;

    /**
     * Intercepts a method of the renderer. The name of the intercepted method is the name of this method without the
     * prefix ('before').
     *
     * @param facesContext   The JSF Context
     * @param uiComponent    The current component
     * @param submittedValue The submitted value
     * @param renderer       The intercepted renderer
     * @throws javax.faces.convert.ConverterException Jerry validation strategies can throw
     *                                                {@link javax.faces.validator.ValidatorException}s.
     *                                                Due to the trick used by Jerry it has to be converted to a {@link javax.faces.convert.ConverterException}
     *                                                (see {@link AbstractValidationInterceptor}).  TODO
     * @throws SkipBeforeInterceptorsException        can be thrown to stop the execution of the subsequent interceptors
     * @throws SkipRendererDelegationException        can be thrown to skip the invocation of the intercepted renderer method.
     */
    void beforeGetConvertedValue(FacesContext facesContext, UIComponent uiComponent, Object submittedValue,
                                 Renderer renderer)
            throws ConverterException, SkipBeforeInterceptorsException, SkipRendererDelegationException;

    /*
     * after
     */

    /**
     * Intercepts a method of the renderer. The name of the intercepted method is the name of this method without the
     * prefix ('after').
     *
     * @param facesContext The JSF Context
     * @param uiComponent  The current component
     * @param renderer     The intercepted renderer
     * @throws SkipAfterInterceptorsException Can be thrown to stop the execution of the subsequent interceptors.
     */
    void afterDecode(FacesContext facesContext, UIComponent uiComponent, Renderer renderer)
            throws SkipAfterInterceptorsException;

    /**
     * Intercepts a method of the renderer. The name of the intercepted method is the name of this method without the
     * prefix ('after').
     *
     * @param facesContext The JSF context
     * @param uiComponent  The current component
     * @param renderer     The intercepted renderer
     * @throws IOException                    In case the response writer is accessed and there was an IO problem.
     * @throws SkipAfterInterceptorsException Can be thrown to stop the execution of the subsequent interceptors.
     */
    void afterEncodeBegin(FacesContext facesContext, UIComponent uiComponent, Renderer renderer)
            throws IOException, SkipAfterInterceptorsException;

    /**
     * Intercepts a method of the renderer. The name of the intercepted method is the name of this method without the
     * prefix ('after').
     *
     * @param facesContext The JSF Context
     * @param uiComponent  The current component
     * @param renderer     The intercepted renderer
     * @throws IOException                    In case the response writer is accessed and there was an IO problem.
     * @throws SkipAfterInterceptorsException Can be thrown to stop the execution of the subsequent interceptors.
     */
    void afterEncodeChildren(FacesContext facesContext, UIComponent uiComponent, Renderer renderer)
            throws IOException, SkipAfterInterceptorsException;

    /**
     * Intercepts a method of the renderer. The name of the intercepted method is the name of this method without the
     * prefix ('after').
     *
     * @param facesContext The JSF Context
     * @param uiComponent  The current component
     * @param renderer     The intercepted renderer
     * @throws IOException                    In case the response writer is accessed and there was an IO problem.
     * @throws SkipAfterInterceptorsException Can be thrown to stop the execution of the subsequent interceptors.
     */
    void afterEncodeEnd(FacesContext facesContext, UIComponent uiComponent, Renderer renderer)
            throws IOException, SkipAfterInterceptorsException;

    /**
     * Intercepts a method of the renderer. The name of the intercepted method is the name of this method without the
     * prefix ('after').
     *
     * @param facesContext   The JSF Context
     * @param uiComponent    The current component
     * @param submittedValue The submitted value
     * @param convertedValue The converted value
     * @param renderer       The intercepted renderer
     * @throws ConverterException             Jerry validation strategies can throw
     *                                        {@link javax.faces.validator.ValidatorException}s.
     *                                        Due to the trick used by Jerry it has to be converted to a {@link ConverterException}
     *                                        (see {@link AbstractValidationInterceptor}). TODO
     * @throws SkipAfterInterceptorsException Can be thrown to stop the execution of the subsequent interceptors.
     */
    void afterGetConvertedValue(FacesContext facesContext, UIComponent uiComponent, Object submittedValue, Object convertedValue,
                                Renderer renderer)
            throws ConverterException, SkipAfterInterceptorsException;
}