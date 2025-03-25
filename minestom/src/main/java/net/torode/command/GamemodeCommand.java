package net.torode.command;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.torode.globals.Messages;
import net.torode.player.CorePlayer;
import net.torode.player.permission.CorePermissions;

public final class GamemodeCommand extends Command
{
    private static final Argument<String> TARGET = ArgumentType.String("target");

    public GamemodeCommand()
    {
        super("gamemode", "gm");

        setDefaultExecutor(GamemodeCommand::gamemode);
        addSyntax(GamemodeCommand::gamemodeTarget, TARGET);
    }

    // /gamemode
    private static void gamemode(CommandSender sender, CommandContext context)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage(Messages.COMMAND_GAMEMODE_USAGE);
            return;
        }

        if (!((CorePlayer) sender).hasPermission(CorePermissions.COMMAND_GAMEMODE))
        {
            sender.sendMessage(Messages.COMMAND_NO_PERMISSION);
            return;
        }

        if (((CorePlayer) sender).getGameMode() == GameMode.CREATIVE)
        {
            ((CorePlayer) sender).setGameMode(GameMode.SURVIVAL);
        } else
        {
            ((CorePlayer) sender).setGameMode(GameMode.CREATIVE);
        }
    }

    // /gamemode [target]
    private static void gamemodeTarget(CommandSender sender, CommandContext context)
    {
        if (!((CorePlayer) sender).hasPermission(CorePermissions.COMMAND_GAMEMODE_TARGET))
        {
            sender.sendMessage(Messages.COMMAND_NO_PERMISSION);
            return;
        }

        Player target = MinecraftServer.getConnectionManager().getOnlinePlayerByUsername(context.get(TARGET));

        if (target == null)
        {
            sender.sendMessage(Messages.COMMAND_INVALID_TARGET);
            return;
        }

        if (target.getGameMode() == GameMode.CREATIVE)
        {
            target.setGameMode(GameMode.SURVIVAL);
        } else
        {
            target.setGameMode(GameMode.CREATIVE);
        }
    }
}
