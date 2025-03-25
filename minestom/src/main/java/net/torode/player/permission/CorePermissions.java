package net.torode.player.permission;

import java.util.ArrayList;
import java.util.List;

public final class CorePermissions
{
    private static final List<String> PERMISSIONS = new ArrayList<>();

    public static byte[] getDefaultPermissions()
    {
        return new byte[PERMISSIONS.size()];
    }

    public static int getIndex(String permission)
    {
        return PERMISSIONS.indexOf(permission);
    }

    public static int newPermission(String permission)
    {
        int index = getIndex(permission);
        if (index == -1)
        {
            PERMISSIONS.add(permission);
            index = PERMISSIONS.size() - 1;
        }
        return index;
    }

    public static int COMMAND_GRANT = newPermission("core.command.grant");
    public static int COMMAND_GAMEMODE = newPermission("core.command.gamemode");
    public static int COMMAND_GAMEMODE_TARGET = newPermission("core.command.gamemode.target");
    public static int COMMAND_STOP = newPermission("core.command.stop");
    public static int COMMAND_RANKCACHE = newPermission("core.command.rankcache");
    public static int COMMAND_RANKS_DELETE = newPermission("core.command.ranks.delete");

    // No need for this to be a class
    private CorePermissions()
    {}
}
