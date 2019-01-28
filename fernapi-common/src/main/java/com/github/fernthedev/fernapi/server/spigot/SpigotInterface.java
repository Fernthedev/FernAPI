package com.github.fernthedev.fernapi.server.spigot;

import com.github.fernthedev.fernapi.server.spigot.player.SpigotFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.handlers.IFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.MethodInterface;
import com.github.fernthedev.fernapi.universal.handlers.ServerType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

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
    public Object convertFPlayerToPlayer(IFPlayer ifPlayer) {
        if(ifPlayer instanceof SpigotFPlayer) {
            return ((SpigotFPlayer) ifPlayer).getPlayer();
        }

        return Bukkit.getPlayer(ifPlayer.getUuid());

    }
}
