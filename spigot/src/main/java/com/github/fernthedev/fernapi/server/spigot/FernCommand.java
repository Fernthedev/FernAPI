package com.github.fernthedev.fernapi.server.spigot;


import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;

import java.util.ArrayList;
import java.util.List;

public abstract class FernCommand implements CommandExecutor {
    protected String message(String text) {
        return ChatColor.translateAlternateColorCodes('&',text);
    }


    /**
     * Allows you to make autocomplete only suggest based off what is written
     * @param arg The argument currently used
     * @param possibilities All of the possibilities
     * @return The auto-complete possibilities
     */
    public List<String> search(String arg, List<String> possibilities) {
        List<String> newPos = new ArrayList<>();
        possibilities.forEach(s -> {
            if(s.startsWith(arg) || s.contains(arg)) {
                newPos.add(s);
            }
        });
        return newPos;
    }
}
