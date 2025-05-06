package io.github.MoYuSOwO.neoArtisan.recipe;

import io.github.MoYuSOwO.neoArtisan.NeoArtisan;
import io.github.MoYuSOwO.neoArtisan.item.ItemRegistry;
import io.github.MoYuSOwO.neoArtisan.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Furnace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class RecipeRegistry implements Listener {

    public static void registerListener() {
        NeoArtisan.registerListener(new RecipeRegistry());
    }

    public static void init() {
        registerListener();
        registerFromMinecraft();
        registerFromFile();
        Bukkit.clearRecipes();
        NeoArtisan.logger().info("成功注册 " + shapedRegistry.size() + " 个自定义有序配方");
        NeoArtisan.logger().info("成功注册 " + shapelessRegistry.size() + " 个自定义无序配方");
    }

    private RecipeRegistry() {}

    private static final ConcurrentHashMap<String, ArtisanShapedRecipe> shapedRegistry = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, ArtisanShapelessRecipe> shapelessRegistry = new ConcurrentHashMap<>();

    private static void registerFromMinecraft() {
        Iterator<Recipe> recipeIterator = Bukkit.recipeIterator();
        while (recipeIterator.hasNext()) {
            Recipe recipe = recipeIterator.next();
            if (recipe instanceof ShapedRecipe shapedRecipe) {
                registerShapedRecipeFromMinecraft(shapedRecipe);
            } else if (recipe instanceof ShapelessRecipe shapelessRecipe) {
                registerShapelessRecipeFromMinecraft(shapelessRecipe);
            }
        }
    }

    private static void registerShapelessRecipeFromMinecraft(ShapelessRecipe recipe) {

    }

    private static void registerShapedRecipeFromMinecraft(ShapedRecipe recipe) {
    }

    public static void registerFromFile() {
        File[] files = ReadUtil.readAllFiles();
        if (files != null) {
            for (File file : files) {
                if (ReadUtil.isYmlFile(file)) {
                    readYml(YamlConfiguration.loadConfiguration(file));
                }
            }
        }
    }

    private static void readYml(YamlConfiguration yml) {
        String recipeType = ReadUtil.getRecipeType(yml);
        if (recipeType.equals("shaped")) readShaped(yml);
        else if (recipeType.equals("shapeless")) readShapeless(yml);
    }

    private static void readShaped(YamlConfiguration yml) {
        List<String> shape = ReadUtil.getShaped(yml);
        ArtisanShapedRecipe r = new ArtisanShapedRecipe(shape.get(0), shape.get(1), shape.get(2));
        for (Map.Entry<Character, String> entry : ReadUtil.getShapedMappings(yml).entrySet()) {
            r.add(entry.getKey(), Util.toNamespaceKey(entry.getValue()));
        }
        r.setResult(Util.toNamespaceKey(ReadUtil.getResult(yml)), ReadUtil.getCount(yml));
        r.build();
    }

    private static void readShapeless(YamlConfiguration yml) {
        List<String> items = ReadUtil.getShapelessItems(yml);
        ArtisanShapelessRecipe r = new ArtisanShapelessRecipe(Util.toNamespaceKey(ReadUtil.getResult(yml)), ReadUtil.getCount(yml));
        for (String item : items) {
            r.add(Util.toNamespaceKey(item));
        }
        r.build();
    }

    static void register(String identifier, ArtisanShapedRecipe r) {
        shapedRegistry.put(identifier, r);
    }

    static void register(String identifier, ArtisanShapelessRecipe r) {
        shapelessRegistry.put(identifier, r);
    }

    @EventHandler
    private static void onPrepareItemCraft(PrepareItemCraftEvent event) {
        CraftingInventory inventory = event.getInventory();
        ItemStack[] matrix = inventory.getMatrix();
        if (event.getRecipe() != null) {
            for (int i = 0; i < 9; i++) {
                if (matrix[i] == null) continue;
                NamespacedKey registryId = ItemRegistry.getRegistryId(matrix[i]);
                if (ItemRegistry.hasArtisanItem(registryId) && (!ItemRegistry.getArtisanItem(registryId).hasOriginalCraft())) {
                    event.getInventory().setResult(null);
                    break;
                } else {
                    return;
                }
            }
        }
        String shapedRegistryKey = ArtisanShapedRecipe.toRegistryKey(matrix);
        if (shapedRegistry.containsKey(shapedRegistryKey)) {
            ArtisanShapedRecipe r = shapedRegistry.get(shapedRegistryKey);
            event.getInventory().setResult(ItemRegistry.getItemStack(r.getResult(), r.getCount()));
        } else {
            String shapelessRegistryKey = ArtisanShapelessRecipe.toRegistryKey(matrix);
            if (shapelessRegistry.containsKey(shapelessRegistryKey)) {
                ArtisanShapelessRecipe r = shapelessRegistry.get(shapelessRegistryKey);
                event.getInventory().setResult(ItemRegistry.getItemStack(r.getResult(), r.getCount()));
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private static void onItemCraft(InventoryClickEvent event) {
        if (event.isCancelled()) return;
    }

    @EventHandler
    private static void onAnvil(PrepareAnvilEvent event) {
        if (event.getResult() == null) return;
        ItemStack firstItem = event.getInventory().getFirstItem();
        ItemStack secondItem = event.getInventory().getSecondItem();
        if (ItemRegistry.hasArtisanItem(firstItem)) {
            event.setResult(null);
            return;
        }
        if (ItemRegistry.hasArtisanItem(secondItem)) {
            event.setResult(null);
        }
    }

    @EventHandler
    private static void onFurnace(FurnaceBurnEvent event) {
        if (event.isCancelled()) return;
        Furnace furnace = (Furnace) event.getBlock().getState();
        ItemStack fuel = furnace.getSnapshotInventory().getFuel();
        ItemStack smelting = furnace.getSnapshotInventory().getSmelting();
        if ((ItemRegistry.hasArtisanItem(smelting)) || (ItemRegistry.hasArtisanItem(fuel))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private static void onSmithing(PrepareSmithingEvent event) {
        if (event.getInventory().getInputEquipment() == null) return;
        if (ItemRegistry.hasArtisanItem(event.getInventory().getInputEquipment())) event.setResult(null);
    }

}
