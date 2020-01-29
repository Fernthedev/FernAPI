package com.github.fernthedev.fernapi.universal.api;

public enum PluginLoadOrder {


    /**
     * Indicates that the plugin will be loaded at startup
     */
    STARTUP,
    /**
     * Indicates that the plugin will be loaded after the first/default world
     * was created
     */
    POSTWORLD

}
