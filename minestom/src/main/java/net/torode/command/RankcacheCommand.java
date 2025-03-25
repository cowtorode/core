package net.torode.command;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.torode.rank.RankCache;
import net.torode.globals.Messages;
import net.torode.player.CorePlayer;

import static net.torode.player.permission.CorePermissions.COMMAND_RANKCACHE;

public final class RankcacheCommand extends Command
{
    public RankcacheCommand()
    {
        super("rankcache", "ranks");

        setDefaultExecutor(RankcacheCommand::rankcache);
    }

    private static void rankcache(CommandSender sender, CommandContext context)
    {
        if (sender instanceof ConsoleSender)
        {
            // usage

            return;
        }

        if (!((CorePlayer) sender).hasPermission(COMMAND_RANKCACHE))
        {
            sender.sendMessage(Messages.COMMAND_NO_PERMISSION);
            return;
        }

        ((CorePlayer) sender).openInventory(RankCache.getRanksMenu());
    }
}
