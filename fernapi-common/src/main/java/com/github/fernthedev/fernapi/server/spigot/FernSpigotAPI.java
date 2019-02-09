package com.github.fernthedev.fernapi.server.spigot;

import com.github.fernthedev.fernapi.server.spigot.chat.SpigotChatHandler;
import com.github.fernthedev.fernapi.server.spigot.database.SpigotDatabase;
import com.github.fernthedev.fernapi.server.spigot.interfaces.UUIDSpigot;
import com.github.fernthedev.fernapi.server.spigot.network.PlaceHolderAPI.HookPlaceHolderAPI;
import com.github.fernthedev.fernapi.server.spigot.network.SpigotMessageHandler;
import com.github.fernthedev.fernapi.universal.UUIDFetcher;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import org.bukkit.plugin.java.JavaPlugin;

public class FernSpigotAPI extends JavaPlugin implements FernAPIPlugin {

    @Override
    public void onEnable() {
        getLogger().info("Registered FernAPI Spigot");
        Universal.getInstance().setup(new SpigotInterface(this),
                new SpigotChatHandler(),
                new SpigotMessageHandler(this),
                new SpigotDatabase(this));
        UUIDFetcher.setFetchManager(new UUIDSpigot());

        Universal.getMessageHandler().registerMessageHandler(new HookPlaceHolderAPI(this));



    }

    @Override
    public void cancelTask(int id) {
        getServer().getScheduler().cancelTask(id);
    }
}
