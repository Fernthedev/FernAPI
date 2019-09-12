package com.github.fernthedev.fernapi.server.spigot.network;

import com.github.fernthedev.fernapi.universal.handlers.NetworkHandler;

public class SpigotNetworkHandler implements NetworkHandler {
    @Override
    public boolean isRegistered(Object sender) {
        return true;
    }
}
