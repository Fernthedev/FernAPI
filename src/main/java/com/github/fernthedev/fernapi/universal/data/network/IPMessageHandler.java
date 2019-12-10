package com.github.fernthedev.fernapi.universal.data.network;

import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.PluginMessageHandler;

import java.util.ArrayList;
import java.util.List;

public interface IPMessageHandler {

    void registerMessageHandler(PluginMessageHandler pluginMessageHandler);

    /**
     * This sends plugin dataInfo.
     * @param data The dataInfo to be sent, player will be specified added automatically
     */
    void sendPluginData(PluginMessageData data);

    /**
     * This sends plugin dataInfo.
     * @param player The player can be null, not necessary
     * @param data The dataInfo to be sent, player will be specified added automatically
     */
    void sendPluginData(IFPlayer player, PluginMessageData data);

    List<PluginMessageHandler> receivers = new ArrayList<>();

    static List<PluginMessageHandler> getReceivers() {
        return receivers;
    }


}
