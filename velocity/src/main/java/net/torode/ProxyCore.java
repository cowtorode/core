package net.torode;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.EventManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelRegistrar;
import net.torode.command.MessageCommand;
import net.torode.network.ProxyNetworkManager;
import net.torode.network.SubChannelHandler;

import java.util.logging.Logger;

@Plugin(id = "proxycore",
        name = "Proxy",
        version = "1.0-SNAPSHOT",
        url = "none",
        description = "Core Proxy Plugin",
        authors = {
                "Cory Torode"
        }
)
public final class ProxyCore
{
    private static ProxyServer SERVER;
    private static Logger LOGGER;

    public static ProxyServer server()
    {
        return SERVER;
    }

    public static Logger logger()
    {
        return LOGGER;
    }

    @Inject
    public ProxyCore(ProxyServer server, Logger logger)
    {
        SERVER = server;
        LOGGER = logger;

        // Load configs

        //storage = Storage.newStorage(this);
    }

    private void registerChannels()
    {
        ChannelRegistrar channels = SERVER.getChannelRegistrar();

    }

    private void registerEvents()
    {
        final EventManager eventManager = SERVER.getEventManager();

        // I dislike this
        eventManager.register(this, new ProxyNetworkManager());

        ProxyNetworkManager.registerChannelHandler("n:c", new SubChannelHandler()
        {
            {
                register(event ->
                {

                });
            }
        });
    }

    private CommandMeta meta(String cmd, String... aliases)
    {
        return SERVER.getCommandManager().metaBuilder(cmd).aliases(aliases).plugin(this).build();
    }

    private void registerCommands()
    {
        CommandManager commandManager = SERVER.getCommandManager();

        commandManager.register(meta("message", "tell", "msg", "m"), new MessageCommand());
    }

    @Subscribe
    private void initialize(ProxyInitializeEvent event)
    {
        registerChannels();
        registerEvents();
        registerCommands();
    }
}
