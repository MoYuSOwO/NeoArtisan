package io.github.MoYuSOwO.neoArtisan.util;

import io.github.MoYuSOwO.neoArtisan.item.ItemRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;

import java.util.Collection;

public final class Util {

    private Util() {}

    public static NamespacedKey stringToNamespaceKey(String s) {
        String id = s;
        if (!id.contains(":")) id = "minecraft:" + s;
        NamespacedKey key = NamespacedKey.fromString(id);
        System.out.println(key);
        if (!ItemRegistry.hasItem(key)) throw new IllegalArgumentException(s + " is not a effective registryId");
        return key;
    }

    public static double getTotalAttributeValue(Collection<AttributeModifier> collection) {
        double add_number = 0, add_scalar = 0, total = 0;
        for (AttributeModifier modifier : collection) {
            if (modifier.getOperation() == AttributeModifier.Operation.ADD_NUMBER) {
                add_number += modifier.getAmount();
            }
        }
        for (AttributeModifier modifier : collection) {
            if (modifier.getOperation() == AttributeModifier.Operation.ADD_SCALAR) {
                add_scalar += modifier.getAmount();
            }
        }
        total = add_number * (1 + add_scalar);
        for (AttributeModifier modifier : collection) {
            if (modifier.getOperation() == AttributeModifier.Operation.MULTIPLY_SCALAR_1) {
                total *= (1 + modifier.getAmount());
            }
        }
        return total;
    }
}
