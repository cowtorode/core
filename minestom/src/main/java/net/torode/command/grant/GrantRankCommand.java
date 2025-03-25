package net.torode.command.grant;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.torode.rank.Rank;
import net.torode.rank.RankCache;
import net.torode.globals.Messages;
import net.torode.player.CorePlayer;

import static net.kyori.adventure.text.format.NamedTextColor.WHITE;

public final class GrantRankCommand extends Command
{
    private static final Argument<String> TARGET = ArgumentType.String("target");
    private static final Argument<String> RANK = ArgumentType.String("rank | rank id");
    private static final Argument<String> DURATION = ArgumentType.String("duration");
    private static final Argument<String> SERVER = ArgumentType.String("server");

    public GrantRankCommand()
    {
        super("rank", "r");

        addSyntax(GrantRankCommand::grant, TARGET, RANK);
        addSyntax(GrantRankCommand::grantDurationServer, TARGET, RANK, DURATION, SERVER);
    }

    private static void grant(CommandSender sender, CommandContext context)
    {
        // todo permission

        // fixme for offline players
        CorePlayer player = (CorePlayer) MinecraftServer.getConnectionManager().getOnlinePlayerByUsername(context.get(TARGET));

        if (player == null)
        {
            sender.sendMessage(Messages.COMMAND_INVALID_TARGET);
            return;
        }

        Rank rank = RankCache.get(context.get(RANK));

        if (rank == null)
        {
            // todo message
            return;
        }

        if (player.grant(rank))
        {
            sender.sendMessage(player.getDisplayName().append(Component.text(" has been granted ", WHITE)).append(rank.display()));
        } else
        {
            sender.sendMessage("target already has that grant");
        }
    }

    private static void grantDurationServer(CommandSender sender, CommandContext context)
    {
        sender.sendMessage("/grant rank target rank duration server");
    }
}
