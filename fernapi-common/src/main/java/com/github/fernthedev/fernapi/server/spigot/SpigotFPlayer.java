package com.github.fernthedev.fernapi.server.spigot;

import com.github.fernthedev.fernapi.universal.data.chat.ChatMessage;
import com.github.fernthedev.fernapi.universal.handlers.IFPlayer;
import net.md_5.bungee.api.chat.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SpigotFPlayer implements IFPlayer{
    private Player player;
    public SpigotFPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void sendChatMessage(ChatMessage chatMessage) {
        TextComponent prefix = new TextComponent(
                ChatColor.translateAlternateColorCodes('&',chatMessage.getMessage())
        );
        
        if(chatMessage.getClickData() != null)
        prefix.setClickEvent(new ClickEvent(
                ClickEvent.Action.valueOf(chatMessage.getClickData().getClickAction().toString()),
                chatMessage.getClickData().getClickValue()));
        
        if(chatMessage.getHoverData() != null) {

            prefix.setHoverEvent(new HoverEvent(
                    HoverEvent.Action.valueOf(chatMessage.getHoverData().getHoverAction().toString()),
                    message(chatMessage.getHoverData().getHoverValue())));
        }
        
        player.spigot().sendMessage(prefix);
    }

    private BaseComponent[] message(String text) {
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',text)).create();
    }
}
