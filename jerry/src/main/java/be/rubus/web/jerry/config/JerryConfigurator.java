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
package be.rubus.web.jerry.config;

import be.rubus.web.jerry.config.logging.ConfigEntry;
import be.rubus.web.jerry.config.logging.ModuleConfig;
import be.rubus.web.jerry.renderkit.JerryRenderKit;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.render.RenderKit;

/**
 *
 */
@ApplicationScoped
public class JerryConfigurator implements ModuleConfig {


    @ConfigEntry(classResult = JerryRenderKit.class)
    public JerryRenderKit getRenderKitWrapper(RenderKit renderKit) {

        return new JerryRenderKit(renderKit);
    }

    @ConfigEntry
    public boolean useBeanInfo() {
        return true;
    }
}
