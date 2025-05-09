package io.github.moyusowo.neoartisan.item;

import io.github.moyusowo.neoartisan.attribute.AttributeRegistryImpl;
import io.github.moyusowo.neoartisan.attribute.AttributeTypeRegistryImpl;
import io.github.moyusowo.neoartisanapi.api.item.AttributeProperty;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AttributePropertyImpl implements AttributeProperty {

    private final Map<NamespacedKey, Object> globalAttributeValues, itemstackAttributeValues;

    public AttributePropertyImpl() {
        this.globalAttributeValues = new HashMap<>();
        this.itemstackAttributeValues = new HashMap<>();
    }

    @Override
    public AttributeProperty empty() {
        return new AttributePropertyImpl();
    }

    @Override
    public void addGlobalAttribute(NamespacedKey attributeKey, Object value) {
        if (!AttributeRegistryImpl.getInstance().hasGlobalAttribute(attributeKey)) throw new IllegalArgumentException("You didn't register this attribute!");
        String typeName = AttributeRegistryImpl.getInstance().getGlobalAttributeTypeName(attributeKey);
        Class<?> typeJavaClass = AttributeTypeRegistryImpl.getInstance().getAttributeJavaType(typeName);
        if (!typeJavaClass.isInstance(value)) throw new IllegalArgumentException("the value doesn't match the attribute!");
        this.globalAttributeValues.put(attributeKey, value);
    }

    @Override
    public void addItemstackAttribute(NamespacedKey attributeKey, Object value) {
        if (attributeKey == null) throw new IllegalArgumentException("You can't provide a null key!");
        if (!AttributeRegistryImpl.getInstance().hasItemstackAttribute(attributeKey)) throw new IllegalArgumentException("You didn't register this attribute!");
        String typeName = AttributeRegistryImpl.getInstance().getItemstackAttributeTypeName(attributeKey);
        Class<?> typeJavaClass = AttributeTypeRegistryImpl.getInstance().getAttributeJavaType(typeName);
        if (!typeJavaClass.isInstance(value)) throw new IllegalArgumentException("the value doesn't match the attribute!");
        this.itemstackAttributeValues.put(attributeKey, value);
    }

    @Override
    public boolean hasGlobalAttribute(NamespacedKey attributeKey) {
        return this.globalAttributeValues.containsKey(attributeKey);
    }

    @Override
    public boolean hasItemstackAttribute(NamespacedKey attributeKey) {
        return this.itemstackAttributeValues.containsKey(attributeKey);
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <T> T getGlobalAttributeValue(NamespacedKey attributeKey) {
        String typeName = AttributeRegistryImpl.getInstance().getGlobalAttributeTypeName(attributeKey);
        Class<T> type = (Class<T>) AttributeTypeRegistryImpl.getInstance().getAttributeJavaType(typeName);
        return type.cast(this.globalAttributeValues.get(attributeKey));
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <T> T getItemstackAttributeValue(NamespacedKey attributeKey) {
        String typeName = AttributeRegistryImpl.getInstance().getItemstackAttributeTypeName(attributeKey);
        Class<T> type = (Class<T>) AttributeTypeRegistryImpl.getInstance().getAttributeJavaType(typeName);
        return type.cast(this.itemstackAttributeValues.get(attributeKey));
    }

    @Override
    public boolean isEmpty() {
        return this.globalAttributeValues.isEmpty() && this.itemstackAttributeValues.isEmpty();
    }

    @Override
    public NamespacedKey[] getItemstackAttributeKeys() {
        return this.itemstackAttributeValues.keySet().toArray(new NamespacedKey[0]);
    }
}
