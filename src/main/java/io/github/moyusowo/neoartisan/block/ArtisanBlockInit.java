package io.github.moyusowo.neoartisan.block;

import io.github.moyusowo.neoartisan.block.crop.ArtisanCropInit;
import io.github.moyusowo.neoartisan.block.network.BlockMappingsManager;

public final class ArtisanBlockInit {

    private ArtisanBlockInit() {}

    public static void init() {
        BlockMappingsManager.init();
        ArtisanCropInit.init();
    }

}
