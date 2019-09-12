package com.github.fernthedev.fernapi.server.bungee.network;

import com.github.fernthedev.fernapi.universal.handlers.NetworkHandler;

public class BungeeNetworkHandler implements NetworkHandler {
    @Override
    public boolean isRegistered(Object sender) {
        return true;
    }
}
