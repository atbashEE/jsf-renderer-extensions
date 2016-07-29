package be.rubus.web.jerry.config;

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