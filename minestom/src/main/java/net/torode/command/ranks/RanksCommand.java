package net.torode.command.ranks;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.torode.rank.RankCache;
import net.torode.globals.Messages;
import net.torode.player.CorePlayer;

import static net.torode.player.permission.CorePermissions.COMMAND_RANKCACHE;

/**
 * Command used to manage the rank cache
 */
public final class RanksCommand extends Command
{
    public RanksCommand()
    {
        super("rankcache", "ranks");

        addSubcommand(new RanksDeleteCommand());
        setDefaultExecutor(RanksCommand::ranks);
    }

    private static void ranks(CommandSender sender, CommandContext context)
    {
        if (sender instanceof ConsoleSender)
        {
            sender.sendMessage(Messages.COMMAND_RANKS_USAGE);
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
