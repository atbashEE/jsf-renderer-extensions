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
package be.atbash.ee.jsf.jerry.ordering;


import be.atbash.util.ProxyUtils;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Comparator which order instances according to the info specified in {see InvocationOrder}.
 * Ordening is done from low value to high value and those instances that doesn't have the annotation are placed at the end.
 */
public class InvocationOrderComparator<T> implements Comparator<T>, Serializable {
    private static final long serialVersionUID = 2287258656373616584L;

    public int compare(T nm1, T nm2) {
        if (hasPriority(nm1) && hasPriority(nm2)) {
            InvocationOrder priority1 = (InvocationOrder) getUnproxiedClass(nm1).getAnnotation(InvocationOrder.class);
            InvocationOrder priority2 = (InvocationOrder) getUnproxiedClass(nm2).getAnnotation(InvocationOrder.class);
            return isPriorityHigher(priority1, priority2);
        }
        if (!hasPriority(nm1) && !hasPriority(nm2)) {
            return 0;
        }
        return hasPriority(nm1) ? -1 : 1;
    }

    private int isPriorityHigher(InvocationOrder priority1, InvocationOrder priority2) {
        if (priority1.value() == priority2.value()) {
            return 0;
        }

        return priority1.value() < priority2.value() ? -1 : 1;
    }

    private boolean hasPriority(Object nm) {
        return getUnproxiedClass(nm).isAnnotationPresent(InvocationOrder.class);
    }

    private Class getUnproxiedClass(Object nm) {
        return ProxyUtils.getUnproxiedClass(nm.getClass());
    }
}