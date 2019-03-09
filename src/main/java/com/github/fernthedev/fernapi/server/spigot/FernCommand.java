package com.github.fernthedev.fernapi.server.spigot;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;

public abstract class FernCommand implements CommandExecutor {
    protected String message(String text) {
        return ChatColor.translateAlternateColorCodes('&',text);
    }
}