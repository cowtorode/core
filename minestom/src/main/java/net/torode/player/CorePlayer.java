package net.torode.player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.minestom.server.command.builder.CommandExecutor;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.network.player.GameProfile;
import net.minestom.server.network.player.PlayerConnection;
import net.torode.ChatEvent;
import net.torode.command.ConfirmCommand;
import net.torode.rank.Rank;
import net.torode.menu.Menu;
import net.torode.player.permission.CorePermissions;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CorePlayer extends Player
{
    private byte[] permissions;
    private int stackDepth = 0;
    private final SortedSet<Rank> ranks = new TreeSet<>();
    /**
     * The last inventories that the player had open (used for backing up in menus)
     */
    private final Stack<Inventory> lastInventories = new Stack<>();
    private ChatEvent chatCallback;

    public CorePlayer(@NotNull PlayerConnection conn, @NotNull GameProfile profile)
    {
        super(conn, profile);

        permissions = CorePermissions.getDefaultPermissions();
    }

    public boolean hasPermission(int permission)
    {
        return permissions[permission] != 0;
    }

    public Rank getHeaviestRank()
    {
        return ranks.isEmpty() ? null : ranks.first();
    }

    /**
     * @throws NullPointerException if the player has no ranks
     */
    public void updateDisplayName()
    {
        setDisplayName(getHeaviestRank().username().replaceText(TextReplacementConfig.builder().match("%username%").replacement(getUsername()).build()));
    }

    /**
     * @return True if added false if already there
     */
    public boolean grant(Rank rank)
    {
        if (ranks.add(rank))
        {
            // add permissions of the rank to the player
            for (int i = 0; i < rank.permissions().length; ++i)
            {
                permissions[i] += rank.permissions()[i];
            }

            // set their new display name
            updateDisplayName();
            return true;
        }
        return false;
    }

    /**
     * @return True if removed false if not there
     */
    public boolean ungrant(Rank rank)
    {
        if (ranks.remove(rank))
        {
            // add permissions of the rank to the player
            for (int i = 0; i < rank.permissions().length; ++i)
            {
                permissions[i] -= rank.permissions()[i];
            }

            // set their new display name
            if (ranks.isEmpty())
            {
                setDisplayName(null);
            } else
            {
                updateDisplayName();
            }
            return true;
        }
        return false;
    }

    public void resort(Rank rank)
    {
        if (ranks.remove(rank))
        {
            ranks.add(rank);
        }
    }

    public void setChatCallback(ChatEvent chatCallback)
    {
        this.chatCallback = chatCallback;
    }

    public ChatEvent getChatCallback()
    {
        return chatCallback;
    }

    public Inventory getLastInventory()
    {
        return lastInventories.pop();
    }

    /**
     * Opens the player's inventory and clears the last inventories stack.
     * This has to clear the player's last inventories because a new menu
     * has been started, so the last menu's pages no longer apply.
     */
    @Override
    public boolean openInventory(@NotNull Inventory inventory)
    {
        lastInventories.clear();
        sendMessage("opening " + (stackDepth = 0));
        return super.openInventory(inventory);
    }

    /**
     * Opens the last inventory that the player had open before closing
     */
    public void pageBack()
    {
        // open the previous inventory
        closeInventory();
        super.openInventory(lastInventories.pop());
        sendMessage("back " + (--stackDepth));
    }

    /**
     * Goes forward a page
     */
    public void pageForward(Menu menu)
    {
        // push onto the inventory stack the inventory that the player is currently viewing
        lastInventories.push((Menu) getOpenInventory());

        // open the new inventory
        closeInventory();
        super.openInventory(menu);
        sendMessage("forward " + (++stackDepth));
    }

    /**
     * Goes forward a page but not with an inventory, with a ChatEvent prompt
     */
    public void promptFromInventory(Component prompt, ChatEvent event)
    {
        lastInventories.push((Menu) getOpenInventory());
        closeInventory();

        sendMessage(prompt);
        setChatCallback(event);
        sendMessage("prompt " + (++stackDepth));
    }

    public void promptConfirmation(Component prompt, CommandExecutor executor)
    {
        lastInventories.push((Menu) getOpenInventory());
        closeInventory();

        sendMessage(prompt);
        ConfirmCommand.register(this, executor);
        sendMessage("confirm " + (++stackDepth));
    }
}
