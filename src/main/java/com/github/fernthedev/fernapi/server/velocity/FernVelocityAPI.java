package com.github.fernthedev.fernapi.server.velocity;

import com.github.fernthedev.fernapi.server.velocity.chat.VelocityChatHandler;
import com.github.fernthedev.fernapi.server.velocity.command.VelocityCommandHandler;
import com.github.fernthedev.fernapi.server.velocity.database.VelocityDatabase;
import com.github.fernthedev.fernapi.server.velocity.interfaces.UUIDVelocity;
import com.github.fernthedev.fernapi.server.velocity.network.VelocityMessageHandler;
import com.github.fernthedev.fernapi.server.velocity.network.VelocityNetworkHandler;
import com.github.fernthedev.fernapi.universal.ProxyAskPlaceHolder;
import com.github.fernthedev.fernapi.universal.util.UUIDFetcher;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.network.vanish.VanishProxyCheck;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;

import java.nio.file.Path;

public class FernVelocityAPI implements FernAPIPlugin {

    private final VelocityMessageHandler messageHandler;
    @Getter
    protected ProxyServer server;

    @Inject
    @Getter
    @DataDirectory
    Path dataDirectory;

    @Getter
    protected Logger logger;

    public FernVelocityAPI(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;

        messageHandler = new VelocityMessageHandler(this);
        Universal.getInstance().setup(new VelocityInterface(this),
                this,
                new VelocityChatHandler(),
                messageHandler,
                new VelocityDatabase(this),
                new VelocityCommandHandler(this),
                new VelocityNetworkHandler());

        UUIDFetcher.setFetchManager(new UUIDVelocity(this));
        Universal.getMessageHandler().registerMessageHandler(new ProxyAskPlaceHolder());
        Universal.getMessageHandler().registerMessageHandler(new VanishProxyCheck());
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // Do some operation demanding access to the Velocity API here.
        // For instance, we could register an event:
        server.getEventManager().register(this, messageHandler);
    }

    @Subscribe
    public void onProxyStop(ProxyShutdownEvent event) {
        Universal.getInstance().onDisable();
    }

    @Override
    public void cancelTask(int id) {
        getLogger().warn("FernAPI currently does not support cancelling tasks using IDs on Velocity due to it's limited scheduling API.");
    }

}
