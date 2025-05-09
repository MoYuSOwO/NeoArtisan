package io.github.moyusowo.neoartisan.block.network;

import io.github.moyusowo.neoartisan.NeoArtisan;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.moyusowo.neoartisan.util.Util.saveDefaultIfNotExists;

public final class BlockMappingsManager {

    private BlockMappingsManager() {}

    private static final Map<Integer, Integer> mappings = new ConcurrentHashMap<>();

    public static void init() {
        AddPipeline.init();
        File dataFolder = NeoArtisan.instance().getDataFolder();
        File itemFolder = new File(dataFolder, "block");
        if (!itemFolder.exists()) {
            itemFolder.mkdirs();
        }
        File configFile = new File(itemFolder, "mappings.yml");
        if (!configFile.exists()) {
            NeoArtisan.logger().warning("missing mappings.yml. Regenerated.");
            saveDefaultIfNotExists("block/mappings.yml");
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        for (String key : config.getKeys(false)) {
            String value = config.getString(key);
            Integer fromState = parseBlockState(key);
            Integer toState = parseBlockState(value);
            if (fromState != null && toState != null) {
                mappings.put(fromState, toState);
            }
        }
    }

    private static Integer parseBlockState(String input) {
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

    public static Integer getMappedStateId(int original) {
        return mappings.getOrDefault(original, null);
    }

    public static BlockState getMappedState(BlockState original) {
        Integer stateId = mappings.getOrDefault(Block.getId(original), null);
        if (stateId != null) return Block.stateById(stateId);
        else return null;
    }

}
