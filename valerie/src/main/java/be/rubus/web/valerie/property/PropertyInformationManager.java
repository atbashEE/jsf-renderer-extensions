package be.rubus.web.valerie.property;

import be.rubus.web.jerry.storage.ComponentStorage;
import be.rubus.web.valerie.metadata.extractor.MetaDataExtractor;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

/**
 *
 */
@ApplicationScoped
public class PropertyInformationManager {

    @Inject
    private MetaDataExtractor extractor;

    @Inject
    private ComponentStorage componentStorage;

    public void determineInformation(FacesContext facesContext, UIComponent uiComponent) {

        String viewId = facesContext.getViewRoot().getViewId();
        String clientId = uiComponent.getClientId(facesContext);

        if (componentStorage.isEntryPossibleFor(viewId, clientId, PropertyInformation.class)) {

            if (!componentStorage.containsEntry(viewId, clientId, PropertyInformation.class)) {


                PropertyInformation info = extractor.extract(facesContext, uiComponent);
                if (info != null) {
                    componentStorage.storeEntry(viewId, clientId, info);
                } else {
                    componentStorage.setNotAvailable(viewId, clientId, PropertyInformation.class);
                }
            }
        }
    }

}
