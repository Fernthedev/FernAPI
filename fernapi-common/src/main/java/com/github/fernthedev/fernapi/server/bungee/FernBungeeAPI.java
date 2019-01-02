package com.github.fernthedev.fernapi.server.bungee;

import com.github.fernthedev.fernapi.universal.UUIDFetcher;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import net.md_5.bungee.api.plugin.Plugin;

public class FernBungeeAPI extends Plugin implements FernAPIPlugin {

    @Override
    public void onEnable() {
        Universal.getInstance().setup(new BungeeInterface(this));
        UUIDFetcher.setFetchManager(new UUIDBungee());
    }


    @Override
    public void cancelTask(int id) {
        getProxy().getScheduler().cancel(id);
    }

}
