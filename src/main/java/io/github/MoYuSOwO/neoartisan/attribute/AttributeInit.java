package io.github.moyusowo.neoartisan.attribute;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisanapi.api.attribute.AttributeRegistry;
import io.github.moyusowo.neoartisanapi.api.attribute.AttributeTypeRegistry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public final class AttributeInit {

    private AttributeInit() {}

    public static void init() {
        AttributeTypeRegistryImpl.init();
        AttributeRegistryImpl.init();
        Bukkit.getServicesManager().register(
                AttributeRegistry.class,
                AttributeRegistryImpl.getInstance(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
        Bukkit.getServicesManager().register(
                AttributeTypeRegistry.class,
                AttributeTypeRegistryImpl.getInstance(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }
}
