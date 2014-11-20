package be.rubus.web.jerry.utils;

import be.rubus.web.jerry.component.ComponentInitializer;
import be.rubus.web.jerry.interceptor.RendererInterceptor;
import be.rubus.web.jerry.ordering.InvocationOrderComparator;
import be.rubus.web.jerry.provider.BeanProvider;

import java.util.Collections;
import java.util.List;

/**
 *
 */
public final class InvocationOrderedArtifactsProvider {

    private static final InvocationOrderedArtifactsProvider INSTANCE = new InvocationOrderedArtifactsProvider();
    private static final Object LOCK = new Object();

    private List<RendererInterceptor> rendererInterceptors;
    private List<ComponentInitializer> initializers;

    private InvocationOrderedArtifactsProvider() {
    }


    private void prepareRendererInterceptors() {
        rendererInterceptors = BeanProvider.getContextualReferences(RendererInterceptor.class, true, false);
        Collections.sort(rendererInterceptors, new InvocationOrderComparator<RendererInterceptor>());
    }

    public static List<RendererInterceptor> getRendererInterceptors() {
        if (INSTANCE.rendererInterceptors == null) {
            synchronized (LOCK) {
                INSTANCE.prepareRendererInterceptors();
            }
        }
        return INSTANCE.rendererInterceptors;
    }

    private void prepareInitializers() {
        initializers = BeanProvider.getContextualReferences(ComponentInitializer.class, true, false);
        Collections.sort(initializers, new InvocationOrderComparator<ComponentInitializer>());
    }

    public static List<ComponentInitializer> getComponentInitializers() {
        if (INSTANCE.initializers == null) {
            synchronized (LOCK) {
                INSTANCE.prepareInitializers();
            }
        }
        return INSTANCE.initializers;
    }

}
