package io.github.moyusowo.neoartisan.block.network;

import io.github.moyusowo.neoartisan.NeoArtisan;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.moyusowo.neoartisan.block.BlockStateUtil.parseBlockStateId;
import static io.github.moyusowo.neoartisan.util.Util.saveDefaultIfNotExists;

public final class BlockMappingsManager {

    private BlockMappingsManager() {}

    private static final Map<Integer, Integer> mappings = new ConcurrentHashMap<>();

    private static final Set<BlockState> usedStates = new HashSet<>();

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
            Integer fromState = parseBlockStateId(key);
            Integer toState = parseBlockStateId(value);
            if (fromState != null && toState != null) {
                mappings.put(fromState, toState);
                usedStates.add(Block.stateById(toState));
            }
        }
    }

    public static Integer getMappedStateId(int original) {
        return mappings.getOrDefault(original, null);
    }

    public static BlockState getMappedState(BlockState original) {
        Integer stateId = mappings.getOrDefault(Block.getId(original), null);
        if (stateId != null) return Block.stateById(stateId);
        else return null;
    }

    public static Set<BlockState> getUsedStates() {
        return Set.copyOf(usedStates);
    }

}
