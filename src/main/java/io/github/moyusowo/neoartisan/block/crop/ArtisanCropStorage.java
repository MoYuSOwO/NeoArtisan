package io.github.moyusowo.neoartisan.block.crop;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class ArtisanCropStorage {

    private ArtisanCropStorage() {}

    private static final Map<Level, Map<ChunkPos, Map<BlockPos, CurrentCropStage>>> storage = new HashMap<>();

    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static void replaceArtisanCrop(Level level, BlockPos blockPos, CurrentCropStage block) {
        ChunkPos chunkPos = new ChunkPos(blockPos);
        lock.writeLock().lock();
        try {
            storage.get(level).get(chunkPos).replace(blockPos, block);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static void placeArtisanCrop(Level level, BlockPos blockPos, CurrentCropStage block) {
        ChunkPos chunkPos = new ChunkPos(blockPos);
        lock.writeLock().lock();
        try {
            storage.computeIfAbsent(level, k -> new HashMap<>()).computeIfAbsent(chunkPos, k -> new HashMap<>()).put(blockPos, block);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static void removeArtisanCrop(Level level, BlockPos blockPos) {
        ChunkPos chunkPos = new ChunkPos(blockPos);
        lock.writeLock().lock();
        try {
            storage.get(level).get(chunkPos).remove(blockPos);
            if (storage.get(level).get(chunkPos).isEmpty()) {
                storage.get(level).remove(chunkPos);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static void removeArtisanCrop(Level level, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        removeArtisanCrop(level, blockPos);
    }

    public static void removeArtisanCrop(Block block) {
        CraftWorld world = (CraftWorld) block.getWorld();
        removeArtisanCrop(world.getHandle(), block.getX(), block.getY(), block.getZ());
    }

    public static @NotNull CurrentCropStage getArtisanCropStage(Level level, BlockPos blockPos) {
        ChunkPos chunkPos = new ChunkPos(blockPos);
        lock.readLock().lock();
        try {
            CurrentCropStage currentCropStage = storage.get(level).get(chunkPos).get(blockPos);
            if (currentCropStage == null) throw new IllegalArgumentException("Please use is method to check first!");
            else return currentCropStage;
        } finally {
            lock.readLock().unlock();
        }
    }

    public static CurrentCropStage getArtisanCropStage(Level level, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        return getArtisanCropStage(level, blockPos);
    }

    public static CurrentCropStage getArtisanCropStage(Block block) {
        CraftWorld world = (CraftWorld) block.getWorld();
        return getArtisanCropStage(world.getHandle(), block.getX(), block.getY(), block.getZ());
    }

    public static Map<BlockPos, CurrentCropStage> getChunkArtisanCrops(Level level, ChunkPos chunkPos) {
        lock.readLock().lock();
        try {
            Map<ChunkPos, Map<BlockPos, CurrentCropStage>> levelMap = storage.getOrDefault(level, null);
            if (levelMap == null) return Collections.emptyMap();
            Map<BlockPos, CurrentCropStage> chunkMap = levelMap.getOrDefault(chunkPos, null);
            return chunkMap != null ? Map.copyOf(chunkMap) : Collections.emptyMap();
        } finally {
            lock.readLock().unlock();
        }
    }

    public static Map<BlockPos, CurrentCropStage> getChunkArtisanCrops(Level level, int chunkX, int chunkZ) {
        ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
        return getChunkArtisanCrops(level, chunkPos);
    }

    public static Map<ChunkPos, Map<BlockPos, CurrentCropStage>> getLevelArtisanCrops(Level level) {
        lock.readLock().lock();
        try {
            Map<ChunkPos, Map<BlockPos, CurrentCropStage>> levelMap = storage.getOrDefault(level, null);
            return levelMap != null ? Map.copyOf(levelMap) : Collections.emptyMap();
        } finally {
            lock.readLock().unlock();
        }
    }

    public static boolean isArtisanCrop(Level level, BlockPos blockPos) {
        ChunkPos chunkPos = new ChunkPos(blockPos);
        lock.readLock().lock();
        try {
            return storage.containsKey(level) && storage.get(level).containsKey(chunkPos) && storage.get(level).get(chunkPos).containsKey(blockPos);
        } finally {
            lock.readLock().unlock();
        }
    }

    public static boolean isArtisanCrop(Level level, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        return isArtisanCrop(level, blockPos);
    }

    public static boolean isArtisanCrop(Block block) {
        CraftWorld world = (CraftWorld) block.getWorld();
        return isArtisanCrop(world.getHandle(), block.getX(), block.getY(), block.getZ());
    }

    public static boolean hasArtisanCropInChunk(Level level, ChunkPos chunkPos) {
        lock.readLock().lock();
        try {
            return storage.containsKey(level) && storage.get(level).containsKey(chunkPos);
        } finally {
            lock.readLock().unlock();
        }
    }

    public static boolean hasArtisanCropInChunk(Level level, int chunkX, int chunkZ) {
        ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
        return hasArtisanCropInChunk(level, chunkPos);
    }

    public static boolean hasArtisanCropInLevel(Level level) {
        lock.readLock().lock();
        try {
            return storage.containsKey(level);
        } finally {
            lock.readLock().unlock();
        }
    }
}
