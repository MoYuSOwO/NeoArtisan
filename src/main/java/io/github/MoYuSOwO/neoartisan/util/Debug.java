package io.github.moyusowo.neoartisan.util;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.attribute.AttributeTypeRegistry;
import io.github.moyusowo.neoartisan.item.ItemRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Debug implements Listener {

    private Debug() {}

    public static void init() {
        NeoArtisan.registerListener(new Debug());
    }

    @EventHandler
    private static void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            NamespacedKey attributeKey = new NamespacedKey(NeoArtisan.instance(), "level");
            Integer level = ItemRegistry.getInstance().getItemstackAttributeValue(itemStack, attributeKey);
            System.out.println(level);
            if (level != null) {
                level++;
                ItemRegistry.getInstance().setItemstackAttributeValue(itemStack, attributeKey, level);
            }
        }
    }

}
