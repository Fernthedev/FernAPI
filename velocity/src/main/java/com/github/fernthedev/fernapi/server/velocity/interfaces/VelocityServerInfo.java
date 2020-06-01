package com.github.fernthedev.fernapi.server.velocity.interfaces;

import co.aikar.commands.CommandIssuer;
import com.github.fernthedev.fernapi.server.velocity.player.VelocityFPlayer;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.network.IServerInfo;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import com.velocitypowered.api.proxy.server.ServerPing;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class VelocityServerInfo implements RegisteredServer, IServerInfo {

    @NonNull
    private RegisteredServer serverInfo;

    /**
     * Get the name of this server.
     *
     * @return the configured name for this server address
     */
    @Override
    public String getName() {
        return serverInfo.getServerInfo().getName();
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
        return serverInfo.getServerInfo().getAddress();
    }

    /**
     * Get the set of all players on this server.
     *
     * @return an unmodifiable collection of all players on this server
     */
    @Override
    public List<VelocityFPlayer> getPlayers() {
        return serverInfo.getPlayersConnected()
                .stream().map(player -> Universal.getMethods().convertPlayerObjectToFPlayer(player))
                .map(playerIFPlayer -> (VelocityFPlayer) playerIFPlayer)
                .collect(Collectors.toList());
    }

    /**
     * Returns the MOTD which should be used when this server is a forced host.
     *
     * @return the motd
     */
    @Override
    public String getMotd() {
        try {
            return serverInfo.ping().get().getDescription().toString();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Whether this server is restricted and therefore only players with the
     * given permission can access it.
     *
     * @return if restricted
     */
    @Override
    public boolean isRestricted() {
        return false;
    }

    /**
     * Get the permission required to access this server. Only enforced when the
     * server is restricted.
     *
     * @return access permission
     */
    @Override
    public String getPermission() {
        return "";
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
        return true;
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
        serverInfo.sendPluginMessage(MinecraftChannelIdentifier.forDefaultNamespace(channel), data);
    }

    /**
     * Returns the {@link ServerInfo} for this server.
     *
     * @return the server info
     */
    @Override
    public ServerInfo getServerInfo() {
        return serverInfo.getServerInfo();
    }

    /**
     * Returns a list of all the players currently connected to this server on this proxy.
     *
     * @return the players on this proxy
     */
    @Override
    public Collection<Player> getPlayersConnected() {
        return serverInfo.getPlayersConnected();
    }

    /**
     * Attempts to ping the remote server and return the server list ping result.
     *
     * @return the server ping result from the server
     */
    @Override
    public CompletableFuture<ServerPing> ping() {
        return serverInfo.ping();
    }

    /**
     * Sends a plugin message to this target.
     *
     * @param identifier the channel identifier to send the message on
     * @param data       the data to send
     * @return whether or not the message could be sent
     */
    @Override
    public boolean sendPluginMessage(ChannelIdentifier identifier, byte[] data) {
        return serverInfo.sendPluginMessage(identifier, data);
    }
}
