package com.github.fernthedev.fernapi.universal.api;

import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.InetSocketAddress;
import java.util.UUID;

@AllArgsConstructor
@Getter
public abstract class IFPlayer implements CommandSender {
    @Getter
    String name = null;

    @Getter
    UUID uuid = null;

    public IFPlayer() {}

    public abstract void sendMessage(BaseMessage textMessage);

    public abstract InetSocketAddress getAddress();

    public abstract long getPing();

    public abstract String getCurrentServerName();
}