package com.github.fernthedev.fernapi.server.sponge;

import com.github.fernthedev.fernapi.server.sponge.chat.SpongeChatHandler;
import com.github.fernthedev.fernapi.server.sponge.command.SpongeCommandHandler;
import com.github.fernthedev.fernapi.server.sponge.database.SpongeDatabase;
import com.github.fernthedev.fernapi.server.sponge.network.SpongeMessageHandler;
import com.github.fernthedev.fernapi.server.sponge.network.SpongeNetworkHandler;
import com.github.fernthedev.fernapi.server.sponge.scheduler.SpongeScheduler;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.google.inject.Inject;
import lombok.Getter;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedEvent;
import org.spongepowered.api.plugin.Plugin;

import java.nio.file.Path;

// Imports for logger


@Plugin(id = "fernapi", name = "FernAPI", version = "1.0", description = "A universal API")
public class FernSpongeAPI implements FernAPIPlugin {

    @Inject
    @Getter
    protected Logger logger;

    @Inject
    @DefaultConfig(sharedRoot = true)
    protected Path defaultConfig;

    @Inject
    @DefaultConfig(sharedRoot = true)
    protected ConfigurationLoader<CommentedConfigurationNode> configManager;

    @Inject
    @ConfigDir(sharedRoot = false)
    protected Path privateConfigDir;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        getLogger().info("Registered FernAPI Spigot");
        Universal.getInstance().setup(new SpongeInterface(this),
                this,
                new SpongeChatHandler(),
                new SpongeMessageHandler(this),
                new SpongeDatabase(this),new SpongeCommandHandler(this),
                new SpongeNetworkHandler(),
                new SpongeScheduler(this));
    }

    @Listener
    public void onServerStop(GameStoppedEvent event) {
        Universal.getInstance().onDisable();
    }
}
