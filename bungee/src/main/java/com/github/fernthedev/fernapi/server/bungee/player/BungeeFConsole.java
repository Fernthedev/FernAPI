package com.github.fernthedev.fernapi.server.bungee.player;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.IFConsole;
import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.checkerframework.checker.nullness.qual.NonNull;

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

    /**
     * Sends a chat message.
     *
     * @param message a message
     * @param type    the type
     * @see Component
     * @since 4.0.0
     */
    @Override
    public void sendMessage(@NonNull Component message, @NonNull MessageType type) {
        
    }

    private BaseComponent[] message(String text) {
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',text)).create();
    }
}
