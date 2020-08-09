package com.github.fernthedev.fernapi.server.spigot.scheduler;

import com.github.fernthedev.fernapi.server.spigot.FernSpigotAPI;
import com.github.fernthedev.fernapi.universal.handlers.IScheduler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class SpigotScheduler implements IScheduler<SpigotScheduledTaskWrapper, Integer> {
    @NonNull
    private FernSpigotAPI fernSpigotAPI;

    @Override
    public void cancelTask(Integer id) {
        fernSpigotAPI.getServer().getScheduler().cancelTask(id);
    }

    @Override
    public void cancelAllTasks() {
        fernSpigotAPI.getServer().getScheduler().cancelTasks(fernSpigotAPI);
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
    public SpigotScheduledTaskWrapper runSchedule(Runnable task, long delay, TimeUnit unit) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
                completableFuture.complete(null);
            }
        };
        runnable.runTaskLaterAsynchronously(fernSpigotAPI, unit.toSeconds(delay) * 20);
        return new SpigotScheduledTaskWrapper(runnable, completableFuture);
    }

    /**
     * Runs the given runnable in async
     *
     * @param runnable the runnable
     */
    @Override
    public SpigotScheduledTaskWrapper runAsync(Runnable runnable) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
                completableFuture.complete(null);
            }
        };
        bukkitRunnable.runTaskAsynchronously(fernSpigotAPI);
        return new SpigotScheduledTaskWrapper(bukkitRunnable, completableFuture);
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
    public SpigotScheduledTaskWrapper runSchedule(Runnable task, long delay, long period, TimeUnit unit) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
                completableFuture.complete(null);
            }
        };
        runnable.runTaskTimerAsynchronously(fernSpigotAPI, unit.toSeconds(delay) * 20, 20 * unit.toSeconds(period));
        return new SpigotScheduledTaskWrapper(runnable, completableFuture);
    }
}
