package io.github.MoYuSOwO.neoArtisan.recipe;

import io.github.MoYuSOwO.neoArtisan.NeoArtisan;
import io.github.MoYuSOwO.neoArtisan.item.ItemRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ArtisanShapelessRecipe {
    private final NamespacedKey[] recipe;
    private NamespacedKey result;
    private int count;
    private int i;
    private boolean built;

    private static final NamespacedKey EMPTY = NamespacedKey.fromString("empty:empty");

    public static String toRegistryKey(ItemStack[] matrix) {
        StringBuilder builtRecipe = new StringBuilder();
        String[] inv = new String[9];
        Arrays.fill(inv, "");
        for (int i = 0; i < 9; i++) {
            if (matrix[i] != null) {
                inv[i] = ItemRegistry.getRegistryId(matrix[i]).asString();
            }
        }
        Arrays.sort(inv);
        for (int i = 0; i < 9; i++) {
            if (inv[i] != null) {
                builtRecipe.append(inv[i]);
            }
            builtRecipe.append(",");
        }
        return builtRecipe.toString();
    }

    public ArtisanShapelessRecipe() {
        this.recipe = new NamespacedKey[9];
        this.i = 0;
        this.built = false;
        Arrays.fill(recipe, EMPTY);
    }

    public ArtisanShapelessRecipe(NamespacedKey result, int count) {
        this();
        this.result = result;
        this.count = count;
    }

    public void add(NamespacedKey registryId) {
        try {
            if (built) throw new IllegalAccessException("It's already registered!");
        } catch (IllegalAccessException e) {
            NeoArtisan.logger().severe(e.getLocalizedMessage());
        }
        if (i == recipe.length) throw new ArrayIndexOutOfBoundsException("You can no longer add!");
        recipe[i++] = registryId;
    }

    @SuppressWarnings("unused")
    public void add(NamespacedKey... registryIds) {
        try {
            if (built) throw new IllegalAccessException("It's already registered!");
        } catch (IllegalAccessException e) {
            NeoArtisan.logger().severe(e.getLocalizedMessage());
        }
        if (registryIds.length + i > recipe.length) throw new ArrayIndexOutOfBoundsException("You can no long add!");
        for (NamespacedKey registryId : registryIds) {
            recipe[i++] = registryId;
        }
    }

    @SuppressWarnings("unused")
    public void setResult(NamespacedKey registryId, int count) {
        try {
            if (built) throw new IllegalAccessException("It's already registered!");
        } catch (IllegalAccessException e) {
            NeoArtisan.logger().severe(e.getLocalizedMessage());
        }
        result = registryId;
        this.count = count;
    }

    public void build() {
        try {
            if (built) throw new IllegalAccessException("It's already registered!");
        } catch (IllegalAccessException e) {
            NeoArtisan.logger().severe(e.getLocalizedMessage());
        }
        String[] recipeString = new String[9];
        Arrays.fill(recipeString, "");
        for (int i = 0; i < 9; i++) {
            if (!recipe[i].equals(EMPTY)) {
                recipeString[i] = recipe[i].asString();
            }
        }
        Arrays.sort(recipeString);
        StringBuilder builtRecipe = new StringBuilder();
        for (String s : recipeString) {
            if (!s.isEmpty()) builtRecipe.append(s);
            builtRecipe.append(",");
        }
        RecipeRegistry.register(builtRecipe.toString(), this);
        built = true;
    }

    protected NamespacedKey getResult() {
        return result;
    }

    protected int getCount() {
        return count;
    }
}
