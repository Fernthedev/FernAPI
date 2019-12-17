package com.github.fernthedev.fernapi.universal.handlers;

import com.github.fernthedev.fernapi.universal.api.CommandSender;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import lombok.NonNull;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 *
 * @param <PlayerType> The player type the server uses
 */
public interface MethodInterface<PlayerType> {



    Logger getLogger();

    ServerType getServerType();

    FernAPIPlugin getInstance();

    <P> IFPlayer<P> convertPlayerObjectToFPlayer(P player);

    <P> P convertFPlayerToPlayer(IFPlayer<P> ifPlayer);

    CommandSender convertCommandSenderToAPISender(@NonNull Object commandSender);

    IFPlayer<PlayerType> getPlayerFromName(String name);

    IFPlayer<PlayerType> getPlayerFromUUID(UUID uuid);

    List<IFPlayer<PlayerType>> getPlayers();

    File getDataFolder();

    String getNameFromPlayer(UUID uuid);

    UUID getUUIDFromPlayer(String name);
}

