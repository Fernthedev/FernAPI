package com.github.fernthedev.fernapi.universal;

import com.github.fernthedev.fernapi.universal.data.network.Channel;

public class Channels {

    private Channels() {
    }

    public static String getPlaceHolderResult = "GetPlaceHolderAPI";

    public static String PlaceHolderValue = "PlaceHolderValue";

    public static Channel PlaceHolderBungeeChannel = new Channel("ferncommands", "ph", Channel.ChannelAction.BOTH);


}
