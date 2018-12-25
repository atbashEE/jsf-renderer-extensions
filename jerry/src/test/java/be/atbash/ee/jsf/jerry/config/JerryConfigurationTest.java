/*
 * Copyright 2014-2018 Rudy De Busscher
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

import be.atbash.config.converter.ClassConverter;
import be.atbash.config.exception.ConfigurationException;
import be.atbash.config.test.TestConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */

public class JerryConfigurationTest {

    private JerryConfiguration configuration = new JerryConfiguration();

    @Before
    public void setupTestClass() {
        TestConfig.registerDefaultConverters();
        TestConfig.registerConverter(new ClassConverter());
    }

    @After
    public void teardown() {
        TestConfig.resetConfig();
    }

    @Test
    public void getRenderKitWrapperClass() {
        assertThat(configuration.getRenderKitWrapperClass().getName()).isEqualTo("be.atbash.ee.jsf.jerry.renderkit.JerryRenderKit");
    }

    @Test
    public void getRenderKitWrapperClass_customClass() {
        TestConfig.addConfigValue("jerry.renderkit.wrapper.class", CustomWrapper.class.getName());
        assertThat(configuration.getRenderKitWrapperClass().getName()).isEqualTo(CustomWrapper.class.getName());
    }

    @Test(expected = ConfigurationException.class)
    public void getRenderKitWrapperClass_customClass_wrongType() {
        TestConfig.addConfigValue("jerry.renderkit.wrapper.class", String.class.getName());

        configuration.getRenderKitWrapperClass();
    }

    @Test(expected = ConfigurationException.class)
    public void getRenderKitWrapperClass_customClass_nonExistent() {
        TestConfig.addConfigValue("jerry.renderkit.wrapper.class", "org.foo.something");

        configuration.getRenderKitWrapperClass();
    }
}