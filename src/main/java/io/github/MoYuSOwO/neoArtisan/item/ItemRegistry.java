package io.github.MoYuSOwO.neoArtisan.item;

import io.github.MoYuSOwO.neoArtisan.NeoArtisan;
import io.github.MoYuSOwO.neoArtisan.util.NamespacedKeyDataType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class ItemRegistry {

    private static final ConcurrentHashMap<NamespacedKey, ArtisanItem> registry = new ConcurrentHashMap<>();

    private ItemRegistry() {}

    public static void init() {
        ItemRegistry.registerItemFromFile();
        NeoArtisan.instance().getLogger().info("成功从文件注册 " + registry.size() + " 个自定义物品");
    }

    public static void registerItemFromFile() {
        File[] files = ReadUtil.readAllFiles();
        if (files != null) {
            for (File file : files) {
                if (ReadUtil.isYmlFile(file)) {
                    readYml(YamlConfiguration.loadConfiguration(file));
                }
            }
        }
    }

    private static void readYml(YamlConfiguration item) {
        NamespacedKey registryId = ReadUtil.getRegistryId(item);
        Material rawMaterial = ReadUtil.getRawMaterial(item);
        boolean hasOriginalCraft = ReadUtil.getOriginalCraft(item);
        int customModelData = ReadUtil.getCustomModelData(item);
        String displayName = ReadUtil.getDisplayName(item);
        List<String> lore = ReadUtil.getLore(item);
        FoodProperty foodProperty = ReadUtil.getFood(item);
        registerItem(registryId, rawMaterial, hasOriginalCraft, customModelData, displayName, lore, foodProperty);
    }

    public static void registerItem(NamespacedKey registryId, Material rawMaterial, boolean hasOriginalCraft, int customModelData, String displayName, List<String> lore, FoodProperty foodProperty) {
        registry.put(registryId, new ArtisanItem(registryId, rawMaterial, hasOriginalCraft, customModelData, displayName, lore, foodProperty));
    }

    public static Set<String> getAllIds() {
        Set<NamespacedKey> keySet = registry.keySet();
        Set<String> keySetString = new HashSet<>();
        for (NamespacedKey key : keySet) {
            keySetString.add(key.asString());
        }
        return keySetString;
    }

    public static @NotNull NamespacedKey getRegistryId(@NotNull ItemStack itemStack) {
        if (!itemStack.getItemMeta().getPersistentDataContainer().has(NeoArtisan.getArtisanItemKey())) return itemStack.getType().getKey();
        NamespacedKey registryId = itemStack.getItemMeta().getPersistentDataContainer().get(NeoArtisan.getArtisanItemKey(), NamespacedKeyDataType.TYPE);
        return Objects.requireNonNull(registryId);
    }

    public static boolean hasArtisanItem(NamespacedKey registryId) {
        return registry.containsKey(registryId);
    }

    public static @NotNull ArtisanItem getArtisanItem(NamespacedKey registryId) {
        ArtisanItem artisanItem = registry.get(registryId);
        if (artisanItem == null) throw new IllegalArgumentException("You should use has method to check before get!");
        return artisanItem;
    }
}
