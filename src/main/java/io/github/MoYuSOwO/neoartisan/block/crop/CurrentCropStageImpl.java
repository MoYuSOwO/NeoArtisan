package io.github.moyusowo.neoartisan.block.crop;

import io.github.moyusowo.neoartisanapi.api.block.crop.CurrentCropStage;
import org.bukkit.NamespacedKey;

record CurrentCropStageImpl(NamespacedKey cropId, int stage) implements CurrentCropStage {

    @Override
    public int getBlockState() {
        return CropRegistryImpl.getInstance().getArtisanCrop(cropId).getStage(stage).appearanceState();
    }

    @Override
    public NamespacedKey[] getDrops() {
        return CropRegistryImpl.getInstance().getArtisanCrop(cropId).getStage(stage).drops();
    }

    @Override
    public boolean hasNextStage() {
        return stage < getMaxStage();
    }

    @Override
    public CurrentCropStageImpl getNextStage() {
        if (!hasNextStage()) throw new IllegalCallerException("use has to check the existence before get!");
        return new CurrentCropStageImpl(cropId, stage + 1);
    }

    @Override
    public int getMaxStage() {
        return CropRegistryImpl.getInstance().getArtisanCrop(cropId).getMaxStage();
    }
}
