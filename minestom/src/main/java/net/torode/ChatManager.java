package net.torode;

import net.minestom.server.event.player.PlayerChatEvent;
import net.torode.player.CorePlayer;

public final class ChatManager
{
    public static void chatEvent(PlayerChatEvent event)
    {
        CorePlayer player = (CorePlayer) event.getPlayer();

        if (player.getChatCallback() != null)
        {
            player.getChatCallback().chat(event);
        }
    }
}
