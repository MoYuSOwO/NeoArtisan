package io.github.MoYuSOwO.neoArtisan.attribute;

import io.github.MoYuSOwO.neoArtisan.util.Todos;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class AttributeTypeRegistry {

    private AttributeTypeRegistry() {}

    private static final Map<String, PersistentDataType<?, ?>> attributeTypeRegistry = new HashMap<>();

    static {
        attributeTypeRegistry.put("int", PersistentDataType.INTEGER);
        attributeTypeRegistry.put("integer", PersistentDataType.INTEGER);
        attributeTypeRegistry.put("double", PersistentDataType.DOUBLE);
        attributeTypeRegistry.put("string", PersistentDataType.STRING);
    }

    public static void registerAttributeType(String typeName, PersistentDataType<?, ?> PDCType) {
        Todos.fail("未完成属性注册系统，不能读取自定义类", Todos.Priority.CRITICAL);
        attributeTypeRegistry.put(typeName, PDCType);
    }

    public static boolean hasAttributeType(String typeName) {
        return attributeTypeRegistry.containsKey(typeName);
    }

    public static @NotNull Class<?> getAttributeJavaType(String typeName) {
        return attributeTypeRegistry.get(typeName).getComplexType();
    }

    public static @NotNull PersistentDataType<?, ?> getAttributePDCType(String typeName) {
        return attributeTypeRegistry.get(typeName);
    }
}
