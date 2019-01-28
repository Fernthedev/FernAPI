package com.github.fernthedev.fernapi.universal.data;

import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import com.github.fernthedev.fernapi.universal.handlers.IFPlayer;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class JSONPlayer extends IFPlayer {

    public JSONPlayer(String name, UUID uuid) {
        super(name,uuid);
    }

    @Override
    public void sendChatMessage(BaseMessage textMessage) {
        throw new IllegalArgumentException("Do not call this method, it is useless. Convert it to player instance or a different FPlayer instance");
    }
}
