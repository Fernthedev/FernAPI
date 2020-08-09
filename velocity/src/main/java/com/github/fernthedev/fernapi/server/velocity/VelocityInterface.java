package com.github.fernthedev.fernapi.server.velocity;

import com.github.fernthedev.fernapi.server.velocity.player.VelocityFConsole;
import com.github.fernthedev.fernapi.server.velocity.player.VelocityFPlayer;
import com.github.fernthedev.fernapi.universal.api.FernCommandIssuer;
import com.github.fernthedev.fernapi.universal.api.IFConsole;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.api.OfflineFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.handlers.MethodInterface;
import com.github.fernthedev.fernapi.universal.handlers.ServerType;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import lombok.NonNull;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class VelocityInterface implements MethodInterface<Player, ConsoleCommandSource> {
    private FernVelocityAPI fernVelocityAPI;

    VelocityInterface(FernVelocityAPI fernVelocityAPI) {
        this.fernVelocityAPI = fernVelocityAPI;
    }

    @Override
    public boolean isMainThread() {
        // TODO: IMPLEMENT
        return false;
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

    /**
     * @param player The player
     * @return The {@link IFPlayer} player instance
     */
    @Override
    public <P> IFPlayer<P> convertPlayerObjectToFPlayer(P player) {
        return (IFPlayer<P>) new VelocityFPlayer((Player) player);
    }

    @Override
    public <P> P convertFPlayerToPlayer(IFPlayer<P> ifPlayer) {
        if(ifPlayer instanceof VelocityFPlayer) {
            return ifPlayer.getPlayer();
        }

        if (fernVelocityAPI.getServer().getPlayer(ifPlayer.getUniqueId()).isPresent()) {
            return (P) fernVelocityAPI.getServer().getPlayer(ifPlayer.getUniqueId()).get();
        } else throw new IllegalStateException("Player is not on server. UUID: " + ifPlayer.getUniqueId());
    }

    @Override
    public FernCommandIssuer convertCommandSenderToAPISender(@NonNull Object commandSender) {
        if(commandSender instanceof Player) {
            return new VelocityFPlayer((Player) commandSender);
        }

        if(commandSender instanceof ConsoleCommandSource) {
            return new VelocityFConsole((ConsoleCommandSource) commandSender);
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
    public IFConsole<ConsoleCommandSource> convertConsoleToAPISender(@NonNull ConsoleCommandSource commandSender) {
        return new VelocityFConsole(commandSender);
    }

    @Override
    public @NonNull OfflineFPlayer<Player> getPlayerFromName(String name) {
        Optional<Player> player = fernVelocityAPI.getServer().getPlayer(name);

        return player.map(value -> new OfflineFPlayer<>(convertPlayerObjectToFPlayer(value)))
                .orElseGet(() -> new OfflineFPlayer<>(name));
    }

    @Override
    public @NonNull OfflineFPlayer<Player> getPlayerFromUUID(UUID uuid) {
        Optional<Player> player = fernVelocityAPI.getServer().getPlayer(uuid);

        return player.map(value -> new OfflineFPlayer<>(convertPlayerObjectToFPlayer(value)))
                .orElseGet(() -> new OfflineFPlayer<>(uuid));
    }

    @Override
    public List<IFPlayer<Player>> getPlayers() {
        return fernVelocityAPI.getServer().getAllPlayers().stream().map(this::convertPlayerObjectToFPlayer).collect(Collectors.toList());
    }

    @Override
    public File getDataFolder() {
        return fernVelocityAPI.getDataDirectory().toFile();
    }

    @Override
    public String getNameFromPlayer(UUID uuid) {
        if(fernVelocityAPI.getServer().getPlayer(uuid).isPresent() && fernVelocityAPI.getServer().getPlayer(uuid).get().isActive()) {
            return fernVelocityAPI.getServer().getPlayer(uuid).get().getUsername();
        }
        return null;
    }

    @Override
    public UUID getUUIDFromPlayerName(String name) {
        if(fernVelocityAPI.getServer().getPlayer(name).isPresent() && fernVelocityAPI.getServer().getPlayer(name).get().isActive()) {
            return fernVelocityAPI.getServer().getPlayer(name).get().getUniqueId();
        }
        return null;
    }

    @Override
    public List<IFPlayer<Player>> matchPlayerName(String name) {
        return getPlayers()
                .parallelStream()
                .filter(proxiedPlayerIFPlayer -> proxiedPlayerIFPlayer.getName().contains(name))
                .collect(Collectors.toList());
    }
}
