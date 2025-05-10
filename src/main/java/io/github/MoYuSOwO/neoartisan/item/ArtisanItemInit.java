package io.github.moyusowo.neoartisan.item;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisanapi.api.item.AttributeProperty;
import io.github.moyusowo.neoartisanapi.api.item.ItemRegistry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public final class ArtisanItemInit {

    private ArtisanItemInit() {}

    public static void init() {
        ItemRegistryImpl.init();
        ItemCommandRegistrar.registerCommands();
        Bukkit.getServicesManager().register(
                ItemRegistry.class,
                ItemRegistryImpl.getInstance(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
        Bukkit.getServicesManager().register(
                AttributeProperty.class,
                new AttributePropertyImpl(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

}
