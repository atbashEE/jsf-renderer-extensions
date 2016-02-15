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
 *
 */
package be.rubus.web.jerry.renderkit;

import javax.enterprise.inject.Typed;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitWrapper;
import javax.faces.render.Renderer;

/**
 *
 */
@Typed()
public class JerryRenderKit extends RenderKitWrapper {
    protected RenderKit wrapped;

    public JerryRenderKit(RenderKit wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public RenderKit getWrapped() {
        return wrapped;
    }

    public void addRenderer(String family, String rendererType, Renderer renderer) {
        if (renderer instanceof JerryRendererWrapper) {
            wrapped.addRenderer(family, rendererType, renderer);
        } else {
            wrapped.addRenderer(family, rendererType, createWrapper(renderer));
        }
    }

    public Renderer getRenderer(String family, String rendererType) {
        Renderer renderer = wrapped.getRenderer(family, rendererType);

        if (renderer != null) {
            return renderer instanceof JerryRendererWrapper ? renderer : createWrapper(renderer);
        }

        return null;
    }

    protected Renderer createWrapper(Renderer renderer) {
        return new JerryRendererWrapper(renderer);
    }

}
