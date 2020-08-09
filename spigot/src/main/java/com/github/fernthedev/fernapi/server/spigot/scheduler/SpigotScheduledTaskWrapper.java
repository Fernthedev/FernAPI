package com.github.fernthedev.fernapi.server.spigot.scheduler;

import com.github.fernthedev.fernapi.universal.data.ScheduleTaskWrapper;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.CompletableFuture;


public class SpigotScheduledTaskWrapper extends ScheduleTaskWrapper<BukkitRunnable, Integer> {

    public SpigotScheduledTaskWrapper(BukkitRunnable task, CompletableFuture<Void> completableFuture) {
        super(task, completableFuture);
    }

    /**
     * Gets the unique ID of this task.
     *
     * @return this tasks ID
     */
    @Override
    public Integer getId() {
        return scheduleTask.getTaskId();
    }

    /**
     * Get the actual method which will be executed by this task.
     *
     * @return the {@link Runnable} behind this task
     */
    @Override
    public Runnable getTask() {
        return scheduleTask;
    }

    /**
     * Cancel this task to suppress subsequent executions.
     */
    @Override
    public void cancel() {
        scheduleTask.cancel();
    }
}
