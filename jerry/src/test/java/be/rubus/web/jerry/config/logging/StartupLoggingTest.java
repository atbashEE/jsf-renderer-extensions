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
package be.rubus.web.jerry.config.logging;

import be.rubus.web.jerry.config.DynamicConfigValueHelper;
import be.rubus.web.jerry.util.TestReflectionUtils;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

public class StartupLoggingTest {

    private static final String VALUE_SEPARATOR = "value:";
    private static final String NO_LOGGING = "Nologgingparameteractive";

    @Test
    public void testGetConfigInfo() throws NoSuchFieldException, IllegalAccessException {
        StartupLogging logging = new StartupLogging();
        TestReflectionUtils.setFieldValue(logging, "valueHelper", new DynamicConfigValueHelper());

        String info = logging.getConfigInfo(new TestModuleConfig());

        assertThat(info).isNotEmpty();
        String data = info.replaceAll("\\s", "");

        assertThat(data).contains("defineStringConfigValue" + VALUE_SEPARATOR + "ConfigValue");
        assertThat(data).contains("defineLongConfigValue" + VALUE_SEPARATOR + "123");
        assertThat(data).contains("defineComplexConfig" + VALUE_SEPARATOR + "MyConfigObject{}");
        assertThat(data).contains("canHandleNull" + VALUE_SEPARATOR + "null");
        assertThat(data).contains("needWarningInLog" + VALUE_SEPARATOR + "unknown-Methodhasaparameter");
        assertThat(data).contains("needWarningInLogNoReturn" + VALUE_SEPARATOR + "unknown-Methodhasnoreturnvalue");
        assertThat(data).doesNotContain("doNotLog");
        assertThat(data).doesNotContain("Should never be in log");
        assertThat(data).contains("getConfigValueWithParameter" + VALUE_SEPARATOR + "[ConfigWithparameter]");
        assertThat(data).contains("getClassResultConfigValue" + VALUE_SEPARATOR + "be.rubus.web.jerry.config.logging.StartupLoggingTest.MyConfigObject");
    }

    @Test
    public void testGetConfigInfo_NoLogging() {
        StartupLogging logging = new StartupLogging();
        String info = logging.getConfigInfo(new NoLogConfig());

        assertThat(info).isNotEmpty();
        String data = info.replaceAll("\\s", "");

        assertThat(data).contains("defineConfigValue" + VALUE_SEPARATOR + NO_LOGGING + "[nonnullvalue]");
        assertThat(data).contains("defineNullConfigValue" + VALUE_SEPARATOR + NO_LOGGING + "null");

    }

    @Test
    public void testGetConfigInfo_AllLogging() throws NoSuchFieldException, IllegalAccessException {
        System.setProperty("jerry.log.all", "tRue");

        StartupLogging logging = new StartupLogging();
        TestReflectionUtils.setFieldValue(logging, "valueHelper", new DynamicConfigValueHelper());

        Field allLoggingActivated = logging.getClass().getDeclaredField("allLoggingActivated");
        allLoggingActivated.setAccessible(true);
        allLoggingActivated.setBoolean(logging, Boolean.TRUE);

        String info = logging.getConfigInfo(new NoLogConfig());

        assertThat(info).isNotEmpty();
        String data = info.replaceAll("\\s", "");

        assertThat(data).contains("defineConfigValue" + VALUE_SEPARATOR + "SecretValue");
        assertThat(data).doesNotContain("defineNullConfigValue" + VALUE_SEPARATOR + NO_LOGGING);

    }

    private static class TestModuleConfig implements ModuleConfig {

        @ConfigEntry
        public String defineStringConfigValue() {
            return "Config Value";
        }

        @ConfigEntry
        public Long defineLongConfigValue() {
            return 123L;
        }

        @ConfigEntry
        public MyConfigObject defineComplexConfig() {
            return new MyConfigObject();
        }

        @ConfigEntry
        public String canHandleNull() {
            return null;
        }

        @ConfigEntry
        public String needWarningInLog(Long parameter) {
            return null;
        }

        @ConfigEntry
        public void needWarningInLogNoReturn() {
        }

        public String doNotLog() {
            return "Should never be in log";
        }

        @ConfigEntry("Config With parameter")
        public Long getConfigValueWithParameter(String code) {
            return null;
        }

        @ConfigEntry(classResult = MyConfigObject.class)
        public MyConfigObject getClassResultConfigValue() {
            return null;

        }
    }

    private static class NoLogConfig implements ModuleConfig {
        @ConfigEntry(noLogging = true)
        public String defineConfigValue() {
            return "Secret Value";
        }

        @ConfigEntry(noLogging = true)
        public Long defineNullConfigValue() {
            return null;
        }

    }

    public static class MyConfigObject {
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("MyConfigObject{");
            sb.append('}');
            return sb.toString();
        }
    }
}