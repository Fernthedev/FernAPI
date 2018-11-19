package com.github.fernthedev.fernapi.server.spigot;

import com.github.fernthedev.fernapi.universal.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.UUIDFetcher;
import com.github.fernthedev.fernapi.universal.Universal;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class FernSpigotAPI extends JavaPlugin implements FernAPIPlugin {

    @Override
    public void onEnable() {
        new Universal().setup(new SpigotInterface(this));
        UUIDFetcher.setFetchManager(new UUIDSpigot());
    }


    @Override
    public void cancelTask(int id) {
        getServer().getScheduler().cancelTask(id);
    }
}
