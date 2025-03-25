package net.torode.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

import static net.torode.ProxyCore.server;

public class MessageCommand implements RawCommand
{
    @Override
    public void execute(Invocation invocation)
    {
        String args = invocation.arguments();
        // index of end of first argument
        int index = args.indexOf(' ');

        if (index == -1)
        {
            // send usage

            return;
        }

        CommandSource sender = invocation.source();

        server().getPlayer(args.substring(0, index)).ifPresentOrElse(player ->
        {
            String messagePrefix;

            if (sender instanceof Player)
            {
                messagePrefix = ((Player) sender).getUsername();
            } else
            {

            }
            // send the message
            player.sendMessage(Component.text("" + args.substring(index)));
        }, () ->
        {

        });
    }
}
