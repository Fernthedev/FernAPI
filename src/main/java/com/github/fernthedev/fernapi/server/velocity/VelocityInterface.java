package com.github.fernthedev.fernapi.server.velocity;

import com.github.fernthedev.fernapi.server.velocity.interfaces.VelocityScheduledTaskWrapper;
import com.github.fernthedev.fernapi.server.velocity.player.VelocityFConsole;
import com.github.fernthedev.fernapi.server.velocity.player.VelocityFPlayer;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.CommandSender;
import com.github.fernthedev.fernapi.universal.data.ScheduleTaskWrapper;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.MethodInterface;
import com.github.fernthedev.fernapi.universal.handlers.ServerType;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import lombok.NonNull;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class VelocityInterface implements MethodInterface {
    private FernVelocityAPI fernVelocityAPI;

    VelocityInterface(FernVelocityAPI fernVelocityAPI) {
        this.fernVelocityAPI = fernVelocityAPI;
    }

    @Override
    public Logger getLogger() {
        fernVelocityAPI.getLogger().warn("Java Logger does not exist in Velocity.");
        return (Logger) fernVelocityAPI.getLogger();
    }

    @Override
    public ServerType getServerType() {
        return ServerType.VELOCITY;
    }

    @Override
    public FernAPIPlugin getInstance() {
        return fernVelocityAPI;
    }

    @Override
    public IFPlayer convertPlayerObjectToFPlayer(Object player) {
        return new VelocityFPlayer((Player) player);
    }

    @Override
    public Player convertFPlayerToPlayer(IFPlayer ifPlayer) {
        if(ifPlayer instanceof VelocityFPlayer) {
            return ((VelocityFPlayer) ifPlayer).getPlayer();
        }

        if (fernVelocityAPI.getServer().getPlayer(ifPlayer.getUuid()).isPresent()) {
            return fernVelocityAPI.getServer().getPlayer(ifPlayer.getUuid()).get();
        } else throw new IllegalStateException("Player is not on server. UUID: " + ifPlayer.getUuid());
    }

    @Override
    public CommandSender convertCommandSenderToAPISender(@NonNull Object commandSender) {
        if(commandSender instanceof Player) {
            return new VelocityFPlayer((Player) commandSender);
        }

        if(commandSender instanceof CommandSource) {
            return new VelocityFConsole((CommandSource) commandSender);
        }

        return null;
    }

    @Override
    public IFPlayer getPlayerFromName(String name) {
        return convertPlayerObjectToFPlayer(fernVelocityAPI.getServer().getPlayer(name));
    }

    @Override
    public IFPlayer getPlayerFromUUID(UUID uuid) {
        return convertPlayerObjectToFPlayer(fernVelocityAPI.getServer().getPlayer(uuid));
    }

    @Override
    public void runAsync(Runnable runnable) {
        new Thread(runnable).start();
    }

    @Override
    public List<IFPlayer> getPlayers() {
        return fernVelocityAPI.getServer().getAllPlayers().stream().map(proxiedPlayer -> Universal.getMethods().convertPlayerObjectToFPlayer(proxiedPlayer)).collect(Collectors.toList());
    }

    @Override
    public File getDataFolder() {
        return fernVelocityAPI.getDataDirectory().toFile();
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
    public ScheduleTaskWrapper runSchedule(Runnable task, long delay, TimeUnit unit) {
        return new VelocityScheduledTaskWrapper(task, fernVelocityAPI.getServer().getScheduler().buildTask(fernVelocityAPI, task).delay(delay, unit).schedule());
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
    public ScheduleTaskWrapper runSchedule(Runnable task, long delay, long period, TimeUnit unit) {
        return new VelocityScheduledTaskWrapper(task, fernVelocityAPI.getServer().getScheduler().buildTask(fernVelocityAPI, task).delay(delay, unit).repeat(period, unit).schedule());
    }
}
