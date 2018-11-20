package com.github.fernthedev.fernapi.universal.handlers;

public interface UUIDFetchManager {
    void stopTimerRequest();

    void stopHourTask();

    void runTimerRequest();

    void runHourTask();
}
