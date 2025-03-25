package net.torode.network;

import net.minestom.server.event.player.PlayerPluginMessageEvent;
import net.minestom.server.network.NetworkBuffer;

public class SubchannelHandler implements ChannelHandler
{
    private ChannelHandler[] subchannels;

    /**
     * @param subchannels Length of subchannels array
     */
    public SubchannelHandler(int subchannels)
    {
        this.subchannels = new ChannelHandler[subchannels];
    }

    /**
     * @param subchannel Index of where this channel should be placed
     * @param handler The handler that's registered to the subchannel
     */
    public void registerSubchannel(int subchannel, ChannelHandler handler)
    {
        subchannels[subchannel] = handler;
    }

    public void handle(PlayerPluginMessageEvent event, NetworkBuffer buffer)
    {
        buffer.extractBytes(extractor -> extractor.read(NetworkBuffer.RAW_BYTES));
        int subchannel = buffer.read(NetworkBuffer.INT);

        subchannels[subchannel].handle(event, buffer);
    }
}
