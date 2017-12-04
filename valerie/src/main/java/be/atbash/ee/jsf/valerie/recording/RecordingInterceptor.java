/*
 * Copyright 2014-2017 Rudy De Busscher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package be.atbash.ee.jsf.valerie.recording;

import be.atbash.ee.jsf.jerry.interceptor.AbstractRendererInterceptor;
import be.atbash.ee.jsf.jerry.interceptor.exception.SkipAfterInterceptorsException;
import be.atbash.ee.jsf.jerry.ordering.InvocationOrder;
import be.atbash.ee.jsf.jerry.storage.ComponentStorage;

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
