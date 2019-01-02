package com.github.fernthedev.fernapi.server.spigot;

import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.UUIDFetcher;
import com.github.fernthedev.fernapi.universal.Universal;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class FernSpigotAPI extends JavaPlugin implements FernAPIPlugin {

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Registered FernAPI Spigot");
        Universal.getInstance().setup(new SpigotInterface(this));
        UUIDFetcher.setFetchManager(new UUIDSpigot());
    }

    @Override
    public void cancelTask(int id) {
        getServer().getScheduler().cancelTask(id);
    }
}
