package io.github.MoYuSOwO.neoArtisan.item;

import io.github.MoYuSOwO.neoArtisan.NeoArtisan;
import io.github.MoYuSOwO.neoArtisan.util.NamespacedKeyDataType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
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
        ItemBehavior.init();
        NeoArtisan.logger().info("成功从文件注册 " + registry.size() + " 个自定义物品");
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
        Integer customModelData = ReadUtil.getCustomModelData(item);
        String displayName = ReadUtil.getDisplayName(item);
        List<String> lore = ReadUtil.getLore(item);
        FoodProperty foodProperty = ReadUtil.getFood(item);
        WeaponProperty weaponProperty = ReadUtil.getWeapon(item);
        Integer maxDurability = ReadUtil.getMaxDurability(item);
        ArmorProperty armorProperty = ReadUtil.getArmor(item);
        registerItem(registryId, rawMaterial, hasOriginalCraft, customModelData, displayName, lore, foodProperty, weaponProperty, maxDurability, armorProperty);
    }

    public static void registerItem(
            NamespacedKey registryId,
            Material rawMaterial,
            boolean hasOriginalCraft,
            @Nullable Integer customModelData,
            String displayName,
            List<String> lore,
            @NotNull FoodProperty foodProperty,
            @NotNull WeaponProperty weaponProperty,
            @Nullable Integer maxDurability,
            @NotNull ArmorProperty armorProperty
    ) {
        registry.put(registryId, new ArtisanItem(registryId, rawMaterial, hasOriginalCraft, customModelData, displayName, lore, foodProperty, weaponProperty, maxDurability, armorProperty));
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
        if (!itemStack.getItemMeta().getPersistentDataContainer().has(NeoArtisan.getArtisanItemIdKey())) return itemStack.getType().getKey();
        NamespacedKey registryId = itemStack.getItemMeta().getPersistentDataContainer().get(NeoArtisan.getArtisanItemIdKey(), NamespacedKeyDataType.TYPE);
        return Objects.requireNonNull(registryId);
    }

    public static boolean hasItem(@Nullable NamespacedKey registryId) {
        if (registryId == null) return false;
        else if (isArtisanItem(registryId)) return true;
        else return registryId.getNamespace().equals("minecraft") && Material.getMaterial(registryId.getKey().toUpperCase()) != null;
    }

    public static ItemStack getItemStack(NamespacedKey registryId, int count) {
        if (isArtisanItem(registryId)) return getArtisanItem(registryId).getItemStack(count);
        Material material = Material.getMaterial(registryId.getKey().toUpperCase());
        if (material == null) throw new IllegalArgumentException("You should use has method to check before get!");
        ItemStack itemStack = new ItemStack(material);
        itemStack.setAmount(Math.min(count, itemStack.getMaxStackSize()));
        return itemStack;
    }

    public static ItemStack getItemStack(NamespacedKey registryId) {
        return getItemStack(registryId, 1);
    }

    public static boolean isArtisanItem(@Nullable NamespacedKey registryId) {
        if (registryId == null) return false;
        return registry.containsKey(registryId);
    }

    public static boolean isArtisanItem(@Nullable ItemStack itemStack) {
        if (itemStack == null) return false;
        return isArtisanItem(getRegistryId(itemStack));
    }

    public static @NotNull ArtisanItem getArtisanItem(NamespacedKey registryId) {
        ArtisanItem artisanItem = registry.get(registryId);
        if (artisanItem == null) throw new IllegalArgumentException("You should use has method to check before get!");
        return artisanItem;
    }

    public static @NotNull ArtisanItem getArtisanItem(ItemStack itemStack) {
        ArtisanItem artisanItem = registry.get(getRegistryId(itemStack));
        if (artisanItem == null) throw new IllegalArgumentException("You should use has method to check before get!");
        return artisanItem;
    }
}
