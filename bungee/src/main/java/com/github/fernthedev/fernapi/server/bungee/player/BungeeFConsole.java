package com.github.fernthedev.fernapi.server.bungee.player;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.IFConsole;
import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import net.kyori.adventure.audience.Audience;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class BungeeFConsole extends IFConsole<CommandSender> {


    public BungeeFConsole(CommandSender commandSender, Audience audience) {
        super(commandSender, audience);
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
    public void sendMessage(BaseMessage baseMessage) {
        BaseComponent fullMessage = (BaseComponent) Universal.getChatHandler().parseComponent(baseMessage);

        ProxyServer.getInstance().getConsole().sendMessage(fullMessage);
    }


    private BaseComponent[] message(String text) {
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',text)).create();
    }
}
