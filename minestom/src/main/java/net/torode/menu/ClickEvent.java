package net.torode.menu;

import net.minestom.server.event.inventory.InventoryPreClickEvent;

public interface ClickEvent
{
    void click(InventoryPreClickEvent event);
}
