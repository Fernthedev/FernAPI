package com.github.fernthedev.fernapi.server.velocity.player;

import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import com.github.fernthedev.fernapi.universal.data.chat.ChatColor;
import com.github.fernthedev.fernapi.universal.handlers.IFPlayer;
import com.velocitypowered.api.proxy.Player;
import lombok.Getter;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;

import java.net.InetSocketAddress;
import java.util.Collection;

public class VelocityFPlayer extends IFPlayer{
    @Getter
    private Player player;

    public VelocityFPlayer(Player player) {
        super(player.getUsername(), player.getUniqueId());
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
        TextComponent fullMessage = TextComponent.of(ChatColor.translateAlternateColorCodes('&',baseMessage.getParentText()));

        for(BaseMessage be : baseMessage.getExtra()) {
            TextComponent te = TextComponent.of(ChatColor.translateAlternateColorCodes('&',be.toPlainText()));

            if (be.getClickData() != null) {
                te.clickEvent(ClickEvent.of(
                        ClickEvent.Action.valueOf(be.getClickData().getAction().toString()),
                        be.getClickData().getClickValue()));
            }

            if (be.getHoverData() != null) {
                te.hoverEvent(HoverEvent.of(
                        HoverEvent.Action.valueOf(be.getHoverData().getAction().toString()),
                        message(be.getHoverData().getHoverValue())));
            }

            fullMessage.append(te);
        }

        player.sendMessage(fullMessage);
    }

    @Override
    public InetSocketAddress getAddress() {
        return player.getRemoteAddress();
    }

    private TextComponent message(String text) {
        return TextComponent.of(ChatColor.translateAlternateColorCodes('&',text));
    }
}
