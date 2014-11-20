package be.rubus.web.jerry.example.interceptor;

import be.rubus.web.jerry.component.ComponentInitializer;
import be.rubus.web.jerry.metadata.CommonMetaDataKeys;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.outputlabel.OutputLabel;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Map;

/**
 *
 */
@ApplicationScoped
public class DummyInitializer implements ComponentInitializer {
    @Override
    public void configureComponent(FacesContext facesContext, UIComponent uiComponent, Map<String, Object> metaData) {
        if (uiComponent instanceof OutputLabel) {
            OutputLabel label = (OutputLabel) uiComponent;
            label.setValue(label.getValue().toString() + "Hijacked");
        }

        if (uiComponent instanceof InputText) {
            InputText inputText = (InputText) uiComponent;
            inputText.setPlaceholder("Interceptor :)");
        }

        if (metaData.containsKey(CommonMetaDataKeys.REQUIRED.getKey())) {
            InputText outputLabel = (InputText) uiComponent;
            String style = outputLabel.getStyle();
            if (style == null) {
                style = "";
            }
            outputLabel.setStyle(style + " background-color: red;");
        }
    }
}
