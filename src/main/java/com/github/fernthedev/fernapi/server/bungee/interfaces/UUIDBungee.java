package com.github.fernthedev.fernapi.server.bungee.interfaces;

import com.github.fernthedev.fernapi.universal.handlers.UUIDFetchManager;
import com.github.fernthedev.fernapi.universal.UUIDFetcher;
import com.github.fernthedev.fernapi.universal.Universal;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.github.fernthedev.fernapi.universal.UUIDFetcher.*;

public class UUIDBungee implements UUIDFetchManager {
    private static ScheduledTask requestTask;

    private static ScheduledTask banHourTask;



    public void runTimerRequest() {
        debug("Server is bungee");
        requestTask = ProxyServer.getInstance().getScheduler().schedule((Plugin) Universal.getMethods().getInstance(), () -> {
            UUIDFetcher.setRequests(0);
            playerNameCache.clear();
            playerUUIDCache.clear();
            playerHistoryCache.clear();
            debug("Refreshed uuid cache.");
        }, 1, 10, TimeUnit.MINUTES);
    }

    public void runHourTask() {

        banHourTask = ProxyServer.getInstance().getScheduler().schedule((Plugin) Universal.getMethods().getInstance(), () -> {
            if (!hourRan && didHourCheck) {
                hourRan = true;
                addRequestTimer();
                stopHourTask();
            } else if (!didHourCheck) didHourCheck = true;
        }, 1, 1, TimeUnit.HOURS);

    }

    @Override
    public String getNameFromPlayer(UUID uuid) {
        if(ProxyServer.getInstance().getPlayer(uuid) != null && ProxyServer.getInstance().getPlayer(uuid).isConnected()) {
            return ProxyServer.getInstance().getPlayer(uuid).getName();
        }
        return null;
    }

    @Override
    public UUID getUUIDFromPlayer(String name) {
        if(ProxyServer.getInstance().getPlayer(name) != null && ProxyServer.getInstance().getPlayer(name).isConnected()) {
            return ProxyServer.getInstance().getPlayer(name).getUniqueId();
        }
        return null;
    }

    public void stopTimerRequest() {
        if(requestTask != null) {
            ProxyServer.getInstance().getScheduler().cancel(requestTask);
        }
    }

    public void stopHourTask() {
        if(banHourTask != null)
        ProxyServer.getInstance().getScheduler().cancel(banHourTask);

    }



    private static void print(Object log) {
        Universal.getMethods().getLogger().info("[" + Universal.getMethods().getServerType() + "] [UUIDFetcher] " + log);
    }

    private static void debug(Object log) {
        Universal.debug("[" + Universal.getMethods().getServerType() + "] [UUIDFetcher] " + log);
    }
}
