package com.github.fernthedev.fernapi.universal.util.network.vanish;

import com.github.fernthedev.fernapi.universal.api.IFPlayer;


@FunctionalInterface
public interface VanishRunnable {

    void run(IFPlayer<?> player, boolean isVanished, boolean timedOut);

}
