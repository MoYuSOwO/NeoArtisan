package io.github.moyusowo.neoartisan.block;

import io.github.moyusowo.neoartisan.NeoArtisan;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

public final class BlockStateUtil {

    private BlockStateUtil() {}

    public static @Nullable Integer parseBlockStateId(String input) {
        try {
            String[] parts = input.split("\\[");
            ResourceLocation blockId = ResourceLocation.parse(parts[0]);
            Block block = BuiltInRegistries.BLOCK.getValue(blockId);
            String propertiesStr = parts[1].replace("]", "");
            BlockState state = block.defaultBlockState();
            for (String propPair : propertiesStr.split(",")) {
                String[] kv = propPair.split("=");
                Property<?> property = block.getStateDefinition().getProperty(kv[0]);
                if (property != null) {
                    state = setProperty(state, property, kv[1]);
                }
            }
            return Block.getId(state);
        } catch (Exception e) {
            NeoArtisan.logger().warning("无法解析 BlockState: " + input);
            return null;
        }
    }

    private static <T extends Comparable<T>> BlockState setProperty(
            BlockState state, Property<T> property, String value
    ) {
        return state.setValue(property, property.getValue(value).orElseThrow());
    }

    public static BlockState stateById(int id) {
        return Block.stateById(id);
    }
}
