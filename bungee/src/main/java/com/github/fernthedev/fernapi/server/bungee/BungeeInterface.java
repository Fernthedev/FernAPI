package com.github.fernthedev.fernapi.server.bungee;

import com.github.fernthedev.fernapi.server.bungee.player.BungeeFConsole;
import com.github.fernthedev.fernapi.server.bungee.player.BungeeFPlayer;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.FernCommandIssuer;
import com.github.fernthedev.fernapi.universal.api.IFConsole;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.api.OfflineFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.handlers.MethodInterface;
import com.github.fernthedev.fernapi.universal.handlers.ServerType;
import lombok.NonNull;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BungeeInterface implements MethodInterface<ProxiedPlayer, CommandSender> {
    private FernBungeeAPI fernBungeeAPI;

    BungeeInterface(FernBungeeAPI fernBungeeAPI) {
        this.fernBungeeAPI = fernBungeeAPI;
    }

    @Override
    public boolean isMainThread() {
        // TODO: IMPLEMENT
        return false;
    }

    @Override
    public Logger getAbstractLogger() {
        return LoggerFactory.getLogger(fernBungeeAPI.getClass());
    }

    @Override
    public ServerType getServerType() {
        return ServerType.BUNGEE;
    }

    @Override
    public FernAPIPlugin getInstance() {
        return fernBungeeAPI;
    }

    /**
     * @param player The player
     * @return The {@link IFPlayer} player instance
     */
    @Override
    public <P> IFPlayer<P> convertPlayerObjectToFPlayer(P player) {
        return (IFPlayer<P>) new BungeeFPlayer((ProxiedPlayer) player, player == null ? null : fernBungeeAPI.audienceProvider.player((ProxiedPlayer) player));
    }

    @Override
    public <P> P convertFPlayerToPlayer(IFPlayer<P> ifPlayer) {
        if(ifPlayer instanceof BungeeFPlayer) {
            return ifPlayer.getPlayer();
        }

        return (P) ProxyServer.getInstance().getPlayer(ifPlayer.getUniqueId());
    }

    @Override
    public FernCommandIssuer convertCommandSenderToAPISender(@NonNull Object commandSender) {
        if(commandSender instanceof ProxiedPlayer) {
            return new BungeeFPlayer((ProxiedPlayer) commandSender, fernBungeeAPI.audienceProvider.player((ProxiedPlayer) commandSender));
        }

        if(commandSender instanceof CommandSender) {
            CommandSender sender = (CommandSender) commandSender;
            return new BungeeFConsole(sender, fernBungeeAPI.audienceProvider.console());
        }

        return null;
    }

    /**
     * Converts the command sender to it's Console instance
     *
     * @param commandSender
     * @return
     */
    @Override
    public IFConsole<CommandSender> convertConsoleToAPISender(@NonNull CommandSender commandSender) {
        return new BungeeFConsole(commandSender, fernBungeeAPI.audienceProvider.console());
    }

    @Override
    public OfflineFPlayer<ProxiedPlayer> getPlayerFromName(String name) {
        ProxiedPlayer player = fernBungeeAPI.getProxy().getPlayer(name);

        if (player == null) {
            return new OfflineFPlayer<>(name);
        }

        return new OfflineFPlayer<>(convertPlayerObjectToFPlayer(player));
    }

    @Override
    public OfflineFPlayer<ProxiedPlayer> getPlayerFromUUID(UUID uuid) {
        ProxiedPlayer player = fernBungeeAPI.getProxy().getPlayer(uuid);

        Universal.debug("Player uuid " + uuid + " for player " + player);

        if (player == null) {
            return new OfflineFPlayer<>(uuid);
        }

        return new OfflineFPlayer<>(convertPlayerObjectToFPlayer(player));
    }



    @Override
    public List<IFPlayer<ProxiedPlayer>> getPlayers() {
        return ProxyServer.getInstance().getPlayers().stream().map(this::convertPlayerObjectToFPlayer).collect(Collectors.toList());
    }

    @Override
    public File getDataFolder() {
        return fernBungeeAPI.getDataFolder();
    }



    @Override
    public String getNameFromPlayer(UUID uuid) {
        if(ProxyServer.getInstance().getPlayer(uuid) != null && ProxyServer.getInstance().getPlayer(uuid).isConnected()) {
            return ProxyServer.getInstance().getPlayer(uuid).getName();
        }
        return null;
    }

    @Override
    public UUID getUUIDFromPlayerName(String name) {
        if(ProxyServer.getInstance().getPlayer(name) != null && ProxyServer.getInstance().getPlayer(name).isConnected()) {
            return ProxyServer.getInstance().getPlayer(name).getUniqueId();
        }
        return null;
    }

    @Override
    public List<IFPlayer<ProxiedPlayer>> matchPlayerName(String name) {
        return getPlayers()
                .parallelStream()
                .filter(proxiedPlayerIFPlayer -> proxiedPlayerIFPlayer.getName().contains(name))
                .collect(Collectors.toList());
    }


}
