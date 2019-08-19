package com.github.fernthedev.fernapi.server.velocity;

import com.github.fernthedev.fernapi.server.velocity.player.VelocityFConsole;
import com.github.fernthedev.fernapi.server.velocity.player.VelocityFPlayer;
import com.github.fernthedev.fernapi.universal.api.CommandSender;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.handlers.IFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.MethodInterface;
import com.github.fernthedev.fernapi.universal.handlers.ServerType;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import lombok.NonNull;

import java.util.UUID;
import java.util.logging.Logger;

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
        return ServerType.BUNGEE;
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

}
