package net.torode.command.grant;

import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;

public final class GrantPermissionCommand extends Command
{
    private static final Argument<String> TARGET = ArgumentType.String("target");
    private static final Argument<String> PERMISSION = ArgumentType.String("permission node");
    private static final Argument<String> DURATION = ArgumentType.String("duration");
    private static final Argument<String> SERVER = ArgumentType.String("server");

    public GrantPermissionCommand()
    {
        super("permission", "perm", "p");

        addSyntax(GrantPermissionCommand::grant, TARGET, PERMISSION);
        addSyntax(GrantPermissionCommand::grantDurationServer, TARGET, PERMISSION, DURATION, SERVER);
    }

    private static void grant(CommandSender sender, CommandContext context)
    {
        sender.sendMessage("/grant p target permission");
    }

    private static void grantDurationServer(CommandSender sender, CommandContext context)
    {
        sender.sendMessage("/grant p target permission duration server");
    }
}
