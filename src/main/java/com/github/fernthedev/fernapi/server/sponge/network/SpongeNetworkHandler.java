package com.github.fernthedev.fernapi.server.sponge.network;

import com.github.fernthedev.fernapi.universal.handlers.NetworkHandler;

public class SpongeNetworkHandler implements NetworkHandler {
    @Override
    public boolean isRegistered(Object sender) {
        return true;
    }
}
