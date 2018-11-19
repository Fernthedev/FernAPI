package com.github.fernthedev.fernapi.universal;

public interface UUIDFetchManager {
    void stopTimerRequest();

    void stopHourTask();

    void runTimerRequest();

    void runHourTask();
}
