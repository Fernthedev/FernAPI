package com.github.fernthedev.fernapi.server.spigot;

import com.github.fernthedev.fernapi.universal.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.MethodInterface;
import com.github.fernthedev.fernapi.universal.ServerType;

import java.util.logging.Logger;

public class SpigotInterface implements MethodInterface {
    private FernSpigotAPI fernSpigotAPI;
    SpigotInterface(FernSpigotAPI fernSpigotAPI) {
        this.fernSpigotAPI = fernSpigotAPI;
    }

    @Override
    public Logger getLogger() {
        return fernSpigotAPI.getLogger();
    }

    @Override
    public ServerType getServeType() {
        return ServerType.BUKKIT;
    }

    @Override
    public FernAPIPlugin getInstance() {
        return fernSpigotAPI;
    }
}
