package com.github.fernthedev.fernapi.universal.data;

import lombok.Getter;

import java.util.concurrent.CompletableFuture;


/**
 * Represents a task scheduled for execution by the server TaskScheduler.
 */
public abstract class ScheduleTaskWrapper<TaskType,IDType> {

    private final CompletableFuture<Void> future;

    @Getter
    protected TaskType scheduleTask;

    public ScheduleTaskWrapper(TaskType task, CompletableFuture<Void> completableFuture) {
        this.scheduleTask = task;
        this.future = completableFuture;
    }

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
     * Get the actual method which will be executed by this task.
     *
     * @return the {@link Runnable} behind this task
     */
    public CompletableFuture<Void> getTaskFuture() {
        return future;
    }

    /**
     * Cancel this task to suppress subsequent executions.
     */
    public abstract void cancel();

}
