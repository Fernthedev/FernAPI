package com.github.fernthedev.fernapi.server.sponge;

import com.github.fernthedev.fernapi.server.sponge.player.SpongeFPlayer;
import com.github.fernthedev.fernapi.universal.api.CommandSender;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.handlers.IFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.MethodInterface;
import com.github.fernthedev.fernapi.universal.handlers.ServerType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;

@RequiredArgsConstructor
public class SpongeInterface implements MethodInterface {
    @NonNull
    private FernSpongeAPI sponge;

    @Override
    public java.util.logging.Logger getLogger() {
        return null;
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
    public CommandSender convertCommandSenderToAPISender(@NonNull Object commandSender) {
        return null;
    }
}
