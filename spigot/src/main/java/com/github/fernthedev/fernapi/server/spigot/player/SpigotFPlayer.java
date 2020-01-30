package com.github.fernthedev.fernapi.server.spigot.player;

import com.github.fernthedev.fernapi.server.spigot.pluginhandlers.VaultHandler;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import net.md_5.bungee.api.chat.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class SpigotFPlayer extends IFPlayer<Player> {

    public SpigotFPlayer(Player player) {
        super(player == null ? null : player.getName(),player == null ? null : player.getUniqueId(), player);
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
        if(VaultHandler.isHooked()) {
            if (value) {
                VaultHandler.getPermissions().playerAdd(player, permission);
            } else {
                VaultHandler.getPermissions().playerRemove(player, permission);
            }
        }else{
            HashMap<UUID, PermissionAttachment> perms = new HashMap<>();
            PermissionAttachment attachment = player.addAttachment((Plugin) Universal.getPlugin());
            perms.put(player.getUniqueId(), attachment);
            PermissionAttachment pperms = perms.get(player.getUniqueId());
            pperms.setPermission(permission, value);
        }
    }

    /**
     * Get all Permissions which this CommandSender has
     *
     * @return a unmodifiable Collection of Strings which represent their
     * permissions
     */
    @Override
    public Collection<String> getPermissions() {
        HashMap<UUID, PermissionAttachment> perms = new HashMap<>();
        PermissionAttachment attachment = player.addAttachment((Plugin) Universal.getPlugin());
        perms.put(player.getUniqueId(), attachment);
        PermissionAttachment pperms = perms.get(player.getUniqueId());
        return pperms.getPermissions().keySet();
    }

    @Override
    public void sendMessage(BaseMessage baseMessage) {
//
//        TextComponent fullMessage = new TextComponent(ChatColor.translateAlternateColorCodes('&',baseMessage.getParentText()));
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
//                            message(be.getHoverData().getHoverValue())));
//                }
//
//                fullMessage.addExtra(te);
//            }
//        }

        BaseComponent fullMessage = (BaseComponent) Universal.getChatHandler().parseComponent(baseMessage);

        player.spigot().sendMessage(fullMessage);


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
    public String getCurrentServerName() {
        return player.getServer().getName();
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
}
