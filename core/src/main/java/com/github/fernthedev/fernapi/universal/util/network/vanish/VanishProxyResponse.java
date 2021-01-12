package com.github.fernthedev.fernapi.universal.util.network.vanish;

import com.github.fernthedev.fernapi.universal.api.IFPlayer;

public class VanishProxyResponse {
    private final IFPlayer<?> player;
    private final boolean isVanished;
    private final boolean timedOut;

    public VanishProxyResponse(IFPlayer<?> player, boolean isVanished, boolean timedOut) {
        this.player = player;
        this.isVanished = isVanished;
        this.timedOut = timedOut;
    }

    public IFPlayer<?> getPlayer() {
        return player;
    }

    public boolean isVanished() {
        return isVanished;
    }

    public boolean isTimedOut() {
        return timedOut;
    }
}
