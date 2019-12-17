package com.github.fernthedev.fernapi.universal.data;

import lombok.Getter;


/**
 * Represents a task scheduled for execution by the server TaskScheduler.
 */

public abstract class ScheduleTaskWrapper<TaskType,IDType> {

    public ScheduleTaskWrapper(TaskType task) {
        this.scheduleTask = task;
    }

    @Getter
    protected TaskType scheduleTask;

    /**
     * Gets the unique ID of this task.
     *
     * @return this tasks ID
     */
    public abstract IDType getId();

    /**
     * Get the actual method which will be executed by this task.
     *
     * @return the {@link Runnable} behind this task
     */
    public abstract Runnable getTask();

    /**
     * Cancel this task to suppress subsequent executions.
     */
    public abstract void cancel();

}
