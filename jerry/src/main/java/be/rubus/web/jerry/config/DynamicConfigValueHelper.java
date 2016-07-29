package be.rubus.web.jerry.config;

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