package com.github.fernthedev.fernapi.universal.handlers;

import java.util.TimerTask;

public interface FernAPIPlugin {

    void cancelTask(int id);

    static void cancelTask(TimerTask timerTask) {
        timerTask.cancel();
    }
}
