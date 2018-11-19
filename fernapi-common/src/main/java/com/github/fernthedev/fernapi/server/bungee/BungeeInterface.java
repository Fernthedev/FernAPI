package com.github.fernthedev.fernapi.server.bungee;

import com.github.fernthedev.fernapi.universal.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.MethodInterface;
import com.github.fernthedev.fernapi.universal.ServerType;

import java.util.logging.Logger;

public class BungeeInterface implements MethodInterface {
    private FernBungeeAPI fernBungeeAPI;

    BungeeInterface(FernBungeeAPI fernBungeeAPI) {
        this.fernBungeeAPI = fernBungeeAPI;
    }

    @Override
    public Logger getLogger() {
        return fernBungeeAPI.getLogger();
    }

    @Override
    public ServerType getServeType() {
        return ServerType.BUNGEE;
    }

    @Override
    public FernAPIPlugin getInstance() {
        return fernBungeeAPI;
    }
}
