package com.github.fernthedev.fernapi.server.bungee;

import com.github.fernthedev.fernapi.server.spigot.UUIDSpigot;
import com.github.fernthedev.fernapi.universal.UUIDFetcher;
import net.md_5.bungee.api.plugin.Plugin;

public class FernBungeeAPI extends Plugin {

    @Override
    public void onEnable() {
        UUIDFetcher.setFetchManager(new UUIDSpigot());
    }

}
