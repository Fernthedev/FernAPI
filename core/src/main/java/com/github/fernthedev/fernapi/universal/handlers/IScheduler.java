package com.github.fernthedev.fernapi.universal.handlers;

import com.github.fernthedev.fernapi.universal.data.ScheduleTaskWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public interface IScheduler<TaskWrapper extends ScheduleTaskWrapper<?,?>, IDType> {
    void cancelTask(IDType id);
    void cancelAllTasks();

    /**
     * Schedules a task to be executed asynchronously after the specified delay
     * is up.
     *
     * @param task  the task to run
     * @param delay the delay before this task will be executed
     * @param unit  the unit in which the delay will be measured
     */
    TaskWrapper runSchedule(Runnable task, long delay, TimeUnit unit);

    /**
     * Runs the given runnable in async
     * @param runnable the runnable
     */
    TaskWrapper runAsync(Runnable runnable);

    /**
     * Schedules a task to be executed asynchronously after the specified delay
     * is up. The scheduled task will continue running at the specified
     * interval. The interval will not begin to count down until the last task
     * invocation is complete.
     *
     * @param task   the task to run
     * @param delay  the delay before this task will be executed
     * @param period the interval before subsequent executions of this task
     * @param unit   the unit in which the delay and period will be measured
     */
    TaskWrapper runSchedule(Runnable task, long delay, long period, TimeUnit unit);
}
