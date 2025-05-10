package io.github.moyusowo.neoartisan.util;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisanapi.api.item.ItemRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;

public final class Util {

    private Util() {}

    public static NamespacedKey stringToNamespaceKey(String s) {
        String id = s;
        if (!id.contains(":")) id = "minecraft:" + s;
        NamespacedKey key = NamespacedKey.fromString(id);
        if (!ItemRegistry.getItemRegistryManager().hasItem(key)) throw new IllegalArgumentException(s + " is not a effective registryId");
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

    public static EquipmentSlot toSlotOrNull(String slot) {
        try {
            return EquipmentSlot.valueOf(slot);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static void saveDefaultIfNotExists(String resourcePath) {
        String targetPath = resourcePath.replace('/', File.separatorChar);
        File targetFile = new File(NeoArtisan.instance().getDataFolder(), targetPath);
        if (targetFile.exists()) {
            return;
        }
        NeoArtisan.instance().saveResource(resourcePath, false);
    }

    public static boolean isYmlFile(@NotNull File file) {
        if (!file.isFile()) return false;
        String name = file.getName().toLowerCase();
        return name.endsWith(".yml") || name.endsWith(".yaml");
    }

}
