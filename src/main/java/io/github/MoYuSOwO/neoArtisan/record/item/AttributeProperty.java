package io.github.MoYuSOwO.neoArtisan.record.item;

import io.github.MoYuSOwO.neoArtisan.attribute.AttributeRegistry;
import io.github.MoYuSOwO.neoArtisan.attribute.AttributeTypeRegistry;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AttributeProperty {

    private final Map<NamespacedKey, Object> globalAttributeValues, itemstackAttributeValues;

    public AttributeProperty() {
        this.globalAttributeValues = new HashMap<>();
        this.itemstackAttributeValues = new HashMap<>();
    }

    public void addGlobalAttribute(NamespacedKey attributeKey, Object value) {
        if (!AttributeRegistry.getInstance().hasGlobalAttribute(attributeKey)) throw new IllegalArgumentException("You didn't register this attribute!");
        String typeName = AttributeRegistry.getInstance().getGlobalAttributeTypeName(attributeKey);
        Class<?> typeJavaClass = AttributeTypeRegistry.getInstance().getAttributeJavaType(typeName);
        if (!typeJavaClass.isInstance(value)) throw new IllegalArgumentException("the value doesn't match the attribute!");
        this.globalAttributeValues.put(attributeKey, value);
    }

    public void addItemstackAttribute(NamespacedKey attributeKey, Object value) {
        if (attributeKey == null) throw new IllegalArgumentException("You can't provide a null key!");
        if (!AttributeRegistry.getInstance().hasItemstackAttribute(attributeKey)) throw new IllegalArgumentException("You didn't register this attribute!");
        String typeName = AttributeRegistry.getInstance().getItemstackAttributeTypeName(attributeKey);
        Class<?> typeJavaClass = AttributeTypeRegistry.getInstance().getAttributeJavaType(typeName);
        if (!typeJavaClass.isInstance(value)) throw new IllegalArgumentException("the value doesn't match the attribute!");
        this.itemstackAttributeValues.put(attributeKey, value);
    }

    public boolean hasGlobalAttribute(NamespacedKey attributeKey) {
        return this.globalAttributeValues.containsKey(attributeKey);
    }

    public boolean hasItemstackAttribute(NamespacedKey attributeKey) {
        return this.itemstackAttributeValues.containsKey(attributeKey);
    }

    @SuppressWarnings("unchecked")
    public @NotNull <T> T getGlobalAttributeValue(NamespacedKey attributeKey) {
        String typeName = AttributeRegistry.getInstance().getGlobalAttributeTypeName(attributeKey);
        Class<T> type = (Class<T>) AttributeTypeRegistry.getInstance().getAttributeJavaType(typeName);
        return type.cast(this.globalAttributeValues.get(attributeKey));
    }

    @SuppressWarnings("unchecked")
    public @NotNull <T> T getItemstackAttributeValue(NamespacedKey attributeKey) {
        String typeName = AttributeRegistry.getInstance().getItemstackAttributeTypeName(attributeKey);
        Class<T> type = (Class<T>) AttributeTypeRegistry.getInstance().getAttributeJavaType(typeName);
        return type.cast(this.itemstackAttributeValues.get(attributeKey));
    }

    public boolean isEmpty() {
        return this.globalAttributeValues.isEmpty() && this.itemstackAttributeValues.isEmpty();
    }

    public NamespacedKey[] getItemstackAttributeKeys() {
        return this.itemstackAttributeValues.keySet().toArray(new NamespacedKey[0]);
    }
}
