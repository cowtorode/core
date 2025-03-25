package net.torode.mexample;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;
import net.minestom.server.instance.block.Block;
import net.torode.Core;

public class Main
{
    public static void main(String[] args)
    {
        // Initialize the Minestom server
        MinecraftServer server = MinecraftServer.init();

        // World
        InstanceContainer world = MinecraftServer.getInstanceManager().createInstanceContainer();

        world.setChunkSupplier(LightingChunk::new);
        world.setGenerator(unit ->
        {
            unit.modifier().fillHeight(-64, -63, Block.BEDROCK);
            unit.modifier().fillHeight(-63, -62, Block.GRASS_BLOCK);
        });

        // Events
        MinecraftServer.getGlobalEventHandler().addListener(AsyncPlayerConfigurationEvent.class, event ->
        {
            // World spawning
            Player player = event.getPlayer();

            event.setSpawningInstance(world);
            player.setRespawnPoint(new Pos(0, -62, 0));
        }).addListener(PlayerChatEvent.class, RanksInChatExample::playerChatEvent);

        // Initialize the core
        Core.enable();

        // Start the server
        server.start("0.0.0.0", 25565);
    }
}
