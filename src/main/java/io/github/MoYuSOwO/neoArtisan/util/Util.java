package io.github.MoYuSOwO.neoArtisan.util;

import io.github.MoYuSOwO.neoArtisan.item.ItemRegistry;
import org.bukkit.NamespacedKey;

public final class Util {

    private Util() {}

    public static NamespacedKey stringToNamespaceKey(String s) {
        String id = s;
        if (!id.contains(":")) id = "minecraft:" + s;
        NamespacedKey key = NamespacedKey.fromString(id);
        System.out.println(key);
        if (!ItemRegistry.hasItem(key)) throw new IllegalArgumentException(s + " is not a effective registryId");
        return key;
    }
}
