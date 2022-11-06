/*
 * Copyright 2014-2022 Rudy De Busscher
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
package be.atbash.ee.jsf.jerry.component;

import be.atbash.ee.jsf.jerry.interceptor.AbstractRendererInterceptor;
import be.atbash.ee.jsf.jerry.interceptor.exception.SkipBeforeInterceptorsException;
import be.atbash.ee.jsf.jerry.interceptor.exception.SkipRendererDelegationException;
import be.atbash.ee.jsf.jerry.ordering.InvocationOrder;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.render.Renderer;
import jakarta.inject.Inject;
import java.io.IOException;

/**
 * An instance of a {@link be.atbash.ee.jsf.jerry.interceptor.RendererInterceptor} which executes all know
 * {@link be.atbash.ee.jsf.jerry.component.ComponentInitializer}s found in the application.
 */
@ApplicationScoped
@InvocationOrder(100)
public class InterceptorComponentInitializer extends AbstractRendererInterceptor {

    @Inject
    private ComponentInitializerManager manager;

    @Override
    public void beforeEncodeBegin(FacesContext facesContext, UIComponent uiComponent,
                                  Renderer wrapped) throws IOException, SkipBeforeInterceptorsException, SkipRendererDelegationException {
        manager.performInitialization(facesContext, uiComponent);

    }

}
