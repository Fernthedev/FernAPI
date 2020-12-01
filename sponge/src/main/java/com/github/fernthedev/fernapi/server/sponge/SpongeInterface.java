package com.github.fernthedev.fernapi.server.sponge;

import com.github.fernthedev.fernapi.server.sponge.player.SpongeFConsole;
import com.github.fernthedev.fernapi.server.sponge.player.SpongeFPlayer;
import com.github.fernthedev.fernapi.universal.api.FernCommandIssuer;
import com.github.fernthedev.fernapi.universal.api.IFConsole;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.api.OfflineFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.handlers.MethodInterface;
import com.github.fernthedev.fernapi.universal.handlers.ServerType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SpongeInterface implements MethodInterface<Player, ConsoleSource> {
    @NonNull
    private final FernSpongeAPI sponge;

    @Override
    public boolean isMainThread() {
        return Sponge.getServer().isMainThread();
    }

    @Override
    public Logger getAbstractLogger() {
        return sponge.getLogger();
    }

    @Override
    public ServerType getServerType() {
        return ServerType.SPONGE;
    }

    @Override
    public FernAPIPlugin getInstance() {
        return sponge;
    }


    /**
     * @param player The player
     * @return The {@link IFPlayer} player instance
     */
    @Override
    public <P> IFPlayer<P> convertPlayerObjectToFPlayer(P player) {
        Player player1 = (Player) player;
        return (IFPlayer<P>) new SpongeFPlayer(player1, sponge.audienceProvider.player(player1));
    }

    @Override
    public Player convertFPlayerToPlayer(IFPlayer ifPlayer) {
        return Sponge.getServer().getPlayer(ifPlayer.getUniqueId()).get();
    }

    @Override
    public FernCommandIssuer convertCommandSenderToAPISender(Object commandSender) {
        if(commandSender instanceof Player) {
            Player player = (Player) commandSender;
            return new SpongeFPlayer(player, sponge.audienceProvider.player(player));
        }

        if(commandSender instanceof ConsoleSource) {
            ConsoleSource commandSender1 = (ConsoleSource) commandSender;
            return new SpongeFConsole(commandSender1, sponge.audienceProvider.console());
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
    public IFConsole<ConsoleSource> convertConsoleToAPISender(@NonNull ConsoleSource commandSender) {
        return new SpongeFConsole(commandSender, sponge.audienceProvider.console());
    }

    @Override
    public @NonNull OfflineFPlayer<Player> getPlayerFromName(String name) {
        Optional<Player> player = Sponge.getServer().getPlayer(name);

        return player.map(value -> new OfflineFPlayer<>(convertPlayerObjectToFPlayer(value)))
                .orElseGet(() -> new OfflineFPlayer<>(name));
    }

    @Override
    public @NonNull OfflineFPlayer<Player> getPlayerFromUUID(UUID uuid) {
        Optional<Player> player = Sponge.getServer().getPlayer(uuid);

        return player.map(value -> new OfflineFPlayer<>(convertPlayerObjectToFPlayer(value)))
                .orElseGet(() -> new OfflineFPlayer<>(uuid));
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
    public UUID getUUIDFromPlayerName(String name) {
        Optional<Player> p = Sponge.getGame().getServer().getPlayer(name);
        return p.map(User::getUniqueId).orElse(null);
    }

    @Override
    public List<IFPlayer<Player>> matchPlayerName(String name) {
        return getPlayers()
                .parallelStream()
                .filter(proxiedPlayerIFPlayer -> proxiedPlayerIFPlayer.getName().contains(name))
                .collect(Collectors.toList());
    }
}
