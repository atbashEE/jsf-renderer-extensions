/*
 * Copyright 2014-2018 Rudy De Busscher
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
import be.atbash.ee.jsf.jerry.util.TestReflectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * TODO Some more use cases.
 */
@RunWith(MockitoJUnitRunner.class)
public class ComponentInitializerManagerTest {

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

    private Map<String, Object> componentAttributes;

    @InjectMocks
    private ComponentInitializerManager componentInitializerManager;

    @Test
    public void performInitialization() throws Exception {

        prepareMocks();

        when(componentInitializerMock.isSupportedComponent(uiComponentMock)).thenReturn(true);
        when(repeatableComponentInitializerMock.isSupportedComponent(uiComponentMock)).thenReturn(false);

        componentInitializerManager.performInitialization(facesContextMock, uiComponentMock);

        verify(componentInitializerMock).isSupportedComponent(uiComponentMock);
        verify(repeatableComponentInitializerMock).isSupportedComponent(uiComponentMock);

        verify(componentInitializerMock).configureComponent(any(FacesContext.class), any(UIComponent.class), anyMap());
        verify(repeatableComponentInitializerMock, never()).configureComponent(any(FacesContext.class), any(UIComponent.class), anyMap());

        assertThat(componentAttributes.keySet()).containsOnly(ComponentInitializer.class.getName());

        verify(componentStorageMock).getComponentInfo(anyString(), anyString()); // TODO Should we verify the correct values?
    }

    @Test
    public void performInitialization_AlreadyInitialized() throws Exception {

        prepareMocks();
        componentAttributes.put(ComponentInitializer.class.getName(), Boolean.TRUE); // any value will do in fact.

        componentInitializerManager.performInitialization(facesContextMock, uiComponentMock);

        verify(componentInitializerMock, never()).isSupportedComponent(uiComponentMock);
        verify(componentInitializerMock, never()).configureComponent(any(FacesContext.class), any(UIComponent.class), anyMap());
        verify(repeatableComponentInitializerMock, never()).configureComponent(any(FacesContext.class), any(UIComponent.class), anyMap());

        assertThat(componentAttributes.keySet()).containsOnly(ComponentInitializer.class.getName());
    }

    @Test
    public void performInitialization_repeatable() throws Exception {

        prepareMocks();

        when(componentInitializerMock.isSupportedComponent(uiComponentMock)).thenReturn(false);
        when(repeatableComponentInitializerMock.isSupportedComponent(uiComponentMock)).thenReturn(true);

        componentInitializerManager.performInitialization(facesContextMock, uiComponentMock);

        verify(componentInitializerMock).isSupportedComponent(uiComponentMock);
        verify(repeatableComponentInitializerMock).isSupportedComponent(uiComponentMock);
        verify(repeatableComponentInitializerMock).configureComponent(any(FacesContext.class), any(UIComponent.class), anyMap());
        verify(componentInitializerMock, never()).configureComponent(any(FacesContext.class), any(UIComponent.class), anyMap());

        assertThat(componentAttributes.keySet()).containsOnly(RepeatableComponentInitializer.class.getName());

        verify(componentStorageMock).getComponentInfo(anyString(), anyString()); // TODO Should we verify the correct values?
    }

    private void prepareMocks() throws NoSuchFieldException, IllegalAccessException {

        List<ComponentInitializer> initializers = new ArrayList<>();
        initializers.add(componentInitializerMock);
        initializers.add(repeatableComponentInitializerMock);
        TestReflectionUtils.setFieldValue(componentInitializerManager, "initializers", initializers);

        UIViewRoot uiViewRootMock = Mockito.mock(UIViewRoot.class);
        when(facesContextMock.getViewRoot()).thenReturn(uiViewRootMock);

        componentAttributes = new HashMap<>();
        when(uiComponentMock.getAttributes()).thenReturn(componentAttributes);
    }

}