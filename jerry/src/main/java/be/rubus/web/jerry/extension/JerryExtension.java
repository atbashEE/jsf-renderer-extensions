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
package be.rubus.web.jerry.extension;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Typed;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

/**
 * TODO: Seems that we don't neeed an extension
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