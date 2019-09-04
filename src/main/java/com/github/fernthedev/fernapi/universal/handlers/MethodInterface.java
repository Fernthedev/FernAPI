package com.github.fernthedev.fernapi.universal.handlers;

import com.github.fernthedev.fernapi.universal.api.CommandSender;
import lombok.NonNull;

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
}

