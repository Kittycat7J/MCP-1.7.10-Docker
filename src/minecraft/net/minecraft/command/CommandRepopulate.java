package net.minecraft.command;

import java.util.List;
import java.util.Collections;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandRepopulate extends CommandBase {

    @Override
    public String getCommandName() {
        return "repopulate";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/repopulate [x z] [now]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2; // OP only
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        World world = sender.getEntityWorld();
        int chunkX;
        int chunkZ;

        // Default: sender position

            if (sender instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) sender;
                chunkX = (int) player.posX >> 4;
                chunkZ = (int) player.posZ >> 4;
            } else {
                chunkX = 0;
                chunkZ = 0;
            }


        boolean now = false;

        // Parse arguments
        if (args.length >= 2) {
            chunkX = parseIntBounded(sender, args[0], -30000000 >> 4, 30000000 >> 4);
            chunkZ = parseIntBounded(sender, args[1], -30000000 >> 4, 30000000 >> 4);
        }

        if (args.length == 3 || args.length == 1) {
            String mode = args[args.length - 1];
            if ("now".equalsIgnoreCase(mode)) {
                now = true;
            }
        }

        if (now) {
            if (!isAreaLoaded(world, chunkX, chunkZ)) {
                sender.addChatMessage(new ChatComponentText("Warning: Area not loaded for repopulation!"));
            }
            repopulate(world, chunkX, chunkZ);
            sender.addChatMessage(new ChatComponentText("Repopulated chunk " + chunkX + ", " + chunkZ));
        } else {
            world.getChunkFromChunkCoords(chunkX, chunkZ).isTerrainPopulated = false;

            sender.addChatMessage(new ChatComponentText("Marked chunk " + chunkX + ", " + chunkZ + " for repopulation"));
        }
    }

    private void repopulate(World world, int x, int z) {
        ChunkProviderServer provider = (ChunkProviderServer) world.getChunkProvider();
        Chunk chunk = world.getChunkFromChunkCoords(x, z);
        chunk.isTerrainPopulated = false;
        provider.populate(provider, x, z);
    }

    private boolean isAreaLoaded(World world, int x, int z) {
        return world.getChunkProvider().chunkExists(x, z)
                && world.getChunkProvider().chunkExists(x + 1, z)
                && world.getChunkProvider().chunkExists(x, z + 1)
                && world.getChunkProvider().chunkExists(x + 1, z + 1);
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        return Collections.emptyList(); // MCP 1.7.10 doesn't have easy username completion
    }

}
