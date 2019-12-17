package com.github.fernthedev.fernapi.universal.data.network;


import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.CommandSender;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * Class used to represent a server to connect to.
 */

/**
 * From net.md_5.bungee.api.config.ServerInfo
 */
public interface IServerInfo {

    /**
     * Get the name of this server.
     *
     * @return the configured name for this server address
     */
    String getName();

    /**
     * Gets the connectable host + port pair for this server. Implementations
     * expect this to be used as the unique identifier per each instance of this
     * class.
     *
     * @return the IP and port pair for this server
     */
    InetSocketAddress getAddress();

    /**
     * Get the set of all players on this server.
     *
     * @return an unmodifiable collection of all players on this server
     */
    default Collection<IFPlayer> getPlayers() {
        return Universal.getMethods().getPlayers();
    }

    /**
     * Returns the MOTD which should be used when this server is a forced host.
     *
     * This can cause long wait times in Velocity
     *
     * @return the motd
     */
    String getMotd();

    /**
     * Whether this server is restricted and therefore only players with the
     * given permission can access it.
     *
     * @return if restricted
     */
    boolean isRestricted();

    /**
     * Get the permission required to access this server. Only enforced when the
     * server is restricted.
     *
     * @return access permission
     */
    String getPermission();

    /**
     * Whether the player can access this server. It will only return false when
     * the player has no permission and this server is restricted.
     *
     * @param sender the player to check access for
     * @return whether access is granted to this server
     */
    boolean canAccess(CommandSender sender);

    /**
     * Send data by any available means to this server. This data may be queued
     * and there is no guarantee of its timely arrival.
     *
     * In recent Minecraft versions channel names must contain a colon separator
     * and consist of [a-z0-9/._-]. This will be enforced in a future version.
     * The "BungeeCord" channel is an exception and may only take this form.
     *
     * @param channel the channel to send this data via
     * @param data the data to send
     */
    void sendData(String channel, byte[] data);

    default void sendData(@NotNull PluginMessageData data) {
        data.setServer(this);
        Universal.getMessageHandler().sendPluginData(data);
    }

}
