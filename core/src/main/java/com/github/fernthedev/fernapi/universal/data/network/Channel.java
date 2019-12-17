package com.github.fernthedev.fernapi.universal.data.network;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Channel {

    private Channel() {}

    /**
     * The namespace, used to identify from which plugin
     */
    private String namespace;

    /**
     * The channel name used to identify what purpose
     */
    private String channelName;

    /**
     * Defines whether the channel is incoming, outgoing or both protocol.
     */
    private ChannelAction channelAction;

    /**
     * It is used to create a channel from a string with namespace and channel name combined
     * This isn't strictly necessary, it is just for convenience in case you have a static field used in many places and cannot make simple changes
     * @param channel The combined namespace and channel, separated with a ":" Example such as "ferncommands:ph", "namespace:channel"
     * @param channelAction The action
     * @return The channel with namespace and channel name seperated
     */
    public static Channel createChannelFromString(String channel, ChannelAction channelAction) {
        String[] splitString = channel.split(":",2);
        String namespace = splitString[0];
        String channelName = splitString[1];
        return new Channel(namespace, channelName, channelAction);
    }

    public String getFullChannelName() {
        return namespace + ":" + channelName;
    }

    public enum ChannelAction {
        INCOMING,
        OUTGOING,
        BOTH;
    }
}
