package com.github.fernthedev.fernapi.server.sponge.network;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.network.IServerInfo;
import com.github.fernthedev.fernapi.universal.handlers.NetworkHandler;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;

import java.util.HashMap;
import java.util.Map;

public class SpongeNetworkHandler implements NetworkHandler<Server> {
    @Override
    public boolean isRegistered(Object sender) {
        return true;
    }

    private SpongeServerInfo serverInfo = new SpongeServerInfo(Sponge.getServer());

    @Override
    public Map<String, IServerInfo> getServers() {
        Map<String, IServerInfo> map = new HashMap<>();


        return map;
    }

    @Override
    public IServerInfo getServer(String name) {
        Universal.debug("NetworkHandler.getServer() does not apply to Bukkit \n");

        if (Universal.isDebug()) new RuntimeException().printStackTrace();

        return serverInfo;
    }

    @Override
    public IServerInfo toServer(Server server) {
        return serverInfo;
    }
}
