package com.github.fernthedev.fernapi.server.spigot;

import com.github.fernthedev.fernapi.server.spigot.chat.SpigotChatHandler;
import com.github.fernthedev.fernapi.server.spigot.command.SpigotCommandHandler;
import com.github.fernthedev.fernapi.server.spigot.database.SpigotDatabase;
import com.github.fernthedev.fernapi.server.spigot.interfaces.UUIDSpigot;
import com.github.fernthedev.fernapi.server.spigot.network.PlaceHolderAPI.HookPlaceHolderAPI;
import com.github.fernthedev.fernapi.server.spigot.network.SpigotMessageHandler;
import com.github.fernthedev.fernapi.server.spigot.pluginhandlers.VaultHandler;
import com.github.fernthedev.fernapi.universal.UUIDFetcher;
import com.github.fernthedev.fernapi.universal.Universal;
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
                new SpigotDatabase(this),new SpigotCommandHandler());
        UUIDFetcher.setFetchManager(new UUIDSpigot());

        Universal.getMessageHandler().registerMessageHandler(new HookPlaceHolderAPI(this));
        if(Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            vaultHandler = new VaultHandler();

            vaultHandler.hook();
        }


    }

    @Override
    public void cancelTask(int id) {
        getServer().getScheduler().cancelTask(id);
    }
}
