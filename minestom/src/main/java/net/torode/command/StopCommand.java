package net.torode.command;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.torode.globals.Messages;
import net.torode.player.CorePlayer;

import static net.torode.player.permission.CorePermissions.COMMAND_STOP;

public final class StopCommand extends Command
{
    public StopCommand()
    {
        super("stop");

        setDefaultExecutor(StopCommand::stop);
    }

    private static void stop(CommandSender sender, CommandContext context)
    {
        if (sender instanceof ConsoleSender || ((CorePlayer) sender).hasPermission(COMMAND_STOP))
        {
            MinecraftServer.getSchedulerManager().scheduleNextTick(MinecraftServer::stopCleanly);
        } else
        {
            sender.sendMessage(Messages.COMMAND_NO_PERMISSION);
        }
    }

    // todo /stop (delay)
}
