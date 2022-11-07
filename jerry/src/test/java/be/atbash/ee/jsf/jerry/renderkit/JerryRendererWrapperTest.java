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
package be.atbash.ee.jsf.jerry.renderkit;

import be.atbash.ee.jsf.jerry.interceptor.RendererInterceptor;
import be.atbash.ee.jsf.jerry.renderkit.model.InterceptorCalls;
import be.atbash.ee.jsf.jerry.renderkit.model.SpyRendererInterceptor;
import be.atbash.ee.jsf.jerry.utils.InvocationOrderedArtifactsProvider;
import be.atbash.util.BeanManagerFake;
import be.atbash.util.TestReflectionUtils;
import jakarta.faces.context.FacesContext;
import jakarta.faces.render.Renderer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class JerryRendererWrapperTest {

    @Mock
    private Renderer rendererMock;

    @Mock
    private FacesContext facesContextMock;

    private BeanManagerFake beanManagerFake;

    private SpyRendererInterceptor rendererInterceptor1;

    private SpyRendererInterceptor rendererInterceptor2;

    @BeforeEach
    public void setup() {
        rendererInterceptor1 = new SpyRendererInterceptor();
        rendererInterceptor2 = new SpyRendererInterceptor.Second();

    }

    @AfterEach
    public void teardown() throws NoSuchFieldException, IllegalAccessException {
        beanManagerFake.deregistration();

        // reset
        Object instance = TestReflectionUtils.getValueOf(InvocationOrderedArtifactsProvider.class, "INSTANCE");
        TestReflectionUtils.resetOf(instance, "rendererInterceptors");

    }

    // encodeBegin
    @Test
    void testEncodeBegin() throws IOException {
        registerInterceptors(rendererInterceptor1);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.encodeBegin(facesContextMock, null);

        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_ENCODE_BEGIN)).isTrue();
        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_ENCODE_BEGIN)).isTrue();
        Mockito.verify(rendererMock).encodeBegin(facesContextMock, null);
    }

    @Test
    void testEncodeBegin_CheckExceptionFlow() throws IOException {
        rendererInterceptor1.throwException(InterceptorCalls.BEFORE_ENCODE_BEGIN);
        rendererInterceptor1.throwException(InterceptorCalls.AFTER_ENCODE_BEGIN);
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.encodeBegin(facesContextMock, null);

        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_ENCODE_BEGIN)).isTrue();
        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_ENCODE_BEGIN)).isTrue();
        Mockito.verify(rendererMock).encodeBegin(facesContextMock, null);
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_ENCODE_BEGIN)).isFalse();
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_ENCODE_BEGIN)).isFalse();

    }

    @Test
    void testEncodeBegin_CheckRendererExceptionFlow() throws IOException {
        rendererInterceptor1.throwRendererException(InterceptorCalls.BEFORE_ENCODE_BEGIN);
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.encodeBegin(facesContextMock, null);

        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_ENCODE_BEGIN)).isTrue();
        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_ENCODE_BEGIN)).isTrue();
        Mockito.verify(rendererMock, Mockito.never()).encodeBegin(facesContextMock, null);
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_ENCODE_BEGIN)).isTrue();
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_ENCODE_BEGIN)).isTrue();

    }

    @Test
    void testEncodeBegin_CheckRendererExceptionFlow2() throws IOException {
        rendererInterceptor1.throwRendererException(InterceptorCalls.BEFORE_ENCODE_BEGIN);
        rendererInterceptor1.setSkipOtherInterceptors();
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.encodeBegin(facesContextMock, null);

        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_ENCODE_BEGIN)).isTrue();
        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_ENCODE_BEGIN)).isTrue();
        Mockito.verify(rendererMock, Mockito.never()).encodeBegin(facesContextMock, null);
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_ENCODE_BEGIN)).isFalse();
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_ENCODE_BEGIN)).isTrue();

    }

    // encodeChildren
    @Test
    void testEncodeChildren() throws IOException {
        registerInterceptors(rendererInterceptor1);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.encodeChildren(facesContextMock, null);

        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_ENCODE_CHILDREN)).isTrue();
        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_ENCODE_CHILDREN)).isTrue();
        Mockito.verify(rendererMock).encodeChildren(facesContextMock, null);
    }

    @Test
    void testEncodeChildren_CheckExceptionFlow() throws IOException {
        rendererInterceptor1.throwException(InterceptorCalls.BEFORE_ENCODE_CHILDREN);
        rendererInterceptor1.throwException(InterceptorCalls.AFTER_ENCODE_CHILDREN);
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.encodeChildren(facesContextMock, null);

        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_ENCODE_CHILDREN)).isTrue();
        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_ENCODE_CHILDREN)).isTrue();
        Mockito.verify(rendererMock).encodeChildren(facesContextMock, null);
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_ENCODE_CHILDREN)).isFalse();
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_ENCODE_CHILDREN)).isFalse();

    }

    @Test
    void testEncodeChildren_CheckRendererExceptionFlow() throws IOException {
        rendererInterceptor1.throwRendererException(InterceptorCalls.BEFORE_ENCODE_CHILDREN);
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.encodeChildren(facesContextMock, null);

        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_ENCODE_CHILDREN)).isTrue();
        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_ENCODE_CHILDREN)).isTrue();
        Mockito.verify(rendererMock, Mockito.never()).encodeChildren(facesContextMock, null);
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_ENCODE_CHILDREN)).isTrue();
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_ENCODE_CHILDREN)).isTrue();

    }

    @Test
    void testEncodeChildren_CheckRendererExceptionFlow2() throws IOException {
        rendererInterceptor1.throwRendererException(InterceptorCalls.BEFORE_ENCODE_CHILDREN);
        rendererInterceptor1.setSkipOtherInterceptors();
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.encodeChildren(facesContextMock, null);

        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_ENCODE_CHILDREN)).isTrue();
        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_ENCODE_CHILDREN)).isTrue();
        Mockito.verify(rendererMock, Mockito.never()).encodeChildren(facesContextMock, null);
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_ENCODE_CHILDREN)).isFalse();
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_ENCODE_CHILDREN)).isTrue();

    }

    // encodeEnd
    @Test
    void testEncodeEnd() throws IOException {
        registerInterceptors(rendererInterceptor1);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.encodeEnd(facesContextMock, null);

        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_ENCODE_END)).isTrue();
        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_ENCODE_END)).isTrue();
        Mockito.verify(rendererMock).encodeEnd(facesContextMock, null);
    }

    @Test
    void testEncodeEnd_CheckExceptionFlow() throws IOException {
        rendererInterceptor1.throwException(InterceptorCalls.BEFORE_ENCODE_END);
        rendererInterceptor1.throwException(InterceptorCalls.AFTER_ENCODE_END);
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.encodeEnd(facesContextMock, null);

        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_ENCODE_END)).isTrue();
        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_ENCODE_END)).isTrue();
        Mockito.verify(rendererMock).encodeEnd(facesContextMock, null);
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_ENCODE_END)).isFalse();
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_ENCODE_END)).isFalse();

    }

    @Test
    void testEncodeEnd_CheckRendererExceptionFlow() throws IOException {
        rendererInterceptor1.throwRendererException(InterceptorCalls.BEFORE_ENCODE_END);
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.encodeEnd(facesContextMock, null);

        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_ENCODE_END)).isTrue();
        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_ENCODE_END)).isTrue();
        Mockito.verify(rendererMock, Mockito.never()).encodeEnd(facesContextMock, null);
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_ENCODE_END)).isTrue();
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_ENCODE_END)).isTrue();

    }

    @Test
    void testEncodeEnd_CheckRendererExceptionFlow2() throws IOException {
        rendererInterceptor1.throwRendererException(InterceptorCalls.BEFORE_ENCODE_END);
        rendererInterceptor1.setSkipOtherInterceptors();
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.encodeEnd(facesContextMock, null);

        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_ENCODE_END)).isTrue();
        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_ENCODE_END)).isTrue();
        Mockito.verify(rendererMock, Mockito.never()).encodeEnd(facesContextMock, null);
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_ENCODE_END)).isFalse();
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_ENCODE_END)).isTrue();

    }

    // decode
    @Test
    void testDecode() throws IOException {
        registerInterceptors(rendererInterceptor1);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.decode(facesContextMock, null);

        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_DECODE)).isTrue();
        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_DECODE)).isTrue();
        Mockito.verify(rendererMock).decode(facesContextMock, null);
    }

    @Test
    void testDecode_CheckExceptionFlow() throws IOException {
        rendererInterceptor1.throwException(InterceptorCalls.BEFORE_DECODE);
        rendererInterceptor1.throwException(InterceptorCalls.AFTER_DECODE);
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.decode(facesContextMock, null);

        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_DECODE)).isTrue();
        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_DECODE)).isTrue();
        Mockito.verify(rendererMock).decode(facesContextMock, null);
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_DECODE)).isFalse();
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_DECODE)).isFalse();

    }

    @Test
    void testDecode_CheckRendererExceptionFlow() throws IOException {
        rendererInterceptor1.throwRendererException(InterceptorCalls.BEFORE_DECODE);
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.decode(facesContextMock, null);

        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_DECODE)).isTrue();
        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_DECODE)).isTrue();
        Mockito.verify(rendererMock, Mockito.never()).decode(facesContextMock, null);
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_DECODE)).isTrue();
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_DECODE)).isTrue();

    }

    @Test
    void testDecode_CheckRendererExceptionFlow2() throws IOException {
        rendererInterceptor1.throwRendererException(InterceptorCalls.BEFORE_DECODE);
        rendererInterceptor1.setSkipOtherInterceptors();
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.decode(facesContextMock, null);

        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_DECODE)).isTrue();
        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_DECODE)).isTrue();
        Mockito.verify(rendererMock, Mockito.never()).decode(facesContextMock, null);
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_DECODE)).isFalse();
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_DECODE)).isTrue();

    }

    // convertedValue
    // encodeBegin
    @Test
    void testGetConvertedValue() throws IOException {
        registerInterceptors(rendererInterceptor1);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.getConvertedValue(facesContextMock, null, null);

        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_CONVERTED_VALUE)).isTrue();
        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_CONVERTED_VALUE)).isTrue();
        Mockito.verify(rendererMock).getConvertedValue(facesContextMock, null, null);
    }

    @Test
    void testGetConvertedValue_CheckExceptionFlow() throws IOException {
        rendererInterceptor1.throwException(InterceptorCalls.BEFORE_CONVERTED_VALUE);
        rendererInterceptor1.throwException(InterceptorCalls.AFTER_CONVERTED_VALUE);
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.getConvertedValue(facesContextMock, null, null);

        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_CONVERTED_VALUE)).isTrue();
        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_CONVERTED_VALUE)).isTrue();
        Mockito.verify(rendererMock).getConvertedValue(facesContextMock, null, null);
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_CONVERTED_VALUE)).isFalse();
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_CONVERTED_VALUE)).isFalse();

    }

    @Test
    void testGetConvertedValue_CheckRendererExceptionFlow() throws IOException {
        rendererInterceptor1.throwRendererException(InterceptorCalls.BEFORE_CONVERTED_VALUE);
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.getConvertedValue(facesContextMock, null, null);

        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_CONVERTED_VALUE)).isTrue();
        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_CONVERTED_VALUE)).isTrue();
        Mockito.verify(rendererMock, Mockito.never()).getConvertedValue(facesContextMock, null, null);
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_CONVERTED_VALUE)).isTrue();
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_CONVERTED_VALUE)).isTrue();

    }

    @Test
    void testGetConvertedValue_CheckRendererExceptionFlow2() throws IOException {
        rendererInterceptor1.throwRendererException(InterceptorCalls.BEFORE_CONVERTED_VALUE);
        rendererInterceptor1.setSkipOtherInterceptors();
        registerInterceptors(rendererInterceptor1, rendererInterceptor2);

        JerryRendererWrapper wrapper = new JerryRendererWrapper(rendererMock);

        wrapper.getConvertedValue(facesContextMock, null, null);

        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.BEFORE_CONVERTED_VALUE)).isTrue();
        Assertions.assertThat(rendererInterceptor1.isCalled(InterceptorCalls.AFTER_CONVERTED_VALUE)).isTrue();
        Mockito.verify(rendererMock, Mockito.never()).getConvertedValue(facesContextMock, null, null);
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.BEFORE_CONVERTED_VALUE)).isFalse();
        Assertions.assertThat(rendererInterceptor2.isCalled(InterceptorCalls.AFTER_CONVERTED_VALUE)).isTrue();

    }

    private void registerInterceptors(SpyRendererInterceptor... interceptors) {
        beanManagerFake = new BeanManagerFake();

        for (SpyRendererInterceptor interceptor : interceptors) {
            beanManagerFake.registerBean(interceptor, RendererInterceptor.class);
        }

        beanManagerFake.endRegistration();

    }
}