package be.rubus.web.jerry.view;

import be.rubus.web.valerie.custom.ValSize;

import javax.enterprise.inject.Model;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 */
@Model
public class RequiredBean {

    private String notRequired;

    @NotNull
    private String required;

    @Size(min = 1)
    private String defaultSize;

    @ValSize
    private String special;

    public String getNotRequired() {
        return notRequired;
    }

    public void setNotRequired(String notRequired) {
        this.notRequired = notRequired;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getDefaultSize() {
        return defaultSize;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }
}
