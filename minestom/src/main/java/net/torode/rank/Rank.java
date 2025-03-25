package net.torode.rank;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Rank implements Comparable<Rank>
{
    private int id;
    private int weight; // 35

    private String name; // 'mod'
    private Component display; // '&aMod'
    private Component prefix; // '&7[&aMod&7] '
    private Component username; // '&a%username%'

    private byte[] permissions;
    private final List<Rank> parents = new ArrayList<>(); // trainee

    // buycraft?

    public Rank(int id, int weight, String name, Component display, Component prefix, Component username, byte[] permissions)
    {
        this.id = id;
        this.weight = weight;
        this.name = name;
        this.display = display;
        this.prefix = prefix;
        this.username = username;
        this.permissions = permissions;
    }

    public void addParent(Rank rank)
    {
        parents.add(rank);

        // todo add parent permissions somehow
    }

    public int id()
    {
        return id;
    }

    public int weight()
    {
        return weight;
    }

    protected void setWeight(int weight)
    {
        this.weight = weight;
    }

    public String name()
    {
        return name;
    }

    protected void setName(String name)
    {
        this.name = name;
    }

    public Component display()
    {
        return display;
    }

    protected void setDisplay(Component display)
    {
        this.display = display;
    }

    public Component prefix()
    {
        return prefix;
    }

    protected void setPrefix(Component prefix)
    {
        this.prefix = prefix;
    }

    public Component username()
    {
        return username;
    }

    protected void setUsername(Component username)
    {
        this.username = username;
    }

    public byte[] permissions()
    {
        return permissions;
    }

    @Override
    public int compareTo(@NotNull Rank rank)
    {
        /* Descending order (places the rank with the highest weight first) */
        return Integer.compare(rank.weight, weight);
    }
}
