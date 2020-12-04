package com.github.fernthedev.fernapi.universal.handlers;

import com.github.fernthedev.fernapi.universal.data.network.IServerInfo;

import java.util.Map;

public interface NetworkHandler<T> {
    boolean isRegistered(Object sender);

    Map<String, IServerInfo> getServers();

    IServerInfo getServer(String name);

    /**
     * Converts server-specific ServerInfo
     * into a cross-server compatible abstracted
     * instance
     *
     * @param server server instance
     * @return abstract server data
     */
    IServerInfo toServer(Object server);

    /**
     * Convenience method for type-checking
     * @param server type checked server
     * @return abstract server data
     */
    default IServerInfo toServerTyped(T server) {
        return toServer(server);
    }
}
