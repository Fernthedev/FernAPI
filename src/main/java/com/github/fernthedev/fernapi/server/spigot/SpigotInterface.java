package com.github.fernthedev.fernapi.server.spigot;

import com.github.fernthedev.fernapi.server.spigot.player.SpigotFConsole;
import com.github.fernthedev.fernapi.server.spigot.player.SpigotFPlayer;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.CommandSender;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.handlers.MethodInterface;
import com.github.fernthedev.fernapi.universal.handlers.ServerType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class SpigotInterface implements MethodInterface {
    private FernSpigotAPI fernSpigotAPI;
    SpigotInterface(FernSpigotAPI fernSpigotAPI) {
        this.fernSpigotAPI = fernSpigotAPI;
    }

    @Override
    public Logger getLogger() {
        return fernSpigotAPI.getLogger();
    }

    @Override
    public ServerType getServerType() {
        return ServerType.BUKKIT;
    }

    @Override
    public FernAPIPlugin getInstance() {
        return fernSpigotAPI;
    }

    @Override
    public IFPlayer convertPlayerObjectToFPlayer(Object player) {
        return new SpigotFPlayer((Player) player);
    }

    @Override
    public Player convertFPlayerToPlayer(IFPlayer ifPlayer) {
        if(ifPlayer instanceof SpigotFPlayer) {
            return ((SpigotFPlayer) ifPlayer).getPlayer();
        }

        return Bukkit.getPlayer(ifPlayer.getUuid());

    }

    @Override
    public CommandSender convertCommandSenderToAPISender(Object commandSender) {
        if(commandSender instanceof Player) {
            return new SpigotFPlayer((Player) commandSender);
        }

        if(commandSender instanceof org.bukkit.command.CommandSender) {
            return new SpigotFConsole((org.bukkit.command.CommandSender) commandSender);
        }

        return null;
    }

    @Override
    public IFPlayer getPlayerFromName(String name) {
        return convertPlayerObjectToFPlayer(fernSpigotAPI.getServer().getPlayer(name));
    }

    @Override
    public IFPlayer getPlayerFromUUID(UUID uuid) {
        return convertPlayerObjectToFPlayer(fernSpigotAPI.getServer().getPlayer(uuid));
    }

    @Override
    public void runAsync(Runnable runnable) {
        fernSpigotAPI.getServer().getScheduler().runTaskAsynchronously(fernSpigotAPI, runnable);
    }

    @Override
    public List<IFPlayer> getPlayers() {
        return Bukkit.getOnlinePlayers().stream().map(proxiedPlayer -> Universal.getMethods().convertPlayerObjectToFPlayer(proxiedPlayer)).collect(Collectors.toList());
    }

    @Override
    public File getDataFolder() {
        return fernSpigotAPI.getDataFolder();
    }

    @Override
    public String getNameFromPlayer(UUID uuid) {
        if (fernSpigotAPI.getServer().getPlayer(uuid) == null) return null;
        return Objects.requireNonNull(fernSpigotAPI.getServer().getPlayer(uuid)).getName();
    }

    @Override
    public UUID getUUIDFromPlayer(String name) {
        if (fernSpigotAPI.getServer().getPlayer(name) == null) return null;
        return Objects.requireNonNull(fernSpigotAPI.getServer().getPlayer(name)).getUniqueId();
    }
}
