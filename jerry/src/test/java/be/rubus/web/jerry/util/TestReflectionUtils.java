/*
 * Copyright 2014-2015 Rudy De Busscher
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
 *
 */
package be.rubus.web.jerry.util;

import java.lang.reflect.Field;

/**
 *
 */
public final class TestReflectionUtils {

    private TestReflectionUtils() {
    }

    public static Object getValueOf(Object instance, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Object target;
        Class<?> targetClass;
        if (instance instanceof Class<?>) {
            target = null;  // static field
            targetClass = (Class<?>) instance;
        } else {
            target = instance;
            targetClass = instance.getClass();
        }
        Field field = targetClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }

    public static void resetOf(Object instance, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Object target;
        Class<?> targetClass;
        if (instance instanceof Class<?>) {
            target = null;  // static field
            targetClass = (Class<?>) instance;
        } else {
            target = instance;
            targetClass = instance.getClass();
        }
        Field field = targetClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, null);
    }

}
