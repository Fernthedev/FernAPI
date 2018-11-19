package com.github.fernthedev.fernapi.server.forge;

import com.github.fernthedev.fernapi.server.spigot.UUIDSpigot;
import com.github.fernthedev.fernapi.universal.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.UUIDFetcher;
import com.github.fernthedev.fernapi.universal.Universal;

public class FernForgeAPI implements FernAPIPlugin {


    public FernForgeAPI() {
        new Universal().setup(new ForgeInterface(this));
        UUIDFetcher.setFetchManager(new UUIDSpigot());
    }

    @Override
    @Deprecated
    public void cancelTask(int id) {

    }
}
