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
package be.rubus.web.jerry.config.logging;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StartupLoggingTest {

    public static final String VALUE_SEPARATOR = "value:";

    @Test
    public void testGetConfigInfo() {
        StartupLogging logging = new StartupLogging();
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

    public static class MyConfigObject {
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("MyConfigObject{");
            sb.append('}');
            return sb.toString();
        }
    }
}