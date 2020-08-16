package com.github.fernthedev.fernapi.server.spigot.player;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import com.github.fernthedev.fernapi.universal.data.network.IServerInfo;
import com.github.fernthedev.fernapi.universal.handlers.NetworkHandler;
import net.kyori.adventure.audience.Audience;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.UUID;

public class SpigotFPlayer extends IFPlayer<Player> {

    public SpigotFPlayer(Player player, Audience audience) {
        super(player == null ? null : player.getName(),player == null ? null : player.getUniqueId(), player, audience);
    }

    public SpigotFPlayer(Player player, @Nullable String name, @Nullable UUID uuid, Audience audience) {
        super(player == null ? name : player.getName(), player == null ? uuid : player.getUniqueId(), player, audience);
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
    public InetSocketAddress getAddress() {
        return player.getAddress();
    }

    @Override
    public long getPing() {
        try {
            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            return (int) entityPlayer.getClass().getField("ping").get(entityPlayer);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void sendMessage(BaseMessage baseMessage) {
        BaseComponent fullMessage = (BaseComponent) Universal.getChatHandler().parseComponent(baseMessage);

        player.spigot().sendMessage(fullMessage);
    }

    @Override
    public IServerInfo getServerInfo() {
        if (player == null) return null;

        return ((NetworkHandler<Server>) Universal.getNetworkHandler()).toServer(player.getServer());
    }

    private BaseComponent[] message(String text) {
        return new ComponentBuilder(ChatColor.translateAlternateColorCodes('&',text)).create();
    }

    @Override
    public boolean isVanished() {
        for (MetadataValue meta : player.getMetadata("vanished")) {
            if (meta.asBoolean()) return true;
        }
        return false;
    }


    @Override
    public boolean canSee(IFPlayer<?> player) {
        return this.player.canSee((Player) player.getPlayer());
    }
}
