package net.torode;

import net.minestom.server.event.player.PlayerChatEvent;

public interface ChatEvent
{
    void chat(PlayerChatEvent event);
}
