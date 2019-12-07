package com.github.fernthedev.fernapi.server.velocity.network;

import com.github.fernthedev.fernapi.universal.handlers.NetworkHandler;
import com.velocitypowered.api.proxy.ServerConnection;

public class VelocityNetworkHandler implements NetworkHandler {
    @Override
    public boolean isRegistered(Object sender) {
        return sender instanceof ServerConnection;
    }
}
