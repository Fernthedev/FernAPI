package com.github.fernthedev.fernapi.universal.data.network;

public interface PacketParser<T extends PluginMessageData> {

    T parse(String json);

}
