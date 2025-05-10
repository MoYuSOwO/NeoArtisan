package io.github.moyusowo.neoartisan.block.crop;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.network.BlockMappingsManager;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.moyusowo.neoartisan.util.Util.isYmlFile;

public class CropRegistryImpl {

    private static CropRegistryImpl instance;

    public static CropRegistryImpl getInstance() {
        return instance;
    }

    public static void init() {
        new CropRegistryImpl();
        ArtisanCropBehavior.init();
        NeoArtisan.logger().info("成功从文件注册 " + instance.registry.size() + " 个自定义作物");
    }

    private final ConcurrentHashMap<NamespacedKey, ArtisanCropImpl> registry;

    private final Set<BlockState> usedStates;

    private CropRegistryImpl() {
        instance = this;
        registry = new ConcurrentHashMap<>();
        usedStates = BlockMappingsManager.getUsedStates();
        registerCropFromFile();
    }

    public void registerCropFromFile() {
        File[] files = ReadUtil.readAllFiles();
        if (files != null) {
            for (File file : files) {
                if (isYmlFile(file)) {
                    readYml(YamlConfiguration.loadConfiguration(file));
                }
            }
        }
    }

    private void readYml(YamlConfiguration yml) {
        registerCrop(ReadUtil.getCropId(yml), ReadUtil.getActualState(yml), ReadUtil.getStages(yml));
    }

    public void registerCrop(NamespacedKey cropId, BlockState actualState, List<ArtisanCropImpl.CropStageProperty> stages) {
        for (ArtisanCropImpl.CropStageProperty property : stages) {
            if (usedStates.contains(property.appearanceState())) {
                throw new IllegalArgumentException("The BlockState: " + property.appearanceState().toString() + " is used!");
            }
        }
        registry.put(cropId, new ArtisanCropImpl(cropId, actualState, stages));
    }

    public boolean isArtisanCrop(NamespacedKey cropId) {
        return registry.containsKey(cropId);
    }

    public ArtisanCropImpl getArtisanCrop(NamespacedKey cropId) {
        return registry.get(cropId);
    }
}
