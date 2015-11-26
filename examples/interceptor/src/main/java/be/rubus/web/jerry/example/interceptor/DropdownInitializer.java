package be.rubus.web.jerry.example.interceptor;

import be.rubus.web.jerry.component.ComponentInitializer;
import org.primefaces.component.selectonemenu.SelectOneMenu;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Map;

/**
 *
 */
@ApplicationScoped
public class DropdownInitializer implements ComponentInitializer {
    @Override
    public void configureComponent(FacesContext facesContext, UIComponent uiComponent, Map<String, Object> metaData) {
        SelectOneMenu selectOneMenu = (SelectOneMenu) uiComponent;
        String style = selectOneMenu.getStyleClass();
        if (style == null) {
            style = "";
        }
        selectOneMenu.setStyleClass(style + " requiredDropdown");
    }

    @Override
    public boolean isSupportedComponent(UIComponent uiComponent) {
        return uiComponent instanceof SelectOneMenu;
    }
}
