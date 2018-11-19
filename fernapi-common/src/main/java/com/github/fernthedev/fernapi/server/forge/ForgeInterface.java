package com.github.fernthedev.fernapi.server.forge;

import com.github.fernthedev.fernapi.universal.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.MethodInterface;
import com.github.fernthedev.fernapi.universal.ServerType;

import java.util.logging.Logger;

public class ForgeInterface implements MethodInterface {
    private FernForgeAPI fernForgeAPI;
    ForgeInterface(FernForgeAPI fernForgeAPI) {
        this.fernForgeAPI = fernForgeAPI;
    }

    @Override
    public Logger getLogger() {
        return Logger.getLogger("fernapi");
    }

    @Override
    public ServerType getServeType() {
        return ServerType.FORGE;
    }

    @Override
    public FernAPIPlugin getInstance() {
        return fernForgeAPI;
    }
}
