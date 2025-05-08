package io.github.MoYuSOwO.neoArtisan.attribute;

import io.github.MoYuSOwO.neoArtisan.NeoArtisan;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class AttributeRegistry {

    private AttributeRegistry() {}

    private static final Map<NamespacedKey, String> globalAttributeRegistry = new HashMap<>();
    private static final Map<NamespacedKey, String> itemstackAttributeRegistry = new HashMap<>();

    public static void init() {
        registerFromFile();
        NeoArtisan.logger().info("成功从文件注册 " + (globalAttributeRegistry.size() + itemstackAttributeRegistry.size()) + " 个全局自定义属性");
    }

    private static void registerFromFile() {
        File file = ReadUtil.readAttributeFiles();
        File globalFile = new File(file, "global_attribute.yml");
        File itemstackFile = new File(file, "itemstack_attribute.yml");
        if (globalFile.isFile() && ReadUtil.isYmlFile(globalFile)) {
            YamlConfiguration global = YamlConfiguration.loadConfiguration(globalFile);
            for (String key : global.getKeys(false)) {
                String value = global.getString(key);
                if (!AttributeTypeRegistry.hasAttributeType(value)) throw new IllegalArgumentException("You must provide a legal type name!");
                globalAttributeRegistry.put(new NamespacedKey(NeoArtisan.instance(), key), value);
            }
        }
        if (itemstackFile.isFile() && ReadUtil.isYmlFile(itemstackFile)) {
            YamlConfiguration attribute = YamlConfiguration.loadConfiguration(itemstackFile);
            for (String key : attribute.getKeys(false)) {
                String value = attribute.getString(key);
                if (!AttributeTypeRegistry.hasAttributeType(value)) throw new IllegalArgumentException("You must provide a legal type name!");
                itemstackAttributeRegistry.put(new NamespacedKey(NeoArtisan.instance(), key), value);
            }
        }
    }

    public static void registerGlobalAttribute(NamespacedKey attributeKey, String typeName) {
        if (!AttributeTypeRegistry.hasAttributeType(typeName)) throw new IllegalArgumentException("You must provide a legal type name!");
        globalAttributeRegistry.put(attributeKey, typeName);
    }

    public static void registerItemstackAttribute(NamespacedKey attributeKey, String typeName) {
        if (!AttributeTypeRegistry.hasAttributeType(typeName)) throw new IllegalArgumentException("You must provide a legal type name!");
        itemstackAttributeRegistry.put(attributeKey, typeName);
    }

    public static boolean hasGlobalAttribute(NamespacedKey attributeKey) {
        return globalAttributeRegistry.containsKey(attributeKey);
    }

    public static boolean hasItemstackAttribute(NamespacedKey attributeKey) {
        return itemstackAttributeRegistry.containsKey(attributeKey);
    }

    public static @NotNull String getGlobalAttributeTypeName(NamespacedKey attributeKey) {
        if (!globalAttributeRegistry.containsKey(attributeKey)) throw new IllegalArgumentException("You must check if attribute exists before get!");
        return globalAttributeRegistry.get(attributeKey);
    }

    public static @NotNull String getItemstackAttributeTypeName(NamespacedKey attributeKey) {
        if (!itemstackAttributeRegistry.containsKey(attributeKey)) throw new IllegalArgumentException("You must check if attribute exists before get!");
        return itemstackAttributeRegistry.get(attributeKey);
    }
}
