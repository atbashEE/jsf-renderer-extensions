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
package be.atbash.ee.jsf.jerry.utils;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public final class CDIUtils {

    private CDIUtils() {
    }

    public static <T> List<T> retrieveInstances(Class<T> classType) {
        List<T> result = new ArrayList<>();
        for (T t : CDI.current().select(classType)) {
            result.add(t);
        }
        return result;
    }

    public static <T> T retrieveInstance(Class<T> classType) {
        return CDI.current().select(classType).get();
    }

    public static <T> T retrieveOptionalInstance(Class<T> classType) {
        Instance<T> instance = CDI.current().select(classType);
        return instance.isUnsatisfied() ? null : instance.get();
    }

    public static void fireEvent(Object event) {
        CDI.current().getBeanManager().fireEvent(event);
    }
}
