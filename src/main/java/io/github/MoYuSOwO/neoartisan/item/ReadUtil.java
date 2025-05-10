package io.github.moyusowo.neoartisan.item;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisanapi.api.attribute.AttributeRegistry;
import io.github.moyusowo.neoartisanapi.api.attribute.AttributeTypeRegistry;
import io.github.moyusowo.neoartisanapi.api.item.ArmorProperty;
import io.github.moyusowo.neoartisanapi.api.item.FoodProperty;
import io.github.moyusowo.neoartisanapi.api.item.WeaponProperty;
import io.github.moyusowo.neoartisan.util.Util;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import static io.github.moyusowo.neoartisan.util.Util.saveDefaultIfNotExists;

final class ReadUtil {

    private ReadUtil() {}

    public static File[] readAllFiles() {
        File dataFolder = NeoArtisan.instance().getDataFolder();
        File itemFolder = new File(dataFolder, "item");
        if (!itemFolder.exists()) {
            itemFolder.mkdirs();
            saveDefaultIfNotExists("item/magic_bread.yml");
            saveDefaultIfNotExists("item/magic_diamond.yml");
            saveDefaultIfNotExists("item/magic_sword.yml");
            saveDefaultIfNotExists("item/broken_stick.yml");
            saveDefaultIfNotExists("item/magic_helmet.yml");
        }
        return itemFolder.listFiles();
    }

    public static NamespacedKey getRegistryId(YamlConfiguration item) {
        String registryId = item.getString("registryId");
        if (registryId != null) return new NamespacedKey(NeoArtisan.instance(), registryId);
        else throw new IllegalArgumentException("You must provide a registryId!");
    }

    public static Material getRawMaterial(YamlConfiguration item) {
        String rawMaterialString = item.getString("rawMaterial");
        if (rawMaterialString == null) throw new IllegalArgumentException("You must provide a raw material!");
        Material rawMaterial;
        if (rawMaterialString.contains(":")) {
            String[] id = rawMaterialString.split(":");
            rawMaterial = Material.matchMaterial(id[1]);
        } else {
            rawMaterial = Material.matchMaterial(rawMaterialString);
        }
        if (rawMaterial == null) throw new IllegalArgumentException("You must provide a effective raw material!");
        return rawMaterial;
    }

    public static Integer getCustomModelData(YamlConfiguration item) {
        int customModelData = item.getInt("customModelData");
        if (customModelData <= 0) return null;
        else return customModelData;
    }

    public static String getDisplayName(YamlConfiguration item) {
        return item.getString("displayName");
    }

    public static @NotNull List<String> getLore(YamlConfiguration item) {
        return item.getStringList("lore");
    }

    public static @NotNull FoodProperty getFood(YamlConfiguration item) {
        FoodProperty foodProperty = FoodProperty.EMPTY;
        ConfigurationSection food = item.getConfigurationSection("food");
        if (food != null) {
            int nutrition = food.getInt("nutrition");
            float saturation = (float) food.getDouble("saturation");
            boolean canAlwaysEat = food.getBoolean("canAlwaysEat");
            if (nutrition <= 0 || saturation <= 0) throw new IllegalArgumentException("You must provide a effective food value!");
            foodProperty = new FoodProperty(nutrition, saturation, canAlwaysEat);
        }
        return foodProperty;
    }

    public static @NotNull WeaponProperty getWeapon(YamlConfiguration item) {
        WeaponProperty weaponProperty = WeaponProperty.EMPTY;
        ConfigurationSection weapon = item.getConfigurationSection("weapon");
        if (weapon != null) {
            float speed = (float) weapon.getDouble("speed");
            float knockback = (float) weapon.getDouble("knockback");
            float damage = (float) weapon.getDouble("damage");
            if (speed <= 0 || knockback <= 0 || damage <= 0) throw new IllegalArgumentException("You must provide a effective weapon value!");
            weaponProperty = new WeaponProperty(speed, knockback, damage);
        }
        return weaponProperty;
    }

    public static Integer getMaxDurability(YamlConfiguration item) {
        int maxDurability = item.getInt("maxDurability");
        if (maxDurability <= 0) return null;
        else return maxDurability;
    }

    public static ArmorProperty getArmor(YamlConfiguration item) {
        ArmorProperty armorProperty = ArmorProperty.EMPTY;
        ConfigurationSection armorConfig = item.getConfigurationSection("armor");
        if (armorConfig != null) {
            int armor = armorConfig.getInt("armor");
            int armorToughness = armorConfig.getInt("armorToughness");
            String slot = armorConfig.getString("slot");
            if (armor <= 0 || armorToughness <= 0 || slot == null || Util.toSlotOrNull(slot.toUpperCase()) == null) throw new IllegalArgumentException("You must provide a effective armor value!");
            armorProperty = new ArmorProperty(armor, armorToughness, Util.toSlotOrNull(slot.toUpperCase()));
        }
        return armorProperty;
    }

    public static @NotNull AttributePropertyImpl getAttribute(YamlConfiguration item) {
        AttributePropertyImpl attributeProperty = new AttributePropertyImpl();
        ConfigurationSection attribute = item.getConfigurationSection("attribute");
        if (attribute != null) {
            ConfigurationSection global = attribute.getConfigurationSection("global");
            ConfigurationSection itemstack = attribute.getConfigurationSection("itemstack");
            if (global != null) {
                for (String key : global.getKeys(false)) {
                    if (key.contains(":")) {
                        NamespacedKey attributeKey = NamespacedKey.fromString(key);
                        String typeName = AttributeRegistry.getAttributeRegistryManager().getGlobalAttributeTypeName(attributeKey);
                        Class<?> javaClass = AttributeTypeRegistry.getAttributeTypeRegistryManager().getAttributeJavaType(typeName);
                        attributeProperty.addGlobalAttribute(attributeKey, global.getObject(key, javaClass));
                    }
                    else {
                        NamespacedKey attributeKey = new NamespacedKey(NeoArtisan.instance(), key);
                        String typeName = AttributeRegistry.getAttributeRegistryManager().getGlobalAttributeTypeName(attributeKey);
                        Class<?> javaClass = AttributeTypeRegistry.getAttributeTypeRegistryManager().getAttributeJavaType(typeName);
                        attributeProperty.addGlobalAttribute(attributeKey, global.getObject(key, javaClass));
                    }
                }
            }
            if (itemstack != null) {
                for (String key : itemstack.getKeys(false)) {
                    if (key.contains(":")) {
                        NamespacedKey attributeKey = NamespacedKey.fromString(key);
                        String typeName = AttributeRegistry.getAttributeRegistryManager().getItemstackAttributeTypeName(attributeKey);
                        Class<?> javaClass = AttributeTypeRegistry.getAttributeTypeRegistryManager().getAttributeJavaType(typeName);
                        attributeProperty.addItemstackAttribute(attributeKey, itemstack.getObject(key, javaClass));
                    }
                    else {
                        NamespacedKey attributeKey = new NamespacedKey(NeoArtisan.instance(), key);
                        String typeName = AttributeRegistry.getAttributeRegistryManager().getItemstackAttributeTypeName(attributeKey);
                        Class<?> javaClass = AttributeTypeRegistry.getAttributeTypeRegistryManager().getAttributeJavaType(typeName);
                        attributeProperty.addItemstackAttribute(attributeKey, itemstack.getObject(key, javaClass));
                    }
                }
            }
        }
        return attributeProperty;
    }

    public static NamespacedKey getCropId(YamlConfiguration item) {
        String cropId = item.getString("cropId");
        if (cropId != null) return NamespacedKey.fromString(cropId);
        else return null;
    }

    public static boolean getOriginalCraft(YamlConfiguration item) {
        return item.getBoolean("hasOriginalCraft");
    }
}
