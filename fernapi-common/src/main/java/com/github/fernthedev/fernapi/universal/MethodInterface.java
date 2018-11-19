package com.github.fernthedev.fernapi.universal;

import java.util.logging.Logger;

public interface MethodInterface {

    Logger getLogger();

    ServerType getServeType();

    FernAPIPlugin getInstance();

}

