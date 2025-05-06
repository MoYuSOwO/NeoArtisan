package io.github.MoYuSOwO.neoArtisan.item;

public record FoodProperty(int nutrition, float saturation, boolean canAlwaysEat) {
    public static final FoodProperty EMPTY = new FoodProperty(-1, -1, false);
}
