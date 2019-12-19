package com.github.fernthedev.fernapi.server.sponge.network;

import com.github.fernthedev.fernapi.universal.api.CommandSender;
import com.github.fernthedev.fernapi.universal.data.network.IServerInfo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;

import java.net.InetSocketAddress;

@RequiredArgsConstructor
public class SpongeServerInfo implements IServerInfo {

    @NonNull
    private Server server;

    private final InetSocketAddress address = Sponge.getServer().getBoundAddress().get();

    /**
     * Get the name of this server.
     *
     * @return the configured name for this server address
     */
    @Override
    public String getName() {
        return server.getDefaultWorldName();
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
        return address;
    }

    /**
     * Returns the MOTD which should be used when this server is a forced host.
     *
     * @return the motd
     */
    @Override
    public String getMotd() {
        return server.getMotd().toString();
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
    public boolean canAccess(CommandSender sender) {
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
        throw new IllegalStateException("Not applicable to Sponge");
    }
}
