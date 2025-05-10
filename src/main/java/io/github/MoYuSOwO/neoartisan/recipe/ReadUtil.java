package io.github.moyusowo.neoartisan.recipe;

import io.github.moyusowo.neoartisan.NeoArtisan;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class ReadUtil {

    private ReadUtil() {}

    public static File[] readAllFiles() {
        File dataFolder = NeoArtisan.instance().getDataFolder();
        File recipeFolder = new File(dataFolder, "recipe");
        if (!recipeFolder.exists()) {
            recipeFolder.mkdirs();
            saveDefaultIfNotExists("recipe/example_shapedRecipe.yml");
            saveDefaultIfNotExists("recipe/example_shapelessRecipe.yml");
        }
        return recipeFolder.listFiles();
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

    public static String getRecipeType(YamlConfiguration yml) {
        String recipeType = yml.getString("type");
        if (recipeType != null) return recipeType;
        else throw new IllegalArgumentException("You must provide a recipeType!");
    }

    public static List<String> getShaped(YamlConfiguration yml) {
        List<String> shaped = yml.getStringList("shape");
        if (shaped.size() == 3 && shaped.get(0).length() == 3 && shaped.get(1).length() == 3 && shaped.get(2).length() == 3) return shaped;
        else throw new IllegalArgumentException("You must provide a correct shape for shapedRecipe!");
    }

    public static Map<Character, String> getShapedMappings(YamlConfiguration yml) {
        if (yml.isConfigurationSection("mappings")) {
            Map<Character, String> mappings = new HashMap<>();
            ConfigurationSection mappingsSection = yml.getConfigurationSection("mappings");
            if (mappingsSection != null) {
                for (String key : mappingsSection.getKeys(false)) {
                    if (key.length() != 1) throw new IllegalArgumentException("You must provide a character for mappings!");
                    String value = mappingsSection.getString(key);
                    if (value == null) throw new IllegalArgumentException("You must provide a item for mappings!");
                    mappings.put(key.charAt(0), value);
                }
            }
            return mappings;
        } else {
            throw new IllegalArgumentException("You must provide a shape for shapedRecipe!");
        }
    }

    public static List<String> getShapelessItems(YamlConfiguration yml) {
        List<String> items = yml.getStringList("items");
        if (!items.isEmpty()) return items;
        else throw new IllegalArgumentException("You must provide a item list for shapelessRecipe!");
    }

    public static String getResult(YamlConfiguration yml) {
        String result = yml.getString("result");
        if (result != null) return result;
        else throw new IllegalArgumentException("You must provide a result for recipe!");
    }

    public static int getCount(YamlConfiguration yml) {
        int count = yml.getInt("count");
        if (count > 0) return count;
        else throw new IllegalArgumentException("You must provide correct count for result!");
    }
}
