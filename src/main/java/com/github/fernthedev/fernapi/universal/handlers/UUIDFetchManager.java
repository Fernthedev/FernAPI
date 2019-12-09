package com.github.fernthedev.fernapi.universal.handlers;

import com.github.fernthedev.fernapi.universal.util.UUIDFetcher;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.ScheduleTaskWrapper;

import java.util.concurrent.TimeUnit;

import static com.github.fernthedev.fernapi.universal.util.UUIDFetcher.*;

public class UUIDFetchManager {
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

    private static ScheduleTaskWrapper requestTask;

    private static ScheduleTaskWrapper banHourTask;

    public void runTimerRequest() {
        debug("Server is bungee");
        requestTask = Universal.getScheduler().runSchedule(() -> {
            UUIDFetcher.setRequests(0);
            playerNameCache.clear();
            playerUUIDCache.clear();
            playerHistoryCache.clear();
            debug("Refreshed uuid cache.");
        }, 1, 10, TimeUnit.MINUTES);
    }

    public void runHourTask() {

        banHourTask = Universal.getScheduler().runSchedule( () -> {
            if (!hourRan && didHourCheck) {
                hourRan = true;
                addRequestTimer();
                stopHourTask();
            } else if (!didHourCheck) didHourCheck = true;
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



    private static void print(Object log) {
        Universal.getMethods().getLogger().info("[" + Universal.getMethods().getServerType() + "] [UUIDFetcher] " + log);
    }

    private static void debug(Object log) {
        Universal.debug("[" + Universal.getMethods().getServerType() + "] [UUIDFetcher] " + log);
    }
}
