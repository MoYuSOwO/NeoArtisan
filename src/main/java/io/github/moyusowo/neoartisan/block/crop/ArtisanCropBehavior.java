package io.github.moyusowo.neoartisan.block.crop;


import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisanapi.api.item.ArtisanItem;
import io.github.moyusowo.neoartisanapi.api.item.ItemRegistry;
import io.papermc.paper.event.block.BlockBreakBlockEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import static io.github.moyusowo.neoartisan.block.BlockStateUtil.stateById;

public final class ArtisanCropBehavior implements Listener {

    private static final HashMap<Block, CurrentCropStageImpl> grownCrop = new HashMap<>();

    private ArtisanCropBehavior() {}

    static void init() {
        NeoArtisan.registerListener(new ArtisanCropBehavior());
    }

    @EventHandler
    private static void onArtisanCropPlace(PlayerInteractEvent event) throws Exception {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (!ItemRegistry.getItemRegistryManager().isArtisanItem(event.getItem())) return;
        ArtisanItem artisanItem = ItemRegistry.getItemRegistryManager().getArtisanItem(event.getItem());
        if (artisanItem.getCropId() == null) return;
        if (event.getClickedBlock().getType() != Material.FARMLAND) return;
        if (event.getBlockFace() != BlockFace.UP) return;
        if (event.getClickedBlock().getRelative(BlockFace.UP).getType() != Material.AIR) return;
        event.setCancelled(true);
        place(event.getClickedBlock().getRelative(BlockFace.UP), new CurrentCropStageImpl(artisanItem.getCropId(), 1));
        event.getItem().setAmount(event.getItem().getAmount() - 1);
    }

    @EventHandler
    private static void onArtisanCropBreak(BlockBreakEvent event) {
        if (!ArtisanCropStorageImpl.getInstance().isArtisanCrop(event.getBlock())) return;
        if (event.isCancelled()) return;
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            ArtisanCropStorageImpl.getInstance().removeArtisanCrop(event.getBlock());
            return;
        }
        event.setCancelled(true);
        event.getBlock().setType(Material.AIR);
        CurrentCropStageImpl currentCropStage = (CurrentCropStageImpl) ArtisanCropStorageImpl.getInstance().getArtisanCropStage(event.getBlock());
        for (NamespacedKey drop : currentCropStage.getDrops()) {
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), ItemRegistry.getItemRegistryManager().getItemStack(drop));
        }
        ArtisanCropStorageImpl.getInstance().removeArtisanCrop(event.getBlock());
    }

    @EventHandler
    private static void onBlockBreakUnderCrop(BlockBreakEvent event) {
        if (!ArtisanCropStorageImpl.getInstance().isArtisanCrop(event.getBlock().getRelative(BlockFace.UP))) return;
        if (event.isCancelled()) return;
        event.setCancelled(true);
        event.getBlock().setType(Material.AIR);
        event.getBlock().getRelative(BlockFace.UP).setType(Material.AIR);
        CurrentCropStageImpl currentCropStage = (CurrentCropStageImpl) ArtisanCropStorageImpl.getInstance().getArtisanCropStage(event.getBlock().getRelative(BlockFace.UP));
        for (NamespacedKey registryId : currentCropStage.getDrops()) {
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getRelative(BlockFace.UP).getLocation(), ItemRegistry.getItemRegistryManager().getItemStack(registryId));
        }
        ArtisanCropStorageImpl.getInstance().removeArtisanCrop(event.getBlock().getRelative(BlockFace.UP));
    }

    @EventHandler
    private static void onWaterFlowOverCustomCrop(BlockBreakBlockEvent event) {
        if (!ArtisanCropStorageImpl.getInstance().isArtisanCrop(event.getBlock())) return;
        while (!event.getDrops().isEmpty()) event.getDrops().removeFirst();
        CurrentCropStageImpl currentCropStage = (CurrentCropStageImpl) ArtisanCropStorageImpl.getInstance().getArtisanCropStage(event.getBlock());
        for (NamespacedKey registryId : currentCropStage.getDrops()) {
            event.getDrops().add(ItemRegistry.getItemRegistryManager().getItemStack(registryId));
        }
        ArtisanCropStorageImpl.getInstance().removeArtisanCrop(event.getBlock());
    }

    @EventHandler
    private static void onEntityChangeFarmland(EntityChangeBlockEvent event) {
        if (!ArtisanCropStorageImpl.getInstance().isArtisanCrop(event.getBlock().getRelative(BlockFace.UP))) return;
        if (event.getBlock().getType() != Material.FARMLAND) return;
        if (event.isCancelled()) return;
        event.setCancelled(true);
        event.getBlock().getRelative(BlockFace.UP).setType(Material.AIR);
        event.getBlock().setType(Material.DIRT);
        CurrentCropStageImpl currentCropStage = (CurrentCropStageImpl) ArtisanCropStorageImpl.getInstance().getArtisanCropStage(event.getBlock().getRelative(BlockFace.UP));
        for (NamespacedKey registryId : currentCropStage.getDrops()) {
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getRelative(BlockFace.UP).getLocation(), ItemRegistry.getItemRegistryManager().getItemStack(registryId));
        }
        ArtisanCropStorageImpl.getInstance().removeArtisanCrop(event.getBlock().getRelative(BlockFace.UP));
    }

    @EventHandler
    private static void onCustomCropGrow(BlockGrowEvent event) {
        if (!ArtisanCropStorageImpl.getInstance().isArtisanCrop(event.getBlock())) return;
        if (event.isCancelled()) return;
        event.setCancelled(true);
        CurrentCropStageImpl currentCropStage = (CurrentCropStageImpl) ArtisanCropStorageImpl.getInstance().getArtisanCropStage(event.getBlock());
        if (currentCropStage.hasNextStage()) {
            grownCrop.put(event.getBlock(), currentCropStage);
            new BukkitRunnable() {
                @Override
                public void run() {
                    grownCrop.remove(event.getBlock());
                }
            }.runTaskLater(NeoArtisan.instance(), 0L);
            replace(event.getBlock(), currentCropStage.getNextStage());
        }
    }

    @EventHandler
    private static void onCustomCropFertilize(BlockFertilizeEvent event) {
        if (event.isCancelled()) return;
        for (BlockState blockState : event.getBlocks()) {
            if (ArtisanCropStorageImpl.getInstance().isArtisanCrop(blockState.getBlock())) {
                replace(blockState.getBlock(), grownCrop.get(blockState.getBlock()));
                grownCrop.remove(blockState.getBlock());
                event.setCancelled(true);
                CurrentCropStageImpl currentCropStage = (CurrentCropStageImpl) ArtisanCropStorageImpl.getInstance().getArtisanCropStage(blockState.getBlock());
                if (currentCropStage.hasNextStage()) {
                    replace(blockState.getBlock(), currentCropStage.getNextStage());
                    playBoneMealEffects(blockState.getLocation());
                }
            }
        }
    }

    private static void place(Block bukkitBlock, CurrentCropStageImpl currentCropStage) throws Exception {
        CraftWorld craftWorld = (CraftWorld) bukkitBlock.getWorld();
        Level nmsWorld = craftWorld.getHandle();
        BlockPos pos = new BlockPos(bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ());
        ArtisanCropStorageImpl.getInstance().placeArtisanCrop(nmsWorld, pos, currentCropStage);
        nmsWorld.setBlock(pos, stateById(currentCropStage.getBlockState()), 3);
    }

    private static void replace(Block bukkitBlock, CurrentCropStageImpl currentCropStage) {
        CraftWorld craftWorld = (CraftWorld) bukkitBlock.getWorld();
        Level nmsWorld = craftWorld.getHandle();
        BlockPos pos = new BlockPos(bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ());
        ArtisanCropStorageImpl.getInstance().replaceArtisanCrop(nmsWorld, pos, currentCropStage);
        nmsWorld.setBlock(pos, stateById(currentCropStage.getBlockState()), 3);
    }

    private static void playBoneMealEffects(Location loc) {
        World world = loc.getWorld();
        world.spawnParticle(
                Particle.HAPPY_VILLAGER,
                loc.clone().add(0.5, 0.5, 0.5),
                15, 0.3, 0.3, 0.3, 0.5
        );
        for (int i = 0; i < 5; i++) {
            world.spawnParticle(
                    Particle.HAPPY_VILLAGER,
                    loc.clone().add(
                            0.5 + ThreadLocalRandom.current().nextGaussian() * 0.3,
                            0.1,
                            0.5 + ThreadLocalRandom.current().nextGaussian() * 0.3
                    ),
                    2, 0, 0.1, 0, 0.2
            );
        }
        world.playSound(loc, Sound.ITEM_BONE_MEAL_USE, 1.0f, 1.2f);
    }
}
