package io.github.MoYuSOwO.neoArtisan.item;

import io.github.MoYuSOwO.neoArtisan.NeoArtisan;
import io.github.MoYuSOwO.neoArtisan.util.NamespacedKeyDataType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ArtisanItem {
    private final NamespacedKey registryId;
    private final Material rawMaterial;
    private final boolean hasOriginalCraft;
    private final int customModelData;
    private final Component displayName;
    private final List<Component> lore;
    private final FoodProperty foodProperty;
    private final ItemMeta itemMeta;

    protected ArtisanItem(NamespacedKey registryId, Material rawMaterial, boolean hasOriginalCraft, Integer customModelData, Component displayName, List<Component> lore, FoodProperty foodProperty) {
        this.registryId = registryId;
        this.rawMaterial = rawMaterial;
        this.hasOriginalCraft = hasOriginalCraft;
        this.customModelData = customModelData;
        this.displayName = displayName;
        this.lore = lore;
        this.foodProperty = foodProperty;
        this.itemMeta = createNewItemMeta();
    }

    protected ArtisanItem(NamespacedKey registryId, Material rawMaterial, boolean hasOriginalCraft, Integer customModelData, String displayName, List<String> lore, FoodProperty foodProperty) {
        this(registryId, rawMaterial, hasOriginalCraft, customModelData, toNameComponent(displayName), toLoreComponentList(lore), foodProperty);
    }

    public ItemStack getItemStack(int count) {
        ItemStack itemStack = new ItemStack(this.rawMaterial, count);
        itemStack.setItemMeta(this.itemMeta.clone());
        return itemStack;
    }

    public ItemStack getItemStack() {
        return this.getItemStack(1);
    }

    public boolean equals(@NotNull ItemStack itemStack) {
        if (!itemStack.getItemMeta().getPersistentDataContainer().has(NeoArtisan.getArtisanItemKey())) return false;
        return itemStack.getItemMeta().getPersistentDataContainer().get(NeoArtisan.getArtisanItemKey(), NamespacedKeyDataType.TYPE).equals(this.registryId);
    }

    public boolean equals(@NotNull NamespacedKey registryId) {
        return registryId.equals(this.registryId);
    }

    public NamespacedKey getRegistryId() {
        return this.registryId;
    }

    public Material getRawMaterial() {
        return this.rawMaterial;
    }

    public boolean hasOriginalCraft() {
        return this.hasOriginalCraft;
    }

    public int getCustomModelData() {
        return this.customModelData;
    }

    public FoodProperty getFoodProperty() {
        return this.foodProperty;
    }

    @SuppressWarnings("UnstableApiUsage")
    private ItemMeta createNewItemMeta() {
        ItemMeta itemMeta = new ItemStack(this.rawMaterial).getItemMeta();
        itemMeta.customName(this.displayName);
        itemMeta.lore(this.lore);
        if (this.customModelData != -1) {
            itemMeta.setCustomModelData(this.customModelData);
        }
        itemMeta.getPersistentDataContainer().set(NeoArtisan.getArtisanItemKey(), NamespacedKeyDataType.TYPE, this.registryId);
        if (this.foodProperty != FoodProperty.EMPTY) {
            FoodComponent foodComponent = itemMeta.getFood();
            foodComponent.setNutrition(this.foodProperty.nutrition());
            foodComponent.setSaturation(this.foodProperty.saturation());
            foodComponent.setCanAlwaysEat(this.foodProperty.canAlwaysEat());
            itemMeta.setFood(foodComponent);
        }
        return itemMeta;
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
