package com.github.fernthedev.fernapi.universal.api;

import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;

import java.util.Collection;

public interface CommandSender {

    /**
     * Checks if this user has the specified permission node.
     *
     * @param permission the node to check
     * @return whether they have this node
     */
    public boolean hasPermission(String permission);

    /**
     * Set a permission node for this user.
     *
     * @param permission the node to set
     * @param value the value of the node
     */
    public void setPermission(String permission, boolean value);

    /**
     * Get all Permissions which this CommandSender has
     *
     * @return a unmodifiable Collection of Strings which represent their
     * permissions
     */
    public Collection<String> getPermissions();

    public void sendMessage(BaseMessage message);

}
