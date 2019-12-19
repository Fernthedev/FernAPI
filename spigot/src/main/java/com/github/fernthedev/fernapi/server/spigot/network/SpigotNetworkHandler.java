package com.github.fernthedev.fernapi.server.spigot.network;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.network.IServerInfo;
import com.github.fernthedev.fernapi.universal.exceptions.FernDebugException;
import com.github.fernthedev.fernapi.universal.handlers.NetworkHandler;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class SpigotNetworkHandler implements NetworkHandler {
    @Override
    public boolean isRegistered(Object sender) {
        return true;
    }

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
}
