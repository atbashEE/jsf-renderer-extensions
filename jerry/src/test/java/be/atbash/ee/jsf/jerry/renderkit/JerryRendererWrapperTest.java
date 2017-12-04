/*
 * Copyright 2014-2016 Rudy De Busscher
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
package be.atbash.ee.jsf.jerry.renderkit;

import be.atbash.ee.jsf.jerry.interceptor.RendererInterceptor;
import be.atbash.ee.jsf.jerry.renderkit.model.InterceptorCalls;
import be.atbash.ee.jsf.jerry.renderkit.model.SpyRendererInterceptor;
import be.atbash.ee.jsf.jerry.util.TestReflectionUtils;
import be.atbash.ee.jsf.jerry.util.cdi.BeanManagerFake;
import be.atbash.ee.jsf.jerry.utils.InvocationOrderedArtifactsProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class JerryRendererWrapperTest {

    @Mock
    private Renderer rendererMock;

    @Mock
    private FacesContext facesContextMock;

    private BeanManagerFake beanManagerFake;

    private SpyRendererInterceptor rendererInterceptor1;

    private SpyRendererInterceptor rendererInterceptor2;

    @Before
    public void setup() {
        rendererInterceptor1 = new SpyRendererInterceptor();
        rendererInterceptor2 = new SpyRendererInterceptor.Second();

    }

    @After
    public void teardown() throws NoSuchFieldException, IllegalAccessException {
        beanManagerFake.deregistration();

        // reset
        Object instance = TestReflectionUtils.getValueOf(InvocationOrderedArtifactsProvider.class, "INSTANCE");
        TestReflectionUtils.resetOf(instance, "rendererInterceptors");

    }

    // encodeBegin
    @Test
    public void testEncodeBegin() throws IOException {
        registerInterceptors(rendererInterceptor1);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.encodeBegin(facesContextMock, null);

        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_ENCODE_BEGIN)).isTrue();
        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_ENCODE_BEGIN)).isTrue();
        verify(rendererMock).encodeBegin(facesContextMock, null);
    }

    @Test
    public void testEncodeBegin_CheckExceptionFlow() throws IOException {
        rendererInterceptor1.throwException(InterceptorCalls.BEFORE_ENCODE_BEGIN);
        rendererInterceptor1.throwException(InterceptorCalls.AFTER_ENCODE_BEGIN);
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.encodeBegin(facesContextMock, null);

        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_ENCODE_BEGIN)).isTrue();
        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_ENCODE_BEGIN)).isTrue();
        verify(rendererMock).encodeBegin(facesContextMock, null);
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_ENCODE_BEGIN)).isFalse();
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_ENCODE_BEGIN)).isFalse();

    }

    @Test
    public void testEncodeBegin_CheckRendererExceptionFlow() throws IOException {
        rendererInterceptor1.throwRendererException(InterceptorCalls.BEFORE_ENCODE_BEGIN);
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.encodeBegin(facesContextMock, null);

        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_ENCODE_BEGIN)).isTrue();
        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_ENCODE_BEGIN)).isTrue();
        verify(rendererMock, never()).encodeBegin(facesContextMock, null);
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_ENCODE_BEGIN)).isTrue();
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_ENCODE_BEGIN)).isTrue();

    }

    @Test
    public void testEncodeBegin_CheckRendererExceptionFlow2() throws IOException {
        rendererInterceptor1.throwRendererException(InterceptorCalls.BEFORE_ENCODE_BEGIN);
        rendererInterceptor1.setSkipOtherInterceptors();
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.encodeBegin(facesContextMock, null);

        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_ENCODE_BEGIN)).isTrue();
        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_ENCODE_BEGIN)).isTrue();
        verify(rendererMock, never()).encodeBegin(facesContextMock, null);
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_ENCODE_BEGIN)).isFalse();
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_ENCODE_BEGIN)).isTrue();

    }

    // encodeChildren
    @Test
    public void testEncodeChildren() throws IOException {
        registerInterceptors(rendererInterceptor1);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.encodeChildren(facesContextMock, null);

        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_ENCODE_CHILDREN)).isTrue();
        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_ENCODE_CHILDREN)).isTrue();
        verify(rendererMock).encodeChildren(facesContextMock, null);
    }

    @Test
    public void ttestEncodeChildren_CheckExceptionFlow() throws IOException {
        rendererInterceptor1.throwException(InterceptorCalls.BEFORE_ENCODE_CHILDREN);
        rendererInterceptor1.throwException(InterceptorCalls.AFTER_ENCODE_CHILDREN);
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.encodeChildren(facesContextMock, null);

        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_ENCODE_CHILDREN)).isTrue();
        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_ENCODE_CHILDREN)).isTrue();
        verify(rendererMock).encodeChildren(facesContextMock, null);
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_ENCODE_CHILDREN)).isFalse();
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_ENCODE_CHILDREN)).isFalse();

    }

    @Test
    public void testEncodeChildren_CheckRendererExceptionFlow() throws IOException {
        rendererInterceptor1.throwRendererException(InterceptorCalls.BEFORE_ENCODE_CHILDREN);
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.encodeChildren(facesContextMock, null);

        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_ENCODE_CHILDREN)).isTrue();
        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_ENCODE_CHILDREN)).isTrue();
        verify(rendererMock, never()).encodeChildren(facesContextMock, null);
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_ENCODE_CHILDREN)).isTrue();
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_ENCODE_CHILDREN)).isTrue();

    }

    @Test
    public void testEncodeChildren_CheckRendererExceptionFlow2() throws IOException {
        rendererInterceptor1.throwRendererException(InterceptorCalls.BEFORE_ENCODE_CHILDREN);
        rendererInterceptor1.setSkipOtherInterceptors();
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.encodeChildren(facesContextMock, null);

        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_ENCODE_CHILDREN)).isTrue();
        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_ENCODE_CHILDREN)).isTrue();
        verify(rendererMock, never()).encodeChildren(facesContextMock, null);
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_ENCODE_CHILDREN)).isFalse();
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_ENCODE_CHILDREN)).isTrue();

    }

    // encodeEnd
    @Test
    public void testEncodeEnd() throws IOException {
        registerInterceptors(rendererInterceptor1);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.encodeEnd(facesContextMock, null);

        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_ENCODE_END)).isTrue();
        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_ENCODE_END)).isTrue();
        verify(rendererMock).encodeEnd(facesContextMock, null);
    }

    @Test
    public void testEncodeEnd_CheckExceptionFlow() throws IOException {
        rendererInterceptor1.throwException(InterceptorCalls.BEFORE_ENCODE_END);
        rendererInterceptor1.throwException(InterceptorCalls.AFTER_ENCODE_END);
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.encodeEnd(facesContextMock, null);

        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_ENCODE_END)).isTrue();
        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_ENCODE_END)).isTrue();
        verify(rendererMock).encodeEnd(facesContextMock, null);
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_ENCODE_END)).isFalse();
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_ENCODE_END)).isFalse();

    }

    @Test
    public void testEncodeEnd_CheckRendererExceptionFlow() throws IOException {
        rendererInterceptor1.throwRendererException(InterceptorCalls.BEFORE_ENCODE_END);
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.encodeEnd(facesContextMock, null);

        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_ENCODE_END)).isTrue();
        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_ENCODE_END)).isTrue();
        verify(rendererMock, never()).encodeEnd(facesContextMock, null);
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_ENCODE_END)).isTrue();
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_ENCODE_END)).isTrue();

    }

    @Test
    public void testEncodeEnd_CheckRendererExceptionFlow2() throws IOException {
        rendererInterceptor1.throwRendererException(InterceptorCalls.BEFORE_ENCODE_END);
        rendererInterceptor1.setSkipOtherInterceptors();
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.encodeEnd(facesContextMock, null);

        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_ENCODE_END)).isTrue();
        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_ENCODE_END)).isTrue();
        verify(rendererMock, never()).encodeEnd(facesContextMock, null);
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_ENCODE_END)).isFalse();
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_ENCODE_END)).isTrue();

    }

    // decode
    @Test
    public void testDecode() throws IOException {
        registerInterceptors(rendererInterceptor1);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.decode(facesContextMock, null);

        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_DECODE)).isTrue();
        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_DECODE)).isTrue();
        verify(rendererMock).decode(facesContextMock, null);
    }

    @Test
    public void testDecode_CheckExceptionFlow() throws IOException {
        rendererInterceptor1.throwException(InterceptorCalls.BEFORE_DECODE);
        rendererInterceptor1.throwException(InterceptorCalls.AFTER_DECODE);
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.decode(facesContextMock, null);

        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_DECODE)).isTrue();
        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_DECODE)).isTrue();
        verify(rendererMock).decode(facesContextMock, null);
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_DECODE)).isFalse();
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_DECODE)).isFalse();

    }

    @Test
    public void testDecode_CheckRendererExceptionFlow() throws IOException {
        rendererInterceptor1.throwRendererException(InterceptorCalls.BEFORE_DECODE);
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.decode(facesContextMock, null);

        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_DECODE)).isTrue();
        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_DECODE)).isTrue();
        verify(rendererMock, never()).decode(facesContextMock, null);
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_DECODE)).isTrue();
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_DECODE)).isTrue();

    }

    @Test
    public void testDecode_CheckRendererExceptionFlow2() throws IOException {
        rendererInterceptor1.throwRendererException(InterceptorCalls.BEFORE_DECODE);
        rendererInterceptor1.setSkipOtherInterceptors();
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.decode(facesContextMock, null);

        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_DECODE)).isTrue();
        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_DECODE)).isTrue();
        verify(rendererMock, never()).decode(facesContextMock, null);
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_DECODE)).isFalse();
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_DECODE)).isTrue();

    }

    // convertedValue
    // encodeBegin
    @Test
    public void testGetConvertedValue() throws IOException {
        registerInterceptors(rendererInterceptor1);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.getConvertedValue(facesContextMock, null, null);

        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_CONVERTED_VALUE)).isTrue();
        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_CONVERTED_VALUE)).isTrue();
        verify(rendererMock).getConvertedValue(facesContextMock, null, null);
    }

    @Test
    public void testGetConvertedValue_CheckExceptionFlow() throws IOException {
        rendererInterceptor1.throwException(InterceptorCalls.BEFORE_CONVERTED_VALUE);
        rendererInterceptor1.throwException(InterceptorCalls.AFTER_CONVERTED_VALUE);
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.getConvertedValue(facesContextMock, null, null);

        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_CONVERTED_VALUE)).isTrue();
        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_CONVERTED_VALUE)).isTrue();
        verify(rendererMock).getConvertedValue(facesContextMock, null, null);
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_CONVERTED_VALUE)).isFalse();
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_CONVERTED_VALUE)).isFalse();

    }

    @Test
    public void testGetConvertedValue_CheckRendererExceptionFlow() throws IOException {
        rendererInterceptor1.throwRendererException(InterceptorCalls.BEFORE_CONVERTED_VALUE);
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.getConvertedValue(facesContextMock, null, null);

        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_CONVERTED_VALUE)).isTrue();
        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_CONVERTED_VALUE)).isTrue();
        verify(rendererMock, never()).getConvertedValue(facesContextMock, null, null);
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_CONVERTED_VALUE)).isTrue();
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_CONVERTED_VALUE)).isTrue();

    }

    @Test
    public void testGetConvertedValue_CheckRendererExceptionFlow2() throws IOException {
        rendererInterceptor1.throwRendererException(InterceptorCalls.BEFORE_CONVERTED_VALUE);
        rendererInterceptor1.setSkipOtherInterceptors();
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.getConvertedValue(facesContextMock, null, null);

        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_CONVERTED_VALUE)).isTrue();
        assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_CONVERTED_VALUE)).isTrue();
        verify(rendererMock, never()).getConvertedValue(facesContextMock, null, null);
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_CONVERTED_VALUE)).isFalse();
        assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_CONVERTED_VALUE)).isTrue();

    }

    private void registerInterceptors(SpyRendererInterceptor... interceptors) {
        beanManagerFake = new BeanManagerFake();

        for (SpyRendererInterceptor interceptor : interceptors) {
            beanManagerFake.registerBean(interceptor, RendererInterceptor.class);
        }

        beanManagerFake.endRegistration();

    }
}