package be.rubus.web.jerry.extension;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Typed;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

/**
 *
 */
@Typed
public class JerryExtension implements Extension {

    <T> void collectImplementations(@Observes ProcessAnnotatedType<T> pat, BeanManager beanManager) {
        // TODO register RendererInterceptor pat.getAnnotatedType().getJavaClass().getName());
    }

    void configModule(@Observes AfterBeanDiscovery afterBeanDiscovery, BeanManager beanManager) {
        // TODO Do we need this?
    }
}