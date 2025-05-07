package io.github.MoYuSOwO.neoArtisan;

import io.github.MoYuSOwO.neoArtisan.item.ItemCommandRegistrar;
import io.github.MoYuSOwO.neoArtisan.item.ItemRegistry;
import io.github.MoYuSOwO.neoArtisan.recipe.RecipeRegistry;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Logger;

public final class NeoArtisan extends JavaPlugin {

    private static NeoArtisan instance;
    private static NamespacedKey artisanItemKey;

    public NeoArtisan() {
        super();
        instance = this;
        artisanItemKey = new NamespacedKey(this, "registryId");
    }

    public static NamespacedKey getArtisanItemKey() {
        return artisanItemKey;
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
