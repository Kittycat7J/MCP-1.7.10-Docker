package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;

public class ItemCloth extends ItemBlock {
   private static final String __OBFID = "CL_00000075";

   public ItemCloth(Block p_i45358_1_) {
      super(p_i45358_1_);
      this.setMaxDamage(0);
      this.setHasSubtypes(true);
   }

   public int getMetadata(int p_77647_1_) {
      return p_77647_1_;
   }

   public String getUnlocalizedName(ItemStack p_77667_1_) {
      return super.getUnlocalizedName() + "." + ItemDye.field_150923_a[BlockColored.func_150032_b(p_77667_1_.getItemDamage())];
   }
}
