package io.github.moyusowo.neoartisan.block.crop;

import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.NamespacedKey;

import java.util.List;

public class ArtisanCropImpl {
    private final NamespacedKey cropId;
    private final List<CropStage> stages;
    private final BlockState actualState;

    public record CropStage(BlockState appearanceState, NamespacedKey[] drops) {}

    public ArtisanCropImpl(NamespacedKey cropId, BlockState actualState, List<CropStage> stages) {
        this.cropId = cropId;
        this.stages = stages;
        this.actualState = actualState;
    }

    public NamespacedKey getCropId() {
        return this.cropId;
    }

    public CropStage getStage(int n) {
        return this.stages.get(n);
    }

    public BlockState getActualState() {
        return this.actualState;
    }

}
