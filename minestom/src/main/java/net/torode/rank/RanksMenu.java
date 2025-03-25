package net.torode.rank;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.CommandExecutor;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.player.PlayerChatEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.torode.globals.Messages;
import net.torode.menu.Menu;
import net.torode.menu.MenuManager;
import net.torode.player.CorePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.format.NamedTextColor.WHITE;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;
import static net.torode.player.permission.CorePermissions.COMMAND_RANKS_DELETE;

public class RanksMenu extends Menu
{
    private static class RankEntry
    {
        final Rank rank;
        Menu editor;

        RankEntry(Rank rank)
        {
            this.rank = rank;
        }
    }

    // sorted list
    private final List<RankEntry> ranks = new ArrayList<>();

    private static void createRank(InventoryPreClickEvent event)
    {
        event.setCancelled(true);

        RankCache.newRank();
    }

    public RanksMenu(@NotNull InventoryType type, @NotNull Component title)
    {
        super(type, title);

        setItemStack(53, ItemStack.builder(Material.WRITABLE_BOOK).customName(Component.text("Create Rank")).build(), RanksMenu::createRank);
        setItemStack(49, Menu.STANDARD_CLOSE, Menu::close);
    }

    private static void weightModify(PlayerChatEvent event, Rank rank)
    {
        event.setCancelled(true); // cancel chat message
        CorePlayer player = (CorePlayer) event.getPlayer();

        try
        {
            RankCache.setWeight(rank, Integer.parseInt(event.getRawMessage()));
        } catch (NumberFormatException nfe)
        {
            player.sendMessage("Cancelled.");
        }

        player.setChatCallback(null);
        player.pageBack();
    }

    private static void weightClicked(InventoryPreClickEvent event, Rank rank)
    {
        event.setCancelled(true);
        ((CorePlayer) event.getPlayer()).promptFromInventory(Component.text("Enter weight for ").append(rank.display()).append(Component.text(" (current " + rank.weight() + ')')), chat -> weightModify(chat, rank));
    }

    private static void nameModify(PlayerChatEvent event, Rank rank)
    {
        event.setCancelled(true);
        CorePlayer player = (CorePlayer) event.getPlayer();

        // todo message verification (length and chars)
        RankCache.setName(rank, event.getRawMessage());

        player.setChatCallback(null);
        player.pageBack();
    }

    private static void nameClicked(InventoryPreClickEvent event, Rank rank)
    {
        event.setCancelled(true);
        ((CorePlayer) event.getPlayer()).promptFromInventory(Component.text("Enter name for ").append(rank.display()).append(Component.text(" (current \"" + rank.name() + "\")")), chat -> nameModify(chat, rank));
    }

    private static void displayModify(PlayerChatEvent event, Rank rank)
    {
        event.setCancelled(true);
        CorePlayer player = (CorePlayer) event.getPlayer();

        RankCache.setDisplay(rank, MiniMessage.miniMessage().deserialize(event.getRawMessage()));

        player.setChatCallback(null);
        player.pageBack();
    }

    private static void displayClicked(InventoryPreClickEvent event, Rank rank)
    {
        event.setCancelled(true);
        ((CorePlayer) event.getPlayer()).promptFromInventory(Component.text("Enter display for ").append(rank.display()).append(Component.text(" (current \"")).append(rank.display()).append(Component.text("\")")), chat -> displayModify(chat, rank));
    }

    private static void prefixModify(PlayerChatEvent event, Rank rank)
    {
        event.setCancelled(true);
        CorePlayer player = (CorePlayer) event.getPlayer();

        RankCache.setPrefix(rank, MiniMessage.miniMessage().deserialize(event.getRawMessage()));

        player.setChatCallback(null);
        player.pageBack();
    }

    private static void prefixClicked(InventoryPreClickEvent event, Rank rank)
    {
        event.setCancelled(true);
        ((CorePlayer) event.getPlayer()).promptFromInventory(Component.text("Enter prefix for ").append(rank.display()).append(Component.text(" (current \"")).append(rank.prefix()).append(Component.text("\")")), chat -> prefixModify(chat, rank));
    }

    private static void usernameModify(PlayerChatEvent event, Rank rank)
    {
        event.setCancelled(true);
        CorePlayer player = (CorePlayer) event.getPlayer();

        RankCache.setUsername(rank, MiniMessage.miniMessage().deserialize(event.getRawMessage()));

        player.setChatCallback(null);
        player.pageBack();
    }

    private static void usernameClicked(InventoryPreClickEvent event, Rank rank)
    {
        event.setCancelled(true);
        ((CorePlayer) event.getPlayer()).promptFromInventory(Component.text("Enter username for ").append(rank.display()).append(Component.text(" (current \"")).append(rank.username()).append(Component.text("\")")), chat -> usernameModify(chat, rank));
    }

    private static void permissionsClicked(InventoryPreClickEvent event, Rank rank)
    {
        event.setCancelled(true);

    }

    private static void parentsClicked(InventoryPreClickEvent event, Rank rank)
    {
        event.setCancelled(true);

    }

    private static void deleteClicked(InventoryPreClickEvent event, Rank rank)
    {
        event.setCancelled(true);

        CorePlayer player = (CorePlayer) event.getPlayer();

        if (!player.hasPermission(COMMAND_RANKS_DELETE))
        {
            player.sendMessage(Messages.NO_PERMISSION);
            return;
        }

        record DeleteConfirm(Rank rank, Inventory inventory) implements CommandExecutor
        {
            @Override
            public void apply(@NotNull CommandSender sender, @NotNull CommandContext context)
            {
                RankCache.unregister(rank);

                // fixme this goes back to the root inventory so there's no problem with going back a page,
                // but this current system only allows you to go back to the root inventory. If yo uhad to
                // go back to an inventory that wasn't a root inventory, backing would error.
                ((CorePlayer) sender).openInventory(inventory);
            }
        }

        player.promptConfirmation(Component.text("Are you sure that you want to delete ")
                                           .append(rank.display()
                                           .append(Component.text("? Every player with this rank granted will have it removed. Type /confirm to proceed.", WHITE))),
                                  new DeleteConfirm(rank, player.getLastInventory()));
    }

    private void rankClicked(InventoryPreClickEvent event, int slot)
    {
        event.setCancelled(true);

        RankEntry entry = ranks.get(slot);
        Rank rank = entry.rank;

        Menu editor = entry.editor;
        if (editor == null)
        {
            editor = MenuManager.register(new Menu(InventoryType.CHEST_2_ROW, rank.display()));

            Component quotation = Component.text('"', WHITE);
            editor.setItemStack(0, ItemStack.builder(Material.NAME_TAG)
                  .customName(Component.text("weight").decoration(ITALIC, false))
                  .lore(Component.text(rank.weight(), WHITE).decoration(TextDecoration.ITALIC, false))
                  .build(), event0 -> weightClicked(event0, rank));
            editor.setItemStack(1, ItemStack.builder(Material.NAME_TAG)
                  .customName(Component.text("name").decoration(ITALIC, false))
                  .lore(Component.text('"' + rank.name() + '"', WHITE).decoration(ITALIC, false))
                  .build(), event0 -> nameClicked(event0, rank));
            editor.setItemStack(2, ItemStack.builder(Material.NAME_TAG)
                  .customName(Component.text("display").decoration(ITALIC, false))
                  .lore(quotation.append(rank.display()).append(quotation).decoration(ITALIC, false))
                  .build(), event0 -> displayClicked(event0, rank));
            editor.setItemStack(3, ItemStack.builder(Material.NAME_TAG)
                  .customName(Component.text("prefix").decoration(ITALIC, false))
                  .lore(quotation.append(rank.prefix()).append(quotation).decoration(ITALIC, false))
                  .build(), event0 -> prefixClicked(event0, rank));
            editor.setItemStack(4, ItemStack.builder(Material.NAME_TAG)
                  .customName(Component.text("username").decoration(ITALIC, false))
                  .lore(quotation.append(rank.username()).append(quotation).decoration(ITALIC, false))
                  .build(), event0 -> usernameClicked(event0, rank));
            editor.setItemStack(5, ItemStack.builder(Material.PAPER)
                  .customName(Component.text("permissions").decoration(ITALIC, false))
                  .build(), event0 -> permissionsClicked(event0, rank));
            editor.setItemStack(6, ItemStack.builder(Material.PAPER)
                  .customName(Component.text("parents").decoration(ITALIC, false))
                  .build(), event0 -> parentsClicked(event0, rank));

            editor.setItemStack(9, Menu.STANDARD_BACK, Menu::back);
            editor.setItemStack(13, Menu.STANDARD_CLOSE, Menu::close);
            editor.setItemStack(17, ItemStack.builder(Material.STRUCTURE_VOID)
                  .customName(Component.text("Delete Rank"))
                  .build(), event0 -> deleteClicked(event0, rank));

            entry.editor = editor;
        }

        ((CorePlayer) event.getPlayer()).pageForward(editor);
    }

    private int getInsertionIndex(Rank rank)
    {
        int i;
        for (i = 0; i < ranks.size(); ++i)
        {
            if (ranks.get(i).rank.weight() < rank.weight())
            {
                break;
            }
        }
        return i;
    }

    /**
     * Sets the ItemStack at the slot with the corresponding callback function
     */
    private void setRankEditorItem(int slot, ItemStack item)
    {
        setItemStack(slot, item, event -> rankClicked(event, slot));
    }

    private void draw(int slot, Rank rank)
    {
        Component quotation = Component.text('"', WHITE);
        setRankEditorItem(slot, ItemStack.builder(Material.BOOK)
                                         .customName(rank.display().decoration(ITALIC, false))
                                         .lore(Component.text("id: " + rank.id(), WHITE)
                                                         .decoration(ITALIC, false),
                                               Component.text("weight: " + rank.weight(), WHITE)
                                                        .decoration(ITALIC, false),
                                               Component.text("name: \"" + rank.name() + '"', WHITE)
                                                        .decoration(ITALIC, false),
                                               Component.text("display: \"", WHITE)
                                                        .append(rank.display()).append(quotation)
                                                        .decoration(ITALIC, false),
                                               Component.text("prefix: \"", WHITE)
                                                        .append(rank.prefix()).append(quotation)
                                                        .decoration(ITALIC, false),
                                               Component.text("username: \"", WHITE)
                                                        .append(rank.username()).append(quotation)
                                                        .decoration(ITALIC, false))
                                         .build());
    }

    private int indexOf(Rank rank)
    {
        for (int i = 0; i < ranks.size(); ++i)
        {
            if (ranks.get(i).rank == rank)
            {
                return i;
            }
        }
        return -1;
    }

    public void draw(Rank rank)
    {
        draw(indexOf(rank), rank);
    }

    public void addRank(Rank rank)
    {
        // add to stored structure
        // move ranks to the right
        // create rank item

        // sorted arraylist of sorts
        int index = getInsertionIndex(rank);

        // shift ranks to the right
        for (int i = ranks.size() - 1; i >= index; --i)
        {
            setRankEditorItem(i + 1, getItemStack(i));
        }

        ranks.add(index, new RankEntry(rank));
        draw(index, rank);
    }

    public void removeRank(Rank rank)
    {
        // remove from stored structure
        // move ranks to the left

        int index = indexOf(rank);

        // shift ranks to the right
        int i;
        for (i = index; i < ranks.size() - 1; ++i)
        {
            setRankEditorItem(i, getItemStack(i + 1));
        }

        ranks.remove(index); // remove the rank
        clearSlot(i); // remove the rank callback that was at the very end because we shifted everything to the left
    }
}
