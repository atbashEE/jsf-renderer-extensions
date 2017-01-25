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
package be.rubus.web.jerry.example.jerry;

import be.rubus.web.jerry.config.JerryConfigurator;
import be.rubus.web.jerry.renderkit.JerryRenderKit;

import javax.enterprise.inject.Specializes;
import javax.faces.render.RenderKit;

/**
 * TODO This is still here until it is documented.
 */
@Specializes
public class CustomConfigurator extends JerryConfigurator {
    @Override
    public JerryRenderKit getRenderKitWrapper(RenderKit renderKit) {
        // We can here return our custom implementation"
        return super.getRenderKitWrapper(renderKit);
    }
}
