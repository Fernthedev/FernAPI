package com.github.fernthedev.fernapi.server.spigot;

import com.github.fernthedev.fernapi.server.spigot.chat.SpigotChatHandler;
import com.github.fernthedev.fernapi.server.spigot.command.SpigotCommandHandler;
import com.github.fernthedev.fernapi.server.spigot.database.SpigotDatabase;
import com.github.fernthedev.fernapi.server.spigot.interfaces.UUIDSpigot;
import com.github.fernthedev.fernapi.server.spigot.network.PlaceHolderAPI.PlaceHolderAPIResponder;
import com.github.fernthedev.fernapi.server.spigot.network.SpigotMessageHandler;
import com.github.fernthedev.fernapi.server.spigot.network.SpigotNetworkHandler;
import com.github.fernthedev.fernapi.server.spigot.pluginhandlers.VaultHandler;
import com.github.fernthedev.fernapi.universal.util.UUIDFetcher;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.network.vanish.VanishProxyCheck;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class FernSpigotAPI extends JavaPlugin implements FernAPIPlugin {

    @Getter
    private VaultHandler vaultHandler;

    @Override
    public void onEnable() {
        getLogger().info("Registered FernAPI Spigot");
        Universal.getInstance().setup(new SpigotInterface(this),
                this,
                new SpigotChatHandler(),
                new SpigotMessageHandler(this),
                new SpigotDatabase(this),new SpigotCommandHandler(),
                new SpigotNetworkHandler());
        UUIDFetcher.setFetchManager(new UUIDSpigot());

        Universal.getMessageHandler().registerMessageHandler(new PlaceHolderAPIResponder(this));
        if(Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            vaultHandler = new VaultHandler();

            vaultHandler.hook();
        }

        Universal.getMessageHandler().registerMessageHandler(new VanishProxyCheck());
    }

    @Override
    public void onDisable() {
        Universal.getInstance().onDisable();
        getServer().getScheduler().cancelTasks(this);
    }

    @Override
    public void cancelTask(int id) {
        getServer().getScheduler().cancelTask(id);
    }
}
