package com.github.fernthedev.fernapi.server.forge;

import com.github.fernthedev.fernapi.universal.handlers.UUIDFetchManager;
import com.github.fernthedev.fernapi.universal.UUIDFetcher;
import com.github.fernthedev.fernapi.universal.Universal;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static com.github.fernthedev.fernapi.universal.UUIDFetcher.*;

public class UUIDForge implements UUIDFetchManager {
    private Timer requestRunnable = new Timer();

    private static Timer banRunnable = new Timer();



    public void runTimerRequest() {
        print("Server is bukkit");

        requestRunnable.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                UUIDFetcher.setRequests(0);
                playerNameCache.clear();
                playerUUIDCache.clear();
                playerHistoryCache.clear();
                print("Refreshed uuid cache.");
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
                    print("Hour is finished, continuing uuid checking");
                    runTimerRequest();
                    stopHourTask();
                } else if (!didHourCheck) didHourCheck = true;
            }
        },TimeUnit.HOURS.toMillis(1),TimeUnit.HOURS.toMillis(1));

        // }.runTaskLater((Plugin) Universal.getMethods().getInstance(),
        //          TimeUnit.HOURS.toSeconds(1) *20);
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
}
