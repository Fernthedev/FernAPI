package com.github.fernthedev.fernapi.server.sponge.scheduler;

import com.github.fernthedev.fernapi.server.sponge.FernSpongeAPI;
import com.github.fernthedev.fernapi.universal.handlers.IScheduler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class SpongeScheduler implements IScheduler<SpongeScheduledTaskWrapper, UUID> {

    @NonNull
    private FernSpongeAPI sponge;

    @Override
    public void cancelTask(UUID id) {
        Sponge.getScheduler().getTaskById(id).ifPresent(Task::cancel);
    }

    @Override
    public void cancelAllTasks() {
        Sponge.getScheduler().getScheduledTasks(sponge).forEach(Task::cancel);
    }

    /**
     * Schedules a task to be executed asynchronously after the specified delay
     * is up.
     *  @param task  the task to run
     * @param delay the delay before this task will be executed
     * @param unit  the unit in which the delay will be measured
     * @return
     */
    @Override
    public SpongeScheduledTaskWrapper runSchedule(Runnable task, long delay, TimeUnit unit) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        Runnable newTask = () -> {
            task.run();
            completableFuture.complete(null);
        };

        return new SpongeScheduledTaskWrapper(task, Sponge.getScheduler().createAsyncExecutor(sponge).schedule(newTask, delay, unit).getTask(), completableFuture);
    }

    /**
     * Runs the given runnable in async
     *
     * @param runnable the runnable
     */
    @Override
    public SpongeScheduledTaskWrapper runAsync(Runnable runnable) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        Runnable newTask = () -> {
            runnable.run();
            completableFuture.complete(null);
        };

        Task t = Task.builder().async().execute(newTask).submit(sponge);

        return new SpongeScheduledTaskWrapper(newTask, t, completableFuture);
    }

    /**
     * Schedules a task to be executed asynchronously after the specified delay
     * is up. The scheduled task will continue running at the specified
     * interval. The interval will not begin to count down until the last task
     * invocation is complete.
     *  @param task   the task to run
     * @param delay  the delay before this task will be executed
     * @param period the interval before subsequent executions of this task
     * @param unit   the unit in which the delay and period will be measured
     * @return
     */
    @Override
    public SpongeScheduledTaskWrapper runSchedule(Runnable task, long delay, long period, TimeUnit unit) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        Runnable newTask = () -> {
            task.run();
            completableFuture.complete(null);
        };

        return new SpongeScheduledTaskWrapper(newTask, Sponge.getScheduler().createAsyncExecutor(sponge).scheduleAtFixedRate(task, delay, period, unit).getTask(), completableFuture);
    }
}
