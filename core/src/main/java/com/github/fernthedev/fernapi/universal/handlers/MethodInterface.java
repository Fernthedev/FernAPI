package com.github.fernthedev.fernapi.universal.handlers;

import com.github.fernthedev.fernapi.universal.api.CommandSender;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import lombok.NonNull;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public interface MethodInterface {

    Logger getLogger();

    ServerType getServerType();

    FernAPIPlugin getInstance();

    IFPlayer convertPlayerObjectToFPlayer(Object player);

    Object convertFPlayerToPlayer(IFPlayer ifPlayer);

    CommandSender convertCommandSenderToAPISender(@NonNull Object commandSender);

    IFPlayer getPlayerFromName(String name);

    IFPlayer getPlayerFromUUID(UUID uuid);

    List<IFPlayer> getPlayers();

    File getDataFolder();

    String getNameFromPlayer(UUID uuid);

    UUID getUUIDFromPlayer(String name);
}

