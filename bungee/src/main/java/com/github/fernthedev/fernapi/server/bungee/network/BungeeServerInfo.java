package com.github.fernthedev.fernapi.server.bungee.network;

import co.aikar.commands.CommandIssuer;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.network.IServerInfo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetSocketAddress;

@RequiredArgsConstructor
public class BungeeServerInfo implements IServerInfo {

    @NonNull
    private final ServerInfo serverInfo;

    /**
     * Get the name of this server.
     *
     * @return the configured name for this server address
     */
    @Override
    public String getName() {
        return serverInfo.getName();
    }

    /**
     * Gets the connectable host + port pair for this server. Implementations
     * expect this to be used as the unique identifier per each instance of this
     * class.
     *
     * @return the IP and port pair for this server
     */
    @Override
    public InetSocketAddress getAddress() {
        return serverInfo.getAddress();
    }

    /**
     * Returns the MOTD which should be used when this server is a forced host.
     *
     * @return the motd
     */
    @Override
    public String getMotd() {
        return serverInfo.getMotd();
    }

    /**
     * Whether this server is restricted and therefore only players with the
     * given permission can access it.
     *
     * @return if restricted
     */
    @Override
    public boolean isRestricted() {
        return serverInfo.isRestricted();
    }

    /**
     * Get the permission required to access this server. Only enforced when the
     * server is restricted.
     *
     * @return access permission
     */
    @Override
    public String getPermission() {
        return serverInfo.getPermission();
    }

    /**
     * Whether the player can access this server. It will only return false when
     * the player has no permission and this server is restricted.
     *
     * @param sender the player to check access for
     * @return whether access is granted to this server
     */
    @Override
    public boolean canAccess(CommandIssuer sender) {
        return serverInfo.canAccess((net.md_5.bungee.api.CommandSender) Universal.getMethods().convertCommandSenderToAPISender(sender));
    }

    /**
     * Send data by any available means to this server. This data may be queued
     * and there is no guarantee of its timely arrival.
     * <p>
     * In recent Minecraft versions channel names must contain a colon separator
     * and consist of [a-z0-9/._-]. This will be enforced in a future version.
     * The "BungeeCord" channel is an exception and may only take this form.
     *
     * @param channel the channel to send this data via
     * @param data    the data to send
     */
    @Override
    public void sendData(String channel, byte[] data) {
        serverInfo.sendData(channel, data);
    }
}
