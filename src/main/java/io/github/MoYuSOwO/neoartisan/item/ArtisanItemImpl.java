package io.github.moyusowo.neoartisan.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisanapi.api.attribute.AttributeRegistry;
import io.github.moyusowo.neoartisanapi.api.attribute.AttributeTypeRegistry;
import io.github.moyusowo.neoartisanapi.api.item.*;
import io.github.moyusowo.neoartisan.util.NamespacedKeyDataType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.EquippableComponent;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
class ArtisanItemImpl implements ArtisanItem {
    private final NamespacedKey registryId;
    private final Material rawMaterial;
    private final boolean hasOriginalCraft;
    private final Integer customModelData;
    private final Component displayName;
    private final List<Component> lore;
    private final FoodProperty foodProperty;
    private final WeaponProperty weaponProperty;
    private final Integer maxDurability;
    private final ArmorProperty armorProperty;
    private final AttributePropertyImpl attributeProperty;
    private final NamespacedKey cropId;
    private final ItemMeta itemMeta;

    protected ArtisanItemImpl(
            NamespacedKey registryId,
            Material rawMaterial,
            boolean hasOriginalCraft,
            @Nullable Integer customModelData,
            Component displayName,
            List<Component> lore,
            @NotNull FoodProperty foodProperty,
            @NotNull WeaponProperty weaponProperty,
            @Nullable Integer maxDurability,
            @NotNull ArmorProperty armorProperty,
            @NotNull AttributePropertyImpl attributeProperty,
            @Nullable NamespacedKey cropId
    ) {
        this.registryId = registryId;
        this.rawMaterial = rawMaterial;
        this.hasOriginalCraft = hasOriginalCraft;
        this.customModelData = customModelData;
        this.displayName = displayName;
        this.lore = lore;
        this.foodProperty = foodProperty;
        this.weaponProperty = weaponProperty;
        this.maxDurability = maxDurability;
        this.armorProperty = armorProperty;
        this.attributeProperty = attributeProperty;
        this.cropId = cropId;
        this.itemMeta = createNewItemMeta();
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

    @Override
    public boolean equals(@NotNull ItemStack itemStack) {
        if (!itemStack.getItemMeta().getPersistentDataContainer().has(NeoArtisan.getArtisanItemIdKey())) return false;
        return itemStack.getItemMeta().getPersistentDataContainer().get(NeoArtisan.getArtisanItemIdKey(), NamespacedKeyDataType.TYPE).equals(this.registryId);
    }

    @Override
    public boolean equals(@NotNull NamespacedKey registryId) {
        return registryId.equals(this.registryId);
    }

    @Override
    public @NotNull NamespacedKey getRegistryId() {
        return this.registryId;
    }

    @Override
    public @NotNull Material getRawMaterial() {
        return this.rawMaterial;
    }

    @Override
    public boolean hasOriginalCraft() {
        return this.hasOriginalCraft;
    }

    @Override
    public Integer getCustomModelData() {
        return this.customModelData;
    }

    @Override
    public @NotNull FoodProperty getFoodProperty() {
        return this.foodProperty;
    }

    @Override
    public @NotNull WeaponProperty getWeaponProperty() {
        return this.weaponProperty;
    }

    @Override
    public Integer getMaxDurability() {
        return this.maxDurability;
    }

    @Override
    public @NotNull AttributeProperty getAttributeProperty() {
        return (AttributeProperty) this.attributeProperty;
    }

    public @NotNull AttributePropertyImpl getOriginalAttributeProperty() {
        return this.attributeProperty;
    }

    @Override
    public @NotNull ArmorProperty getArmorProperty() {
        return this.armorProperty;
    }

    @Override
    public @Nullable NamespacedKey getCropId() {
        return cropId;
    }


    @SuppressWarnings("UnstableApiUsage")
    private ItemMeta createNewItemMeta() {
        ItemMeta itemMeta = new ItemStack(this.rawMaterial).getItemMeta();
        Multimap<Attribute, AttributeModifier> modifiers = ArrayListMultimap.create();
        modifiers.putAll(this.rawMaterial.getDefaultAttributeModifiers());
        itemMeta.displayName(this.displayName);
        itemMeta.lore(this.lore);
        if (this.customModelData != null) {
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
            modifiers.removeAll(Attribute.ATTACK_DAMAGE);
            modifiers.removeAll(Attribute.ATTACK_SPEED);
            modifiers.removeAll(Attribute.ATTACK_KNOCKBACK);
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
        }
        if (this.armorProperty != ArmorProperty.EMPTY) {
            EquippableComponent equippableComponent = itemMeta.getEquippable();
            equippableComponent.setSlot(this.armorProperty.slot());
            itemMeta.setEquippable(equippableComponent);
            if (this.armorProperty.armor() != null) {
                modifiers.removeAll(Attribute.ARMOR);
                modifiers.put(
                        Attribute.ARMOR,
                        new AttributeModifier(
                                this.registryId,
                                this.armorProperty.armor(),
                                AttributeModifier.Operation.ADD_NUMBER,
                                this.armorProperty.slot().getGroup()
                        )
                );
            }
            if (this.armorProperty.armorToughness() != null) {
                modifiers.removeAll(Attribute.ARMOR_TOUGHNESS);
                modifiers.put(
                        Attribute.ARMOR_TOUGHNESS,
                        new AttributeModifier(
                                this.registryId,
                                this.armorProperty.armorToughness(),
                                AttributeModifier.Operation.ADD_NUMBER,
                                this.armorProperty.slot().getGroup()
                        )
                );
            }
        }
        if (this.maxDurability != null && (itemMeta instanceof Damageable)) {
            ((Damageable) itemMeta).setMaxDamage(this.maxDurability);
        }
        if (!this.attributeProperty.isEmpty()) {
            NamespacedKey[] keys = this.attributeProperty.getItemstackAttributeKeys();
            for (NamespacedKey key : keys) {
                String typeName = AttributeRegistry.getAttributeRegistryManager().getItemstackAttributeTypeName(key);
                PersistentDataType<?, ?> PDCType = AttributeTypeRegistry.getAttributeTypeRegistryManager().getAttributePDCType(typeName);
                itemMeta.getPersistentDataContainer().set(key, PDCType, this.attributeProperty.getItemstackAttributeValue(key));
            }
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
