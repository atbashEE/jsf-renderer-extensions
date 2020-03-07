/*
 * Copyright 2014-2020 Rudy De Busscher
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
package be.atbash.ee.jsf.valerie.utils;

import be.atbash.ee.jsf.valerie.config.ValerieConfiguration;
import be.atbash.ee.jsf.valerie.storage.PropertyStorage;
import be.atbash.util.CDIUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Utilities for reflection of Fields and methods.  We need to keep reflection API as we need to extract the annotations.
 */
public class ReflectionUtils {

    private static Boolean USE_BEANINFO = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtils.class);

    public static Method tryToGetMethodOfProperty(PropertyStorage storage, Class<?> entity, String property) {

        if (isCachedMethod(storage, entity, property)) {
            return getCachedMethod(storage, entity, property);
        }

        Method method = tryToGetReadMethod(entity, property);

        tryToCacheMethod(storage, entity, property, method);

        return method;
    }

    private static Method tryToGetReadMethod(Class<?> baseBeanClass, String property) {
        Method method = ReflectionUtils.tryToGetReadMethodViaBeanInfo(baseBeanClass, property);

        if (method == null) {
            method = ReflectionUtils.tryToGetReadMethodManually(baseBeanClass, property);
        }
        return method;
    }

    private static Method tryToGetReadMethodViaBeanInfo(Class<?> entity, String property) {
        if (useBeanInfo()) {
            try {
                BeanInfo beanInfo = Introspector.getBeanInfo(entity);
                for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                    if (property.equals(propertyDescriptor.getName()) && propertyDescriptor.getReadMethod() != null) {
                        return propertyDescriptor.getReadMethod();
                    }
                }
            } catch (IntrospectionException e) {
                //do nothing
            }
        }
        return null;
    }

    private static boolean useBeanInfo() {
        if (USE_BEANINFO == null) {
            USE_BEANINFO = CDIUtils.retrieveInstance(ValerieConfiguration.class).useBeanInfo();

        }
        return USE_BEANINFO;
    }

    private static Method tryToGetReadMethodManually(Class<?> entity, String property) {
        property = property.substring(0, 1).toUpperCase() + property.substring(1);

        try {
            //changed to official bean spec. due to caching there is no performance issue any more
            return entity.getDeclaredMethod("is" + property);
        } catch (NoSuchMethodException e) {
            try {
                return entity.getDeclaredMethod("get" + property);
            } catch (NoSuchMethodException e1) {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("method not found - class: " + entity.getName()
                            + " - methods: " + "get" + property + " " + "is" + property);
                }
                return null;
            }
        }
    }

    public static Field tryToGetFieldOfProperty(PropertyStorage storage, Class<?> entity, String property) {
        if (isCachedField(storage, entity, property)) {
            return getCachedField(storage, entity, property);
        }

        Field field = null;

        try {
            field = entity.getDeclaredField(property);
        } catch (Exception e) {
            try {
                try {
                    field = entity.getDeclaredField("_" + property);
                } catch (Exception e1) {
                    if (property.length() > 1
                            && Character.isUpperCase(property.charAt(0))
                            && Character.isUpperCase(property.charAt(1))) {
                        //don't use Introspector#decapitalize here
                        field = entity.getDeclaredField(property.substring(0, 1).toLowerCase() + property.substring(1));
                    } else {
                        field = entity.getDeclaredField(Introspector.decapitalize(property));
                    }
                }
            } catch (NoSuchFieldException e1) {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("field " + property + " or _" + property + " not found", e1);
                }
            }
        }

        tryToCacheField(storage, entity, property, field);

        return field;
    }

    private static void tryToCacheField(PropertyStorage storage, Class<?> entity, String property, Field field) {
        if (!storage.containsField(entity, property)) {
            storage.storeField(entity, property, field);
        }
    }

    private static boolean isCachedField(PropertyStorage storage, Class<?> entity, String property) {
        return storage.containsField(entity, property);
    }

    private static Field getCachedField(PropertyStorage storage, Class<?> entity, String property) {
        return storage.getField(entity, property);
    }

    private static boolean isCachedMethod(PropertyStorage storage, Class<?> entity, String property) {
        return storage.containsMethod(entity, property);
    }

    private static void tryToCacheMethod(PropertyStorage storage, Class<?> entity, String property, Method method) {
        if (!storage.containsMethod(entity, property)) {
            storage.storeMethod(entity, property, method);
        }
    }

    private static Method getCachedMethod(PropertyStorage storage, Class<?> entity, String property) {
        return storage.getMethod(entity, property);
    }
}

