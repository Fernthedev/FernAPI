package com.github.fernthedev.fernapi.server.bungee.scheduler;

import com.github.fernthedev.fernapi.universal.data.ScheduleTaskWrapper;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.CompletableFuture;

public class BungeeScheduledTaskWrapper extends ScheduleTaskWrapper<ScheduledTask, Integer> {


    public BungeeScheduledTaskWrapper(ScheduledTask task, CompletableFuture<Void> completableFuture) {
        super(task, completableFuture);
    }

    /**
     * Gets the unique ID of this task.
     *
     * @return this tasks ID
     */
    @Override
    public Integer getId() {
        return scheduleTask.getId();
    }

    /**
     * Get the actual method which will be executed by this task.
     *
     * @return the {@link Runnable} behind this task
     */
    @Override
    public Runnable getTask() {
        return scheduleTask.getTask();
    }

    /**
     * Cancel this task to suppress subsequent executions.
     */
    @Override
    public void cancel() {
        scheduleTask.cancel();
    }
}
