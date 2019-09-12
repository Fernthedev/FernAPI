package com.github.fernthedev.fernapi.server.sponge;

import com.github.fernthedev.fernapi.server.sponge.player.SpongeFConsole;
import com.github.fernthedev.fernapi.server.sponge.player.SpongeFPlayer;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.CommandSender;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.handlers.IFPlayer;
import com.github.fernthedev.fernapi.universal.misc.MethodInterface;
import com.github.fernthedev.fernapi.universal.handlers.ServerType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SpongeInterface implements MethodInterface {
    @NonNull
    private FernSpongeAPI sponge;

    @Override
    public java.util.logging.Logger getLogger() {
        sponge.getLogger().warn("Java Logger does not exist in Sponge.");
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
    public IFPlayer convertPlayerObjectToFPlayer(Object player) {
        return new SpongeFPlayer((Player) player);
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
    public IFPlayer getPlayerFromName(String name) {
        return convertPlayerObjectToFPlayer(Sponge.getServer().getPlayer(name));
    }

    @Override
    public IFPlayer getPlayerFromUUID(UUID uuid) {
        return convertPlayerObjectToFPlayer(Sponge.getServer().getPlayer(uuid));
    }

    @Override
    public void runAsync(Runnable runnable) {
        new Thread(runnable).start();
    }

    @Override
    public List<IFPlayer> getPlayers() {
        return Sponge.getServer().getOnlinePlayers().stream().map(proxiedPlayer -> Universal.getMethods().convertPlayerObjectToFPlayer(proxiedPlayer)).collect(Collectors.toList());
    }
}
