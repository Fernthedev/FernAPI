package com.github.fernthedev.fernapi.universal.data;

import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import com.github.fernthedev.fernapi.universal.handlers.IFPlayer;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class JSONPlayer extends IFPlayer {

    public JSONPlayer(String name, UUID uuid) {
        super(name,uuid);
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

    /**
     * Set a permission node for this user.
     *
     * @param permission the node to set
     * @param value      the value of the node
     */
    @Override
    public void setPermission(String permission, boolean value) {

    }

    /**
     * Get all Permissions which this CommandSender has
     *
     * @return a unmodifiable Collection of Strings which represent their
     * permissions
     */
    @Override
    public Collection<String> getPermissions() {
        return null;
    }

    @Override
    public void sendMessage(BaseMessage textMessage) {
        throw new IllegalArgumentException("Do not call this method, it is useless. Convert it to player instance or a different FPlayer instance");
    }

    @Override
    public InetSocketAddress getAddress() {
        return null;
    }
}
