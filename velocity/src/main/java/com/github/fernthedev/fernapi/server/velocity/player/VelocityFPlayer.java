package com.github.fernthedev.fernapi.server.velocity.player;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import com.github.fernthedev.fernapi.universal.data.network.IServerInfo;
import com.github.fernthedev.fernapi.universal.handlers.NetworkHandler;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import net.kyori.text.TextComponent;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.UUID;

public class VelocityFPlayer extends IFPlayer<Player> {

    public VelocityFPlayer(Player player) {
        super(player == null ? null : player.getUsername(),player == null ? null : player.getUniqueId(), player);
    }

    public VelocityFPlayer(Player player, @Nullable String name, @Nullable UUID uuid) {
        super(player == null ? name : player.getUsername(), player == null ? uuid : player.getUniqueId(), player);
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

    @Override
    public void sendMessage(BaseMessage baseMessage) {
//        TextComponent fullMessage = TextComponent.of(ChatColor.translateAlternateColorCodes('&',baseMessage.getParentText()));
//
//        if (baseMessage.getExtra() != null) {
//            for(BaseMessage be : baseMessage.getExtra()) {
//                TextComponent te = TextComponent.of(ChatColor.translateAlternateColorCodes('&',be.toPlainText()));
//
//                if (be.getClickData() != null) {
//                    te.clickEvent(ClickEvent.of(
//                            ClickEvent.Action.valueOf(be.getClickData().getAction().toString()),
//                            be.getClickData().getClickValue()));
//                }
//
//                if (be.getHoverData() != null) {
//                    te.hoverEvent(HoverEvent.of(
//                            HoverEvent.Action.valueOf(be.getHoverData().getAction().toString()),
//                            message(be.getHoverData().getHoverValue())));
//                }
//
//                fullMessage.append(te);
//            }
//        }
        TextComponent fullMessage = (TextComponent) Universal.getChatHandler().parseComponent(baseMessage);


        player.sendMessage(fullMessage);
    }

    @Override
    public InetSocketAddress getAddress() {
        return player.getRemoteAddress();
    }

    @Override
    public long getPing() {
        return player.getPing();
    }

    @Override
    public IServerInfo getServerInfo() {
        return ((NetworkHandler< ServerConnection>) Universal.getNetworkHandler()).toServer(player.getCurrentServer().orElse(null));
    }
}
