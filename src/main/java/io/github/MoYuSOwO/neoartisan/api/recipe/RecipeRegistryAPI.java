package io.github.moyusowo.neoartisan.api.recipe;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * 自定义合成配方注册表API，提供标准化的配方创建接口。
 *
 * <p>支持两种配方类型：</p>
 * <ul>
 *   <li><b>有序合成(Shaped)</b> - 精确匹配物品排列位置</li>
 *   <li><b>无序合成(Shapeless)</b> - 仅需材料无需考虑排列</li>
 * </ul>
 *
 * <p>通过 {@link org.bukkit.Bukkit#getServicesManager()} 获取实例。</p>
 *
 * @see ArtisanShapedRecipeAPI
 * @see ArtisanShapelessRecipeAPI
 */
public interface RecipeRegistryAPI {

    /**
     * 创建有序合成配方模板。
     *
     * <p>配方格式使用字母代表材料，每行对应工作台的3个格子：</p>
     * <pre>{@code
     * 示例：
     * " A "   → 中间放置A材料
     * " B "   → 中间下方放B材料
     * "   "   → 其他位置为空（空行也可以传入空字符串""）
     * }</pre>
     *
     * @param line1 第一行配方模式（3字符，不能为null）
     * @param line2 第二行配方模式（3字符，不能为null）
     * @param line3 第三行配方模式（3字符，不能为null）
     * @return 配方构建器实例（不会为null）
     * @throws IllegalArgumentException 如果格式不符合3字符长度或含非法字符
     * @see ArtisanShapedRecipeAPI
     */
    @NotNull ArtisanShapedRecipeAPI createShapedRecipe(@NotNull String line1, @NotNull String line2, @NotNull String line3);

    /**
     * 创建基础无序合成配方模板。
     *
     * <p>通过返回的 {@link ArtisanShapelessRecipeAPI} 继续添加材料</p>
     *
     * @return 配方构建器实例（不会为null）
     * @see ArtisanShapelessRecipeAPI
     */
    @NotNull ArtisanShapelessRecipeAPI createShapelessRecipe();

    /**
     * 创建预设结果的无序合成配方模板。
     *
     * <p>通过返回的 {@link ArtisanShapelessRecipeAPI} 继续添加材料</p>
     *
     * @param result 合成结果物品ID（不能为null）
     * @param count 产出数量（不能超过堆叠上限）
     * @return 配方构建器实例（不会为null）
     * @throws IllegalArgumentException 如果数量超出范围或物品未注册
     * @see ArtisanShapelessRecipeAPI
     */
    @NotNull ArtisanShapelessRecipeAPI createShapelessRecipe(NamespacedKey result, int count);

}
