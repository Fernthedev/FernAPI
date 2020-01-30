package com.github.fernthedev.fernapi.server.velocity.network;

import com.github.fernthedev.fernapi.server.velocity.FernVelocityAPI;
import com.github.fernthedev.fernapi.server.velocity.interfaces.VelocityServerInfo;
import com.github.fernthedev.fernapi.universal.data.network.IServerInfo;
import com.github.fernthedev.fernapi.universal.handlers.NetworkHandler;
import com.velocitypowered.api.proxy.ServerConnection;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class VelocityNetworkHandler implements NetworkHandler {
    @Override
    public boolean isRegistered(Object sender) {
        return sender instanceof ServerConnection;
    }

    @NonNull
    private FernVelocityAPI plugin;

    @Override
    public Map<String, IServerInfo> getServers() {
        Map<String, IServerInfo> map = new HashMap<>();

        plugin.getServer().getAllServers().forEach((serverInfo) -> map.put(serverInfo.getServerInfo().getName(), new VelocityServerInfo(serverInfo)));
        return map;
    }

    @Override
    public IServerInfo getServer(String name) {
        return new VelocityServerInfo(plugin.getServer().getServer(name).get());
    }
}
