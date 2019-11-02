package com.github.fernthedev.fernapi.server.sponge;

import com.github.fernthedev.fernapi.server.sponge.interfaces.SpongeScheduledTaskWrapper;
import com.github.fernthedev.fernapi.server.sponge.player.SpongeFConsole;
import com.github.fernthedev.fernapi.server.sponge.player.SpongeFPlayer;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.CommandSender;
import com.github.fernthedev.fernapi.universal.data.ScheduleTaskWrapper;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.MethodInterface;
import com.github.fernthedev.fernapi.universal.handlers.ServerType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SpongeInterface implements MethodInterface {
    @NonNull
    private FernSpongeAPI sponge;

    @Override
    public java.util.logging.Logger getLogger() {
        sponge.getLogger().warn("Java Logger does not exist in Sponge.");
        return (java.util.logging.Logger) sponge.getLogger();
    }

    @Override
    public ServerType getServerType() {
        return ServerType.SPONGE;
    }

    @Override
    public FernAPIPlugin getInstance() {
        return sponge;
    }

    @Override
    public IFPlayer convertPlayerObjectToFPlayer(Object player) {
        return new SpongeFPlayer((Player) player);
    }

    @Override
    public Player convertFPlayerToPlayer(IFPlayer ifPlayer) {
        return Sponge.getServer().getPlayer(ifPlayer.getUuid()).get();
    }

    @Override
    public CommandSender convertCommandSenderToAPISender(Object commandSender) {
        if(commandSender instanceof Player) {
            return new SpongeFPlayer((Player) commandSender);
        }

        if(commandSender instanceof CommandSource) {
            return new SpongeFConsole((CommandSource) commandSender);
        }

        return null;
    }

    @Override
    public IFPlayer getPlayerFromName(String name) {
        return convertPlayerObjectToFPlayer(Sponge.getServer().getPlayer(name));
    }

    @Override
    public IFPlayer getPlayerFromUUID(UUID uuid) {
        return convertPlayerObjectToFPlayer(Sponge.getServer().getPlayer(uuid));
    }

    @Override
    public void runAsync(Runnable runnable) {
        new Thread(runnable).start();
    }

    @Override
    public List<IFPlayer> getPlayers() {
        return Sponge.getServer().getOnlinePlayers().stream().map(proxiedPlayer -> Universal.getMethods().convertPlayerObjectToFPlayer(proxiedPlayer)).collect(Collectors.toList());
    }

    @Override
    public File getDataFolder() {
        return sponge.privateConfigDir.toFile();
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
        return new SpongeScheduledTaskWrapper(task, Sponge.getScheduler().createAsyncExecutor(sponge).schedule(task, delay, unit).getTask());
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
        return new SpongeScheduledTaskWrapper(task, Sponge.getScheduler().createAsyncExecutor(sponge).scheduleAtFixedRate(task, delay, period, unit).getTask());
    }
}
