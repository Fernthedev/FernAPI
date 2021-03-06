package com.github.fernthedev.fernapi.server.spigot;

import com.github.fernthedev.fernapi.server.spigot.player.SpigotFConsole;
import com.github.fernthedev.fernapi.server.spigot.player.SpigotFPlayer;
import com.github.fernthedev.fernapi.universal.api.FernCommandIssuer;
import com.github.fernthedev.fernapi.universal.api.IFConsole;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.api.OfflineFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.handlers.MethodInterface;
import com.github.fernthedev.fernapi.universal.handlers.ServerType;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class SpigotInterface implements MethodInterface<Player, ConsoleCommandSender> {

    private final FernSpigotAPI fernSpigotAPI;
    private final Logger abstractLogger;
    private final IFConsole<ConsoleCommandSender> console;

    SpigotInterface(FernSpigotAPI fernSpigotAPI) {
        this.fernSpigotAPI = fernSpigotAPI;
        abstractLogger = LoggerFactory.getLogger(fernSpigotAPI.getLogger().getName());
        console = new SpigotFConsole(Bukkit.getConsoleSender(), fernSpigotAPI.getAudienceProvider().console());
    }

    @Override
    public boolean isMainThread() {
        return Bukkit.isPrimaryThread();
    }

    @Override
    public Logger getAbstractLogger() {
        return abstractLogger;
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
    public @NonNull ConsoleCommandSender getConsole() {
        return Bukkit.getConsoleSender();
    }

    @Override
    public @NonNull IFConsole<ConsoleCommandSender> getConsoleAbstract() {
        return console;
    }

    @Override
    public <P> IFPlayer<P> convertPlayerObjectToFPlayer(P player) {
        return (IFPlayer<P>) new SpigotFPlayer((Player) player, player == null ? null : fernSpigotAPI.audienceProvider.player((Player) player));
//        return new SpigotFPlayer((Player) player);
    }

    @Override
    public <P> P convertFPlayerToPlayer(IFPlayer<P> ifPlayer) {
        if(ifPlayer instanceof SpigotFPlayer) {
            return ifPlayer.getPlayer();
        }

        return (P) Bukkit.getPlayer(ifPlayer.getUniqueId());
    }

    @Override
    public FernCommandIssuer convertCommandSenderToAPISender(Object commandSender) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            return new SpigotFPlayer(player, fernSpigotAPI.audienceProvider.player(player));
        }

        if(commandSender instanceof ConsoleCommandSender) {
            return console;
        }

        return null;
    }

    /**
     * Converts the command sender to it's IFPlayer instance
     *
     * @param commandSender
     * @return
     */
    @Override
    public IFConsole<ConsoleCommandSender> convertConsoleToAPISender(@NonNull ConsoleCommandSender commandSender) {
        return console;
    }

    @Override
    public @NonNull OfflineFPlayer<Player> getPlayerFromName(String name) {
        Player player = fernSpigotAPI.getServer().getPlayer(name);

        if (player == null) {
            return new OfflineFPlayer<>(name);
        }

        return new OfflineFPlayer<>(convertPlayerObjectToFPlayer(player));
    }

    @Override
    public @NonNull OfflineFPlayer<Player> getPlayerFromUUID(UUID uuid) {
        Player player = fernSpigotAPI.getServer().getPlayer(uuid);

        if (player == null) {
            return new OfflineFPlayer<>(uuid);
        }

        return new OfflineFPlayer<>(convertPlayerObjectToFPlayer(player));
    }

    @Override
    public List<IFPlayer<Player>> getPlayers() {
        return Bukkit.getOnlinePlayers().stream().map(player -> (Player) player) // Cast all player interfaces to just players
                .map(this::convertPlayerObjectToFPlayer).collect(Collectors.toList()); // Convert players to wrapped IFPLayer
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
    public UUID getUUIDFromPlayerName(String name) {
        if (fernSpigotAPI.getServer().getPlayer(name) == null) return null;
        return Objects.requireNonNull(fernSpigotAPI.getServer().getPlayer(name)).getUniqueId();
    }

    @Override
    public List<IFPlayer<Player>> matchPlayerName(String name) {
        return getPlayers()
                .parallelStream()
                .filter(proxiedPlayerIFPlayer -> proxiedPlayerIFPlayer.getName().contains(name))
                .collect(Collectors.toList());
    }
}
