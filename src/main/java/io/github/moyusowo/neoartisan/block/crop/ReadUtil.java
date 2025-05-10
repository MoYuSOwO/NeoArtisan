package io.github.moyusowo.neoartisan.block.crop;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.network.BlockStateUtil;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.github.moyusowo.neoartisan.util.Util.saveDefaultIfNotExists;
import static io.github.moyusowo.neoartisan.util.Util.stringToNamespaceKey;

final class ReadUtil {

    private ReadUtil() {}

    public static File[] readAllFiles() {
        File dataFolder = NeoArtisan.instance().getDataFolder();
        File blockFolder = new File(dataFolder, "block");
        File cropFolder = new File(blockFolder, "crop");
        if (!cropFolder.exists()) {
            cropFolder.mkdirs();
            saveDefaultIfNotExists("block/crop/magic_crop.yml");
        }
        return cropFolder.listFiles();
    }

    public static NamespacedKey getCropId(YamlConfiguration crop) {
        String cropId = crop.getString("cropId");
        if (cropId != null) return new NamespacedKey(NeoArtisan.instance(), cropId);
        else throw new IllegalArgumentException("You must provide a cropId!");
    }

    public static BlockState getActualState(YamlConfiguration crop) {
        String actualStateName = crop.getString("actualState");
        BlockState blockState = BlockStateUtil.parseBlockState(actualStateName);
        if (blockState != null) return blockState;
        else throw new IllegalArgumentException("You must provide a existed BlockState!");
    }

    public static List<ArtisanCropImpl.CropStageProperty> getStages(YamlConfiguration crop) {
        List<ArtisanCropImpl.CropStageProperty> properties = new ArrayList<>();
        ConfigurationSection stagesSection = crop.getConfigurationSection("stages");
        if (stagesSection == null) throw new IllegalArgumentException("You must provide any stages!");
        for (String key : stagesSection.getKeys(false)) {
            ConfigurationSection section = stagesSection.getConfigurationSection(key);
            if (section == null) throw new IllegalArgumentException("You can not provide a empty stage!");
            BlockState appearance = BlockStateUtil.parseBlockState(section.getString("appearance"));
            List<String> dropNames = section.getStringList("drops");
            List<NamespacedKey> drops = new ArrayList<>();
            for (String dropName : dropNames) {
                drops.add(stringToNamespaceKey(dropName));
            }
            ArtisanCropImpl.CropStageProperty cropStageProperty = new ArtisanCropImpl.CropStageProperty(appearance, drops.toArray(new NamespacedKey[0]));
            properties.add(cropStageProperty);
        }
        if (properties.isEmpty()) throw new IllegalArgumentException("You must provide any stages!");
        return properties;
    }
}
