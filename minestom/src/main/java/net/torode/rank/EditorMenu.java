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
import net.torode.player.CorePlayer;
import org.jetbrains.annotations.NotNull;

import static net.kyori.adventure.text.format.NamedTextColor.WHITE;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;
import static net.torode.player.permission.CorePermissions.COMMAND_RANKS_DELETE;

public class EditorMenu extends Menu
{
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
            player.sendMessage(Messages.GENERIC_NO_PERMISSION);
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

    private final Rank rank;

    public void drawWeight()
    {
        setItemStack(0, ItemStack.builder(Material.NAME_TAG)
                                      .customName(Component.text("weight").decoration(ITALIC, false))
                                      .lore(Component.text(rank.weight(), WHITE).decoration(TextDecoration.ITALIC, false))
                                      .build(), event0 -> weightClicked(event0, rank));
    }

    public void drawName()
    {
        setItemStack(1, ItemStack.builder(Material.NAME_TAG)
                                      .customName(Component.text("name").decoration(ITALIC, false))
                                      .lore(Component.text('"' + rank.name() + '"', WHITE).decoration(ITALIC, false))
                                      .build(), event0 -> nameClicked(event0, rank));
    }

    public void drawDisplay()
    {
        Component quotation = Component.text('"', WHITE);
        setItemStack(2, ItemStack.builder(Material.NAME_TAG)
                                      .customName(Component.text("display").decoration(ITALIC, false))
                                      .lore(quotation.append(rank.display()).append(quotation).decoration(ITALIC, false))
                                      .build(), event0 -> displayClicked(event0, rank));
    }

    public void drawPrefix()
    {
        Component quotation = Component.text('"', WHITE);
        setItemStack(3, ItemStack.builder(Material.NAME_TAG)
                                      .customName(Component.text("prefix").decoration(ITALIC, false))
                                      .lore(quotation.append(rank.prefix()).append(quotation).decoration(ITALIC, false))
                                      .build(), event0 -> prefixClicked(event0, rank));
    }

    public void drawUsername()
    {
        Component quotation = Component.text('"', WHITE);
        setItemStack(4, ItemStack.builder(Material.NAME_TAG)
                                      .customName(Component.text("username").decoration(ITALIC, false))
                                      .lore(quotation.append(rank.username()).append(quotation).decoration(ITALIC, false))
                                      .build(), event0 -> usernameClicked(event0, rank));
    }

    public void drawPermissions()
    {
        setItemStack(5, ItemStack.builder(Material.PAPER)
                                      .customName(Component.text("permissions").decoration(ITALIC, false))
                                      .build(), event0 -> permissionsClicked(event0, rank));
    }

    public void drawParents()
    {
        setItemStack(6, ItemStack.builder(Material.PAPER)
                                      .customName(Component.text("parents").decoration(ITALIC, false))
                                      .build(), event0 -> parentsClicked(event0, rank));
    }
    public EditorMenu(Rank rank)
    {
        super(InventoryType.CHEST_2_ROW, rank.display());

        this.rank = rank;

        drawWeight();
        drawName();
        drawDisplay();
        drawPrefix();
        drawUsername();
        drawPermissions();
        drawParents();

        setItemStack(9, Menu.STANDARD_BACK, Menu::back);
        setItemStack(13, Menu.STANDARD_CLOSE, Menu::close);
        setItemStack(17, ItemStack.builder(Material.STRUCTURE_VOID)
                                       .customName(Component.text("Delete Rank"))
                                       .build(), event -> deleteClicked(event, rank));
    }
}
