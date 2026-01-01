package net.minecraft.block.material;

public class Material {
   public static final Material air = new MaterialTransparent(MapColor.field_151660_b);
   public static final Material field_151577_b = new Material(MapColor.field_151661_c);
   public static final Material ground = new Material(MapColor.field_151664_l);
   public static final Material wood = (new Material(MapColor.field_151663_o)).setBurning();
   public static final Material rock = (new Material(MapColor.field_151665_m)).setRequiresTool();
   public static final Material iron = (new Material(MapColor.field_151668_h)).setRequiresTool();
   public static final Material field_151574_g = (new Material(MapColor.field_151668_h)).setRequiresTool().setImmovableMobility();
   public static final Material field_151586_h = (new MaterialLiquid(MapColor.field_151662_n)).setNoPushMobility();
   public static final Material field_151587_i = (new MaterialLiquid(MapColor.field_151656_f)).setNoPushMobility();
   public static final Material field_151584_j = (new Material(MapColor.field_151669_i)).setBurning().setTranslucent().setNoPushMobility();
   public static final Material plants = (new MaterialLogic(MapColor.field_151669_i)).setNoPushMobility();
   public static final Material field_151582_l = (new MaterialLogic(MapColor.field_151669_i)).setBurning().setNoPushMobility().setReplaceable();
   public static final Material field_151583_m = new Material(MapColor.field_151659_e);
   public static final Material cloth = (new Material(MapColor.field_151659_e)).setBurning();
   public static final Material field_151581_o = (new MaterialTransparent(MapColor.field_151660_b)).setNoPushMobility();
   public static final Material field_151595_p = new Material(MapColor.field_151658_d);
   public static final Material circuits = (new MaterialLogic(MapColor.field_151660_b)).setNoPushMobility();
   public static final Material field_151593_r = (new MaterialLogic(MapColor.field_151659_e)).setBurning();
   public static final Material field_151592_s = (new Material(MapColor.field_151660_b)).setTranslucent().setAdventureModeExempt();
   public static final Material redstoneLight = (new Material(MapColor.field_151660_b)).setAdventureModeExempt();
   public static final Material field_151590_u = (new Material(MapColor.field_151656_f)).setBurning().setTranslucent();
   public static final Material coral = (new Material(MapColor.field_151669_i)).setNoPushMobility();
   public static final Material field_151588_w = (new Material(MapColor.field_151657_g)).setTranslucent().setAdventureModeExempt();
   public static final Material field_151598_x = (new Material(MapColor.field_151657_g)).setAdventureModeExempt();
   public static final Material field_151597_y = (new MaterialLogic(MapColor.field_151666_j)).setReplaceable().setTranslucent().setRequiresTool().setNoPushMobility();
   public static final Material craftedSnow = (new Material(MapColor.field_151666_j)).setRequiresTool();
   public static final Material field_151570_A = (new Material(MapColor.field_151669_i)).setTranslucent().setNoPushMobility();
   public static final Material field_151571_B = new Material(MapColor.field_151667_k);
   public static final Material field_151572_C = (new Material(MapColor.field_151669_i)).setNoPushMobility();
   public static final Material dragonEgg = (new Material(MapColor.field_151669_i)).setNoPushMobility();
   public static final Material field_151567_E = (new MaterialPortal(MapColor.field_151660_b)).setImmovableMobility();
   public static final Material field_151568_F = (new Material(MapColor.field_151660_b)).setNoPushMobility();
   public static final Material field_151569_G = (new Material(MapColor.field_151659_e) {
      private static final String __OBFID = "CL_00000543";

      public boolean blocksMovement() {
         return false;
      }
   }).setRequiresTool().setNoPushMobility();
   public static final Material piston = (new Material(MapColor.field_151665_m)).setImmovableMobility();
   private boolean canBurn;
   private boolean replaceable;
   private boolean isTranslucent;
   private final MapColor materialMapColor;
   private boolean requiresNoTool = true;
   private int mobilityFlag;
   private boolean isAdventureModeExempt;
   private static final String __OBFID = "CL_00000542";

   public Material(MapColor p_i2116_1_) {
      this.materialMapColor = p_i2116_1_;
   }

   public boolean isLiquid() {
      return false;
   }

   public boolean isSolid() {
      return true;
   }

   public boolean getCanBlockGrass() {
      return true;
   }

   public boolean blocksMovement() {
      return true;
   }

   private Material setTranslucent() {
      this.isTranslucent = true;
      return this;
   }

   protected Material setRequiresTool() {
      this.requiresNoTool = false;
      return this;
   }

   protected Material setBurning() {
      this.canBurn = true;
      return this;
   }

   public boolean getCanBurn() {
      return this.canBurn;
   }

   public Material setReplaceable() {
      this.replaceable = true;
      return this;
   }

   public boolean isReplaceable() {
      return this.replaceable;
   }

   public boolean isOpaque() {
      return this.isTranslucent?false:this.blocksMovement();
   }

   public boolean isToolNotRequired() {
      return this.requiresNoTool;
   }

   public int getMaterialMobility() {
      return this.mobilityFlag;
   }

   protected Material setNoPushMobility() {
      this.mobilityFlag = 1;
      return this;
   }

   protected Material setImmovableMobility() {
      this.mobilityFlag = 2;
      return this;
   }

   protected Material setAdventureModeExempt() {
      this.isAdventureModeExempt = true;
      return this;
   }

   public boolean isAdventureModeExempt() {
      return this.isAdventureModeExempt;
   }

   public MapColor getMaterialMapColor() {
      return this.materialMapColor;
   }
}
