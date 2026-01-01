package net.minecraft.world.biome;

import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.feature.WorldGenSpikes;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BiomeEndDecorator extends BiomeDecorator {
    private static final Logger logger = LogManager.getLogger();
   protected WorldGenerator spikeGen;
   private static final String __OBFID = "CL_00000188";

   public BiomeEndDecorator() {
      this.spikeGen = new WorldGenSpikes(Blocks.end_stone);
   }

   protected void func_150513_a(BiomeGenBase p_150513_1_) {
      this.generateOres();
      randomGenerator.nextInt(5);
      if(true) {
         int var2 = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
         int var3 = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
         logger.info("custom logger BiomeEndDecorator: x:{}; y:{}",var2,var3);
         var2 = this.chunk_X + 24;
         var3 = this.chunk_Z + 15;
         int var4 = this.currentWorld.getTopSolidOrLiquidBlock(var2, var3);
         this.spikeGen.generate(this.currentWorld, this.randomGenerator, var2, var4, var3);
      }

      if(this.chunk_X == 0 && this.chunk_Z == 0) {
         EntityDragon var5 = new EntityDragon(this.currentWorld);
         var5.setLocationAndAngles(0.0D, 128.0D, 0.0D, this.randomGenerator.nextFloat() * 360.0F, 0.0F);
         this.currentWorld.spawnEntityInWorld(var5);
      }
   }
}
