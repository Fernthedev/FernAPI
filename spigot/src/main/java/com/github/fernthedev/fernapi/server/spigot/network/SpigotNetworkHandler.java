package com.github.fernthedev.fernapi.server.spigot.network;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.network.IServerInfo;
import com.github.fernthedev.fernapi.universal.exceptions.FernDebugException;
import com.github.fernthedev.fernapi.universal.handlers.NetworkHandler;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Server;

import java.util.HashMap;
import java.util.Map;

public class SpigotNetworkHandler implements NetworkHandler<Server> {
    @Override
    public boolean isRegistered(Object sender) {
        return true;
    }

    @Getter
    private SpigotServerInfo serverInfo = new SpigotServerInfo(Bukkit.getServer());

    @Override
    public Map<String, IServerInfo> getServers() {
        Map<String, IServerInfo> map = new HashMap<>();

        if (Universal.isDebug()) new FernDebugException("NetworkHandler.getServers() does not apply to Sponge. (This can be ignored, though it should be noted)").printStackTrace();

        return map;
    }

    @Override
    public IServerInfo getServer(String name) {


        if (Universal.isDebug()) new FernDebugException("NetworkHandler.getServer() does not apply to Sponge. (This can be ignored, though it should be noted)").printStackTrace();

        return serverInfo;
    }

    @Override
    public IServerInfo toServer(Server server) {
        return serverInfo;
    }
}
