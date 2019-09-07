package com.github.fernthedev.fernapi.server.sponge;

import com.github.fernthedev.fernapi.server.sponge.chat.SpongeChatHandler;
import com.github.fernthedev.fernapi.server.sponge.command.SpongeCommandHandler;
import com.github.fernthedev.fernapi.server.sponge.database.SpongeDatabase;
import com.github.fernthedev.fernapi.server.sponge.interfaces.UUIDSponge;
import com.github.fernthedev.fernapi.server.sponge.network.SpongeMessageHandler;
import com.github.fernthedev.fernapi.universal.UUIDFetcher;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import lombok.Getter;
import org.slf4j.Logger;
import org.spongepowered.api.event.game.state.GameStoppedEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;

// Imports for logger
import com.google.inject.Inject;


@Plugin(id = "fernapi", name = "FernAPI", version = "1.0", description = "A universal API")
public class FernSpongeAPI implements FernAPIPlugin {

    @Inject
    @Getter
    private Logger logger;


    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        getLogger().info("Registered FernAPI Spigot");
        Universal.getInstance().setup(new SpongeInterface(this),
                this,
                new SpongeChatHandler(),
                new SpongeMessageHandler(this),
                new SpongeDatabase(this),new SpongeCommandHandler(this));
        UUIDFetcher.setFetchManager(new UUIDSponge(this));
    }

    @Listener
    public void onServerStart(GameStoppedEvent event) {
        Universal.getInstance().onDisable();

    }

    @Override
    public void cancelTask(int id) {

    }
}
