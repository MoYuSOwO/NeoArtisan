package io.github.moyusowo.neoartisan.block.crop;

import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCrop;
import io.github.moyusowo.neoartisanapi.api.block.crop.CropStageProperty;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

class ArtisanCropImpl implements ArtisanCrop {
    private final NamespacedKey cropId;
    private final List<CropStageProperty> stages;
    private final int actualState;

    public ArtisanCropImpl(NamespacedKey cropId, int actualState, List<CropStageProperty> stages) {
        this.cropId = cropId;
        this.stages = stages;
        this.actualState = actualState;
    }

    @Override
    public @NotNull NamespacedKey getCropId() {
        return this.cropId;
    }

    @Override
    public @NotNull CropStageProperty getStage(int n) {
        if (n > getMaxStage()) return this.stages.getLast();
        else return this.stages.get(n);
    }

    @Override
    public int getMaxStage() {
        return this.stages.size() - 1;
    }

    @Override
    public int getActualState() {
        return this.actualState;
    }

}
