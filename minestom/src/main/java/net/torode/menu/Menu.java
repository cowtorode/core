package net.torode.menu;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.torode.player.CorePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;
import static net.minestom.server.item.ItemStack.AIR;

public class Menu extends Inventory implements ClickEvent
{
    public static final ItemStack STANDARD_CLOSE = ItemStack.builder(Material.BARRIER)
                                                            .customName(Component.text("Exit", RED)
                                                            .decoration(ITALIC, false))
                                                            .build();
    public static final ItemStack STANDARD_BACK = ItemStack.builder(Material.ARROW)
                                                           .customName(Component.text("Back", WHITE)
                                                           .decoration(ITALIC, false))
                                                           .build();

    public static void close(InventoryPreClickEvent event)
    {
        event.setCancelled(true);
        event.getPlayer().closeInventory();
    }

    public static void back(InventoryPreClickEvent event)
    {
        event.setCancelled(true);
        ((CorePlayer) event.getPlayer()).pageBack();
    }

    private final Map<Integer, ClickEvent> actions = new HashMap<>();

    public Menu(@NotNull InventoryType type, @NotNull Component title)
    {
        super(type, title);
    }

    public void clearSlot(int slot)
    {
        setItemStack(slot, AIR);
        actions.remove(slot);
    }

    public void setClickEvent(int slot, @NotNull ClickEvent callback)
    {
        actions.put(slot, callback);
    }

    public void setItemStack(int slot, @NotNull ItemStack item, @NotNull ClickEvent event)
    {
        setClickEvent(slot, event);
        setItemStack(slot, item);
    }

    @Override
    public void click(InventoryPreClickEvent event)
    {
        ClickEvent callback = actions.get(event.getSlot());

        if (callback != null)
        {
            callback.click(event);
        }
    }
}
