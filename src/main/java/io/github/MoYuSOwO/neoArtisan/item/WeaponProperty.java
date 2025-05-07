package io.github.MoYuSOwO.neoArtisan.item;

public record WeaponProperty(Float speed, Float knockback, Float damage) {
    public static final WeaponProperty EMPTY = new WeaponProperty(null, null, null);
}
