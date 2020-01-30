package com.github.fernthedev.fernapi.universal.handlers;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.PluginData;

public interface FernAPIPlugin {

    /**
     *
     * @return plugin data
     */
    default PluginData<?> getPluginData() {
        return Universal.getPluginData();
    }

}
