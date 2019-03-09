package com.github.fernthedev.fernapi.universal.data.network;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Channel {

    private String channel;
    private ChannelAction channelAction;

    public enum ChannelAction {
        INCOMING,
        OUTCOMING,
        BOTH;
    }
}
