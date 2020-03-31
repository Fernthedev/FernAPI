package com.github.fernthedev.fernapi.universal.handlers;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.ScheduleTaskWrapper;
import com.github.fernthedev.fernapi.universal.util.UUIDFetcher;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.TimeUnit;

import static com.github.fernthedev.fernapi.universal.util.UUIDFetcher.*;

@RequiredArgsConstructor
public class UUIDFetchManager {

    @NonNull
    private Runnable clearCache;

//    void stopTimerRequest();
//
//    void stopHourTask();
//
//    void runTimerRequest();
//
//    void runHourTask();
//
//    String getNameFromPlayer(UUID uuid);
//
//    UUID getUUIDFromPlayer(String name);

    private static ScheduleTaskWrapper<?, ?> requestTask;

    private static ScheduleTaskWrapper<?, ?> banHourTask;

    public void runTimerRequest() {
        debug("Server is bungee");
        requestTask = Universal.getScheduler().runSchedule(() -> {
            UUIDFetcher.setRequests(0);
            clearCache.run();
            debug("Refreshed uuid cache.");
        }, 1, 10, TimeUnit.MINUTES);
    }

    public void runHourTask() {
        banHourTask = Universal.getScheduler().runSchedule( () -> {
            if (!UUIDFetcher.isHourRan() && UUIDFetcher.isDidHourCheck()) {
                UUIDFetcher.setHourRan(true);
                addRequestTimer();
                stopHourTask();
            } else if (!UUIDFetcher.isDidHourCheck()) UUIDFetcher.setDidHourCheck(true);
        }, 1, 1, TimeUnit.HOURS);

    }




    public void stopTimerRequest() {
        if(requestTask != null) {
            requestTask.cancel();
        }
    }

    public void stopHourTask() {
        if(banHourTask != null)
            banHourTask.cancel();

    }

    private static void debug(Object log) {
        Universal.debug("[" + Universal.getMethods().getServerType() + "] [UUIDFetcher] " + log);
    }
}
