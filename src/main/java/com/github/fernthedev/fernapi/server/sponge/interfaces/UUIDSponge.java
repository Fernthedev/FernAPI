package com.github.fernthedev.fernapi.server.sponge.interfaces;

import com.github.fernthedev.fernapi.server.sponge.FernSpongeAPI;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.handlers.UUIDFetchManager;
import com.github.fernthedev.fernapi.universal.util.UUIDFetcher;
import org.spongepowered.api.scheduler.Task;

import java.util.concurrent.TimeUnit;

import static com.github.fernthedev.fernapi.universal.util.UUIDFetcher.*;

@Deprecated
public class UUIDSponge extends UUIDFetchManager {

    private FernSpongeAPI sponge;

    private Task timerTask;
    private Task hourTask;

    public UUIDSponge(FernSpongeAPI sponge) {
        this.sponge =sponge;
    }


    @Override
    public void stopTimerRequest() {
        if(timerTask != null)
        timerTask.cancel();
    }

    @Override
    public void stopHourTask() {
        if(hourTask != null)
        hourTask.cancel();
    }

    @Override
    public void runTimerRequest() {
        Task.Builder taskBuilder = Task.builder();

        timerTask = taskBuilder.execute(() -> {
            UUIDFetcher.setRequests(0);
            playerNameCache.clear();
            playerUUIDCache.clear();
            playerHistoryCache.clear();
            debug("Refreshed uuid cache.");
        }).interval(10,TimeUnit.MINUTES).name("UUID Timer refresher").async().submit(sponge);
    }

    private static void debug(Object log) {
        Universal.debug("[UUIDFetcher/FernAPI] " + log);
    }

    @Override
    public void runHourTask() {
        Task.Builder taskBuilder = Task.builder();

        hourTask = taskBuilder.execute(() -> {
            if (!hourRan && didHourCheck) {
                hourRan = true;
                debug("Hour is finished, continuing uuid checking");
                runTimerRequest();
                stopHourTask();
            } else if (!didHourCheck) didHourCheck = true;
        }).delay(1,TimeUnit.HOURS).async().submit(sponge);

    }


}
