package io.github.MoYuSOwO.neoArtisan.item;

import io.github.MoYuSOwO.neoArtisan.NeoArtisan;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.FoodComponent;

public final class ItemBehavior implements Listener {

    private ItemBehavior() {}

    static void init() {
        NeoArtisan.registerListener(new ItemBehavior());
    }

    @EventHandler
    private static void onAttackWithArtisanWeapon(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;
        if (event.getDamager() instanceof Player player) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            ItemMeta itemMeta = itemStack.getItemMeta();
            net.minecraft.world.item.ItemStack nmsItem = ((CraftItemStack) itemStack).handle;
            DataComponentMap tag = nmsItem.getComponents();
            var modifiers = tag.get(DataComponents.ATTRIBUTE_MODIFIERS);
            if (modifiers != null) {
                System.out.println(modifiers.modifiers());
            }
            System.out.println(itemMeta.getAttributeModifiers());
            System.out.println(((Damageable) itemMeta).getMaxDamage());
        }
    }

}
