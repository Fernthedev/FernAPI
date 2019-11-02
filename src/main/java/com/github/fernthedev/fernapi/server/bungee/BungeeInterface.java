package com.github.fernthedev.fernapi.server.bungee;

import com.github.fernthedev.fernapi.server.bungee.interfaces.BungeeScheduledTaskWrapper;
import com.github.fernthedev.fernapi.server.bungee.player.BungeeFConsole;
import com.github.fernthedev.fernapi.server.bungee.player.BungeeFPlayer;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.CommandSender;
import com.github.fernthedev.fernapi.universal.data.ScheduleTaskWrapper;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.MethodInterface;
import com.github.fernthedev.fernapi.universal.handlers.ServerType;
import lombok.NonNull;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class BungeeInterface implements MethodInterface {
    private FernBungeeAPI fernBungeeAPI;

    BungeeInterface(FernBungeeAPI fernBungeeAPI) {
        this.fernBungeeAPI = fernBungeeAPI;
    }

    @Override
    public Logger getLogger() {
        return fernBungeeAPI.getLogger();
    }

    @Override
    public ServerType getServerType() {
        return ServerType.BUNGEE;
    }

    @Override
    public FernAPIPlugin getInstance() {
        return fernBungeeAPI;
    }

    @Override
    public IFPlayer convertPlayerObjectToFPlayer(Object player) {
        return new BungeeFPlayer((ProxiedPlayer) player);
    }

    @Override
    public ProxiedPlayer convertFPlayerToPlayer(IFPlayer ifPlayer) {
        if(ifPlayer instanceof BungeeFPlayer) {
            return ((BungeeFPlayer) ifPlayer).getPlayer();
        }

        return ProxyServer.getInstance().getPlayer(ifPlayer.getUuid());
    }

    @Override
    public CommandSender convertCommandSenderToAPISender(@NonNull Object commandSender) {
        if(commandSender instanceof ProxiedPlayer) {
            return new BungeeFPlayer((ProxiedPlayer) commandSender);
        }

        if(commandSender instanceof net.md_5.bungee.api.CommandSender) {
            return new BungeeFConsole((net.md_5.bungee.api.CommandSender) commandSender);
        }

        return null;
    }

    @Override
    public IFPlayer getPlayerFromName(String name) {
        return convertPlayerObjectToFPlayer(fernBungeeAPI.getProxy().getPlayer(name));
    }

    @Override
    public IFPlayer getPlayerFromUUID(UUID uuid) {
        return convertPlayerObjectToFPlayer(fernBungeeAPI.getProxy().getPlayer(uuid));
    }

    @Override
    public void runAsync(Runnable runnable) {
        fernBungeeAPI.getProxy().getScheduler().runAsync(fernBungeeAPI, runnable);
    }

    @Override
    public List<IFPlayer> getPlayers() {
        return ProxyServer.getInstance().getPlayers().stream().map(proxiedPlayer -> Universal.getMethods().convertPlayerObjectToFPlayer(proxiedPlayer)).collect(Collectors.toList());
    }

    @Override
    public File getDataFolder() {
        return fernBungeeAPI.getDataFolder();
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
        return new BungeeScheduledTaskWrapper(ProxyServer.getInstance().getScheduler().schedule(fernBungeeAPI, task, delay, unit));
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
        return new BungeeScheduledTaskWrapper(ProxyServer.getInstance().getScheduler().schedule(fernBungeeAPI, task, delay, period, unit));
    }
}
