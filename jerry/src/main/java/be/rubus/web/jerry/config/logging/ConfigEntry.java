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
package be.rubus.web.jerry.config.logging;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Methods annotated with ConfigEntry will be shown in the log file during startup of the container.
 */
@Target({METHOD})
@Retention(RUNTIME)
public @interface ConfigEntry {
    /**
     * Define the value for the log entry because the method can't or shouldn't be executed just for logging purposes.
     *
     * @return Array of String values which are used to print out in the log.
     */
    String[] value() default {};

    /**
     * Special  case of the value attribute where the method returns a class.
     *
     * @return Type to be logged
     */
    Class<?> classResult() default Void.class;
}
