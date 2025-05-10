package io.github.moyusowo.neoartisan.block.crop;

import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.NamespacedKey;

public record CurrentCropStage(NamespacedKey cropId, int stage) {

    public BlockState getBlockState() {
        return CropRegistryImpl.getInstance().getArtisanCrop(cropId).getStage(stage).appearanceState();
    }

    public NamespacedKey[] getDrops() {
        return CropRegistryImpl.getInstance().getArtisanCrop(cropId).getStage(stage).drops();
    }

    public boolean hasNextStage() {
        return stage < getMaxStage();
    }

    public CurrentCropStage getNextStage() {
        if (!hasNextStage()) throw new IllegalCallerException("use has to check the existence before get!");
        return new CurrentCropStage(cropId, stage + 1);
    }

    private int getMaxStage() {
        return CropRegistryImpl.getInstance().getArtisanCrop(cropId).getMaxStage();
    }
}
