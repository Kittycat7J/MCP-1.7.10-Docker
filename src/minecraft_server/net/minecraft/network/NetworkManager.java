package net.minecraft.network;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GenericFutureListener;
import java.net.SocketAddress;
import java.util.Queue;
import javax.crypto.SecretKey;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.CryptManager;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class NetworkManager extends SimpleChannelInboundHandler {
   private static final Logger logger = LogManager.getLogger();
   public static final Marker logMarkerNetwork = MarkerManager.getMarker("NETWORK");
   public static final Marker logMarkerPackets = MarkerManager.getMarker("NETWORK_PACKETS", logMarkerNetwork);
   public static final Marker field_152461_c = MarkerManager.getMarker("NETWORK_STAT", logMarkerNetwork);
   public static final AttributeKey attrKeyConnectionState = new AttributeKey("protocol");
   public static final AttributeKey attrKeyReceivable = new AttributeKey("receivable_packets");
   public static final AttributeKey attrKeySendable = new AttributeKey("sendable_packets");
   public static final NioEventLoopGroup eventLoops = new NioEventLoopGroup(0, (new ThreadFactoryBuilder()).setNameFormat("Netty Client IO #%d").setDaemon(true).build());
   public static final NetworkStatistics field_152462_h = new NetworkStatistics();
   private final boolean isClientSide;
   private final Queue receivedPacketsQueue = Queues.newConcurrentLinkedQueue();
   private final Queue outboundPacketsQueue = Queues.newConcurrentLinkedQueue();
   private Channel channel;
   private SocketAddress socketAddress;
   private INetHandler packetListener;
   private EnumConnectionState connectionState;
   private IChatComponent terminationReason;
   private boolean field_152463_r;
   private static final String __OBFID = "CL_00001240";

   public NetworkManager(boolean p_i45147_1_) {
      this.isClientSide = p_i45147_1_;
   }

   public void channelActive(ChannelHandlerContext p_channelActive_1_) throws Exception {
      super.channelActive(p_channelActive_1_);
      this.channel = p_channelActive_1_.channel();
      this.socketAddress = this.channel.remoteAddress();
      this.setConnectionState(EnumConnectionState.HANDSHAKING);
   }

   public void setConnectionState(EnumConnectionState p_150723_1_) {
      this.connectionState = (EnumConnectionState)this.channel.attr(attrKeyConnectionState).getAndSet(p_150723_1_);
      this.channel.attr(attrKeyReceivable).set(p_150723_1_.func_150757_a(this.isClientSide));
      this.channel.attr(attrKeySendable).set(p_150723_1_.func_150754_b(this.isClientSide));
      this.channel.config().setAutoRead(true);
      logger.debug("Enabled auto read");
   }

   public void channelInactive(ChannelHandlerContext p_channelInactive_1_) {
      this.closeChannel(new ChatComponentTranslation("disconnect.endOfStream", new Object[0]));
   }

   public void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_) {
      ChatComponentTranslation var3;
      if(p_exceptionCaught_2_ instanceof TimeoutException) {
         var3 = new ChatComponentTranslation("disconnect.timeout", new Object[0]);
      } else {
         var3 = new ChatComponentTranslation("disconnect.genericReason", new Object[]{"Internal Exception: " + p_exceptionCaught_2_});
      }

      this.closeChannel(var3);
   }

   protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, Packet p_channelRead0_2_) {
      if(this.channel.isOpen()) {
         if(p_channelRead0_2_.hasPriority()) {
            p_channelRead0_2_.func_148833_a(this.packetListener);
         } else {
            this.receivedPacketsQueue.add(p_channelRead0_2_);
         }
      }
   }

   public void setNetHandler(INetHandler p_150719_1_) {
      Validate.notNull(p_150719_1_, "packetListener", new Object[0]);
      logger.debug("Set listener of {} to {}", new Object[]{this, p_150719_1_});
      this.packetListener = p_150719_1_;
   }

   public void scheduleOutboundPacket(Packet p_150725_1_, GenericFutureListener ... p_150725_2_) {
      if(this.channel != null && this.channel.isOpen()) {
         this.flushOutboundQueue();
         this.dispatchPacket(p_150725_1_, p_150725_2_);
      } else {
         this.outboundPacketsQueue.add(new NetworkManager.InboundHandlerTuplePacketListener(p_150725_1_, p_150725_2_));
      }
   }

   private void dispatchPacket(final Packet p_150732_1_, final GenericFutureListener[] p_150732_2_) {
      final EnumConnectionState var3 = EnumConnectionState.func_150752_a(p_150732_1_);
      final EnumConnectionState var4 = (EnumConnectionState)this.channel.attr(attrKeyConnectionState).get();
      if(var4 != var3) {
         logger.debug("Disabled auto read");
         this.channel.config().setAutoRead(false);
      }

      if(this.channel.eventLoop().inEventLoop()) {
         if(var3 != var4) {
            this.setConnectionState(var3);
         }

         this.channel.writeAndFlush(p_150732_1_).addListeners(p_150732_2_).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
      } else {
         this.channel.eventLoop().execute(new Runnable() {
            private static final String __OBFID = "CL_00001241";

            public void run() {
               if(var3 != var4) {
                  NetworkManager.this.setConnectionState(var3);
               }

               NetworkManager.this.channel.writeAndFlush(p_150732_1_).addListeners(p_150732_2_).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
            }
         });
      }
   }

   private void flushOutboundQueue() {
      if(this.channel != null && this.channel.isOpen()) {
         while(!this.outboundPacketsQueue.isEmpty()) {
            NetworkManager.InboundHandlerTuplePacketListener var1 = (NetworkManager.InboundHandlerTuplePacketListener)this.outboundPacketsQueue.poll();
            this.dispatchPacket(var1.field_150774_a, var1.field_150773_b);
         }
      }
   }

   public void processReceivedPackets() {
      this.flushOutboundQueue();
      EnumConnectionState var1 = (EnumConnectionState)this.channel.attr(attrKeyConnectionState).get();
      if(this.connectionState != var1) {
         if(this.connectionState != null) {
            this.packetListener.onConnectionStateTransition(this.connectionState, var1);
         }

         this.connectionState = var1;
      }

      if(this.packetListener != null) {
         for(int var2 = 1000; !this.receivedPacketsQueue.isEmpty() && var2 >= 0; --var2) {
            Packet var3 = (Packet)this.receivedPacketsQueue.poll();
            var3.func_148833_a(this.packetListener);
         }

         this.packetListener.onNetworkTick();
      }

      this.channel.flush();
   }

   public SocketAddress getRemoteAddress() {
      return this.socketAddress;
   }

   public void closeChannel(IChatComponent p_150718_1_) {
      if(this.channel.isOpen()) {
         this.channel.close();
         this.terminationReason = p_150718_1_;
      }
   }

   public boolean isLocalChannel() {
      return this.channel instanceof LocalChannel || this.channel instanceof LocalServerChannel;
   }

   public void enableEncryption(SecretKey p_150727_1_) {
      this.channel.pipeline().addBefore("splitter", "decrypt", new NettyEncryptingDecoder(CryptManager.func_151229_a(2, p_150727_1_)));
      this.channel.pipeline().addBefore("prepender", "encrypt", new NettyEncryptingEncoder(CryptManager.func_151229_a(1, p_150727_1_)));
      this.field_152463_r = true;
   }

   public boolean isChannelOpen() {
      return this.channel != null && this.channel.isOpen();
   }

   public INetHandler getNetHandler() {
      return this.packetListener;
   }

   public IChatComponent getExitMessage() {
      return this.terminationReason;
   }

   public void disableAutoRead() {
      this.channel.config().setAutoRead(false);
   }

   protected void channelRead0(ChannelHandlerContext p_channelRead0_1_, Object p_channelRead0_2_) {
      this.channelRead0(p_channelRead0_1_, (Packet)p_channelRead0_2_);
   }

   static class InboundHandlerTuplePacketListener {
      private final Packet field_150774_a;
      private final GenericFutureListener[] field_150773_b;
      private static final String __OBFID = "CL_00001244";

      public InboundHandlerTuplePacketListener(Packet p_i45146_1_, GenericFutureListener ... p_i45146_2_) {
         this.field_150774_a = p_i45146_1_;
         this.field_150773_b = p_i45146_2_;
      }
   }
}
