package io.github.moyusowo.neoartisan.attribute;

import io.github.moyusowo.neoartisanapi.api.attribute.AttributeTypeRegistry;
import io.github.moyusowo.neoartisan.util.Todos;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

final class AttributeTypeRegistryImpl implements AttributeTypeRegistry {

    private static AttributeTypeRegistryImpl instance;

    public static void init() {
        new AttributeTypeRegistryImpl();
    }

    public static AttributeTypeRegistryImpl getInstance() {
        return instance;
    }

    private AttributeTypeRegistryImpl() {
        attributeTypeRegistry = new HashMap<>();
        attributeTypeRegistry.put("int", PersistentDataType.INTEGER);
        attributeTypeRegistry.put("integer", PersistentDataType.INTEGER);
        attributeTypeRegistry.put("double", PersistentDataType.DOUBLE);
        attributeTypeRegistry.put("string", PersistentDataType.STRING);
        instance = this;
    }

    private final Map<String, PersistentDataType<?, ?>> attributeTypeRegistry;

    @Override
    public void registerAttributeType(@NotNull String typeName, @NotNull PersistentDataType<?, ?> PDCType) {
        Todos.fail("未完成属性注册系统，不能读取自定义类", Todos.Priority.CRITICAL);
//        attributeTypeRegistry.put(typeName, PDCType);
    }

    @Override
    public boolean hasAttributeType(@NotNull String typeName) {
        return attributeTypeRegistry.containsKey(typeName);
    }

    @Override
    public @NotNull Class<?> getAttributeJavaType(@NotNull String typeName) {
        return attributeTypeRegistry.get(typeName).getComplexType();
    }

    @Override
    public @NotNull PersistentDataType<?, ?> getAttributePDCType(@NotNull String typeName) {
        return attributeTypeRegistry.get(typeName);
    }
}
