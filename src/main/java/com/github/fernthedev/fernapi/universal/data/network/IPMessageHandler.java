package com.github.fernthedev.fernapi.universal.data.network;

import com.github.fernthedev.fernapi.universal.handlers.IFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.PluginMessageHandler;

import java.util.ArrayList;
import java.util.List;

public interface IPMessageHandler {

    void registerMessageHandler(PluginMessageHandler pluginMessageHandler);

    void sendPluginData(PluginMessageData data);

    void sendPluginData(IFPlayer player, PluginMessageData data);

    List<PluginMessageHandler> recievers = new ArrayList<>();

    static List<PluginMessageHandler> getRecievers() {
        return recievers;
    }


}
