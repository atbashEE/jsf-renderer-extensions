/*
 * Copyright 2014-2020 Rudy De Busscher
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

import be.atbash.ee.jsf.jerry.config.JerryConfiguration;
import be.atbash.util.CDIUtils;
import be.atbash.util.reflection.ClassUtils;

import javax.enterprise.inject.Typed;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.RenderKitWrapper;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 */
@Typed()
public class JerryRenderKitFactory extends RenderKitFactory {

    private JerryConfiguration configurator;

    private Map<String, RenderKit> renderKitMap;
    private Class<? extends RenderKitWrapper> wrapperClass;

    public JerryRenderKitFactory(RenderKitFactory wrapped) {
        super(wrapped);
        renderKitMap = new HashMap<>();

        configurator = CDIUtils.retrieveInstance(JerryConfiguration.class);

        wrapperClass = configurator.getRenderKitWrapperClass();
    }

    @Override
    public void addRenderKit(String renderKitId, RenderKit renderKit) {
        getWrapped().addRenderKit(renderKitId, renderKit);
    }

    @Override
    public RenderKit getRenderKit(FacesContext context, String renderKitId) {
        RenderKit result =  renderKitMap.get(renderKitId);

        if (result == null) {
            RenderKit renderKit = getWrapped().getRenderKit(context, renderKitId);

            result = ClassUtils.newInstance(wrapperClass, renderKit);
            renderKitMap.put(renderKitId, result);
        }
        return result;
    }

    @Override
    public Iterator<String> getRenderKitIds() {
        return getWrapped().getRenderKitIds();
    }
}
