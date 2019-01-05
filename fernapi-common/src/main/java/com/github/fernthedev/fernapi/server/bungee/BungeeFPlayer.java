package com.github.fernthedev.fernapi.server.bungee;

import com.github.fernthedev.fernapi.universal.data.chat.ChatMessage;
import com.github.fernthedev.fernapi.universal.handlers.IFPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeeFPlayer implements IFPlayer{
    private ProxiedPlayer player;
    public BungeeFPlayer(ProxiedPlayer player) {
        this.player = player;
    }

    @Override
    public void sendChatMessage(ChatMessage chatMessage) {
        TextComponent prefix = new TextComponent(
                ChatColor.translateAlternateColorCodes('&',chatMessage.getMessage())
        );

        if(chatMessage.getClickData() != null)
            prefix.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(
                    ClickEvent.Action.valueOf(chatMessage.getClickData().getClickAction().toString()),
                    chatMessage.getClickData().getClickValue()));

        if(chatMessage.getHoverData() != null) {

            prefix.setHoverEvent(new net.md_5.bungee.api.chat.HoverEvent(
                    HoverEvent.Action.valueOf(chatMessage.getHoverData().getHoverAction().toString()),
                    message(chatMessage.getHoverData().getHoverValue())));
        }

        player.sendMessage(prefix);
    }

    private BaseComponent[] message(String text) {
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',text)).create();
    }
}
