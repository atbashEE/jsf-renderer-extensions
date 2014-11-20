package be.rubus.web.valerie.utils;

import be.rubus.web.valerie.storage.PropertyStorage;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

/**
 *
 */
// FIXME, use the Java 7 MethodHandler instead of reflection API
public class ReflectionUtils {

    public static Method tryToGetMethod(Class targetClass, String targetMethodName) {
        return tryToGetMethod(targetClass, targetMethodName, null);
    }

    public static Method tryToGetMethod(Class targetClass, String targetMethodName, Class... parameterTypes) {
        try {
            return getMethod(targetClass, targetMethodName, parameterTypes);
        } catch (Exception e) {
            //do nothing - it's just a try
            return null;
        }
    }

    public static Method getMethod(Class targetClass, String targetMethodName)
            throws NoSuchMethodException {
        return getMethod(targetClass, targetMethodName, null);
    }

    public static Method getMethod(Class targetClass, String targetMethodName, Class... parameterTypes)
            throws NoSuchMethodException {
        Class currentClass = targetClass;
        Method targetMethod = null;

        while (!Object.class.getName().equals(currentClass.getName())) {
            try {
                targetMethod = currentClass.getDeclaredMethod(targetMethodName, parameterTypes);
                break;
            } catch (NoSuchMethodException e) {
                currentClass = currentClass.getSuperclass();
            }
        }

        if (targetMethod == null) {
            for (Class currentInterface : targetClass.getInterfaces()) {
                currentClass = currentInterface;

                while (currentClass != null) {
                    try {
                        targetMethod = currentClass.getDeclaredMethod(targetMethodName, parameterTypes);
                        break;
                    } catch (NoSuchMethodException e) {
                        currentClass = currentClass.getSuperclass();
                    }
                }
            }
        }

        if (targetMethod != null) {
            return targetMethod;
        }

        throw new NoSuchMethodException("there is no method with the name '" + targetMethodName + "'" +
                " class: " + targetClass.getName());
    }

    public static Object tryToInvokeMethod(Object target, Method method) {
        return tryToInvokeMethod(target, method, null);
    }

    public static Object tryToInvokeMethodOfClass(Class target, Method method) {
        return tryToInvokeMethodOfClass(target, method, null);
    }

    public static Object tryToInvokeMethodOfClass(Class target, Method method, Object[] args) {
        try {
            return invokeMethodOfClass(target, method, args);
        } catch (Exception e) {
            //do nothing - it's just a try
            return null;
        }
    }

    public static Object invokeMethodOfClass(Class target, Method method)
            throws IllegalAccessException, InstantiationException, InvocationTargetException {
        return invokeMethod(target.newInstance(), method, null);
    }

    public static Object invokeMethodOfClass(Class target, Method method, Object... args)
            throws IllegalAccessException, InstantiationException, InvocationTargetException {
        return invokeMethod(target.newInstance(), method, args);
    }

    public static Object tryToInvokeMethod(Object target, Method method, Object... args) {
        try {
            return invokeMethod(target, method, args);
        } catch (Exception e) {
            //do nothing - it's just a try
            return null;
        }
    }

    public static Object invokeMethod(Object target, Method method)
            throws InvocationTargetException, IllegalAccessException {
        return invokeMethod(target, method, null);
    }

    public static Object invokeMethod(Object target, Method method, Object... args)
            throws InvocationTargetException, IllegalAccessException {
        method.setAccessible(true);
        return method.invoke(target, args);
    }

    public static Object getBaseOfPropertyChain(Object baseObject, String propertyChain) {
        StringTokenizer tokenizer = new StringTokenizer(propertyChain, ".");

        Object currentBase = baseObject;
        String currentProperty;
        Method currentMethod;

        while (tokenizer.hasMoreTokens()) {
            currentProperty = tokenizer.nextToken();

            //ignore the last property
            if (!tokenizer.hasMoreTokens()) {
                break;
            }

            //no is - it's only possible at properties not at bean level
            currentMethod = tryToGetMethod(ProxyUtils.getUnproxiedClass(currentBase.getClass()),
                    "get" + currentProperty.substring(0, 1).toUpperCase() + currentProperty.substring(1));
            currentBase = tryToInvokeMethod(currentBase, currentMethod);
        }

        return currentBase;
    }


    public static Method tryToGetMethodOfProperty(PropertyStorage storage, Class entity, String property) {
        if (isCachedMethod(storage, entity, property)) {
            return getCachedMethod(storage, entity, property);
        }

        Method method = tryToGetReadMethod(entity, property);

        tryToCacheMethod(storage, entity, property, method);

        return method;
    }


    public static Field tryToGetFieldOfProperty(PropertyStorage storage, Class entity, String property) {
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
                // TODO
                //LOGGER.log(Level.FINEST, "field " + property + " or _" + property + " not found", e1);
            }
        }

        tryToCacheField(storage, entity, property, field);

        return field;
    }

    private static void tryToCacheField(PropertyStorage storage, Class entity, String property, Field field) {
        if (!storage.containsField(entity, property)) {
            storage.storeField(entity, property, field);
        }
    }

    private static boolean isCachedField(PropertyStorage storage, Class entity, String property) {
        return storage.containsField(entity, property);
    }

    private static Method tryToGetReadMethod(Class baseBeanClass, String property) {
        Method method = ReflectionUtils.tryToGetReadMethodViaBeanInfo(baseBeanClass, property);

        if (method == null) {
            method = ReflectionUtils.tryToGetReadMethodManually(baseBeanClass, property);
        }
        return method;
    }

    private static Method tryToGetReadMethodViaBeanInfo(Class entity, String property) {
        if (useBeanInfo()) {
            try {
                BeanInfo beanInfo = Introspector.getBeanInfo(entity);
                for (PropertyDescriptor propertyDescriptor : beanInfo
                        .getPropertyDescriptors()) {
                    if (property.equals(propertyDescriptor.getName())
                            && propertyDescriptor.getReadMethod() != null) {
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
        // FIXME
        return true;
        //return Boolean.TRUE.equals(ExtValContext.getContext().getGlobalProperty(BeanInfo.class.getName()));
    }

    private static Method tryToGetReadMethodManually(Class entity, String property) {
        property = property.substring(0, 1).toUpperCase() + property.substring(1);

        try {
            //changed to official bean spec. due to caching there is no performance issue any more
            return entity.getDeclaredMethod("is" + property);
        } catch (NoSuchMethodException e) {
            try {
                return entity.getDeclaredMethod("get" + property);
            } catch (NoSuchMethodException e1) {
                // TODO

                //LOGGER.finest("method not found - class: " + entity.getName()
                //        + " - methods: " + "get" + property + " " + "is" + property);

                return null;
            }
        }
    }

    private static Field getCachedField(PropertyStorage storage, Class entity, String property) {
        return storage.getField(entity, property);
    }

    private static boolean isCachedMethod(PropertyStorage storage, Class entity, String property) {
        return storage.containsMethod(entity, property);
    }

    private static void tryToCacheMethod(PropertyStorage storage, Class entity, String property, Method method) {
        if (!storage.containsMethod(entity, property)) {
            storage.storeMethod(entity, property, method);
        }
    }

    private static Method getCachedMethod(PropertyStorage storage, Class entity, String property) {
        return storage.getMethod(entity, property);
    }
}

