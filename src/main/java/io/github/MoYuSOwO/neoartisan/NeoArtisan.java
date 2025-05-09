package io.github.moyusowo.neoartisan;

import io.github.moyusowo.neoartisan.item.AttributePropertyImpl;
import io.github.moyusowo.neoartisanapi.api.attribute.AttributeRegistry;
import io.github.moyusowo.neoartisanapi.api.attribute.AttributeTypeRegistry;
import io.github.moyusowo.neoartisanapi.api.item.AttributeProperty;
import io.github.moyusowo.neoartisanapi.api.item.ItemRegistry;
import io.github.moyusowo.neoartisanapi.api.recipe.RecipeRegistry;
import io.github.moyusowo.neoartisan.attribute.AttributeRegistryImpl;
import io.github.moyusowo.neoartisan.attribute.AttributeTypeRegistryImpl;
import io.github.moyusowo.neoartisan.item.ItemCommandRegistrar;
import io.github.moyusowo.neoartisan.item.ItemRegistryImpl;
import io.github.moyusowo.neoartisan.recipe.RecipeRegistryImpl;
import io.github.moyusowo.neoartisan.util.Debug;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.ServicePriority;
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
        AttributeTypeRegistryImpl.init();
        AttributeRegistryImpl.init();
        ItemRegistryImpl.init();
        ItemCommandRegistrar.registerCommands();
        RecipeRegistryImpl.init();
        Debug.init();
        Bukkit.getServicesManager().register(
                AttributeRegistry.class,
                AttributeRegistryImpl.getInstance(),
                this,
                ServicePriority.Normal
        );
        Bukkit.getServicesManager().register(
                AttributeTypeRegistry.class,
                AttributeTypeRegistryImpl.getInstance(),
                this,
                ServicePriority.Normal
        );
        Bukkit.getServicesManager().register(
                ItemRegistry.class,
                ItemRegistryImpl.getInstance(),
                this,
                ServicePriority.Normal
        );
        Bukkit.getServicesManager().register(
                RecipeRegistry.class,
                RecipeRegistryImpl.getInstance(),
                this,
                ServicePriority.Normal
        );
        Bukkit.getServicesManager().register(
                AttributeProperty.class,
                new AttributePropertyImpl(),
                this,
                ServicePriority.Normal
        );
    }

    @Override
    public void onDisable() {
        Bukkit.resetRecipes();
    }
}
