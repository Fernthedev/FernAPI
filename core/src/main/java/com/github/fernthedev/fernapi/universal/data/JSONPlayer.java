package com.github.fernthedev.fernapi.universal.data;

import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import com.github.fernthedev.fernapi.universal.data.network.IServerInfo;
import lombok.Getter;

import java.net.InetSocketAddress;
import java.util.UUID;

@Getter
public class JSONPlayer extends IFPlayer<Object> {

    public JSONPlayer(String name, UUID uuid) {
        super(name, uuid, null);
    }

    public JSONPlayer() {
        super(null, null, null);
    }

    /**
     * Checks if this user has the specified permission node.
     *
     * @param permission the node to check
     * @return whether they have this node
     */
    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

    @Override
    public InetSocketAddress getAddress() {
        return null;
    }

    @Override
    public void sendMessage(BaseMessage textMessage) {
        throw new IllegalArgumentException("Do not call this method, it is useless. Convert it to player instance or a different FPlayer instance");
    }

    @Override
    public long getPing() {
        return -1;
    }

    @Override
    public IServerInfo getServerInfo() {
        return null;
    }
}
