package io.github.moyusowo.neoartisan.api.recipe;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * 有序合成配方构建器，用于定义精确布局的自定义合成配方。
 *
 * <p>通过此接口可以：</p>
 * <ul>
 *   <li>将字符符号绑定到特定材料</li>
 *   <li>设置合成结果物品和数量</li>
 *   <li>最终注册配方到服务器</li>
 * </ul>
 *
 * @see RecipeRegistryAPI#createShapedRecipe(String, String, String)
 */
public interface ArtisanShapedRecipeAPI {

    /**
     * 将配方符号绑定到自定义物品。
     *
     * <p>示例将'A'绑定到钻石剑：</p>
     * <pre>{@code
     * add('A', new NamespacedKey("myplugin", "diamond_sword"))
     * }</pre>
     *
     * <p>调用过 {@link #build()} 方法之后不能再调用。</p>
     *
     * @param c 配方模式中的字符符号（大小写敏感）
     * @param registryId 自定义物品注册ID（不能为null）
     * @throws IllegalArgumentException 如果重复绑定同一字符
     */
    void add(char c, @NotNull NamespacedKey registryId);

    /**
     * 设置合成结果。
     *
     * <p>调用过 {@link #build()} 方法之后不能再调用。</p>
     *
     * @param result 结果物品注册ID（不能为null）
     * @param count 产出数量
     * @see #build()
     */
    void setResult(@NotNull NamespacedKey result, int count);

    /**
     * 完成配方构建并注册到服务器。
     *
     * <p><b>必须调用此方法配方才会生效！</b></p>
     *
     * <p>调用过本方法之后不能再调用。</p>
     *
     * @see RecipeRegistryAPI
     */
    void build();

}
