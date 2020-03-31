package com.github.fernthedev.fernapi.server.bungee.network;

import com.github.fernthedev.fernapi.universal.data.network.IServerInfo;
import com.github.fernthedev.fernapi.universal.handlers.NetworkHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.HashMap;
import java.util.Map;

public class BungeeNetworkHandler implements NetworkHandler<ServerInfo> {
    @Override
    public boolean isRegistered(Object sender) {
        return true;
    }

    @Override
    public Map<String, IServerInfo> getServers() {
        Map<String, IServerInfo> map = new HashMap<>();

        ProxyServer.getInstance().getServers().forEach((s, serverInfo) -> map.put(s, new BungeeServerInfo(serverInfo)));
        return map;
    }

    @Override
    public IServerInfo getServer(String name) {
        return new BungeeServerInfo(ProxyServer.getInstance().getServerInfo(name));
    }

    @Override
    public IServerInfo toServer(ServerInfo server) {
        return new BungeeServerInfo(server);
    }
}
