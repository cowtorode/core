package net.torode.command.ranks;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.torode.globals.Messages;
import net.torode.player.CorePlayer;

import static net.torode.player.permission.CorePermissions.COMMAND_RANKS_DELETE;

public final class RanksDeleteCommand extends Command
{
    private static final Argument<String> RANK = ArgumentType.String("rank");

    public RanksDeleteCommand()
    {
        super("delete");
        setDefaultExecutor(RanksDeleteCommand::usage);
        addSyntax(RanksDeleteCommand::delete, RANK);
    }

    private static boolean disallow(CommandSender sender)
    {
        return sender instanceof Player && !((CorePlayer) sender).hasPermission(COMMAND_RANKS_DELETE);
    }

    private static void usage(CommandSender sender, CommandContext context)
    {
        if (disallow(sender))
        {
            sender.sendMessage(Messages.COMMAND_NO_PERMISSION);
            return;
        }
        // allow
        sender.sendMessage(Messages.COMMAND_RANKS_DELETE_USAGE);
    }

    // /ranks delete admin
    private static void delete(CommandSender sender, CommandContext context)
    {
        if (disallow(sender))
        {
            sender.sendMessage(Messages.COMMAND_NO_PERMISSION);
            return;
        }
        // allow
        sender.sendMessage("[/confirm]");
    }
}
