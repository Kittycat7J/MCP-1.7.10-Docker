package net.minecraft.command;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;

public class SpawnerBlockState {

    /** Block to place instead of mob spawner */
    public static Block block = Blocks.mob_spawner;

    /** Metadata to use */
    public static int meta = 0;

    /** Optional NBT to apply to tile entity */
    public static NBTTagCompound nbt = null;

    public static void reset() {
        block = Blocks.mob_spawner;
        meta = 0;
        nbt = null;
    }
}
