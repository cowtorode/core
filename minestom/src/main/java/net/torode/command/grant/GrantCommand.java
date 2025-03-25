package net.torode.command.grant;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.torode.globals.Messages;
import net.torode.player.CorePlayer;
import net.torode.player.permission.CorePermissions;

public final class GrantCommand extends Command
{
    public GrantCommand()
    {
        super("grant");

        // /grant - open menu (for players)
        setDefaultExecutor(GrantCommand::grant);

        // /grant rank [target] [rank] (duration) (server)
        // /confirm
        addSubcommand(new GrantRankCommand());
        // /grant permission [target] [permission] (duration) (server)
        // /confirm
        addSubcommand(new GrantPermissionCommand());
    }

    private static void grant(CommandSender sender, CommandContext context)
    {
        if (sender instanceof ConsoleSender)
        {
            sender.sendMessage(Messages.COMMAND_GRANT_USAGE);
            return;
        }

        if (!((CorePlayer) sender).hasPermission(CorePermissions.COMMAND_GRANT))
        {
            sender.sendMessage(Messages.COMMAND_NO_PERMISSION);
            return;
        }

        sender.sendMessage("[open menu]");
    }
}
