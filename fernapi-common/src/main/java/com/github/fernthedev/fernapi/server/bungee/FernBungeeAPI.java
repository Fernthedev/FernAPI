package com.github.fernthedev.fernapi.server.bungee;

import com.github.fernthedev.fernapi.server.spigot.UUIDSpigot;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.UUIDFetcher;
import com.github.fernthedev.fernapi.universal.Universal;
import net.md_5.bungee.api.plugin.Plugin;

public class FernBungeeAPI extends Plugin implements FernAPIPlugin {

    @Override
    public void onEnable() {
        Universal.getInstance().setup(new BungeeInterface(this));
        UUIDFetcher.setFetchManager(new UUIDSpigot());
    }

    @Override
    public void cancelTask(int id) {
        getProxy().getScheduler().cancel(id);
    }

}
