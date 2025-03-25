package net.torode.menu;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.tag.Tag;

public final class MenuManager
{
    private static final Tag<Boolean> MENU_TAG = Tag.Boolean("InventoryPreClickEvent");

    public static <M extends Menu> M register(M menu)
    {
        menu.setTag(MENU_TAG, true);
        return menu;
    }

    /**
     * use #register
     * @deprecated
     */
    public static Menu newMenu(InventoryType type, Component title)
    {
        Menu rax = new Menu(type, title);

        rax.setTag(MENU_TAG, true);

        return rax;
    }

    public static void inventoryPreClickEvent(InventoryPreClickEvent event)
    {
        AbstractInventory inventory = event.getInventory();

        if (inventory != null)
        {
            Boolean hasMenuComponent = inventory.getTag(MENU_TAG);

            if (hasMenuComponent != null && hasMenuComponent)
            {
                ((Menu) inventory).click(event);
            }
        }
    }
}
