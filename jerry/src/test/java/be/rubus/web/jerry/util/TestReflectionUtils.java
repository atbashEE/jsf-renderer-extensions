package be.rubus.web.jerry.util;

import java.lang.reflect.Field;

/**
 *
 */
public final class TestReflectionUtils {

    private TestReflectionUtils() {
    }

    public static Object getValueOf(Object instance, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Object target;
        Class<?> targetClass;
        if (instance instanceof Class<?>) {
            target = null;  // static field
            targetClass = (Class<?>) instance;
        } else {
            target = instance;
            targetClass = instance.getClass();
        }
        Field field = targetClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }

    public static void resetOf(Object instance, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Object target;
        Class<?> targetClass;
        if (instance instanceof Class<?>) {
            target = null;  // static field
            targetClass = (Class<?>) instance;
        } else {
            target = instance;
            targetClass = instance.getClass();
        }
        Field field = targetClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, null);
    }

}
