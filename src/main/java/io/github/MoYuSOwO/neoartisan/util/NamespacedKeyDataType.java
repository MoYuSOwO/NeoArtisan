package io.github.moyusowo.neoartisan.util;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class NamespacedKeyDataType implements PersistentDataType<String, NamespacedKey> {

    public static final NamespacedKeyDataType TYPE = new NamespacedKeyDataType();

    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public @NotNull Class<NamespacedKey> getComplexType() {
        return NamespacedKey.class;
    }

    @Override
    public @NotNull String toPrimitive(@NotNull NamespacedKey complex, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        return complex.toString();
    }

    @Override
    public @NotNull NamespacedKey fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext persistentDataAdapterContext) {
        NamespacedKey key = NamespacedKey.fromString(primitive);
        if (key == null) throw new IllegalArgumentException("Invalid NamespacedKey format: " + primitive);
        return key;
    }
}
