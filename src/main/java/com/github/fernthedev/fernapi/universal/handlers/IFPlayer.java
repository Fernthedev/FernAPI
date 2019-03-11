package com.github.fernthedev.fernapi.universal.handlers;

import com.github.fernthedev.fernapi.universal.api.CommandSender;
import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.InetSocketAddress;
import java.util.UUID;

@AllArgsConstructor
@Getter
public abstract class IFPlayer implements CommandSender {
    String name = null;
    UUID uuid = null;

    public IFPlayer() {}

    public abstract void sendMessage(BaseMessage textMessage);

    public abstract InetSocketAddress getAddress();


}
