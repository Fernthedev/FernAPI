package com.github.fernthedev.fernapi.universal;

import com.github.fernthedev.fernapi.universal.data.network.Channel;

public class FernAPIChannels {

    private FernAPIChannels() {
    }

    public static final String BUNGEECORD_PROXY_NAME = "BungeeCord";

    public static final String getPlaceHolderResult = "GetPlaceHolderAPI";

    public static final String PlaceHolderValue = "PlaceHolderValue";

    public static final Channel PlaceHolderBungeeChannel = new Channel("ferncommands", "ph", Channel.ChannelAction.BOTH);

    public static final Channel VANISH_CHANNEL = new Channel("fernapi", "vanish", Channel.ChannelAction.BOTH);

    public static final String VANISH_SUBCHANNEL = "vanish";



}
