package net.torode.mexample;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.event.player.PlayerChatEvent;
import net.torode.rank.Rank;
import net.torode.player.CorePlayer;

public class RanksInChatExample
{
    public static void playerChatEvent(PlayerChatEvent event)
    {
        // Rank chat prefix
        CorePlayer player = (CorePlayer) event.getPlayer();
        Rank rank = player.getHeaviestRank();
        String message = event.getRawMessage();

        if (rank == null)
        {
            // Default format
            Component name = player.getDisplayName() == null ? player.getName().color(NamedTextColor.GRAY) : player.getDisplayName();

            // name: message
            event.setFormattedMessage(name.append(Component.text(": " + message, NamedTextColor.WHITE)));
        } else
        {
            Component name = player.getDisplayName() == null ? player.getName() : player.getDisplayName();

            // [Prefix] name: message
            event.setFormattedMessage(rank.prefix()
                    .append(name)
                    .append(Component.text(": " + message)));
        }
    }
}
