package net.minecraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;

public class ItemEditableBook extends Item {
   private static final String __OBFID = "CL_00000077";

   public ItemEditableBook() {
      this.setMaxStackSize(1);
   }

   public static boolean validBookTagContents(NBTTagCompound p_77828_0_) {
      if(!ItemWritableBook.func_150930_a(p_77828_0_)) {
         return false;
      } else if(!p_77828_0_.func_150297_b("title", 8)) {
         return false;
      } else {
         String var1 = p_77828_0_.getString("title");
         return var1 != null && var1.length() <= 16?p_77828_0_.func_150297_b("author", 8):false;
      }
   }

   public String getItemStackDisplayName(ItemStack p_77653_1_) {
      if(p_77653_1_.hasTagCompound()) {
         NBTTagCompound var2 = p_77653_1_.getTagCompound();
         String var3 = var2.getString("title");
         if(!StringUtils.isNullOrEmpty(var3)) {
            return var3;
         }
      }

      return super.getItemStackDisplayName(p_77653_1_);
   }

   public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
      p_77659_3_.displayGUIBook(p_77659_1_);
      return p_77659_1_;
   }

   public boolean getShareTag() {
      return true;
   }
}
