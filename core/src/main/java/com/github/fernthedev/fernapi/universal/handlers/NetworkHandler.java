package com.github.fernthedev.fernapi.universal.handlers;

import com.github.fernthedev.fernapi.universal.data.network.IServerInfo;

import java.util.Map;

public interface NetworkHandler<T> {
    boolean isRegistered(Object sender);

    Map<String, IServerInfo> getServers();

    IServerInfo getServer(String name);

    IServerInfo toServer(T server);
}
