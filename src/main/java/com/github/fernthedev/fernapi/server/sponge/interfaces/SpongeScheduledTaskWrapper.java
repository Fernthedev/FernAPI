package com.github.fernthedev.fernapi.server.sponge.interfaces;

import com.github.fernthedev.fernapi.universal.data.ScheduleTaskWrapper;
import org.spongepowered.api.scheduler.Task;

import java.util.UUID;

public class SpongeScheduledTaskWrapper extends ScheduleTaskWrapper<Task, UUID> {

    protected Runnable runnable;

    public SpongeScheduledTaskWrapper(Runnable runnable, Task task) {
        super(task);
        this.runnable = runnable;
    }

    /**
     * Gets the unique ID of this task.
     *
     * @return this tasks ID
     */
    @Override
    public UUID getId() {
        return scheduleTask.getUniqueId();
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
