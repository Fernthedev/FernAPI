package com.github.fernthedev.fernapi.server.spigot.player;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.IFConsole;
import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.ConsoleCommandSender;

public class SpigotFConsole extends IFConsole<ConsoleCommandSender> {


    public SpigotFConsole(ConsoleCommandSender commandSender) {
        super(commandSender);
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
        BaseComponent baseComponent = (BaseComponent) Universal.getChatHandler().parseComponent(message);

        commandSender.spigot().sendMessage(baseComponent);
    }
}
