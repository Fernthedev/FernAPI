package com.github.fernthedev.fernapi.server.bungee.player;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import com.github.fernthedev.fernapi.universal.data.network.IServerInfo;
import com.github.fernthedev.fernapi.universal.handlers.NetworkHandler;
import lombok.NonNull;
import net.kyori.adventure.audience.Audience;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.InetSocketAddress;

public class BungeeFPlayer extends IFPlayer<ProxiedPlayer> {

    public BungeeFPlayer(ProxiedPlayer player, Audience audience) {
        super(player == null ? null : player.getName(),player == null ? null : player.getUniqueId(), player, audience);
    }

//    public BungeeFPlayer(ProxiedPlayer player, @Nullable String name, @Nullable UUID uuid) {
//        super(player == null ? name : player.getName(), player == null ? uuid : player.getUniqueId(), player);
//    }



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
    public void sendMessage(@NonNull BaseMessage baseMessage) {
//        String text = baseMessage.getParentText();
//
//        if (text == null) text = "";
//
//        TextComponent fullMessage = new TextComponent(ChatColor.translateAlternateColorCodes('&', baseMessage.getParentText()));
//
//        if (baseMessage.getExtra() != null) {
//            for(BaseMessage be : baseMessage.getExtra()) {
//                TextComponent te = new TextComponent(ChatColor.translateAlternateColorCodes('&',be.toPlainText()));
//
//                if (be.getClickData() != null) {
//                    te.setClickEvent(new ClickEvent(
//                            ClickEvent.Action.valueOf(be.getClickData().getAction().toString()),
//                            be.getClickData().getClickValue()));
//                }
//
//                if (be.getHoverData() != null) {
//                    te.setHoverEvent(new HoverEvent(
//                            HoverEvent.Action.valueOf(be.getHoverData().getAction().toString()),
//                            new ComponentBuilder(be.getHoverData().getHoverValue()).create()));
//                }
//
//                fullMessage.addExtra(te);
//            }
//        }

        BaseComponent fullMessage = (BaseComponent) Universal.getChatHandler().parseComponent(baseMessage);

        player.sendMessage(fullMessage);
    }

    @Override
    public InetSocketAddress getAddress() {
        return player.getAddress();
    }

    @Override
    public long getPing() {
        return player.getPing();
    }

    @Override
    public IServerInfo getServerInfo() {
        if (player == null || player.getServer() == null || player.getServer().getInfo() == null) return null;

        return ((NetworkHandler<ServerInfo>) Universal.getNetworkHandler()).toServer(player.getServer().getInfo());
    }

    private BaseComponent[] message(String text) {
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',text)).create();
    }
}
