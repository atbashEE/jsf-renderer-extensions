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
package be.atbash.ee.jsf.jerry.config;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
public class DynamicConfigValueHelperTest {

    private DynamicConfigValueHelper dynamicConfigValueHelper = new DynamicConfigValueHelper();

    @Test
    public void getTruncatedConfigValue_stdCase() {
        String value = dynamicConfigValueHelper.getTruncatedConfigValue("{5}12345678");
        assertThat(value).isEqualTo("12345");
    }

    @Test
    public void getTruncatedConfigValue_LongerThenLenght() {
        String value = dynamicConfigValueHelper.getTruncatedConfigValue("{7}1234");
        assertThat(value).isEqualTo("1234");
    }

    @Test
    public void getTruncatedConfigValue_NoTruncationInfo() {
        String value = dynamicConfigValueHelper.getTruncatedConfigValue("123456");
        assertThat(value).isEqualTo("123456");
    }

    @Test
    public void getTruncatedConfigValue_CompleteTruncation() {
        String value = dynamicConfigValueHelper.getTruncatedConfigValue("{0}123456");
        assertThat(value).isEqualTo("[Dynamic value]");
    }

    @Test
    public void getCompleteConfigValue_LongerThenLenght() {
        String value = dynamicConfigValueHelper.getCompleteConfigValue("{7}1234");
        assertThat(value).isEqualTo("1234");
    }

    @Test
    public void getCompleteConfigValue_NoTruncationInfo() {
        String value = dynamicConfigValueHelper.getCompleteConfigValue("123");
        assertThat(value).isEqualTo("123");
    }

    @Test
    public void getCompleteConfigValue_CompleteTruncation() {
        String value = dynamicConfigValueHelper.getCompleteConfigValue("{0}123456");
        assertThat(value).isEqualTo("123456");
    }

}