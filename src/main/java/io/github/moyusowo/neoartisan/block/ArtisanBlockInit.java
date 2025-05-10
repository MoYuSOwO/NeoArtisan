package io.github.moyusowo.neoartisan.block;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.crop.ArtisanCropStorageImpl;
import io.github.moyusowo.neoartisan.block.crop.CropRegistryImpl;
import io.github.moyusowo.neoartisan.block.network.BlockMappingsManager;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCropStorage;
import io.github.moyusowo.neoartisanapi.api.block.crop.CropRegistry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public final class ArtisanBlockInit {

    private ArtisanBlockInit() {}

    public static void init() {
        BlockMappingsManager.init();
        CropRegistryImpl.init();
        Bukkit.getServicesManager().register(
                CropRegistry.class,
                CropRegistryImpl.getInstance(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
        Bukkit.getServicesManager().register(
                ArtisanCropStorage.class,
                ArtisanCropStorageImpl.getInstance(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

}
