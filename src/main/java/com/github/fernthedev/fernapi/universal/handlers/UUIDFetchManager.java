package com.github.fernthedev.fernapi.universal.handlers;

import java.util.UUID;

public interface UUIDFetchManager {
    void stopTimerRequest();

    void stopHourTask();

    void runTimerRequest();

    void runHourTask();

    String getNameFromPlayer(UUID uuid);

    UUID getUUIDFromPlayer(String name);
}
