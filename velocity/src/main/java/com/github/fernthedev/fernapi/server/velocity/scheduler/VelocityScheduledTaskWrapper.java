package com.github.fernthedev.fernapi.server.velocity.scheduler;

import com.github.fernthedev.fernapi.universal.data.ScheduleTaskWrapper;
import com.velocitypowered.api.scheduler.ScheduledTask;

import java.util.UUID;

public class VelocityScheduledTaskWrapper extends ScheduleTaskWrapper<ScheduledTask, UUID> {

    protected Runnable runnable;
    protected UUID uuid;

    public VelocityScheduledTaskWrapper(Runnable runnable, ScheduledTask task, UUID uuid) {
        super(task);
        this.runnable = runnable;
        this.uuid = uuid;
    }

    /**
     * Gets the unique ID of this task.
     *
     * @return this tasks ID
     */
    @Override
    public UUID getId() {
        return uuid;
    }

    /**
     * Get the actual method which will be executed by this task.
     *
     * @return the {@link Runnable} behind this task
     */
    @Override
    public Runnable getTask() {
        return runnable;
    }

    /**
     * Cancel this task to suppress subsequent executions.
     */
    @Override
    public void cancel() {
        scheduleTask.cancel();
    }
}
