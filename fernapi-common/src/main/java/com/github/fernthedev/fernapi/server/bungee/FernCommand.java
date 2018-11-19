package com.github.fernthedev.fernapi.server.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public abstract class FernCommand extends Command {
    /**
     * Construct a new command with no permissions or aliases.
     *
     * @param name the name of this command
     */
    public FernCommand(String name) {
        super(name);
    }

    /**
     * Construct a new command.
     *
     * @param name       primary name of this command
     * @param permission the permission node required to execute this command,
     *                   null or empty string allows it to be executed by everyone
     * @param aliases    aliases which map back to this command
     */
    public FernCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    protected BaseComponent message(String text) {
        return new TextComponent(ChatColor.translateAlternateColorCodes('&',text));
    }
}
