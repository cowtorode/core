package net.torode.network;

import net.minestom.server.event.player.PlayerPluginMessageEvent;
import net.minestom.server.network.NetworkBuffer;

public interface ChannelHandler
{
    void handle(PlayerPluginMessageEvent event, NetworkBuffer buffer);
}
