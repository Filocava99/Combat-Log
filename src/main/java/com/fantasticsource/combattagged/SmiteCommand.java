package com.fantasticsource.combattagged;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class SmiteCommand extends CommandBase
{
    /**
     * Gets the name of the command
     */
    public String getName()
    {
        return "smite";
    }

    /**
     * Return the required permission level for this command.
     */
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    /**
     * Gets the usage string for the command.
     */
    public String getUsage(ICommandSender sender)
    {
        return "/smite <playername>";
    }

    /**
     * Callback for when the command is executed
     */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args)
    {
        if (args.length == 0) sender.getCommandSenderEntity().sendMessage(new TextComponentString(getUsage(sender)));
        else try
        {
            EntityPlayer player = server.getPlayerList().getPlayerByUsername(args[0]);
            if (CombatTagged.smite(player)) notifyCommandListener(sender, this, "Smote " + args[0]);
            else sender.getCommandSenderEntity().sendMessage(new TextComponentString(args[0] + " is immune to smite"));
        }
        catch (Exception e)
        {
            sender.getCommandSenderEntity().sendMessage(new TextComponentString("Player not found: " + args[0]));
        }
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     */
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }

    /**
     * Get a list of options for when the user presses the TAB key
     */
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
    }
}