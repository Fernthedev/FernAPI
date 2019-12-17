package com.github.fernthedev.fernapi.server.bungee.scheduler;

import com.github.fernthedev.fernapi.universal.data.ScheduleTaskWrapper;
import net.md_5.bungee.api.scheduler.ScheduledTask;

public class BungeeScheduledTaskWrapper extends ScheduleTaskWrapper<ScheduledTask, Integer> {


    public BungeeScheduledTaskWrapper(ScheduledTask task) {
        super(task);
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
