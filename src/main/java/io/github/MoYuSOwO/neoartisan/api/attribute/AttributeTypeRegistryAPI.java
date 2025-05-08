package io.github.moyusowo.neoartisan.api.attribute;

import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * 属性数据类型注册表API，用于管理属性系统支持的数据类型与持久化存储类型的映射关系。
 *
 * <p>本接口提供了属性数据类型与 {@link PersistentDataType} 之间的转换能力，
 * 用于确保属性值能够正确地在 Bukkit 持久化数据容器(PDC)中存储和读取。</p>
 *
 * <p><b>当前版本限制：</b></p>
 * <ul>
 *   <li>不支持运行时动态注册新数据类型</li>
 *   <li>所有数据类型必须在插件初始化时注册</li>
 *   <li>YAML 序列化/反序列化需要额外配置</li>
 * </ul>
 *
 * <p>通过 {@link org.bukkit.Bukkit#getServicesManager()} 获取实例。</p>
 *
 * @apiNote 此接口的实现不保证线程安全，建议在主线程调用
 * @see org.bukkit.persistence.PersistentDataType
 * @see org.bukkit.plugin.ServicesManager
 */
public interface AttributeTypeRegistryAPI {

    /**
     * 注册属性数据类型与持久化类型的映射关系。
     *
     * <p><b>当前版本限制：</b></p>
     * <ul>
     *   <li>此方法在当前版本暂不可用</li>
     *   <li>调用将抛出 {@link UnsupportedOperationException}</li>
     * </ul>
     *
     * @param typeName 数据类型唯一标识（大小写敏感，不能为null或空）
     * @param PDCType 对应的持久化数据类型（不能为null）
     * @throws UnsupportedOperationException 当前版本不支持此操作
     * @throws IllegalArgumentException 如果参数无效
     */
    void registerAttributeType(@NotNull String typeName, @NotNull PersistentDataType<?, ?> PDCType);

    /**
     * 检查指定数据类型是否已注册。
     *
     * @param typeName 要检查的数据类型名称（不能为null）
     * @return 如果该类型已注册返回true，否则返回false
     * @apiNote 此方法总是立即返回，不会抛出异常
     */
    boolean hasAttributeType(@NotNull String typeName);

    /**
     * 获取数据类型对应的Java类。
     *
     * <p>返回的Class对象可用于类型检查和反射操作。</p>
     *
     * @param typeName 已注册的数据类型名称（不能为null）
     * @return 对应的Java类型（不会为null）
     * @throws IllegalArgumentException 如果类型未注册
     * @see PersistentDataType#getPrimitiveType()
     * @apiNote 调用该方法之前应该总是调用 {@link AttributeTypeRegistryAPI#hasAttributeType(String)}
     */
    @NotNull Class<?> getAttributeJavaType(@NotNull String typeName);

    /**
     * 获取数据类型对应的持久化存储类型。
     *
     * <p>返回的 {@link PersistentDataType} 可用于Bukkit持久化数据容器的操作。</p>
     *
     * @param typeName 已注册的数据类型名称（不能为null）
     * @return 对应的PDC持久化类型（不会为null）
     * @throws IllegalArgumentException 如果类型未注册
     * @see org.bukkit.persistence.PersistentDataContainer
     * @apiNote 调用该方法之前应该总是调用 {@link AttributeTypeRegistryAPI#hasAttributeType(String)}
     */
    @NotNull PersistentDataType<?, ?> getAttributePDCType(@NotNull String typeName);
}
