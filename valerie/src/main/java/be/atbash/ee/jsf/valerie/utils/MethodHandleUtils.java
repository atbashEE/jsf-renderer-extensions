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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Date;

public class MethodHandleUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandleUtils.class);
    private static final MethodType DATE_GETTER = MethodType.methodType(Date.class);

    public static MethodHandle getSetterHandle(Class<?> target, String property, Class<?> propertyType) {

        MethodType methodType = MethodType.methodType(void.class, propertyType);

        try {
            return MethodHandles.lookup().findVirtual(target, setAccessorMethodName(property), methodType);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            LOGGER.warn(String.format("Unable to find/access the %s property '%s' of class %s", propertyType.getSimpleName(), property, target.getName()), e);
        }
        return null;
    }


    /**
     * Only for a Date Property!
     *
     * @param target
     * @param property
     * @return
     */
    public static MethodHandle getGetterHandle(Class<?> target, String property) {

        try {
            return MethodHandles.lookup().findVirtual(target, getAccessorMethodName(property), DATE_GETTER);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            LOGGER.warn(String.format("Unable to find/access the Date property '%s' of class %s", property, target.getName()), e);
        }
        return null;
    }

    // FIXME We should cache this.

    private static String setAccessorMethodName(String property) {
        String builder;
        builder = "set" + Character.toUpperCase(property.charAt(0)) +
                property.substring(1);
        return builder;
    }

    private static String getAccessorMethodName(String property) {
        return "get" + Character.toUpperCase(property.charAt(0)) +
                property.substring(1);
    }

}
