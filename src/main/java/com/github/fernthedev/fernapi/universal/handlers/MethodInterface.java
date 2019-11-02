package com.github.fernthedev.fernapi.universal.handlers;

import com.github.fernthedev.fernapi.universal.api.CommandSender;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.data.ScheduleTaskWrapper;
import lombok.NonNull;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public interface MethodInterface {

    Logger getLogger();

    ServerType getServerType();

    FernAPIPlugin getInstance();

    IFPlayer convertPlayerObjectToFPlayer(Object player);

    Object convertFPlayerToPlayer(IFPlayer ifPlayer);

    CommandSender convertCommandSenderToAPISender(@NonNull Object commandSender);

    IFPlayer getPlayerFromName(String name);

    IFPlayer getPlayerFromUUID(UUID uuid);

    void runAsync(Runnable runnable);

    List<IFPlayer> getPlayers();

    File getDataFolder();

    /**
     * Schedules a task to be executed asynchronously after the specified delay
     * is up.
     *
     * @param task  the task to run
     * @param delay the delay before this task will be executed
     * @param unit  the unit in which the delay will be measured
     */
    ScheduleTaskWrapper runSchedule(Runnable task, long delay, TimeUnit unit);


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
    ScheduleTaskWrapper runSchedule(Runnable task, long delay, long period, TimeUnit unit);
}

