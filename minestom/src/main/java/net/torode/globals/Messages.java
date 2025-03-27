package net.torode.globals;

import net.kyori.adventure.text.Component;

import static net.kyori.adventure.text.format.NamedTextColor.RED;

public final class Messages
{
    public static final Component GENERIC_NO_PERMISSION = Component.text("You do not have permission to do this.", RED);

    public static final Component CONSOLE_BLOCKED = Component.text("This operation is not supported in console.", RED);

    public static final Component COMMAND_NO_PERMISSION = Component.text("You do not have permission to execute this command.", RED);
    public static final Component COMMAND_INVALID_TARGET = Component.text("Invalid target.", RED);

    public static final Component COMMAND_GRANT_USAGE = Component.text("Usage: /grant [target] [rank] (duration) (server)");

    public static final Component COMMAND_GAMEMODE_USAGE = Component.text("Usage: /gamemode [target]", RED);

    public static final Component COMMAND_CONFIRM_FAILED = Component.text("You do not have any commands waiting.", RED);

    public static final Component COMMAND_RANKS_USAGE = Component.text("Usage:\n- /ranks delete [rank]", RED);
    public static final Component COMMAND_RANKS_DELETE_USAGE = Component.text("Usage: /ranks delete [rank]", RED);

    private Messages()
    {
    }
}
