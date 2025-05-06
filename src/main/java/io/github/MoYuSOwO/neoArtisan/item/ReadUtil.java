package io.github.MoYuSOwO.neoArtisan.item;

import io.github.MoYuSOwO.neoArtisan.NeoArtisan;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public final class ReadUtil {

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
        }
        return itemFolder.listFiles();
    }

    private static void saveDefaultIfNotExists(String resourcePath) {
        String targetPath = resourcePath.replace('/', File.separatorChar);
        File targetFile = new File(NeoArtisan.instance().getDataFolder(), targetPath);
        if (targetFile.exists()) {
            return;
        }
        NeoArtisan.instance().saveResource(resourcePath, false);
    }

    public static boolean isYmlFile(@NotNull File file) {
        if (!file.isFile()) return false;
        String name = file.getName().toLowerCase();
        return name.endsWith(".yml") || name.endsWith(".yaml");
    }

    public static NamespacedKey getRegistryId(YamlConfiguration item) {
        String registryId = item.getString("registryId");
        if (registryId != null) return new NamespacedKey("neoartisan", registryId);
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

    public static int getCustomModelData(YamlConfiguration item) {
        int customModelData = item.getInt("customModelData");
        if (customModelData <= 0) return -1;
        else return customModelData;
    }

    public static String getDisplayName(YamlConfiguration item) {
        String itemName = item.getString("displayName");
        if (itemName == null) throw new IllegalArgumentException("You must provide a name!");
        else return itemName;
    }

    public static @NotNull List<String> getLore(YamlConfiguration item) {
        return item.getStringList("lore");
    }

    public static @NotNull FoodProperty getFood(YamlConfiguration item) {
        FoodProperty foodProperty = FoodProperty.EMPTY;
        ConfigurationSection food = item.getConfigurationSection("food");
        if (food != null) {
            int nutrition = food.getInt("nutrition");
            int saturation = food.getInt("saturation");
            boolean canAlwaysEat = food.getBoolean("canAlwaysEat");
            if (nutrition == 0 || saturation == 0) throw new IllegalArgumentException("You must provide a effective food value!");
            foodProperty = new FoodProperty(nutrition, saturation, canAlwaysEat);
        }
        return foodProperty;
    }

    public static boolean getOriginalCraft(YamlConfiguration item) {
        return item.getBoolean("hasOriginalCraft");
    }
}
