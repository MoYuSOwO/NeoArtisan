package io.github.MoYuSOwO.neoArtisan.recipe;

import io.github.MoYuSOwO.neoArtisan.NeoArtisan;
import io.github.MoYuSOwO.neoArtisan.api.recipe.ArtisanShapedRecipeAPI;
import io.github.MoYuSOwO.neoArtisan.api.recipe.ArtisanShapelessRecipeAPI;
import io.github.MoYuSOwO.neoArtisan.api.recipe.RecipeRegistryAPI;
import io.github.MoYuSOwO.neoArtisan.item.ItemRegistry;
import io.github.MoYuSOwO.neoArtisan.util.Util;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Furnace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class RecipeRegistry implements Listener, RecipeRegistryAPI {

    private static RecipeRegistry instance;

    public static RecipeRegistry getInstance() {
        return instance;
    }

    public static void init() {
        new RecipeRegistry();
    }

    private RecipeRegistry() {
        shapedRegistry = new ConcurrentHashMap<>();
        shapelessRegistry = new ConcurrentHashMap<>();
        registerListener();
        registerFromFile();
        NeoArtisan.logger().info("成功从文件注册 " + shapedRegistry.size() + " 个自定义有序配方");
        NeoArtisan.logger().info("成功从文件注册 " + shapelessRegistry.size() + " 个自定义无序配方");
        instance = this;
    }

    private final ConcurrentHashMap<String, ArtisanShapedRecipe> shapedRegistry;
    private final ConcurrentHashMap<String, ArtisanShapelessRecipe> shapelessRegistry;

    @Override
    @NotNull
    public ArtisanShapedRecipeAPI createShapedRecipe(@NotNull String line1, @NotNull String line2, @NotNull String line3) {
        return new ArtisanShapedRecipe(line1, line2, line3);
    }

    @Override
    @NotNull
    public ArtisanShapelessRecipeAPI createShapelessRecipe() {
        return new ArtisanShapelessRecipe();
    }

    @Override
    @NotNull
    public ArtisanShapelessRecipeAPI createShapelessRecipe(NamespacedKey result, int count) {
        return new ArtisanShapelessRecipe(result, count);
    }

    public void registerListener() {
        NeoArtisan.registerListener(new RecipeRegistry());
    }

    public void registerFromFile() {
        File[] files = ReadUtil.readAllFiles();
        if (files != null) {
            for (File file : files) {
                if (ReadUtil.isYmlFile(file)) {
                    readYml(YamlConfiguration.loadConfiguration(file));
                }
            }
        }
    }

    private void readYml(YamlConfiguration yml) {
        String recipeType = ReadUtil.getRecipeType(yml);
        if (recipeType.equals("shaped")) readShaped(yml);
        else if (recipeType.equals("shapeless")) readShapeless(yml);
    }

    private void readShaped(YamlConfiguration yml) {
        List<String> shape = ReadUtil.getShaped(yml);
        ArtisanShapedRecipe r = new ArtisanShapedRecipe(shape.get(0), shape.get(1), shape.get(2));
        for (Map.Entry<Character, String> entry : ReadUtil.getShapedMappings(yml).entrySet()) {
            r.add(entry.getKey(), Util.stringToNamespaceKey(entry.getValue()));
        }
        r.setResult(Util.stringToNamespaceKey(ReadUtil.getResult(yml)), ReadUtil.getCount(yml));
        r.build();
    }

    private void readShapeless(YamlConfiguration yml) {
        List<String> items = ReadUtil.getShapelessItems(yml);
        ArtisanShapelessRecipe r = new ArtisanShapelessRecipe(Util.stringToNamespaceKey(ReadUtil.getResult(yml)), ReadUtil.getCount(yml));
        for (String item : items) {
            r.add(Util.stringToNamespaceKey(item));
        }
        r.build();
    }

    void register(String identifier, ArtisanShapedRecipe r) {
        shapedRegistry.put(identifier, r);
    }

    void register(String identifier, ArtisanShapelessRecipe r) {
        shapelessRegistry.put(identifier, r);
    }

    @EventHandler
    private void onPrepareItemCraft(PrepareItemCraftEvent event) {
        CraftingInventory inventory = event.getInventory();
        ItemStack[] matrix = inventory.getMatrix();
        if (event.getRecipe() != null) {
            for (int i = 0; i < 9; i++) {
                if (matrix[i] == null) continue;
                NamespacedKey registryId = ItemRegistry.getInstance().getRegistryId(matrix[i]);
                if (ItemRegistry.getInstance().isArtisanItem(registryId) && (!ItemRegistry.getInstance().getArtisanItemAPI(registryId).hasOriginalCraft())) {
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
            event.getInventory().setResult(ItemRegistry.getInstance().getItemStack(r.getResult(), r.getCount()));
        } else {
            String shapelessRegistryKey = ArtisanShapelessRecipe.toRegistryKey(matrix);
            if (shapelessRegistry.containsKey(shapelessRegistryKey)) {
                ArtisanShapelessRecipe r = shapelessRegistry.get(shapelessRegistryKey);
                event.getInventory().setResult(ItemRegistry.getInstance().getItemStack(r.getResult(), r.getCount()));
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onItemCraft(InventoryClickEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getClickedInventory() instanceof CraftingInventory inventory)) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getSlotType() == InventoryType.SlotType.RESULT) {
            if (inventory.getRecipe() != null || inventory.getResult() == null) return;
            event.setCancelled(true);
            if (event.isShiftClick()) {
                ItemStack[] matrix = inventory.getMatrix();
                int minAmount = Integer.MAX_VALUE;
                for (ItemStack itemStack : matrix) {
                    if (itemStack != null) {
                        minAmount = Math.min(minAmount, itemStack.getAmount());
                    }
                }
                for (int i = 0; i < minAmount; i++) {
                    Map<Integer, ItemStack> leftovers = player.getInventory().addItem(inventory.getResult().clone());
                    if (!leftovers.isEmpty()) {
                        ItemStack reduce = inventory.getResult().clone();
                        reduce.setAmount(reduce.getAmount() - leftovers.get(0).getAmount());
                        player.getInventory().removeItem(reduce);
                        break;
                    } else {
                        for (ItemStack itemStack : matrix) {
                            if (itemStack != null) {
                                itemStack.setAmount(itemStack.getAmount() - 1);
                            }
                        }
                    }
                }
                inventory.setMatrix(matrix);
            } else if (event.getCursor().getType() != Material.AIR && event.getCursor().isSimilar(inventory.getResult()) && event.getCursor().getAmount() + inventory.getResult().getAmount() < event.getCursor().getMaxStackSize()) {
                ItemStack[] matrix = inventory.getMatrix();
                for (ItemStack itemStack : matrix) {
                    if (itemStack != null) {
                        itemStack.setAmount(itemStack.getAmount() - 1);
                    }
                }
                event.getCursor().setAmount(event.getCursor().getAmount() + inventory.getResult().getAmount());
                inventory.setMatrix(matrix);
            } else if (event.getCursor().getType() == Material.AIR) {
                ItemStack[] matrix = inventory.getMatrix();
                for (ItemStack itemStack : matrix) {
                    if (itemStack != null) {
                        itemStack.setAmount(itemStack.getAmount() - 1);
                    }
                }
                player.setItemOnCursor(inventory.getResult().clone());
                inventory.setMatrix(matrix);
            }
            player.updateInventory();
        }
    }

    @EventHandler
    private void onAnvil(PrepareAnvilEvent event) {
        if (event.getResult() == null) return;
        ItemStack firstItem = event.getInventory().getFirstItem();
        ItemStack secondItem = event.getInventory().getSecondItem();
        if (ItemRegistry.getInstance().isArtisanItem(firstItem)) {
            event.setResult(null);
            return;
        }
        if (ItemRegistry.getInstance().isArtisanItem(secondItem)) {
            event.setResult(null);
        }
    }

    @EventHandler
    private void onFurnace(FurnaceBurnEvent event) {
        if (event.isCancelled()) return;
        Furnace furnace = (Furnace) event.getBlock().getState();
        ItemStack fuel = furnace.getSnapshotInventory().getFuel();
        ItemStack smelting = furnace.getSnapshotInventory().getSmelting();
        if ((ItemRegistry.getInstance().isArtisanItem(smelting)) || (ItemRegistry.getInstance().isArtisanItem(fuel))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onSmithing(PrepareSmithingEvent event) {
        if (event.getInventory().getInputEquipment() == null) return;
        if (ItemRegistry.getInstance().isArtisanItem(event.getInventory().getInputEquipment())) event.setResult(null);
    }

}
