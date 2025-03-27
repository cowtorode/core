package net.torode.rank;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.torode.menu.Menu;
import net.torode.menu.MenuManager;
import net.torode.player.CorePlayer;
import net.torode.player.permission.CorePermissions;

import java.util.*;

public final class RankCache
{
    private static final RanksMenu ranksMenu = MenuManager.register(new RanksMenu());

    private static final Map<Integer, Rank> ranksById = new HashMap<>();
    private static final Map<String, Rank> ranksByName = new HashMap<>();

    // fixme remove
    public static final Rank ADMIN;
    public static final Rank MOD;
    public static final Rank HELPER;

    // fixme remove
    private static int id = 0;

    public static Rank newRank()
    {
        // todo get id from database and commit empty components
        String name = "rank_" + id;
        Rank rank = new Rank(id, 0, name, Component.text(name), Component.empty(), Component.text("%username%"), CorePermissions.getDefaultPermissions());
        ++id;

        register(rank);

        return rank;
    }

    public static void register(Rank rank)
    {
        ranksById.put(rank.id(), rank);
        ranksByName.put(rank.name(), rank);
        ranksMenu.addRank(rank);
    }

    public static void unregister(Rank rank)
    {
        ranksById.remove(rank.id(), rank);
        ranksByName.remove(rank.name(), rank);

        for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers())
        {
            // resort this rank for each player
            ((CorePlayer) player).ungrant(rank);
        }

        ranksMenu.removeRank(rank);
    }

    public static Rank get(String name)
    {
        return ranksByName.get(name);
    }

    public static void setWeight(Rank rank, int weight)
    {
        rank.setWeight(weight);
        // instead of just calling ranksMenu#draw, we remove and readd to resort the list
        ranksMenu.redrawWeight(rank);

        for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers())
        {
            // resort this rank for each player
            ((CorePlayer) player).resort(rank);
        }
    }

    public static void setName(Rank rank, String name)
    {
        // reinit in ranksByName map
        ranksByName.remove(rank.name());
        ranksByName.put(name, rank);
        rank.setName(name);
        ranksMenu.redrawName(rank);
    }

    public static void setDisplay(Rank rank, Component display)
    {
        rank.setDisplay(display);
        ranksMenu.redrawDisplay(rank);
    }

    public static void setPrefix(Rank rank, Component prefix)
    {
        rank.setPrefix(prefix);
        ranksMenu.redrawPrefix(rank);
    }

    public static void setUsername(Rank rank, Component username)
    {
        rank.setUsername(username);
        ranksMenu.redrawUsername(rank);

        for (Player player : MinecraftServer.getConnectionManager().getOnlinePlayers())
        {
            // update display name for all applicable players
            if (((CorePlayer) player).getHeaviestRank() == rank)
            {
                ((CorePlayer) player).updateDisplayName();
            }
        }
    }

    public static Menu getRanksMenu()
    {
        return ranksMenu;
    }

    static
    {
        // simulate loading ranks
        byte[] op = CorePermissions.getDefaultPermissions();
        Arrays.fill(op, (byte) 1);

        HELPER = new Rank(id++, 125, "helper",
                MiniMessage.miniMessage().deserialize("<dark_green>Helper</dark_green>"),
                MiniMessage.miniMessage().deserialize("<gray>[</gray><dark_green>Helper</dark_green><gray>]</gray> "),
                MiniMessage.miniMessage().deserialize("<dark_green>%username%</dark_green>"),
                CorePermissions.getDefaultPermissions());
        MOD = new Rank(id++, 126, "mod",
                MiniMessage.miniMessage().deserialize("<dark_purple>Mod</dark_purple>"),
                MiniMessage.miniMessage().deserialize("<gray>[</gray><dark_purple>Mod</dark_purple><gray>]</gray> "),
                MiniMessage.miniMessage().deserialize("<dark_purple>%username%</dark_purple>"),
                CorePermissions.getDefaultPermissions());
        ADMIN = new Rank(id++, 127, "admin",
                MiniMessage.miniMessage().deserialize("<red>Admin</red>"),
                MiniMessage.miniMessage().deserialize("<gray>[</gray><red>Admin</red><gray>]</gray> "),
                MiniMessage.miniMessage().deserialize("<red>%username%</red>"),
                op);

        MOD.addParent(HELPER);
        ADMIN.addParent(MOD);

        register(ADMIN);
        register(MOD);
        register(HELPER);
    }

    private RankCache() {}
}
