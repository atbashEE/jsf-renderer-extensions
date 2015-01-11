package be.rubus.web.jerry.view;

import javax.enterprise.inject.Model;

/**
 *
 */
@Model
public class HelloBean {
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
