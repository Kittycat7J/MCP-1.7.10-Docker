package net.minecraft.command;

import net.minecraft.block.Block;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;

public class SpawnerBlockCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "spawnerblock";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/spawnerblock <block> [meta|nbt]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length < 1) {
            throw new WrongUsageException(getCommandUsage(sender));
        }

        Block block = Block.getBlockFromName(args[0]);
        if (block == null) {
            throw new CommandException("Unknown block: " + args[0]);
        }

        int meta = 0;
        NBTTagCompound nbt = null;

        if (args.length >= 2) {
            try {
                // Try meta first
                meta = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                // Otherwise parse NBT
                try {
                    nbt = (NBTTagCompound) JsonToNBT.func_150315_a(args[1]);
                } catch (Exception ex) {
                    throw new CommandException("Invalid meta or NBT");
                }
            }
        }

        SpawnerBlockState.block = block;
        SpawnerBlockState.meta = meta;
        SpawnerBlockState.nbt = nbt;

        sender.addChatMessage(new ChatComponentText(
                "Dungeon spawner block set to " + args[0]
        ));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
