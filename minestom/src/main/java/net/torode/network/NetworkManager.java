package net.torode.network;

import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerPluginMessageEvent;
import net.minestom.server.network.NetworkBuffer;
import net.torode.network.out.ProxyBoundMessage;

import java.util.HashMap;
import java.util.Map;

import static net.minestom.server.MinecraftServer.LOGGER;

public final class NetworkManager
{
    private static final Map<String, ChannelHandler> handlers = new HashMap<>();
    private static final NetworkBuffer IN = NetworkBuffer.resizableBuffer();
    private static final NetworkBuffer OUT = NetworkBuffer.resizableBuffer();

    public static void registerChannel(String channel, ChannelHandler handler)
    {
        handlers.put(channel, handler);
    }

    public static void sendPluginMessage(Player player, ProxyBoundMessage message)
    {
        player.sendPluginMessage(message.getChannel(), message.serialize(OUT));
        // clear buffer for next use
        OUT.clear();
    }

    public static void pluginMessageListener(PlayerPluginMessageEvent event)
    {
        ChannelHandler handler = handlers.get(event.getIdentifier());

        if (handler == null)
        {
            LOGGER.warn("No handler registered for channel '{}'", event.getIdentifier());
            return;
        }

        handler.handle(event, IN);
        IN.clear();
    }

    private NetworkManager()
    {}
}
