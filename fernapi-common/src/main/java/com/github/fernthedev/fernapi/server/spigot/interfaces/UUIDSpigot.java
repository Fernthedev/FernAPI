package com.github.fernthedev.fernapi.server.spigot.interfaces;

import com.github.fernthedev.fernapi.universal.handlers.UUIDFetchManager;
import com.github.fernthedev.fernapi.universal.UUIDFetcher;
import com.github.fernthedev.fernapi.universal.Universal;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;

import static com.github.fernthedev.fernapi.universal.UUIDFetcher.*;

public class UUIDSpigot implements UUIDFetchManager {

    private BukkitTask requestBukkitRunnable;

    private BukkitTask banBukkitRunnable;



    public void runTimerRequest() {
        debug("Server is bukkit");
        this.requestBukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                UUIDFetcher.setRequests(0);
                playerNameCache.clear();
                playerUUIDCache.clear();
                playerHistoryCache.clear();
                debug("Refreshed uuid cache.");
            }
        }.runTaskLater((Plugin) Universal.getMethods().getInstance(),
                TimeUnit.MINUTES.toSeconds(10) * 20);
    }

    public void runHourTask() {
        banBukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (!hourRan && didHourCheck) {
                    hourRan = true;
                    debug("Hour is finished, continuing uuid checking");
                    runTimerRequest();
                    stopHourTask();
                } else if (!didHourCheck) didHourCheck = true;
            }
        }.runTaskLater((Plugin) Universal.getMethods().getInstance(),
                TimeUnit.HOURS.toSeconds(1) *20);
    }

    public void stopTimerRequest() {
        if(requestBukkitRunnable != null) {
            Universal.getMethods().getInstance().cancelTask(requestBukkitRunnable.getTaskId());
        }
    }

    public void stopHourTask() {
        if(banBukkitRunnable != null) Universal.getMethods().getInstance().cancelTask(banBukkitRunnable.getTaskId());
    }



    private static void print(Object log) {
        Universal.getMethods().getLogger().info("[" + Universal.getMethods().getServerType() + "] [UUIDFetcher] " + log);
    }

    private static void debug(Object log) {
        Universal.debug("[" + Universal.getMethods().getServerType() + "] [UUIDFetcher] " + log);
    }
}
