package io.github.moyusowo.neoartisan.item;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisanapi.api.item.*;
import io.github.moyusowo.neoartisan.attribute.AttributeRegistryImpl;
import io.github.moyusowo.neoartisan.attribute.AttributeTypeRegistryImpl;
import io.github.moyusowo.neoartisan.util.NamespacedKeyDataType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class ItemRegistryImpl implements ItemRegistry {

    private static ItemRegistryImpl instance;

    public static ItemRegistryImpl getInstance() {
        return instance;
    }

    private final ConcurrentHashMap<NamespacedKey, ArtisanItemImpl> registry;

    private ItemRegistryImpl() {
        registry = new ConcurrentHashMap<>();
        registerItemFromFile();
        NeoArtisan.logger().info("成功从文件注册 " + registry.size() + " 个自定义物品");
        instance = this;
    }

    public static void init() {
        new ItemRegistryImpl();
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
        BuilderImpl builder = (BuilderImpl) builder();
        builder.registryId(ReadUtil.getRegistryId(item));
        builder.rawMaterial(ReadUtil.getRawMaterial(item));
        builder.hasOriginalCraft(ReadUtil.getOriginalCraft(item));
        Integer customModelData = ReadUtil.getCustomModelData(item);
        if (customModelData != null) builder.customModelData(customModelData);
        String displayName = ReadUtil.getDisplayName(item);
        if (displayName != null) builder.displayName(displayName);
        List<String> lore = ReadUtil.getLore(item);
        builder.lore(lore);
        builder.foodProperty(ReadUtil.getFood(item));
        builder.weaponProperty(ReadUtil.getWeapon(item));
        Integer maxDurability = ReadUtil.getMaxDurability(item);
        if (maxDurability != null) builder.maxDurability(maxDurability);
        builder.armorProperty(ReadUtil.getArmor(item));
        builder.attributeProperty(ReadUtil.getAttribute(item));
        builder.cropId(ReadUtil.getCropKey(item));
    }

    @NotNull
    @Override
    public Builder builder() {
        return new BuilderImpl();
    }

    @Override
    public void registerItem(@NotNull Builder builder) {
        BuilderImpl builderImpl = (BuilderImpl) builder;
        registry.put(builderImpl.registryId, builderImpl.build());
    }

    private static class BuilderImpl implements Builder {
        private NamespacedKey registryId;
        private Material rawMaterial;
        private boolean hasOriginalCraft;
        private Integer customModelData;
        private Component displayName;
        private List<Component> lore;
        private FoodProperty foodProperty;
        private WeaponProperty weaponProperty;
        private Integer maxDurability;
        private ArmorProperty armorProperty;
        private AttributePropertyImpl attributeProperty;
        private NamespacedKey cropId;

        private BuilderImpl() {
            this.registryId = null;
            this.rawMaterial = null;
            this.hasOriginalCraft = false;
            this.customModelData = null;
            this.displayName = null;
            this.lore = new ArrayList<>();
            this.foodProperty = FoodProperty.EMPTY;
            this.weaponProperty = WeaponProperty.EMPTY;
            this.maxDurability = null;
            this.armorProperty = ArmorProperty.EMPTY;
            this.attributeProperty = new AttributePropertyImpl();
            this.cropId = null;
        }

        @NotNull
        @Override
        public Builder registryId(@NotNull NamespacedKey registryId) {
            this.registryId = Objects.requireNonNull(registryId);
            return this;
        }

        @NotNull
        @Override
        public Builder rawMaterial(@NotNull Material rawMaterial) {
            this.rawMaterial = Objects.requireNonNull(rawMaterial);
            return this;
        }

        @NotNull
        @Override
        public Builder hasOriginalCraft(boolean hasOriginalCraft) {
            this.hasOriginalCraft = hasOriginalCraft;
            return this;
        }

        @NotNull
        @Override
        public Builder customModelData(int customModelData) {
            this.customModelData = customModelData;
            return this;
        }

        @NotNull
        @Override
        public Builder displayName(@NotNull String displayName) {
            this.displayName = toNameComponent(Objects.requireNonNull(displayName));
            return this;
        }

        @NotNull
        @Override
        public Builder displayName(@NotNull Component component) {
            this.displayName = Objects.requireNonNull(component);
            return this;
        }

        @NotNull
        @Override
        public Builder lore(@NotNull List<String> lore) {
            this.lore = toLoreComponentList(Objects.requireNonNull(lore));
            return this;
        }

        @NotNull
        @Override
        public Builder loreComponent(@NotNull List<Component> lore) {
            this.lore = Objects.requireNonNull(lore);
            return this;
        }

        @NotNull
        @Override
        public Builder foodProperty(@NotNull FoodProperty foodProperty) {
            this.foodProperty = Objects.requireNonNull(foodProperty);
            return this;
        }

        @NotNull
        @Override
        public Builder weaponProperty(@NotNull WeaponProperty weaponProperty) {
            this.weaponProperty = Objects.requireNonNull(weaponProperty);
            return this;
        }

        @NotNull
        @Override
        public Builder maxDurability(int maxDurability) {
            this.maxDurability = maxDurability;
            return this;
        }

        @NotNull
        @Override
        public Builder armorProperty(ArmorProperty armorProperty) {
            this.armorProperty = Objects.requireNonNull(armorProperty);
            return this;
        }

        @NotNull
        @Override
        public Builder attributeProperty(AttributeProperty attributeProperty) {
            this.attributeProperty = Objects.requireNonNull((AttributePropertyImpl) attributeProperty);
            return this;
        }

        @NotNull
        @Override
        public Builder cropId(NamespacedKey cropId) {
            this.cropId = cropId;
            return this;
        }

        @NotNull private ArtisanItemImpl build() {
            if (registryId == null || rawMaterial == null) {
                throw new IllegalArgumentException("At least you should provide registryId and rawMaterial!");
            }
            return new ArtisanItemImpl(
                    registryId,
                    rawMaterial,
                    hasOriginalCraft,
                    customModelData,
                    displayName,
                    lore,
                    foodProperty,
                    weaponProperty,
                    maxDurability,
                    armorProperty,
                    attributeProperty,
                    cropId
            );
        }

        private static Component toNameComponent(String s) {
            s = "<white><italic:false>" + s;
            return MiniMessage.miniMessage().deserialize(s);
        }

        private static List<Component> toLoreComponentList(List<String> list) {
            List<Component> newList = new ArrayList<>();
            for (String s : list) {
                newList.add(
                        MiniMessage.miniMessage().deserialize(
                                "<gray><italic:false>" + s
                        )
                );
            }
            return newList;
        }
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
    public @NotNull ArtisanItem getArtisanItemAPI(@NotNull NamespacedKey registryId) {
        ArtisanItemImpl artisanItem = registry.get(registryId);
        if (artisanItem == null) throw new IllegalArgumentException("You should use has method to check before get!");
        return artisanItem;
    }

    @Override
    public @NotNull ArtisanItem getArtisanItemAPI(ItemStack itemStack) {
        ArtisanItemImpl artisanItem = registry.get(getRegistryId(itemStack));
        if (artisanItem == null) throw new IllegalArgumentException("You should use has method to check before get!");
        return artisanItem;
    }

    public @NotNull ArtisanItemImpl getArtisanItem(NamespacedKey registryId) {
        ArtisanItemImpl artisanItem = registry.get(registryId);
        if (artisanItem == null) throw new IllegalArgumentException("You should use has method to check before get!");
        return artisanItem;
    }

    public @NotNull ArtisanItemImpl getArtisanItem(ItemStack itemStack) {
        ArtisanItemImpl artisanItem = registry.get(getRegistryId(itemStack));
        if (artisanItem == null) throw new IllegalArgumentException("You should use has method to check before get!");
        return artisanItem;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @Nullable <T> T getItemstackAttributeValue(@NotNull ItemStack itemStack, @NotNull NamespacedKey attributeKey) {
        if (itemStack.getPersistentDataContainer().has(attributeKey)) {
            String typeName = AttributeRegistryImpl.getInstance().getItemstackAttributeTypeName(attributeKey);
            return (T) itemStack.getPersistentDataContainer().get(attributeKey, AttributeTypeRegistryImpl.getInstance().getAttributePDCType(typeName));
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
        String typeName = AttributeRegistryImpl.getInstance().getItemstackAttributeTypeName(attributeKey);
        PersistentDataType<?, T> type = (PersistentDataType<?, T>) AttributeTypeRegistryImpl.getInstance().getAttributePDCType(typeName);
        meta.getPersistentDataContainer().set(attributeKey, type, value);
        itemStack.setItemMeta(meta);
    }
}
