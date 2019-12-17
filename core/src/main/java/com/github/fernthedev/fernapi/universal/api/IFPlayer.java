package com.github.fernthedev.fernapi.universal.api;

import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import com.github.fernthedev.fernapi.universal.exceptions.FernRuntimeException;
import com.github.fernthedev.fernapi.universal.exceptions.network.PluginTimeoutException;
import com.github.fernthedev.fernapi.universal.util.network.vanish.VanishProxyCheck;
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

    public boolean isVanished() {
        final boolean[] vanished = new boolean[1];
        try {
            new VanishProxyCheck(this, (player, isVanished, timedOut) -> {
                if (timedOut) throw new PluginTimeoutException("The vanish check timed out. The server must have FernAPI enabled and registered");

                vanished[0] = isVanished;
            }).awaitVanishResponse(20);
        } catch (InterruptedException e) {
            throw new FernRuntimeException("Interrupted", e);
        }

        return vanished[0];
    }
}
