package com.github.fernthedev.fernapi.server.spigot;

import com.github.fernthedev.fernapi.universal.UUIDFetcher;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class FernSpigotAPI extends JavaPlugin {

    @Override
    public void onEnable() {
        UUIDFetcher.setFetchManager(new UUIDSpigot());
    }

}
