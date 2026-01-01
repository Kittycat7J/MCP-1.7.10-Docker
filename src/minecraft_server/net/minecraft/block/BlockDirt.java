package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockDirt extends Block {
   public static final String[] field_150009_a = new String[]{"default", "default", "podzol"};
   private static final String __OBFID = "CL_00000228";

   protected BlockDirt() {
      super(Material.ground);
      this.setcreativeTab(CreativeTabs.tabBlock);
   }

   public int damageDropped(int p_149692_1_) {
      return 0;
   }

   protected ItemStack createStackedBlock(int p_149644_1_) {
      if(p_149644_1_ == 1) {
         p_149644_1_ = 0;
      }

      return super.createStackedBlock(p_149644_1_);
   }

   public int getDamageValue(World p_149643_1_, int p_149643_2_, int p_149643_3_, int p_149643_4_) {
      int var5 = p_149643_1_.getBlockMetadata(p_149643_2_, p_149643_3_, p_149643_4_);
      if(var5 == 1) {
         var5 = 0;
      }

      return var5;
   }
}
