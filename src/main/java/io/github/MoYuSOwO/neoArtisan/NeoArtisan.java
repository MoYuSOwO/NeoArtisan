package io.github.MoYuSOwO.neoArtisan;

import io.github.MoYuSOwO.neoArtisan.item.ItemCommandRegistrar;
import io.github.MoYuSOwO.neoArtisan.item.ItemRegistry;
import io.github.MoYuSOwO.neoArtisan.recipe.RecipeRegistry;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class NeoArtisan extends JavaPlugin {

    private static NeoArtisan instance;
    private static NamespacedKey artisanItemIdKey;
    private static NamespacedKey artisanItemAttackDamageKey, artisanItemAttackKnockbackKey, artisanItemAttackSpeedKey;

    public NeoArtisan() {
        super();
        instance = this;
        artisanItemIdKey = new NamespacedKey(this, "registry_id");
        artisanItemAttackDamageKey = new NamespacedKey("minecraft", "base_attack_damage");
        artisanItemAttackKnockbackKey = new NamespacedKey("minecraft", "base_attack_knockback");
        artisanItemAttackSpeedKey = new NamespacedKey("minecraft", "base_attack_speed");
//        artisanItemAttackDamageKey = new NamespacedKey(this, "artisan_attack_damage");
//        artisanItemAttackKnockbackKey = new NamespacedKey(this, "artisan_attack_knockback");
//        artisanItemAttackSpeedKey = new NamespacedKey(this, "artisan_attack_speed");
    }

    public static NamespacedKey getArtisanItemIdKey() {
        return artisanItemIdKey;
    }

    public static NamespacedKey getArtisanItemAttackDamageKey() {
        return artisanItemAttackDamageKey;
    }

    public static NamespacedKey getArtisanItemAttackKnockbackKey() {
        return artisanItemAttackKnockbackKey;
    }

    public static NamespacedKey getArtisanItemAttackSpeedKey() {
        return artisanItemAttackSpeedKey;
    }

    public static NeoArtisan instance() {
        return instance;
    }

    public static Logger logger() {
        return instance.getLogger();
    }

    public static void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, instance);
    }

    public static Server server() {
        return instance.getServer();
    }

    @Override
    public void onEnable() {
        ItemRegistry.init();
        ItemCommandRegistrar.registerCommands();
        RecipeRegistry.init();
    }

    @Override
    public void onDisable() {
        Bukkit.resetRecipes();
    }
}
