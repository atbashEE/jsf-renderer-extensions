package be.rubus.web.valerie.metadata;

import be.rubus.web.jerry.interceptor.AbstractRendererInterceptor;
import be.rubus.web.jerry.interceptor.exception.SkipBeforeInterceptorsException;
import be.rubus.web.jerry.interceptor.exception.SkipRendererDelegationException;
import be.rubus.web.jerry.ordering.InvocationOrder;
import be.rubus.web.jerry.storage.ComponentStorage;
import be.rubus.web.valerie.metadata.extractor.MetaDataExtractor;
import be.rubus.web.valerie.property.PropertyInformation;
import be.rubus.web.valerie.property.PropertyInformationManager;
import be.rubus.web.valerie.storage.MetaDataStorage;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import javax.inject.Inject;
import java.io.IOException;

/**
 *
 */
@ApplicationScoped
@InvocationOrder(0)
public class MetaDataInterceptor extends AbstractRendererInterceptor {

    @Inject
    private PropertyInformationManager manager;

    @Override
    public void beforeDecode(FacesContext facesContext, UIComponent uiComponent, Renderer wrapped) throws SkipBeforeInterceptorsException, SkipRendererDelegationException {
    }

    @Override
    public void beforeEncodeBegin(FacesContext facesContext, UIComponent uiComponent,
                                  Renderer wrapped) throws IOException, SkipBeforeInterceptorsException, SkipRendererDelegationException {


        manager.determineInformation(facesContext, uiComponent);
    }

}
