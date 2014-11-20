package be.rubus.web.valerie.primefaces;

import be.rubus.web.jerry.component.ComponentInitializer;
import be.rubus.web.jerry.component.ComponentInitializerManager;
import be.rubus.web.jerry.metadata.CommonMetaDataKeys;
import be.rubus.web.jerry.ordering.InvocationOrder;
import be.rubus.web.valerie.property.PropertyInformationManager;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.outputlabel.OutputLabel;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.Map;

/**
 *
 */
@ApplicationScoped
@InvocationOrder(60)
public class RequiredMarkerInitializer implements ComponentInitializer {

    @Inject
    private ComponentInitializerManager initializerManager;

    @Inject
    private PropertyInformationManager informationManager;

    @Override
    public void configureComponent(FacesContext facesContext, UIComponent uiComponent, Map<String, Object> metaData) {
        if (uiComponent instanceof OutputLabel) {
            OutputLabel label = (OutputLabel) uiComponent;
            UIComponent targetComponent = label.findComponent(label.getFor());

            informationManager.determineInformation(facesContext, targetComponent);
            initializerManager.performInitialization(facesContext, targetComponent);

        }

        if (uiComponent instanceof InputText) {
            InputText inputText = (InputText) uiComponent;


            if (metaData.containsKey(CommonMetaDataKeys.REQUIRED.getKey())) {
                inputText.setRequired(true);
            }
        }

    }

    @Override
    public boolean isSupportedComponent(UIComponent uiComponent) {
        return uiComponent instanceof OutputLabel || uiComponent instanceof InputText;
    }
}
