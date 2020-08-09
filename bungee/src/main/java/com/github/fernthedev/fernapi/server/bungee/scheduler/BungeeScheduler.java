package com.github.fernthedev.fernapi.server.bungee.scheduler;

import com.github.fernthedev.fernapi.server.bungee.FernBungeeAPI;
import com.github.fernthedev.fernapi.universal.handlers.IScheduler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ProxyServer;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class BungeeScheduler implements IScheduler<BungeeScheduledTaskWrapper, Integer> {

    @NonNull
    private FernBungeeAPI fernBungeeAPI;

    @Override
    public void cancelTask(Integer id) {
        fernBungeeAPI.getProxy().getScheduler().cancel(id);
    }

    @Override
    public void cancelAllTasks() {
        fernBungeeAPI.getProxy().getScheduler().cancel(fernBungeeAPI);
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
    public BungeeScheduledTaskWrapper runSchedule(Runnable task, long delay, TimeUnit unit) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        Runnable newTask = () -> {
            task.run();
            completableFuture.complete(null);
        };

        return new BungeeScheduledTaskWrapper(ProxyServer.getInstance().getScheduler().schedule(fernBungeeAPI, newTask, delay, unit), completableFuture);
    }

    /**
     * Runs the given runnable in async
     *
     * @param runnable the runnable
     * @return
     */
    @Override
    public BungeeScheduledTaskWrapper runAsync(Runnable runnable) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        Runnable newTask = () -> {
            runnable.run();
            completableFuture.complete(null);
        };

        return new BungeeScheduledTaskWrapper(fernBungeeAPI.getProxy().getScheduler().runAsync(fernBungeeAPI, newTask), completableFuture);
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
    public BungeeScheduledTaskWrapper runSchedule(Runnable task, long delay, long period, TimeUnit unit) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        Runnable newTask = () -> {
            task.run();
            completableFuture.complete(null);
        };

        return new BungeeScheduledTaskWrapper(ProxyServer.getInstance().getScheduler().schedule(fernBungeeAPI, newTask, delay, period, unit), completableFuture);
    }



}
