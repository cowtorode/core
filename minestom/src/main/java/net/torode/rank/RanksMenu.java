package net.torode.rank;

import net.kyori.adventure.text.Component;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.torode.menu.Menu;
import net.torode.menu.MenuManager;
import net.torode.player.CorePlayer;

import java.util.ArrayList;
import java.util.List;

import static net.kyori.adventure.text.format.NamedTextColor.WHITE;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

public class RanksMenu extends Menu
{
    private static class RankEntry
    {
        final Rank rank;
        EditorMenu editor;

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

    public RanksMenu()
    {
        super(InventoryType.CHEST_6_ROW, Component.text("Rank Editor"));

        // draw utilities
        setItemStack(53, ItemStack.builder(Material.WRITABLE_BOOK).customName(Component.text("Create Rank")).build(), RanksMenu::createRank);
        setItemStack(49, Menu.STANDARD_CLOSE, Menu::close);
    }

    private void rankClicked(InventoryPreClickEvent event, int slot)
    {
        event.setCancelled(true);

        RankEntry entry = ranks.get(slot);

        // Create the rank editor for this rank.
        // todo when can we free this up? Maybe when the last viewer is removed...
        if (entry.editor == null)
        {
            entry.editor = MenuManager.register(new EditorMenu(entry.rank));
        }

        ((CorePlayer) event.getPlayer()).pageForward(entry.editor);
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
     * Copies ranks[i] to ranks[i + 1] from [start,end] including the callback functions, and leaves the first item to be cleared.
     */
    private void shiftRight(int start, int end)
    {
        // shift ranks to the right
        for (int i = end; i >= start; --i)
        {
            System.out.println(i + " copied to " + (i + 1));
            setRankEditorItem(i + 1, getItemStack(i));
        }
    }

    public void addRank(Rank rank)
    {
        // add to stored structure
        // move ranks to the right
        // create rank item

        // sorted arraylist of sorts
        int index = getInsertionIndex(rank);

        shiftRight(index, ranks.size() - 1);

        ranks.add(index, new RankEntry(rank));
        draw(index, rank);
    }

    /**
     * Copies ranks[i - 1] to ranks[i] from [start,end] including the callback functions, and leaves the last item to be cleared.
     */
    private void shiftLeft(int start, int end)
    {
        // shift ranks to the left
        for (int i = start; i <= end; ++i)
        {
            System.out.println(i + " copied to " + (i - 1));
            setRankEditorItem(i - 1, getItemStack(i));
        }
    }

    public void removeRank(Rank rank)
    {
        // remove from stored structure
        // move ranks to the left

        int index = indexOf(rank);

        shiftLeft(index + 1, ranks.size() - 1);

        ranks.remove(index); // remove the rank
        clearSlot(ranks.size()); // remove the rank callback that was at the very end because we shifted everything to the left
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

    public void draw(Rank rank)
    {
        draw(indexOf(rank), rank);
    }

    public void redrawWeight(Rank rank)
    {
        int from = indexOf(rank);

        // reorder array according to new weight
        // you have to remove the rank before you get the new insertion index so it doesn't iterate over itself when comparing weights
        RankEntry entry = ranks.remove(from);

        int to = getInsertionIndex(rank);

        ranks.add(to, entry);

        System.out.println("moving " + from + " to " + to);

        if (from != to)
        {
            // if to > from,
            // x x x f y y y x x
            //        \___/
            // all y needs shifted left and t inserted at that index

            // if from > to
            // x x y y y f x x x
            //      \___/
            // all y needs shifted right and t inserted at that index
            if (to > from)
            {
                System.out.println("shifting left");
                shiftLeft(from + 1, to);
            } else
            {
                System.out.println("shifting right");
                // from != to and !(to > from) so to < from
                shiftRight(to, from - 1);
            }
        }
        // in both cases from is copied over, but we were redrawing it anyway at to so:
        draw(to, rank);

        if (entry.editor != null)
        {
            entry.editor.drawWeight();
        }
    }

    public void redrawName(Rank rank)
    {
        int slot = indexOf(rank);
        draw(slot, rank);

        RankEntry entry = ranks.get(slot);
        if (entry.editor != null)
        {
            entry.editor.drawName();
        }
    }

    public void redrawDisplay(Rank rank)
    {
        int slot = indexOf(rank);
        draw(slot, rank);

        RankEntry entry = ranks.get(slot);
        if (entry.editor != null)
        {
            entry.editor.drawDisplay();
        }
    }

    public void redrawPrefix(Rank rank)
    {
        int slot = indexOf(rank);
        draw(slot, rank);

        RankEntry entry = ranks.get(slot);
        if (entry.editor != null)
        {
            entry.editor.drawPrefix();
        }
    }

    public void redrawUsername(Rank rank)
    {
        int slot = indexOf(rank);
        draw(slot, rank);

        RankEntry entry = ranks.get(slot);
        if (entry.editor != null)
        {
            entry.editor.drawUsername();
        }
    }
}
