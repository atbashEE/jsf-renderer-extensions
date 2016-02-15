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
 *
 */
package be.rubus.web.jerry.renderkit.model;

/**
 *
 */
public enum InterceptorCalls {
    BEFORE_ENCODE_BEGIN, AFTER_ENCODE_BEGIN,
    BEFORE_ENCODE_CHILDREN, AFTER_ENCODE_CHILDREN,
    BEFORE_ENCODE_END, AFTER_ENCODE_END,
    BEFORE_DECODE, AFTER_DECODE,
    BEFORE_CONVERTED_VALUE, AFTER_CONVERTED_VALUE
}
