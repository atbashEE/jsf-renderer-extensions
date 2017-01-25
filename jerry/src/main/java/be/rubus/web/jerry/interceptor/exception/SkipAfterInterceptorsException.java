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
 */
package be.rubus.web.jerry.interceptor.exception;

/**
 * Marker exception that can be thrown by an after-method
 * ({@link be.rubus.web.jerry.interceptor.RendererInterceptor})
 * to stop the execution of the subsequent interceptors.
 */
public class SkipAfterInterceptorsException extends Exception {
    private static final long serialVersionUID = -1472790498766251346L;
}