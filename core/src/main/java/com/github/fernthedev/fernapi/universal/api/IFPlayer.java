package com.github.fernthedev.fernapi.universal.api;

import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import com.github.fernthedev.fernapi.universal.exceptions.FernDebugException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.InetSocketAddress;
import java.util.UUID;

@AllArgsConstructor
@Getter
public abstract class IFPlayer<T> implements CommandSender {
    @Getter
    protected String name = null;

    @Getter
    protected UUID uuid = null;

    @Getter
    protected T player;

    public IFPlayer() {}

    public abstract void sendMessage(BaseMessage textMessage);

    public abstract InetSocketAddress getAddress();

    public abstract long getPing();

    public abstract String getCurrentServerName();

    /**
     * Returns true if all data is null
     * If player is null but name and uuid aren't, this returns false
     * @return
     */
    public boolean isNull() {
        boolean isPlayer = player == null; // Player must be null
        boolean isDataNull = name == null || uuid == null; // Name and UUID both must be null

        return isPlayer && isDataNull;
    }

    // TODO: Finish method
    public boolean isVanished(Object fal) {
        throw new FernDebugException("Not done yet");
    }
}
