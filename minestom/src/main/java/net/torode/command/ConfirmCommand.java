package net.torode.command;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.CommandExecutor;
import net.minestom.server.entity.Player;
import net.torode.globals.Messages;
import net.torode.player.CorePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ConfirmCommand extends Command
{
    private static final Map<UUID, CommandExecutor> callbacks = new HashMap<>();

    public ConfirmCommand()
    {
        super("confirm");

        setDefaultExecutor(ConfirmCommand::confirm);
    }

    public static void register(Player player, CommandExecutor executor)
    {
        callbacks.put(player.getUuid(), executor);
    }

    public static void confirm(CommandSender sender, CommandContext context)
    {
        if (sender instanceof ConsoleSender)
        {
            sender.sendMessage(Messages.CONSOLE_BLOCKED);
            return;
        }

        CommandExecutor executor = callbacks.remove(((Player) sender).getUuid());

        if (executor == null)
        {
            sender.sendMessage(Messages.COMMAND_CONFIRM_FAILED);
            return;
        }

        executor.apply(sender, context);
    }
}
