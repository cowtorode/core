package net.torode.network;

import com.velocitypowered.api.event.connection.PluginMessageEvent;

public interface ChannelHandler
{
    void handle(PluginMessageEvent event);
}
