package net.torode.network;

import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;

public final class ProxyChannels
{
    public static final ChannelIdentifier DEFAULT = MinecraftChannelIdentifier.from("n:c");

    private ProxyChannels()
    {}
}
