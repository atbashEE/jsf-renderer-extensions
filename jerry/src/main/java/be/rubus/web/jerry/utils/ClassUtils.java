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
package be.rubus.web.jerry.utils;

import javax.enterprise.inject.Typed;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Util methods for classes, {@link ClassLoader} and {@link java.util.jar.Manifest} handling
 */
// Copied from DeltaSpike
public abstract class ClassUtils {
    /**
     * Constructor which prevents the instantiation of this class
     */
    private ClassUtils() {
        // prevent instantiation
    }

    /**
     * Detect the right ClassLoader.
     * The lookup order is determined by:
     * <ol>
     * <li>ContextClassLoader of the current Thread</li>
     * <li>ClassLoader of the given Object 'o'</li>
     * <li>ClassLoader of this very ClassUtils class</li>
     * </ol>
     *
     * @param o if not <code>null</code> it may get used to detect the classloader.
     * @return The {@link ClassLoader} which should get used to create new instances
     */
    public static ClassLoader getClassLoader(Object o) {
        if (System.getSecurityManager() != null) {
            return AccessController.doPrivileged(new GetClassLoaderAction(o));
        } else {
            return getClassLoaderInternal(o);
        }
    }

    @Typed()
    static class GetClassLoaderAction implements PrivilegedAction<ClassLoader> {
        private Object object;

        GetClassLoaderAction(Object object) {
            this.object = object;
        }

        @Override
        public ClassLoader run() {
            try {
                return getClassLoaderInternal(object);
            } catch (Exception e) {
                return null;
            }
        }
    }

    private static ClassLoader getClassLoaderInternal(Object o) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        if (loader == null && o != null) {
            loader = o.getClass().getClassLoader();
        }

        if (loader == null) {
            loader = ClassUtils.class.getClassLoader();
        }

        return loader;
    }

    /**
     * Tries to load a class based on the given name
     *
     * @param name name of the class
     * @return loaded class or <code>null</code> if it isn't in the classpath
     */
    public static Class tryToLoadClassForName(String name) {
        try {
            return loadClassForName(name);
        } catch (ClassNotFoundException e) {
            //do nothing - it's just a try
            return null;
        }
    }

    /**
     * Loads class for the given name
     *
     * @param name name of the class
     * @return loaded class
     * @throws ClassNotFoundException if the class can't be loaded
     */
    public static Class loadClassForName(String name) throws ClassNotFoundException {
        try {
            // Try WebApp ClassLoader first
            return Class.forName(name, false, // do not initialize for faster startup
                    getClassLoader(null));
        } catch (ClassNotFoundException ignore) {
            // fallback: Try ClassLoader for ClassUtils (i.e. the myfaces.jar lib)
            return Class.forName(name, false, // do not initialize for faster startup
                    ClassUtils.class.getClassLoader());
        }
    }

}
