package io.github.MoYuSOwO.neoArtisan.attribute;

import io.github.MoYuSOwO.neoArtisan.NeoArtisan;
import io.github.MoYuSOwO.neoArtisan.api.attribute.AttributeRegistryAPI;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class AttributeRegistry implements AttributeRegistryAPI {

    public static void init() {
        new AttributeRegistry();
    }

    private AttributeRegistry() {
        globalAttributeRegistry = new HashMap<>();
        itemstackAttributeRegistry = new HashMap<>();
        instance = this;
        registerFromFile();
        NeoArtisan.logger().info("成功从文件注册 " + (globalAttributeRegistry.size() + itemstackAttributeRegistry.size()) + " 个全局自定义属性");
    }

    private final Map<NamespacedKey, String> globalAttributeRegistry, itemstackAttributeRegistry;

    private static AttributeRegistry instance;

    public static AttributeRegistry getInstance() {
        return instance;
    }

    private void registerFromFile() {
        File file = ReadUtil.readAttributeFiles();
        File globalFile = new File(file, "global_attribute.yml");
        File itemstackFile = new File(file, "itemstack_attribute.yml");
        if (globalFile.isFile() && ReadUtil.isYmlFile(globalFile)) {
            YamlConfiguration global = YamlConfiguration.loadConfiguration(globalFile);
            for (String key : global.getKeys(false)) {
                String value = global.getString(key);
                if (!AttributeTypeRegistry.getInstance().hasAttributeType(value)) throw new IllegalArgumentException("You must provide a legal type name!");
                globalAttributeRegistry.put(new NamespacedKey(NeoArtisan.instance(), key), value);
            }
        }
        if (itemstackFile.isFile() && ReadUtil.isYmlFile(itemstackFile)) {
            YamlConfiguration attribute = YamlConfiguration.loadConfiguration(itemstackFile);
            for (String key : attribute.getKeys(false)) {
                String value = attribute.getString(key);
                if (!AttributeTypeRegistry.getInstance().hasAttributeType(value)) throw new IllegalArgumentException("You must provide a legal type name!");
                itemstackAttributeRegistry.put(new NamespacedKey(NeoArtisan.instance(), key), value);
            }
        }
    }

    @Override
    public void registerGlobalAttribute(@NotNull NamespacedKey attributeKey, @NotNull String typeName) {
        if (!AttributeTypeRegistry.getInstance().hasAttributeType(typeName)) throw new IllegalArgumentException("You must provide a legal type name!");
        globalAttributeRegistry.put(attributeKey, typeName);
    }

    @Override
    public void registerItemstackAttribute(@NotNull NamespacedKey attributeKey, @NotNull String typeName) {
        if (!AttributeTypeRegistry.getInstance().hasAttributeType(typeName)) throw new IllegalArgumentException("You must provide a legal type name!");
        itemstackAttributeRegistry.put(attributeKey, typeName);
    }

    @Override
    public boolean hasGlobalAttribute(@NotNull NamespacedKey attributeKey) {
        return globalAttributeRegistry.containsKey(attributeKey);
    }

    @Override
    public boolean hasItemstackAttribute(@NotNull NamespacedKey attributeKey) {
        return itemstackAttributeRegistry.containsKey(attributeKey);
    }

    @Override
    public @NotNull String getGlobalAttributeTypeName(@NotNull NamespacedKey attributeKey) {
        if (!globalAttributeRegistry.containsKey(attributeKey)) throw new IllegalArgumentException("You must check if attribute exists before get!");
        return globalAttributeRegistry.get(attributeKey);
    }

    @Override
    public @NotNull String getItemstackAttributeTypeName(@NotNull NamespacedKey attributeKey) {
        if (!itemstackAttributeRegistry.containsKey(attributeKey)) throw new IllegalArgumentException("You must check if attribute exists before get!");
        return itemstackAttributeRegistry.get(attributeKey);
    }
}
