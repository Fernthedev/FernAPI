package com.github.fernthedev.fernapi.universal.api;

import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import com.github.fernthedev.fernapi.universal.data.network.IServerInfo;

import java.net.InetSocketAddress;
import java.util.UUID;

public class OfflineFPlayer<P> extends IFPlayer<IFPlayer<P>> {

    public OfflineFPlayer() {
        super(null, null, null);
    }

    public OfflineFPlayer(IFPlayer<P> player) {
        super(player.getName(), player.getUuid(), player);
    }

    public OfflineFPlayer(String name, UUID uuid, IFPlayer<P> player) {
        super(name, uuid, player);
    }

    public OfflineFPlayer(String name) {
        super(name, null, null);
    }

    public OfflineFPlayer(UUID uuid) {
        super(null, uuid, null);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUniqueId(UUID uniqueId) {
        this.uuid = uniqueId;
    }

    /**
     * Returns true if all data is null
     * If player is null but name and uuid aren't, this returns false
     *
     * @return
     */
    @Override
    public boolean isPlayerNull() {
        return player == null;
    }

    /**
     * Is online
     * @return
     */
    public boolean isOnline() {
        return player != null;
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
        return player.getServerInfo();
    }

    @Override
    public void sendMessage(BaseMessage message) {
        player.sendMessage(message);
    }

    @Override
    public IFPlayer<P> getPlayer() {
        return player;
    }

    @Override
    public String getCurrentServerName() {
        return player.getCurrentServerName();
    }

    @Override
    public boolean isVanished() {
        return player.isVanished();
    }

    @Override
    public boolean canSee(IFPlayer<?> player) {
        return player.canSee(player);
    }

    /**
     * Has permission node
     *
     * @param permission
     * @return
     */
    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    /**
     * Is this issue a player, or server/console sender
     *
     * @return
     */
    @Override
    public boolean isPlayer() {
        return player.isPlayer();
    }

    @Override
    public String toString() {
        return "OfflineFPlayer{" +
                "name='" + name + '\'' +
                ", uuid=" + uuid +
                ", player=" + player +
                '}';
    }
}
