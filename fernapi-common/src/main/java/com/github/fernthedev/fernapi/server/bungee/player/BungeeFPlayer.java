package com.github.fernthedev.fernapi.server.bungee.player;

import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import com.github.fernthedev.fernapi.universal.handlers.IFPlayer;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.InetSocketAddress;
import java.util.Collection;

public class BungeeFPlayer extends IFPlayer{
    @Getter
    private ProxiedPlayer player;

    public BungeeFPlayer(ProxiedPlayer player) {
        super(player.getName(),player.getUniqueId());
        this.player = player;
    }

    /**
     * Checks if this user has the specified permission node.
     *
     * @param permission the node to check
     * @return whether they have this node
     */
    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    /**
     * Set a permission node for this user.
     *
     * @param permission the node to set
     * @param value      the value of the node
     */
    @Override
    public void setPermission(String permission, boolean value) {

    }

    /**
     * Get all Permissions which this CommandSender has
     *
     * @return a unmodifiable Collection of Strings which represent their
     * permissions
     */
    @Override
    public Collection<String> getPermissions() {
        return null;
    }

    @Override
    public void sendMessage(BaseMessage baseMessage) {
        TextComponent fullMessage = new TextComponent(ChatColor.translateAlternateColorCodes('&',baseMessage.getParentText()));

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

    @Override
    public InetSocketAddress getAddress() {
        return player.getAddress();
    }

    private BaseComponent[] message(String text) {
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',text)).create();
    }
}
