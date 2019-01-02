package com.github.fernthedev.fernapi.universal.handlers;

import java.util.logging.Logger;

public interface MethodInterface {

    Logger getLogger();

    ServerType getServerType();

    FernAPIPlugin getInstance();

}

