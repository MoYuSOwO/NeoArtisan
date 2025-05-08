package io.github.moyusowo.neoartisan.api.attribute;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * 自定义物品属性注册表API，用于管理全局属性和物品堆属性。
 *
 * <p>属性分为两种类型：</p>
 * <ul>
 *   <li><b>全局属性(GlobalAttribute)</b> - 对所有同ID物品生效的共享属性</li>
 *   <li><b>物品堆属性(ItemstackAttribute)</b> - 仅对单个物品实例有效的独立属性（如耐久、等级等）</li>
 * </ul>
 *
 * <p>通过此接口可以注册、获取和管理服务器中属性系统的所有属性。
 *  * 通过 {@link org.bukkit.Bukkit#getServicesManager()} 获取实例。</p>
 *
 * @apiNote 不保证API的线程安全
 */
public interface AttributeRegistryAPI {

    /**
     * 注册一个全局属性类型。
     *
     * <p>全局属性对所有具有相同ID的物品实例共享相同的值，
     * 适用于定义物品的基础属性（如武器类型、基础伤害等）。</p>
     *
     * @param attributeKey 属性的命名空间键（不能为null）
     * @param typeName 属性类型名称（不能为null或空）
     * @throws IllegalArgumentException 如果属性已注册或参数无效
     */
    void registerGlobalAttribute(@NotNull NamespacedKey attributeKey, @NotNull String typeName);

    /**
     * 注册一个物品堆属性类型。
     *
     * <p>物品堆属性每个物品实例拥有独立的值，
     * 适用于实例特定的数据（如当前耐久、强化等级等）。</p>
     *
     * @param attributeKey 属性的命名空间键（不能为null）
     * @param typeName 属性类型名称（不能为null或空）
     * @throws IllegalArgumentException 如果属性已注册或参数无效
     */
    void registerItemstackAttribute(@NotNull NamespacedKey attributeKey, @NotNull String typeName);

    /**
     * 检查是否已注册指定的全局属性。
     *
     * @param attributeKey 要检查的属性键（不能为null）
     * @return 如果属性已注册返回true，否则返回false
     */
    boolean hasGlobalAttribute(@NotNull NamespacedKey attributeKey);

    /**
     * 检查是否已注册指定的物品堆属性。
     *
     * @param attributeKey 要检查的属性键（不能为null）
     * @return 如果属性已注册返回true，否则返回false
     */
    boolean hasItemstackAttribute(@NotNull NamespacedKey attributeKey);

    /**
     * 获取全局属性的类型名称。
     *
     * @param attributeKey 要查询的属性键（不能为null）
     * @return 注册时指定的类型名称（不会为null）
     * @throws IllegalArgumentException 如果属性未注册
     * @apiNote 调用该方法之前应该总是调用 {@link AttributeRegistryAPI#hasGlobalAttribute(NamespacedKey)}
     */
    @NotNull String getGlobalAttributeTypeName(@NotNull NamespacedKey attributeKey);

    /**
     * 获取物品堆属性的类型名称。
     *
     * @param attributeKey 要查询的属性键（不能为null）
     * @return 注册时指定的类型名称（不会为null）
     * @throws IllegalArgumentException 如果属性未注册
     * @apiNote 调用该方法之前应该总是调用 {@link AttributeRegistryAPI#hasItemstackAttribute(NamespacedKey)}
     */
    @NotNull String getItemstackAttributeTypeName(@NotNull NamespacedKey attributeKey);
}
