package io.github.MoYuSOwO.neoArtisan;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

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

    @Override
    public void onEnable() {
        // Plugin startup logic
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
