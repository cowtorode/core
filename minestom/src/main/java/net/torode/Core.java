package net.torode;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.event.player.PlayerPluginMessageEvent;
import net.torode.command.ConfirmCommand;
import net.torode.command.GamemodeCommand;
import net.torode.command.ranks.RanksCommand;
import net.torode.command.grant.GrantCommand;
import net.torode.command.StopCommand;
import net.torode.menu.MenuManager;
import net.torode.network.SubchannelHandler;
import net.torode.network.NetworkManager;
import net.torode.player.CorePlayer;

public final class Core
{
    private Core()
    {}

    private static void initManagers()
    {
        SubchannelHandler channelHandler = new SubchannelHandler(1);
        channelHandler.registerSubchannel(0, (event, buffer) ->
        {

        });

        NetworkManager.registerChannel("n:c", channelHandler);
    }

    private static void initEvents()
    {
        MinecraftServer.getGlobalEventHandler()
                       .addListener(PlayerPluginMessageEvent.class, NetworkManager::pluginMessageListener)
                       .addListener(InventoryPreClickEvent.class, MenuManager::inventoryPreClickEvent)
                       .addListener(PlayerChatEvent.class, ChatManager::chatEvent);
    }

    private static void initCommands()
    {
        MinecraftServer.getCommandManager()
                       .register(new GrantCommand(),
                                 new StopCommand(),
                                 new GamemodeCommand(),
                                 new RanksCommand(),
                                 new ConfirmCommand());
    }

    public static void enable()
    {
        MinecraftServer.getConnectionManager().setPlayerProvider(CorePlayer::new);

        initManagers();
        initEvents();
        initCommands();
    }
}
