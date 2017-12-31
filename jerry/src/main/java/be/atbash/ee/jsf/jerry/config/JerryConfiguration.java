/*
 * Copyright 2014-2017 Rudy De Busscher
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
package be.atbash.ee.jsf.jerry.config;

import be.atbash.config.AbstractConfiguration;
import be.atbash.config.exception.ConfigurationException;
import be.atbash.config.logging.ConfigEntry;
import be.atbash.config.logging.ModuleConfig;
import be.atbash.ee.jsf.jerry.renderkit.JerryRenderKit;
import be.atbash.ee.jsf.jerry.startup.StartupEvent;
import be.atbash.ee.jsf.jerry.utils.ClassUtils;
import be.atbash.ee.jsf.jerry.utils.UnknownClassException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.faces.render.RenderKitWrapper;

/**
 *
 */
@ApplicationScoped
public class JerryConfiguration extends AbstractConfiguration implements ModuleConfig {

    private boolean jsfReady = false;

    public void onStartup(@Observes StartupEvent startupEvent) {
        jsfReady = true;
    }

    @ConfigEntry
    public String getRenderKitWrapperClass() {
        String renderKitWrapperClassName = getOptionalValue("jerry.renderkit.wrapper.class", JerryRenderKit.class.getName(), String.class);
        try {
            Class renderKitWrapperClass = ClassUtils.forName(renderKitWrapperClassName);

            if (!RenderKitWrapper.class.isAssignableFrom(renderKitWrapperClass)) {
                throw new ConfigurationException("Class specified in parameter 'jerry.renderkit.wrapper.class' must be extending from javax.faces.render.RenderKitWrapper");
            }
        } catch (UnknownClassException e) {
            throw new ConfigurationException(String.format("Class specified in parameter 'jerry.renderkit.wrapper.class' not found '%s'", renderKitWrapperClassName));
        }

        return renderKitWrapperClassName;
    }

    public boolean isJsfReady() {
        return jsfReady;
    }
}
