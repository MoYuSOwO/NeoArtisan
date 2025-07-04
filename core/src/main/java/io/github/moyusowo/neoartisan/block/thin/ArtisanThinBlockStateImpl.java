package io.github.moyusowo.neoartisan.block.thin;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockStateBase;
import io.github.moyusowo.neoartisanapi.api.block.thin.ThinBlockAppearance;
import io.github.moyusowo.neoartisanapi.api.block.thin.ArtisanThinBlockState;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

final class ArtisanThinBlockStateImpl extends ArtisanBlockStateBase implements ArtisanThinBlockState {

    @InitMethod(priority = InitPriority.REGISTRAR)
    private static void init() {
        Bukkit.getServicesManager().register(
                BuilderFactory.class,
                new BuilderFactory() {
                    @Override
                    public @NotNull Builder builder() {
                        return new BuilderImpl();
                    }
                },
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    ArtisanThinBlockStateImpl(int appearanceState, int actualState, ItemGenerator[] generators) {
        super(appearanceState, actualState, generators);
    }

    public static class BuilderImpl implements Builder {
        protected ThinBlockAppearance thinBlockAppearance;
        protected ItemGenerator[] generators;
        private static final int actualState = Block.getId(
                Blocks.OAK_PRESSURE_PLATE.defaultBlockState()
        );

        private int generateAppearanceState() {
            BlockState blockState;
            switch (this.thinBlockAppearance.appearance) {
                case HEAVY_WEIGHTED_PRESSURE_PLATE -> blockState = Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE.defaultBlockState();
                case null, default -> blockState = Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE.defaultBlockState();
            }
            return Block.getId(
                    blockState.setValue(BlockStateProperties.POWER, this.thinBlockAppearance.power)
            );
        }

        public BuilderImpl() {
            thinBlockAppearance = null;
            generators = null;
        }


        @Override
        public @NotNull Builder appearanceState(@NotNull ThinBlockAppearance thinBlockAppearance) {
            this.thinBlockAppearance = thinBlockAppearance;
            return this;
        }

        @Override
        public @NotNull Builder generators(ItemGenerator[] generators) {
            this.generators = generators;
            return this;
        }

        @Override
        public @NotNull ArtisanThinBlockState build() {
            if (generators == null || thinBlockAppearance == null) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanThinBlockStateImpl(generateAppearanceState(), actualState, generators);
        }
    }
}
