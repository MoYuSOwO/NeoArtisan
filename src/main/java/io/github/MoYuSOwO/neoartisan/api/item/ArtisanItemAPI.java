package io.github.moyusowo.neoartisan.api.item;

import io.github.moyusowo.neoartisan.record.item.ArmorProperty;
import io.github.moyusowo.neoartisan.record.item.AttributeProperty;
import io.github.moyusowo.neoartisan.record.item.FoodProperty;
import io.github.moyusowo.neoartisan.record.item.WeaponProperty;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 自定义物品核心接口，提供对自定义物品属性和特性的访问。
 *
 * <p>此接口代表一个在系统中注册的自定义物品实例，包含物品的基础信息
 * 和各种扩展属性。所有自定义物品都应有唯一的 {@link NamespacedKey} 标识。</p>
 *
 * @apiNote 不保证API的线程安全
 * @see AttributeProperty
 * @see WeaponProperty
 * @see FoodProperty
 * @see ArmorProperty
 */
public interface ArtisanItemAPI {

    /**
     * 通过物品堆判断是否为此自定义物品。
     *
     * @param itemStack 要比较的物品堆（不能为null）
     * @return 如果物品堆代表此自定义物品返回true，否则返回false
     */
    boolean equals(@NotNull ItemStack itemStack);

    /**
     * 通过注册ID判断是否为此自定义物品。
     *
     * @param registryId 要比较的注册ID（不能为null）
     * @return 如果ID匹配返回true，否则返回false
     */
    boolean equals(@NotNull NamespacedKey registryId);

    /**
     * 获取此自定义物品的唯一注册ID。
     *
     * @return 物品的命名空间键（不会为null）
     */
    @NotNull NamespacedKey getRegistryId();

    /**
     * 获取此自定义物品的基础材质。
     *
     * @return 物品的原始Material类型（不会为null）
     */
    @NotNull Material getRawMaterial();

    /**
     * 检查此物品是否保留原版合成配方。
     *
     * @return 如果保留原版合成返回true，否则返回false
     */
    boolean hasOriginalCraft();

    /**
     * 获取此物品的自定义模型数据值。
     *
     * @return 自定义模型数据值，如果没有设置返回null
     */
    @Nullable Integer getCustomModelData();

    /**
     * 获取此物品的食物属性配置。
     *
     * @return 食物属性对象，如果没有设置返回 {@link FoodProperty#EMPTY}
     * @see FoodProperty
     */
    @NotNull FoodProperty getFoodProperty();

    /**
     * 获取此物品的武器属性配置。
     *
     * @return 武器属性对象，如果没有设置返回 {@link WeaponProperty#EMPTY}
     * @see WeaponProperty
     */
    @NotNull WeaponProperty getWeaponProperty();

    /**
     * 获取此物品的最大耐久值。
     *
     * @return 最大耐久值，如果使用默认耐久返回null
     */
    @Nullable Integer getMaxDurability();

    /**
     * 获取此物品的属性系统配置。
     *
     * @return 属性配置对象，不会返回null，可以使用 {@link AttributeProperty#isEmpty()} 判空
     * @see AttributeProperty
     */
    @NotNull AttributeProperty getAttributeProperty();

    /**
     * 获取此物品的武器属性配置。
     *
     * @return 武器属性对象，如果没有设置返回 {@link ArmorProperty#EMPTY}
     * @see ArmorProperty
     */
    @NotNull ArmorProperty getArmorProperty();
}
