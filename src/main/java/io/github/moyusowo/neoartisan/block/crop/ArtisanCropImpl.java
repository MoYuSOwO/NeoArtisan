package io.github.moyusowo.neoartisan.block.crop;

import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.NamespacedKey;

import java.util.Arrays;
import java.util.List;

public class ArtisanCropImpl {
    private final NamespacedKey cropId;
    private final List<CropStageProperty> stages;
    private final BlockState actualState;

    public record CropStageProperty(BlockState appearanceState, NamespacedKey[] drops) {
        public NamespacedKey[] drops() {
            return Arrays.copyOf(drops, drops.length);
        }
    }

    public ArtisanCropImpl(NamespacedKey cropId, BlockState actualState, List<CropStageProperty> stages) {
        this.cropId = cropId;
        this.stages = stages;
        this.actualState = actualState;
    }

    public NamespacedKey getCropId() {
        return this.cropId;
    }

    public CropStageProperty getStage(int n) {
        if (n > getMaxStage()) return this.stages.getLast();
        else return this.stages.get(n);
    }

    public int getMaxStage() {
        return this.stages.size() - 1;
    }

    public BlockState getActualState() {
        return this.actualState;
    }

}
