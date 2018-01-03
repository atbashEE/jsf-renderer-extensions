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

/**
 * For those situations where you need a {@link ComponentInitializer} which is executed every time the view is rendered.
 * A standard initializer is only executed once, except for those components which are within a ui:repeat or UIData component (like h:datatable).
 */
public interface RepeatableComponentInitializer extends ComponentInitializer {
}
