package com.github.fernthedev.fernapi.server.velocity;

import co.aikar.commands.VelocityCommandManager;
import com.github.fernthedev.fernapi.server.velocity.chat.VelocityChatHandler;
import com.github.fernthedev.fernapi.server.velocity.interfaces.VelocityPluginData;
import com.github.fernthedev.fernapi.server.velocity.network.VelocityMessageHandler;
import com.github.fernthedev.fernapi.server.velocity.network.VelocityNetworkHandler;
import com.github.fernthedev.fernapi.server.velocity.scheduler.VelocityScheduler;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.util.ProxyAskPlaceHolder;
import com.github.fernthedev.fernapi.universal.util.network.vanish.VanishProxyCheck;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import org.slf4j.Logger;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.nio.file.Path;

public class FernVelocityAPI implements FernAPIPlugin {

    private final VelocityMessageHandler messageHandler;
    @Getter
    protected ProxyServer server;

    @Inject
    @Getter
    @DataDirectory
    protected Path dataDirectory;

    @Getter
    protected Logger logger;

    @Getter
    protected final PluginContainer pluginContainer;

    @Inject
    public FernVelocityAPI(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;



        messageHandler = new VelocityMessageHandler(this);


        pluginContainer = server.getPluginManager().fromInstance(this).orElseThrow(() -> new IllegalStateException("The server does not have plugin container instance loaded yet?"));


        Universal.getInstance().setup(new VelocityInterface(this),
                this,
                new VelocityChatHandler(),
                messageHandler,
                new VelocityCommandManager(server, this),
                new VelocityNetworkHandler(this),
                new VelocityScheduler(this),
                new VelocityPluginData(this, pluginContainer.getDescription()));

//        UUIDFetcher.setFetchManager(new UUIDVelocity(this));
        Universal.getMessageHandler().registerMessageHandler(ProxyAskPlaceHolder.LISTENER_INSTANCE);
        Universal.getMessageHandler().registerMessageHandler(VanishProxyCheck.LISTENER_INSTANCE);
    }

    @Subscribe
    @OverridingMethodsMustInvokeSuper
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // Do some operation demanding access to the Velocity API here.
        // For instance, we could register an event:
        server.getEventManager().register(this, messageHandler);
    }

    @Subscribe
    @OverridingMethodsMustInvokeSuper
    public void onProxyStop(ProxyShutdownEvent event) {
        Universal.getInstance().onDisable();
    }
}
