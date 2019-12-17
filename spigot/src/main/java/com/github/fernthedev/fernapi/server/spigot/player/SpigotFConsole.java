package com.github.fernthedev.fernapi.server.spigot.player;

import com.github.fernthedev.fernapi.universal.api.IFConsole;
import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;

public class SpigotFConsole extends IFConsole {
    private org.bukkit.command.CommandSender commandSender;

    public SpigotFConsole(org.bukkit.command.CommandSender commandSender) {
        this.commandSender = commandSender;
    }

    /**
     * Checks if this user has the specified permission node.
     *
     * @param permission the node to check
     * @return whether they have this node
     */
    @Override
    public boolean hasPermission(String permission) {
        return commandSender.hasPermission(permission);
    }

    @Override
    public void sendMessage(BaseMessage message) {

    }
}
