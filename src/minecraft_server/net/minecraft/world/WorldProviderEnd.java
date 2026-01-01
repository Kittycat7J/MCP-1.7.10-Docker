package net.minecraft.world;

import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderEnd;

public class WorldProviderEnd extends WorldProvider {
   private static final String __OBFID = "CL_00000389";

   public void registerWorldChunkManager() {
      this.worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.sky, 0.0F);
      this.dimensionId = 1;
      this.hasNoSky = true;
   }

   public IChunkProvider createChunkGenerator() {
      return new ChunkProviderEnd(this.worldObj, this.worldObj.getSeed());
   }

   public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_) {
      return 0.0F;
   }

   public boolean canRespawnHere() {
      return false;
   }

   public boolean isSurfaceWorld() {
      return false;
   }

   public boolean canCoordinateBeSpawn(int p_76566_1_, int p_76566_2_) {
      return this.worldObj.getTopBlock(p_76566_1_, p_76566_2_).getMaterial().blocksMovement();
   }

   public ChunkCoordinates getEntrancePortalLocation() {
      return new ChunkCoordinates(100, 50, 0);
   }

   public int getAverageGroundLevel() {
      return 50;
   }

   public String getDimensionName() {
      return "The End";
   }
}
