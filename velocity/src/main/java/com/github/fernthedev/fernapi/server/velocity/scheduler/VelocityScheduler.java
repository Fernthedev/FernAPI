package com.github.fernthedev.fernapi.server.velocity.scheduler;

import com.github.fernthedev.fernapi.server.velocity.FernVelocityAPI;
import com.github.fernthedev.fernapi.universal.handlers.IScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class VelocityScheduler implements IScheduler<VelocityScheduledTaskWrapper, UUID> {
    private final FernVelocityAPI fernVelocityAPI;

    private final Map<UUID, VelocityScheduledTaskWrapper> taskWrapperMap = new HashMap<>();

    public VelocityScheduler(FernVelocityAPI fernVelocityAPI) {
        this.fernVelocityAPI = fernVelocityAPI;
    }

    @Override
    public void cancelTask(UUID id) {
        taskWrapperMap.get(id).cancel();
    }

    @Override
    public void cancelAllTasks() {
        for (VelocityScheduledTaskWrapper task : taskWrapperMap.values()) {
            task.cancel();
        }
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
    public VelocityScheduledTaskWrapper runSchedule(Runnable task, long delay, TimeUnit unit) {
        UUID uuid = UUID.randomUUID();

        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        Runnable newTask = () -> {
            task.run();
            completableFuture.complete(null);
        };

        VelocityScheduledTaskWrapper taskWrapper = new VelocityScheduledTaskWrapper(newTask, fernVelocityAPI.getServer().getScheduler().buildTask(fernVelocityAPI, newTask).delay(delay, unit).schedule(), uuid, completableFuture);
        taskWrapperMap.put(uuid, taskWrapper);

        return taskWrapper;
    }

    /**
     * Runs the given runnable in async
     *
     * @param runnable the runnable
     */
    @Override
    public VelocityScheduledTaskWrapper runAsync(Runnable runnable) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        Runnable newTask = () -> {
            runnable.run();
            completableFuture.complete(null);
        };

        UUID uuid = UUID.randomUUID();
        VelocityScheduledTaskWrapper taskWrapper = new VelocityScheduledTaskWrapper(newTask, fernVelocityAPI.getServer().getScheduler().buildTask(fernVelocityAPI, newTask).schedule(), uuid, completableFuture);
        taskWrapperMap.put(uuid, taskWrapper);
        return taskWrapper;
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
    public VelocityScheduledTaskWrapper runSchedule(Runnable task, long delay, long period, TimeUnit unit) {
        CompletableFuture<Void> completableFuture = new CompletableFuture<>();
        Runnable newTask = () -> {
            task.run();
            completableFuture.complete(null);
        };

        UUID uuid = UUID.randomUUID();
        VelocityScheduledTaskWrapper taskWrapper = new VelocityScheduledTaskWrapper(newTask, fernVelocityAPI.getServer().getScheduler().buildTask(fernVelocityAPI, newTask).delay(delay, unit).repeat(period, unit).schedule(), uuid, completableFuture);
        taskWrapperMap.put(uuid, taskWrapper);
        return taskWrapper;
    }
}
