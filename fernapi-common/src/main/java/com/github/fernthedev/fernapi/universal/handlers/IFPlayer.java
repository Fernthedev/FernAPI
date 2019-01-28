package com.github.fernthedev.fernapi.universal.handlers;

import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public abstract class IFPlayer {
    String name = null;
    UUID uuid = null;

    public IFPlayer() {}

    public abstract void sendChatMessage(BaseMessage textMessage);
}
