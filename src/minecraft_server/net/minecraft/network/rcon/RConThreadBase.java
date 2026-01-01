package net.minecraft.network.rcon;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class RConThreadBase implements Runnable {
   private static final AtomicInteger field_164004_h = new AtomicInteger(0);
   protected boolean running;
   protected IServer server;
   protected final String field_164003_c;
   protected Thread rconThread;
   protected int field_72615_d = 5;
   protected List socketList = new ArrayList();
   protected List serverSocketList = new ArrayList();
   private static final String __OBFID = "CL_00001801";

   protected RConThreadBase(IServer p_i45300_1_, String p_i45300_2_) {
      this.server = p_i45300_1_;
      this.field_164003_c = p_i45300_2_;
      if(this.server.isDebuggingEnabled()) {
         this.logWarning("Debugging is enabled, performance maybe reduced!");
      }
   }

   public synchronized void startThread() {
      this.rconThread = new Thread(this, this.field_164003_c + " #" + field_164004_h.incrementAndGet());
      this.rconThread.start();
      this.running = true;
   }

   public boolean isRunning() {
      return this.running;
   }

   protected void logDebug(String p_72607_1_) {
      this.server.logDebug(p_72607_1_);
   }

   protected void logInfo(String p_72609_1_) {
      this.server.logInfo(p_72609_1_);
   }

   protected void logWarning(String p_72606_1_) {
      this.server.logWarning(p_72606_1_);
   }

   protected void logSevere(String p_72610_1_) {
      this.server.logSevere(p_72610_1_);
   }

   protected int getNumberOfPlayers() {
      return this.server.getCurrentPlayerCount();
   }

   protected void registerSocket(DatagramSocket p_72601_1_) {
      this.logDebug("registerSocket: " + p_72601_1_);
      this.socketList.add(p_72601_1_);
   }

   protected boolean closeSocket(DatagramSocket p_72604_1_, boolean p_72604_2_) {
      this.logDebug("closeSocket: " + p_72604_1_);
      if(null == p_72604_1_) {
         return false;
      } else {
         boolean var3 = false;
         if(!p_72604_1_.isClosed()) {
            p_72604_1_.close();
            var3 = true;
         }

         if(p_72604_2_) {
            this.socketList.remove(p_72604_1_);
         }

         return var3;
      }
   }

   protected boolean closeServerSocket(ServerSocket p_72608_1_) {
      return this.closeServerSocket_do(p_72608_1_, true);
   }

   protected boolean closeServerSocket_do(ServerSocket p_72605_1_, boolean p_72605_2_) {
      this.logDebug("closeSocket: " + p_72605_1_);
      if(null == p_72605_1_) {
         return false;
      } else {
         boolean var3 = false;

         try {
            if(!p_72605_1_.isClosed()) {
               p_72605_1_.close();
               var3 = true;
            }
         } catch (IOException var5) {
            this.logWarning("IO: " + var5.getMessage());
         }

         if(p_72605_2_) {
            this.serverSocketList.remove(p_72605_1_);
         }

         return var3;
      }
   }

   protected void closeAllSockets() {
      this.closeAllSockets_do(false);
   }

   protected void closeAllSockets_do(boolean p_72612_1_) {
      int var2 = 0;
      Iterator var3 = this.socketList.iterator();

      while(var3.hasNext()) {
         DatagramSocket var4 = (DatagramSocket)var3.next();
         if(this.closeSocket(var4, false)) {
            ++var2;
         }
      }

      this.socketList.clear();
      var3 = this.serverSocketList.iterator();

      while(var3.hasNext()) {
         ServerSocket var5 = (ServerSocket)var3.next();
         if(this.closeServerSocket_do(var5, false)) {
            ++var2;
         }
      }

      this.serverSocketList.clear();
      if(p_72612_1_ && 0 < var2) {
         this.logWarning("Force closed " + var2 + " sockets");
      }
   }
}
