package io.github.MoYuSOwO.neoArtisan.item;

public record WeaponProperty(float speed, float knockback, float damage) {
    public static final WeaponProperty EMPTY = new WeaponProperty(-1, -1, -1);
}
