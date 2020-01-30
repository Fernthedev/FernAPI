package com.github.fernthedev.fernapi.server.velocity.interfaces;

import com.github.fernthedev.fernapi.server.velocity.FernVelocityAPI;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.handlers.UUIDFetchManager;
import com.github.fernthedev.fernapi.universal.util.UUIDFetcher;
import com.velocitypowered.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

import static com.github.fernthedev.fernapi.universal.util.UUIDFetcher.*;

@Deprecated
public class UUIDVelocity extends UUIDFetchManager {
    private static ScheduledTask requestTask;

    private static ScheduledTask banHourTask;

    private FernVelocityAPI api;

    public UUIDVelocity(FernVelocityAPI api) {
        this.api = api;
    }

    public void runTimerRequest() {
        debug("Server is bungee");
        requestTask = api.getServer().getScheduler()
                .buildTask(api, () -> {
            UUIDFetcher.setRequests(0);
            playerNameCache.clear();
            playerUUIDCache.clear();
            playerHistoryCache.clear();
            debug("Refreshed uuid cache.");
        })
                .delay(  1, TimeUnit.MINUTES)
                .repeat(10, TimeUnit.MINUTES)
        .schedule();
    }

    public void runHourTask() {

        banHourTask = api.getServer().getScheduler()
                .buildTask(api, () -> {
            if (!hourRan && didHourCheck) {
                hourRan = true;
                addRequestTimer();
                stopHourTask();
            } else if (!didHourCheck) didHourCheck = true;
        })
                .delay(1, TimeUnit.HOURS)
                .schedule();

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
