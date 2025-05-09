package io.github.moyusowo.neoartisan.util;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

public class ReflectionUtil {
    private static final ConcurrentHashMap<String, Field> FIELD_CACHE = new ConcurrentHashMap<>();

    public static Object getField(Object obj, String fieldName) throws Exception {
        String cacheKey = obj.getClass().getName() + "#" + fieldName;
        Field field = FIELD_CACHE.computeIfAbsent(cacheKey, k -> {
            try {
                Field f = obj.getClass().getDeclaredField(fieldName);
                f.setAccessible(true);
                return f;
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Field not found: " + fieldName, e);
            }
        });
        return field.get(obj);
    }
    public static void setField(Object obj, String fieldName, Object value) throws Exception {
        String cacheKey = obj.getClass().getName() + "#" + fieldName;
        Field field = FIELD_CACHE.computeIfAbsent(cacheKey, k -> {
            try {
                Field f = obj.getClass().getDeclaredField(fieldName);
                f.setAccessible(true);
                return f;
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Field not found: " + fieldName, e);
            }
        });
        field.set(obj, value);
    }
}
