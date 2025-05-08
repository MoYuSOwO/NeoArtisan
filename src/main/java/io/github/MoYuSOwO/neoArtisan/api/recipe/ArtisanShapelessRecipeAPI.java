package io.github.MoYuSOwO.neoArtisan.api.recipe;

import org.bukkit.NamespacedKey;

/**
 * 无序合成配方构建器，用于定义材料自由摆放的自定义合成配方。
 *
 * <p>无序合成特点：</p>
 * <ul>
 *   <li>材料无需按特定顺序排列</li>
 *   <li>支持批量添加同类材料</li>
 *   <li>最少需要1个、最多9个输入材料</li>
 * </ul>
 *
 * @see RecipeRegistryAPI#createShapelessRecipe()
 * @see RecipeRegistryAPI#createShapelessRecipe(NamespacedKey, int)
 */
public interface ArtisanShapelessRecipeAPI {

    /**
     * 添加单个材料到合成配方。
     *
     * <p>调用过 {@link #build()} 方法之后不能再调用。</p>
     *
     * @param registryId 自定义物品注册ID（不能为null）
     * @see #add(NamespacedKey...)
     */
    void add(NamespacedKey registryId);

    /**
     * 批量添加多个相同材料到合成配方。
     *
     * <p>示例添加3个钻石：</p>
     * <pre>{@code
     * add(
     *     new NamespacedKey("myplugin", "diamond"),
     *     new NamespacedKey("myplugin", "diamond"),
     *     new NamespacedKey("myplugin", "diamond")
     * )
     * }</pre>
     *
     * <p>调用过 {@link #build()} 方法之后不能再调用。</p>
     *
     * @param registryIds 相同材料的注册ID数组（不能为null或空）
     * @see #add(NamespacedKey)
     */
    void add(NamespacedKey... registryIds);

    /**
     * 设置合成结果物品及数量。
     *
     * <p>调用过 {@link #build()} 方法之后不能再调用。</p>
     *
     * @param registryId 结果物品注册ID（不能为null）
     * @param count 产出数量
     */
    void setResult(NamespacedKey registryId, int count);

    /**
     * 完成配方构建并注册到服务器。
     *
     * <p><b>前置条件检查：</b></p>
     * <ul>
     *   <li>必须设置结果物品</li>
     *   <li>材料总数必须在1-9个之间</li>
     *   <li>配方未构建过</li>
     * </ul>
     *
     * <p>调用过本方法之后不能再调用。</p>
     *
     */
    void build();
}
