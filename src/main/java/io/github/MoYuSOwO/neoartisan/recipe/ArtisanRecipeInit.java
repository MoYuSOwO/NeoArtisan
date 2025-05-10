package io.github.moyusowo.neoartisan.recipe;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisanapi.api.recipe.RecipeRegistry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public final class ArtisanRecipeInit {

    private ArtisanRecipeInit() {}

    public static void init() {
        RecipeRegistryImpl.init();
        Bukkit.getServicesManager().register(
                RecipeRegistry.class,
                RecipeRegistryImpl.getInstance(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

}
