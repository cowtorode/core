package net.torode.network.out;

import com.velocitypowered.api.proxy.messages.ChannelIdentifier;

public interface BackendBoundMessage
{
    ChannelIdentifier getChannel();

    byte[] serialize();
}
