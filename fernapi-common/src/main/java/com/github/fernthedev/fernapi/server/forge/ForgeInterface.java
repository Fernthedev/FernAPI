package com.github.fernthedev.fernapi.server.forge;

import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.handlers.MethodInterface;
import com.github.fernthedev.fernapi.universal.handlers.ServerType;

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
    public ServerType getServerType() {
        return ServerType.FORGE;
    }

    @Override
    public FernAPIPlugin getInstance() {
        return fernForgeAPI;
    }
}
