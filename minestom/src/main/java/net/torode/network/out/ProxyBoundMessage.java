package net.torode.network.out;

import net.minestom.server.network.NetworkBuffer;

public interface ProxyBoundMessage
{
    String getChannel();

    byte[] serialize(NetworkBuffer buffer);
}
