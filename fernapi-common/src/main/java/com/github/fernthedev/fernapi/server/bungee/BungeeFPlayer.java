package com.github.fernthedev.fernapi.server.bungee;

import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
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
    public void sendChatMessage(BaseMessage baseMessage) {
        TextComponent fullMessage = new TextComponent(baseMessage.getParentText());

        for(BaseMessage be : baseMessage.getExtra()) {
            TextComponent te = new TextComponent(ChatColor.translateAlternateColorCodes('&',be.toPlainText()));

            if (be.getClickData() != null) {
                te.setClickEvent(new ClickEvent(
                        ClickEvent.Action.valueOf(be.getClickData().getAction().toString()),
                        be.getClickData().getClickValue()));
            }

            if (be.getHoverData() != null) {
                te.setHoverEvent(new HoverEvent(
                        HoverEvent.Action.valueOf(be.getHoverData().getAction().toString()),
                        message(be.getHoverData().getHoverValue())));
            }

            fullMessage.addExtra(te);
        }

        player.sendMessage(fullMessage);
    }

    private BaseComponent[] message(String text) {
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',text)).create();
    }
}
