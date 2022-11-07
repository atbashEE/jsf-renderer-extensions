/*
 * Copyright 2014-2022 Rudy De Busscher
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
package be.atbash.ee.jsf.jerry.component;

import be.atbash.ee.jsf.jerry.storage.ComponentStorage;
import be.atbash.util.TestReflectionUtils;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIViewRoot;
import jakarta.faces.context.FacesContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO Some more use cases.
 */
@ExtendWith(MockitoExtension.class)
class ComponentInitializerManagerTest {

    @Mock
    private ComponentInitializer componentInitializerMock;

    @Mock
    private RepeatableComponentInitializer repeatableComponentInitializerMock;

    @Mock
    private UIComponent uiComponentMock;

    @Mock
    private ComponentStorage componentStorageMock;

    @Mock
    private FacesContext facesContextMock;

    private final Map<String, Object> componentAttributes = new HashMap<>();

    @InjectMocks
    private ComponentInitializerManager componentInitializerManager;

    @Test
    void performInitialization() throws Exception {

        prepareMocks(false);

        Mockito.when(componentInitializerMock.isSupportedComponent(uiComponentMock)).thenReturn(true);
        Mockito.when(repeatableComponentInitializerMock.isSupportedComponent(uiComponentMock)).thenReturn(false);

        componentInitializerManager.performInitialization(facesContextMock, uiComponentMock);

        Mockito.verify(componentInitializerMock).isSupportedComponent(uiComponentMock);
        Mockito.verify(repeatableComponentInitializerMock).isSupportedComponent(uiComponentMock);

        Mockito.verify(componentInitializerMock).configureComponent(Mockito.any(FacesContext.class), Mockito.any(UIComponent.class), Mockito.anyMap());
        Mockito.verify(repeatableComponentInitializerMock, Mockito.never()).configureComponent(Mockito.any(FacesContext.class), Mockito.any(UIComponent.class), Mockito.anyMap());

        Assertions.assertThat(componentAttributes.keySet()).containsOnly(ComponentInitializer.class.getName());

        Mockito.verify(componentStorageMock).getComponentInfo(Mockito.anyString(), Mockito.anyString()); // TODO Should we verify the correct values?
    }

    @Test
    void performInitialization_AlreadyInitialized() throws Exception {

        prepareMocks(true);
        componentAttributes.put(ComponentInitializer.class.getName(), Boolean.TRUE); // any value will do in fact.

        componentInitializerManager.performInitialization(facesContextMock, uiComponentMock);

        Mockito.verify(componentInitializerMock, Mockito.never()).isSupportedComponent(uiComponentMock);
        Mockito.verify(componentInitializerMock, Mockito.never()).configureComponent(Mockito.any(FacesContext.class), Mockito.any(UIComponent.class), Mockito.anyMap());
        Mockito.verify(repeatableComponentInitializerMock, Mockito.never()).configureComponent(Mockito.any(FacesContext.class), Mockito.any(UIComponent.class), Mockito.anyMap());

        Assertions.assertThat(componentAttributes.keySet()).containsOnly(ComponentInitializer.class.getName());
    }

    @Test
    void performInitialization_repeatable() throws Exception {

        prepareMocks(false);

        Mockito.when(componentInitializerMock.isSupportedComponent(uiComponentMock)).thenReturn(false);
        Mockito.when(repeatableComponentInitializerMock.isSupportedComponent(uiComponentMock)).thenReturn(true);

        componentInitializerManager.performInitialization(facesContextMock, uiComponentMock);

        Mockito.verify(componentInitializerMock).isSupportedComponent(uiComponentMock);
        Mockito.verify(repeatableComponentInitializerMock).isSupportedComponent(uiComponentMock);
        Mockito.verify(repeatableComponentInitializerMock).configureComponent(Mockito.any(FacesContext.class), Mockito.any(UIComponent.class), Mockito.anyMap());
        Mockito.verify(componentInitializerMock, Mockito.never()).configureComponent(Mockito.any(FacesContext.class), Mockito.any(UIComponent.class), Mockito.anyMap());

        Assertions.assertThat(componentAttributes.keySet()).containsOnly(RepeatableComponentInitializer.class.getName());

        Mockito.verify(componentStorageMock).getComponentInfo(Mockito.anyString(), Mockito.anyString()); // TODO Should we verify the correct values?
    }

    private void prepareMocks(boolean minimal) throws NoSuchFieldException {

        List<ComponentInitializer> initializers = new ArrayList<>();
        initializers.add(componentInitializerMock);
        initializers.add(repeatableComponentInitializerMock);
        TestReflectionUtils.setFieldValue(componentInitializerManager, "initializers", initializers);

        UIViewRoot uiViewRootMock = Mockito.mock(UIViewRoot.class);

        Mockito.when(uiComponentMock.getAttributes()).thenReturn(componentAttributes);

        if (minimal) {
            return;
        }
        Mockito.when(facesContextMock.getViewRoot()).thenReturn(uiViewRootMock);

        Mockito.when(uiViewRootMock.getViewId()).thenReturn("theViewId");

        Mockito.when(uiComponentMock.getClientId(facesContextMock)).thenReturn("theClientId");
    }

}