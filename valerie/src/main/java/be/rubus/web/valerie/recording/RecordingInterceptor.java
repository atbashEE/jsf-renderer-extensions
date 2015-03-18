package be.rubus.web.valerie.recording;

import be.rubus.web.jerry.interceptor.AbstractRendererInterceptor;
import be.rubus.web.jerry.interceptor.exception.SkipAfterInterceptorsException;
import be.rubus.web.jerry.ordering.InvocationOrder;
import be.rubus.web.jerry.storage.ComponentStorage;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.render.Renderer;
import javax.inject.Inject;
import java.util.List;

/**
 *
 */
@ApplicationScoped
@InvocationOrder(5)
public class RecordingInterceptor extends AbstractRendererInterceptor {

    @Inject
    private RecordingInfoManager recordingInfoManager;

    @Inject
    private ComponentStorage storage;

    @Override
    public void afterGetConvertedValue(FacesContext facesContext, UIComponent uiComponent, Object submittedValue, Object convertedValue,
                                       Renderer wrapped) throws ConverterException, SkipAfterInterceptorsException {
        // FIXME We have this on various locations !!
        String viewId = facesContext.getViewRoot().getViewId();
        String clientId = uiComponent.getClientId(facesContext);

        List<RecordValueInfo> recordingInformation = (List<RecordValueInfo>) storage.getRecordingInformation(viewId, clientId);

        for (RecordValueInfo recordValueInfo : recordingInformation) {
            recordingInfoManager.keepInfo(facesContext, recordValueInfo, convertedValue);
        }
    }
}
