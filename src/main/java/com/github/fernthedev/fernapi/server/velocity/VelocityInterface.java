package com.github.fernthedev.fernapi.server.velocity;

import com.github.fernthedev.fernapi.server.velocity.player.VelocityFConsole;
import com.github.fernthedev.fernapi.server.velocity.player.VelocityFPlayer;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.CommandSender;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.handlers.MethodInterface;
import com.github.fernthedev.fernapi.universal.handlers.ServerType;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import lombok.NonNull;

import java.io.File;
import java.util.List;
import java.util.UUID;
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

    @Override
    public String getNameFromPlayer(UUID uuid) {
        if(fernVelocityAPI.getServer().getPlayer(uuid).isPresent() && fernVelocityAPI.getServer().getPlayer(uuid).get().isActive()) {
            return fernVelocityAPI.getServer().getPlayer(uuid).get().getUsername();
        }
        return null;
    }

    @Override
    public UUID getUUIDFromPlayer(String name) {
        if(fernVelocityAPI.getServer().getPlayer(name).isPresent() && fernVelocityAPI.getServer().getPlayer(name).get().isActive()) {
            return fernVelocityAPI.getServer().getPlayer(name).get().getUniqueId();
        }
        return null;
    }
}
