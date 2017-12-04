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
package be.atbash.ee.jsf.jerry.ordering;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Documented
/**
 * allowed to use for classes which implement interfaces which have the marker @InvocationOrderSupport
 *
 * suggested ranges (mainly for name-mappers):
 * negative values for "extreme" cases
 *
 * 1-49 for custom artifacts which should have the highest priority
 * 50-99 for add-ons which provide artifacts which should have a higher priority than the default artifacts
 * 100-999 internal artifacts
 * 1000+ for custom artifacts
 *
 *
 * a priority should be unique within one artifact-type - that means
 * if a name-mapper has priority 100, it's ok that an exception-interceptor also has priority 100.
 * but a 2nd name-mapper shouldn't have priority 100
 *
 */
public @interface InvocationOrder {
    /**
     * default priority for custom artifacts (if they should get added after the internal versions
     *
     * @return the priority of an artifact annotated with this annotation
     */
    int value() default 1000;
}