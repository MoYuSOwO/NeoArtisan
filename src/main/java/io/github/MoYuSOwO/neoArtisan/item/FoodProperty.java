package io.github.MoYuSOwO.neoArtisan.item;

public record FoodProperty(Integer nutrition, Float saturation, boolean canAlwaysEat) {
    public static final FoodProperty EMPTY = new FoodProperty(null, null, false);
}
