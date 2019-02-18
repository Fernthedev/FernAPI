package com.github.fernthedev.fernapi.server.bungee;

import com.github.fernthedev.fernapi.server.bungee.player.BungeeFConsole;
import com.github.fernthedev.fernapi.server.bungee.player.BungeeFPlayer;
import com.github.fernthedev.fernapi.universal.api.CommandSender;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.handlers.IFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.MethodInterface;
import com.github.fernthedev.fernapi.universal.handlers.ServerType;
import lombok.NonNull;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.logging.Logger;

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
}
