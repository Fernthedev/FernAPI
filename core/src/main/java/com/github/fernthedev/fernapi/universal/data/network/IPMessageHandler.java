package com.github.fernthedev.fernapi.universal.data.network;

import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.PluginMessageHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class IPMessageHandler {

    public final List<PluginMessageHandler> receivers = new ArrayList<>();

    private final Map<String, PacketParser<?>> packetParserMap = new HashMap<>();

    public void registerPacketParser(String name, PacketParser<?> packetParser) {
        packetParserMap.put(name, packetParser);
    }

    public PacketParser<?> getPacketParser(String name) {
        return packetParserMap.get(name);
    }

    public abstract void registerMessageHandler(PluginMessageHandler pluginMessageHandler);

    /**
     * This sends plugin dataInfo.
     * @param data The dataInfo to be sent, player will be specified added automatically
     */
    public abstract void sendPluginData(PluginMessageData data);

    /**
     * This sends plugin dataInfo.
     * @param player The player can be null, not necessary
     * @param data The dataInfo to be sent, player will be specified added automatically
     */
    public abstract void sendPluginData(IFPlayer<?> player, PluginMessageData data);


    public List<PluginMessageHandler> getReceivers() {
        return receivers;
    }


}
