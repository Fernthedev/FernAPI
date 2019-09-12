package com.github.fernthedev.fernapi.universal.misc;

import com.github.fernthedev.fernapi.universal.api.CommandSender;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.handlers.IFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.ServerType;
import lombok.NonNull;

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

    void runAsync(Runnable runnable);

    List<IFPlayer> getPlayers();
}

