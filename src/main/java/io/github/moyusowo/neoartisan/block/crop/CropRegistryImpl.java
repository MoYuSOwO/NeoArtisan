package io.github.moyusowo.neoartisan.block.crop;

import org.bukkit.NamespacedKey;

import java.util.concurrent.ConcurrentHashMap;

public class CropRegistryImpl {

    private static CropRegistryImpl instance;

    public static CropRegistryImpl getInstance() {
        return instance;
    }

    public static void init() {
        new CropRegistryImpl();
    }

    private final ConcurrentHashMap<NamespacedKey, ArtisanCropImpl> registry;

    private CropRegistryImpl() {
        instance = this;
        registry = new ConcurrentHashMap<>();
    }
}
