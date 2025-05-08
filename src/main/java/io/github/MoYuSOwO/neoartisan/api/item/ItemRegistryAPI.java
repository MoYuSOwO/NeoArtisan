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

import java.util.List;

/**
 * 自定义物品注册表核心接口，提供完整的自定义物品生命周期管理。
 *
 * <p>此接口负责：</p>
 * <ul>
 *   <li>自定义物品的注册与配置</li>
 *   <li>物品实例的获取与验证</li>
 *   <li>物品属性的动态读写</li>
 * </ul>
 *
 * <p>通过 {@link org.bukkit.Bukkit#getServicesManager()} 获取实例。</p>
 *
 * @apiNote 部分方法涉及持久化操作，请在主线程调用
 * @see ArtisanItemAPI
 * @see org.bukkit.plugin.ServicesManager
 */
public interface ItemRegistryAPI {

    /**
     * 注册一个新的自定义物品模板。
     *
     * <p>注册后的物品可以通过 {@link #getItemStack(NamespacedKey)} 获取实例。</p>
     *
     * @param registryId 物品唯一标识（不能为null）
     * @param rawMaterial 基础材质类型（不能为null）
     * @param hasOriginalCraft 是否保留原版合成
     * @param customModelData 自定义模型数据（可选）
     * @param displayName 显示名称（不能为null或空）
     * @param lore 物品描述（不能为null，可空列表）
     * @param foodProperty 食物属性配置（不能为null，可为EMPTY）
     * @param weaponProperty 武器属性配置（不能为null，可为EMPTY）
     * @param maxDurability 最大耐久值（可选）
     * @param armorProperty 护甲属性配置（不能为null，可为EMPTY）
     * @param attributeProperty 属性系统配置（不能为null，可为EMPTY）
     * @throws IllegalArgumentException 如果参数无效或ID已存在
     * @see FoodProperty
     * @see WeaponProperty
     * @see ArmorProperty
     * @see AttributeProperty
     */
    void registerItem(
            @NotNull NamespacedKey registryId,
            @NotNull Material rawMaterial,
            boolean hasOriginalCraft,
            @Nullable Integer customModelData,
            @NotNull String displayName,
            @NotNull List<String> lore,
            @NotNull FoodProperty foodProperty,
            @NotNull WeaponProperty weaponProperty,
            @Nullable Integer maxDurability,
            @NotNull ArmorProperty armorProperty,
            @NotNull AttributeProperty attributeProperty
    );

    /**
     * 从物品堆解析注册ID。
     *
     * <p>minecraft原版物品会返回原版物品的命名空间ID。</p>
     *
     * @param itemStack 目标物品堆（不能为null）
     * @return 对应的注册ID（不会为null）
     */
    @NotNull NamespacedKey getRegistryId(@NotNull ItemStack itemStack);

    /**
     * 检查指定ID的自定义物品是否已注册。
     *
     * <p>该方法包括minecraft原版物品</p>
     *
     * @param registryId 要检查的物品ID（可为null）
     * @return 如果物品存在则返回true，否则返回false
     * @apiNote 此方法总是立即返回，不会抛出异常
     */
    boolean hasItem(@Nullable NamespacedKey registryId);

    /**
     * 创建指定数量的指定命名空间ID的物品堆。
     *
     * <p>该方法兼容minecraft原版物品命名空间ID</p>
     *
     * @param registryId 物品注册ID（不能为null）
     * @param count 物品数量（超过堆叠上限的数值会自动变为上限值）
     * @return 新的物品堆实例（不会为null）
     * @throws IllegalArgumentException 如果物品未注册或参数无效
     */
    @NotNull ItemStack getItemStack(NamespacedKey registryId, int count);

    /**
     * 创建数量为1的指定命名空间ID的物品堆。
     *
     * <p>该方法兼容minecraft原版物品命名空间ID</p>
     *
     * @param registryId 物品注册ID（不能为null）
     * @return 新的物品堆实例（不会为null）
     */
    @NotNull ItemStack getItemStack(NamespacedKey registryId);

    /**
     * 通过ID验证是否为有效自定义物品。
     *
     * @param registryId 要检查的物品ID（可为null）
     * @return 如果是注册的自定义物品返回true
     */
    boolean isArtisanItem(@Nullable NamespacedKey registryId);

    /**
     * 通过物品堆验证是否为自定义物品。
     *
     * @param itemStack 要检查的物品堆（可为null）
     * @return 如果是本系统注册的自定义物品返回true
     */
    boolean isArtisanItem(@Nullable ItemStack itemStack);

    /**
     * 通过ID获取物品API实例。
     *
     * @param registryId 物品注册ID（不能为null）
     * @return 物品API接口实例（不会为null）
     * @throws IllegalArgumentException 如果物品未注册
     * @see ArtisanItemAPI
     * @apiNote 调用该方法之前应该总是调用 {@link ItemRegistryAPI#isArtisanItem(NamespacedKey)} 以检查有效性
     */
    @NotNull ArtisanItemAPI getArtisanItemAPI(@NotNull NamespacedKey registryId);

    /**
     * 通过物品堆获取物品API实例。
     *
     * @param itemStack 目标物品堆（不能为null）
     * @return 物品API接口实例（不会为null）
     * @throws IllegalArgumentException 如果不是有效自定义物品
     * @see #getArtisanItemAPI(NamespacedKey)
     * @apiNote 调用该方法之前应该总是调用 {@link ItemRegistryAPI#isArtisanItem(ItemStack)} 以检查有效性
     */
    @NotNull ArtisanItemAPI getArtisanItemAPI(ItemStack itemStack);

    /**
     * 读取物品堆上的动态属性值。
     *
     * @param itemStack 目标物品堆（不能为null）
     * @param attributeKey 属性键（不能为null）
     * @return 属性值，如果不存在返回null
     * @param <T> 属性值类型
     * @throws IllegalStateException 如果属性类型不匹配
     * @see #setItemstackAttributeValue(ItemStack, NamespacedKey, Object)
     */
    @Nullable <T> T getItemstackAttributeValue(@NotNull ItemStack itemStack, @NotNull NamespacedKey attributeKey);

    /**
     * 设置物品堆上的动态属性值。
     *
     * @param itemStack 目标物品堆（不能为null）
     * @param attributeKey 属性键（不能为null）
     * @param value 要设置的值（不能为null）
     * @param <T> 属性值类型
     * @throws IllegalArgumentException 如果属性未注册或值类型无效
     * @see #getItemstackAttributeValue(ItemStack, NamespacedKey)
     */
    <T> void setItemstackAttributeValue(@NotNull ItemStack itemStack, @NotNull NamespacedKey attributeKey, @NotNull T value);
}
