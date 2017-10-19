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

import javax.enterprise.context.ApplicationScoped;

/**
 *
 */
@ApplicationScoped
public class DynamicConfigValueHelper {

    public boolean isDynamicValue(String data) {
        return data.startsWith("{");
    }

    public String getTruncatedConfigValue(String data) {
        String result = data;
        if (data.startsWith("{")) {
            int idx = data.indexOf('}');
            int truncateLength = Integer.valueOf(data.substring(1, idx));
            if (truncateLength <= 0) {
                result = "[Dynamic value]";
            } else {
                String configValue = result.substring(idx + 1);
                if (truncateLength < configValue.length()) {
                    result = configValue.substring(0, truncateLength);
                } else {
                    result = configValue;
                }
            }
        }
        return result;
    }

    public String getCompleteConfigValue(String data) {
        String result = data;
        if (data.startsWith("{")) {
            int idx = data.indexOf('}');
            result = result.substring(idx + 1);

        }
        return result;
    }
}