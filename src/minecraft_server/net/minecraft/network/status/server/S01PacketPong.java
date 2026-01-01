package net.minecraft.network.status.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusClient;

public class S01PacketPong extends Packet {
   private long field_149293_a;
   private static final String __OBFID = "CL_00001383";

   public S01PacketPong() {}

   public S01PacketPong(long p_i45272_1_) {
      this.field_149293_a = p_i45272_1_;
   }

   public void readPacketData(PacketBuffer p_148837_1_) throws IOException {
      this.field_149293_a = p_148837_1_.readLong();
   }

   public void writePacketData(PacketBuffer p_148840_1_) throws IOException {
      p_148840_1_.writeLong(this.field_149293_a);
   }

   public void func_148833_a(INetHandlerStatusClient p_148833_1_) {
      p_148833_1_.handlePong(this);
   }

   public boolean hasPriority() {
      return true;
   }

   public void func_148833_a(INetHandler p_148833_1_) {
      this.func_148833_a((INetHandlerStatusClient)p_148833_1_);
   }
}
