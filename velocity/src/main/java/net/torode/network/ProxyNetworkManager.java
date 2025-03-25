package net.torode.network;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.messages.ChannelMessageSink;
import net.torode.network.out.BackendBoundMessage;

import java.util.HashMap;
import java.util.Map;

public final class ProxyNetworkManager
{
    private static final Map<String, ChannelHandler> handlers = new HashMap<>();
    // TODO implement reusable network buffer for #sendPluginMessage

    public static void registerChannelHandler(String channel, ChannelHandler handler)
    {
        handlers.put(channel, handler);
    }

    public static <T extends BackendBoundMessage> void sendPluginMessage(ChannelMessageSink sink, T message)
    {
        sink.sendPluginMessage(message.getChannel(), message.serialize());
    }

    // https://docs.papermc.io/velocity/dev/plugin-messaging
    @Subscribe
    public static void messageEvent(PluginMessageEvent event)
    {
        handlers.get(event.getIdentifier().getId()).handle(event);
    }
}
