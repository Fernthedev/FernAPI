package com.github.fernthedev.fernapi.server.forge.interfaces;

import com.github.fernthedev.fernapi.universal.UUIDFetcher;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.handlers.UUIDFetchManager;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.github.fernthedev.fernapi.universal.UUIDFetcher.*;

public class UUIDForge implements UUIDFetchManager {
    private Timer requestRunnable = new Timer();

    private static Timer banRunnable = new Timer();



    public void runTimerRequest() {
        debug("Server is bukkit");

        requestRunnable.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                UUIDFetcher.setRequests(0);
                playerNameCache.clear();
                playerUUIDCache.clear();
                playerHistoryCache.clear();
                debug("Refreshed uuid cache.");
            }
        }, TimeUnit.MINUTES.toMillis(10),TimeUnit.MINUTES.toMillis(10));


        //.runTaskLater((Plugin) Universal.getMethods().getInstance(),
        //        TimeUnit.MINUTES.toSeconds(10) * 20);
    }

    public void runHourTask() {
        banRunnable.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!hourRan && didHourCheck) {
                    hourRan = true;
                    debug("Hour is finished, continuing uuid checking");
                    runTimerRequest();
                    stopHourTask();
                } else if (!didHourCheck) didHourCheck = true;
            }
        },TimeUnit.HOURS.toMillis(1),TimeUnit.HOURS.toMillis(1));

        // }.runTaskLater((Plugin) Universal.getMethods().getInstance(),
        //          TimeUnit.HOURS.toSeconds(1) *20);
    }

    @Override
    public String getNameFromPlayer(UUID uuid) {
        return null;
    }

    @Override
    public UUID getUUIDFromPlayer(String name) {
        return null;
    }

    public void stopTimerRequest() {
        if(requestRunnable != null) {
            requestRunnable.cancel();
        }
    }

    public void stopHourTask() {
        banRunnable.cancel();
    }



    private static void print(Object log) {
        Universal.getMethods().getLogger().info("[" + Universal.getMethods().getServerType() + "] [UUIDFetcher] " + log);
    }

    private static void debug(Object log) {
        Universal.debug("[" + Universal.getMethods().getServerType() + "] [UUIDFetcher] " + log);
    }
}
