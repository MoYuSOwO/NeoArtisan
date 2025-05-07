package io.github.MoYuSOwO.neoArtisan.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.github.MoYuSOwO.neoArtisan.NeoArtisan;
import io.github.MoYuSOwO.neoArtisan.util.NamespacedKeyDataType;
import io.github.MoYuSOwO.neoArtisan.util.Util;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.inventory.meta.components.UseCooldownComponent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ArtisanItem {
    private final NamespacedKey registryId;
    private final Material rawMaterial;
    private final boolean hasOriginalCraft;
    private final int customModelData;
    private final Component displayName;
    private final List<Component> lore;
    private final FoodProperty foodProperty;
    private final WeaponProperty weaponProperty;
    private final ItemMeta itemMeta;

    protected ArtisanItem(NamespacedKey registryId, Material rawMaterial, boolean hasOriginalCraft, Integer customModelData, Component displayName, List<Component> lore, @NotNull FoodProperty foodProperty, @NotNull WeaponProperty weaponProperty) {
        this.registryId = registryId;
        this.rawMaterial = rawMaterial;
        this.hasOriginalCraft = hasOriginalCraft;
        this.customModelData = customModelData;
        this.displayName = displayName;
        this.lore = lore;
        this.foodProperty = foodProperty;
        this.weaponProperty = weaponProperty;
        this.itemMeta = createNewItemMeta();
    }

    protected ArtisanItem(NamespacedKey registryId, Material rawMaterial, boolean hasOriginalCraft, Integer customModelData, String displayName, List<String> lore, @NotNull FoodProperty foodProperty, @NotNull WeaponProperty weaponProperty) {
        this(registryId, rawMaterial, hasOriginalCraft, customModelData, toNameComponent(displayName), toLoreComponentList(lore), foodProperty, weaponProperty);
    }

    protected ItemStack getItemStack(int count) {
        ItemStack itemStack = new ItemStack(this.rawMaterial);
        itemStack.setAmount(Math.min(count, itemStack.getMaxStackSize()));
        itemStack.setItemMeta(this.itemMeta.clone());
        return itemStack;
    }

    protected ItemStack getItemStack() {
        return this.getItemStack(1);
    }

    public boolean equals(@NotNull ItemStack itemStack) {
        if (!itemStack.getItemMeta().getPersistentDataContainer().has(NeoArtisan.getArtisanItemIdKey())) return false;
        return itemStack.getItemMeta().getPersistentDataContainer().get(NeoArtisan.getArtisanItemIdKey(), NamespacedKeyDataType.TYPE).equals(this.registryId);
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

    public WeaponProperty getWeaponProperty() {
        return this.weaponProperty;
    }

    @SuppressWarnings("UnstableApiUsage")
    private ItemMeta createNewItemMeta() {
        ItemMeta itemMeta = new ItemStack(this.rawMaterial).getItemMeta();
        Multimap<Attribute, AttributeModifier> modifiers = ArrayListMultimap.create();
        itemMeta.displayName(this.displayName);
        itemMeta.lore(this.lore);
        if (this.customModelData != -1) {
            itemMeta.setCustomModelData(this.customModelData);
        }
        itemMeta.getPersistentDataContainer().set(NeoArtisan.getArtisanItemIdKey(), NamespacedKeyDataType.TYPE, this.registryId);
        if (this.foodProperty != FoodProperty.EMPTY) {
            FoodComponent foodComponent = itemMeta.getFood();
            foodComponent.setNutrition(this.foodProperty.nutrition());
            foodComponent.setSaturation(this.foodProperty.saturation());
            foodComponent.setCanAlwaysEat(this.foodProperty.canAlwaysEat());
            itemMeta.setFood(foodComponent);
        }
        if (this.weaponProperty != WeaponProperty.EMPTY) {
            modifiers.put(
                    Attribute.ATTACK_DAMAGE,
                    new AttributeModifier(
                            NeoArtisan.getArtisanItemAttackDamageKey(),
                            this.weaponProperty.damage(),
                            AttributeModifier.Operation.ADD_NUMBER,
                            EquipmentSlotGroup.MAINHAND
                    )
            );
            modifiers.put(
                    Attribute.ATTACK_SPEED,
                    new AttributeModifier(
                            NeoArtisan.getArtisanItemAttackSpeedKey(),
                            this.weaponProperty.speed(),
                            AttributeModifier.Operation.ADD_NUMBER,
                            EquipmentSlotGroup.MAINHAND
                    )
            );
            modifiers.put(
                    Attribute.ATTACK_KNOCKBACK,
                    new AttributeModifier(
                            NeoArtisan.getArtisanItemAttackKnockbackKey(),
                            this.weaponProperty.knockback(),
                            AttributeModifier.Operation.ADD_NUMBER,
                            EquipmentSlotGroup.MAINHAND
                    )
            );
//            double baseAttackDamage = Util.getTotalAttributeValue(this.rawMaterial.getDefaultAttributeModifiers().get(Attribute.ATTACK_DAMAGE));
//            itemMeta.addAttributeModifier(
//                    Attribute.ATTACK_DAMAGE,
//                    new AttributeModifier(
//                            NeoArtisan.getArtisanItemAttackDamageKey(),
//                            this.weaponProperty.damage() - baseAttackDamage,
//                            AttributeModifier.Operation.ADD_NUMBER,
//                            EquipmentSlotGroup.MAINHAND
//                    )
//            );
//            double baseAttackSpeed = Util.getTotalAttributeValue(this.rawMaterial.getDefaultAttributeModifiers().get(Attribute.ATTACK_SPEED));
//            itemMeta.addAttributeModifier(
//                    Attribute.ATTACK_SPEED,
//                    new AttributeModifier(
//                            NeoArtisan.getArtisanItemAttackSpeedKey(),
//                            this.weaponProperty.speed() - baseAttackSpeed,
//                            AttributeModifier.Operation.ADD_NUMBER,
//                            EquipmentSlotGroup.MAINHAND
//                    )
//            );
//            double baseAttackKnockback = Util.getTotalAttributeValue(this.rawMaterial.getDefaultAttributeModifiers().get(Attribute.ATTACK_KNOCKBACK));
//            itemMeta.addAttributeModifier(
//                    Attribute.ATTACK_KNOCKBACK,
//                    new AttributeModifier(
//                            NeoArtisan.getArtisanItemAttackKnockbackKey(),
//                            this.weaponProperty.knockback() - baseAttackKnockback,
//                            AttributeModifier.Operation.ADD_NUMBER,
//                            EquipmentSlotGroup.MAINHAND
//                    )
//            );
        }
        itemMeta.setAttributeModifiers(modifiers);
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
