package net.torode.network;

import com.velocitypowered.api.event.connection.PluginMessageEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SubChannelHandler implements ChannelHandler
{
    private final List<Consumer<PluginMessageEvent>> handlers;

    public SubChannelHandler()
    {
        handlers = new ArrayList<>();
    }

    public void register(Consumer<PluginMessageEvent> handler)
    {
        handlers.add(handler);
    }

    public void handle(PluginMessageEvent event)
    {
//        int packetId = SerializationUtil.bigBytesToInt(event.getData());
//
//        handlers.get(packetId).accept(event);
    }
}
