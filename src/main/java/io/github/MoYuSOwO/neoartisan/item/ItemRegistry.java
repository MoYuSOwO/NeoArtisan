package io.github.moyusowo.neoartisan.item;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisanapi.api.item.*;
import io.github.moyusowo.neoartisan.attribute.AttributeRegistry;
import io.github.moyusowo.neoartisan.attribute.AttributeTypeRegistry;
import io.github.moyusowo.neoartisan.util.NamespacedKeyDataType;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class ItemRegistry implements ItemRegistryAPI {

    private static ItemRegistry instance;

    public static ItemRegistry getInstance() {
        return instance;
    }

    private final ConcurrentHashMap<NamespacedKey, ArtisanItem> registry;

    private ItemRegistry() {
        registry = new ConcurrentHashMap<>();
        registerItemFromFile();
        NeoArtisan.logger().info("成功从文件注册 " + registry.size() + " 个自定义物品");
        instance = this;
    }

    public static void init() {
        new ItemRegistry();
    }

    public void registerItemFromFile() {
        File[] files = ReadUtil.readAllFiles();
        if (files != null) {
            for (File file : files) {
                if (ReadUtil.isYmlFile(file)) {
                    readYml(YamlConfiguration.loadConfiguration(file));
                }
            }
        }
    }

    private void readYml(YamlConfiguration item) {
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
        AttributeProperty attributeProperty = ReadUtil.getAttribute(item);
        registerItem(registryId, rawMaterial, hasOriginalCraft, customModelData, displayName, lore, foodProperty, weaponProperty, maxDurability, armorProperty, attributeProperty);
    }

    @Override
    public void registerItem(
            @NotNull NamespacedKey registryId,
            @NotNull Material rawMaterial,
            boolean hasOriginalCraft,
            @Nullable Integer customModelData,
            @NotNull String displayName,
            @NotNull List<String> lore,
            @NotNull FoodProperty foodProperty,
            @NotNull WeaponProperty weaponProperty,
            @Nullable Integer maxDurability,
            @NotNull ArmorProperty armorProperty,
            @NotNull AttributePropertyAPI attributeProperty
    ) {
        registry.put(registryId, new ArtisanItem(registryId, rawMaterial, hasOriginalCraft, customModelData, displayName, lore, foodProperty, weaponProperty, maxDurability, armorProperty, (AttributeProperty) attributeProperty));
    }

    public Set<String> getAllIds() {
        Set<NamespacedKey> keySet = registry.keySet();
        Set<String> keySetString = new HashSet<>();
        for (NamespacedKey key : keySet) {
            keySetString.add(key.asString());
        }
        return keySetString;
    }

    @Override
    public @NotNull NamespacedKey getRegistryId(@NotNull ItemStack itemStack) {
        if (!itemStack.getItemMeta().getPersistentDataContainer().has(NeoArtisan.getArtisanItemIdKey())) return itemStack.getType().getKey();
        NamespacedKey registryId = itemStack.getItemMeta().getPersistentDataContainer().get(NeoArtisan.getArtisanItemIdKey(), NamespacedKeyDataType.TYPE);
        return Objects.requireNonNull(registryId);
    }

    @Override
    public boolean hasItem(@Nullable NamespacedKey registryId) {
        if (registryId == null) return false;
        else if (isArtisanItem(registryId)) return true;
        else return registryId.getNamespace().equals("minecraft") && Material.getMaterial(registryId.getKey().toUpperCase()) != null;
    }

    @Override
    public @NotNull ItemStack getItemStack(NamespacedKey registryId, int count) {
        if (isArtisanItem(registryId)) return getArtisanItem(registryId).getItemStack(count);
        Material material = Material.getMaterial(registryId.getKey().toUpperCase());
        if (material == null) throw new IllegalArgumentException("You should use has method to check before get!");
        ItemStack itemStack = new ItemStack(material);
        itemStack.setAmount(Math.min(count, itemStack.getMaxStackSize()));
        return itemStack;
    }

    @Override
    public @NotNull ItemStack getItemStack(NamespacedKey registryId) {
        return getItemStack(registryId, 1);
    }

    @Override
    public boolean isArtisanItem(@Nullable NamespacedKey registryId) {
        if (registryId == null) return false;
        return registry.containsKey(registryId);
    }

    @Override
    public boolean isArtisanItem(@Nullable ItemStack itemStack) {
        if (itemStack == null) return false;
        return isArtisanItem(getRegistryId(itemStack));
    }

    @Override
    public @NotNull ArtisanItemAPI getArtisanItemAPI(@NotNull NamespacedKey registryId) {
        ArtisanItem artisanItem = registry.get(registryId);
        if (artisanItem == null) throw new IllegalArgumentException("You should use has method to check before get!");
        return artisanItem;
    }

    @Override
    public @NotNull ArtisanItemAPI getArtisanItemAPI(ItemStack itemStack) {
        ArtisanItem artisanItem = registry.get(getRegistryId(itemStack));
        if (artisanItem == null) throw new IllegalArgumentException("You should use has method to check before get!");
        return artisanItem;
    }

    public @NotNull ArtisanItem getArtisanItem(NamespacedKey registryId) {
        ArtisanItem artisanItem = registry.get(registryId);
        if (artisanItem == null) throw new IllegalArgumentException("You should use has method to check before get!");
        return artisanItem;
    }

    public @NotNull ArtisanItem getArtisanItem(ItemStack itemStack) {
        ArtisanItem artisanItem = registry.get(getRegistryId(itemStack));
        if (artisanItem == null) throw new IllegalArgumentException("You should use has method to check before get!");
        return artisanItem;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @Nullable <T> T getItemstackAttributeValue(@NotNull ItemStack itemStack, @NotNull NamespacedKey attributeKey) {
        if (itemStack.getPersistentDataContainer().has(attributeKey)) {
            String typeName = AttributeRegistry.getInstance().getItemstackAttributeTypeName(attributeKey);
            return (T) itemStack.getPersistentDataContainer().get(attributeKey, AttributeTypeRegistry.getInstance().getAttributePDCType(typeName));
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void setItemstackAttributeValue(@NotNull ItemStack itemStack, @NotNull NamespacedKey attributeKey, @NotNull T value) {
        ItemMeta meta = itemStack.getItemMeta();
        if (meta.getPersistentDataContainer().has(attributeKey)) {
            meta.getPersistentDataContainer().remove(attributeKey);
        }
        String typeName = AttributeRegistry.getInstance().getItemstackAttributeTypeName(attributeKey);
        PersistentDataType<?, T> type = (PersistentDataType<?, T>) AttributeTypeRegistry.getInstance().getAttributePDCType(typeName);
        meta.getPersistentDataContainer().set(attributeKey, type, value);
        itemStack.setItemMeta(meta);
    }
}
