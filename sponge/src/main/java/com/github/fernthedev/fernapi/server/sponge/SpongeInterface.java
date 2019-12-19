package com.github.fernthedev.fernapi.server.sponge;

import com.github.fernthedev.fernapi.server.sponge.player.SpongeFConsole;
import com.github.fernthedev.fernapi.server.sponge.player.SpongeFPlayer;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.CommandSender;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.handlers.MethodInterface;
import com.github.fernthedev.fernapi.universal.handlers.ServerType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SpongeInterface implements MethodInterface<Player> {
    @NonNull
    private FernSpongeAPI sponge;

    @Override
    public java.util.logging.Logger getLogger() {
        Universal.debug("Java Logger does not exist in Sponge.");
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
    public <P> IFPlayer<P> convertPlayerObjectToFPlayer(P player) {
        return (IFPlayer<P>) new SpongeFPlayer((Player) player);
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
    public IFPlayer<Player> getPlayerFromName(String name) {

        return convertPlayerObjectToFPlayer(Sponge.getServer().getPlayer(name).get());
    }

    @Override
    public IFPlayer<Player> getPlayerFromUUID(UUID uuid) {
        return convertPlayerObjectToFPlayer(Sponge.getServer().getPlayer(uuid).get());
    }

    @Override
    public List<IFPlayer<Player>> getPlayers() {
        return Sponge.getServer().getOnlinePlayers().stream().map(this::convertPlayerObjectToFPlayer).collect(Collectors.toList());
    }

    @Override
    public File getDataFolder() {
        return sponge.privateConfigDir.toFile();
    }

    @Override
    public String getNameFromPlayer(UUID uuid) {
        Optional<Player> p = Sponge.getGame().getServer().getPlayer(uuid);
        return p.map(User::getName).orElse(null);
    }

    @Override
    public UUID getUUIDFromPlayer(String name) {
        Optional<Player> p = Sponge.getGame().getServer().getPlayer(name);
        return p.map(User::getUniqueId).orElse(null);
    }
}