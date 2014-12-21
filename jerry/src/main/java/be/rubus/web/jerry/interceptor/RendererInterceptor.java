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
 * Allows to intercept renderer methods.<br/>
 * It's the base mechanism of Jerry which enables most of the concepts provided by the framework.
 * Furthermore, it allows to add custom concepts.
 */
public interface RendererInterceptor {

    String getInterceptorId();

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
     * @param renderer       The intercepted renderer
     * @throws ConverterException             Jerry validation strategies can throw
     *                                        {@link javax.faces.validator.ValidatorException}s.
     *                                        Due to the trick used by Jerry it has to be converted to a {@link ConverterException}
     *                                        (see {@link AbstractValidationInterceptor}). TODO
     * @throws SkipAfterInterceptorsException Can be thrown to stop the execution of the subsequent interceptors.
     */
    void afterGetConvertedValue(FacesContext facesContext, UIComponent uiComponent, Object submittedValue, Renderer
            renderer)
            throws ConverterException, SkipAfterInterceptorsException;
}