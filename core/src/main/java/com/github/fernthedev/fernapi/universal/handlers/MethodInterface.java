package com.github.fernthedev.fernapi.universal.handlers;

import com.github.fernthedev.fernapi.universal.api.FernCommandIssuer;
import com.github.fernthedev.fernapi.universal.api.IFConsole;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.api.OfflineFPlayer;
import lombok.NonNull;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

/**
 *
 * @param <PlayerType> The player type the server uses
 */
public interface MethodInterface<PlayerType, ConsoleType> {
    boolean isMainThread();

    Logger getLogger();

    ServerType getServerType();

    FernAPIPlugin getInstance();

    /**
     *
     * @param player The player
     * @param <P> The type of player to return
     * @return The {@link IFPlayer} player instance
     */
    <P> IFPlayer<P> convertPlayerObjectToFPlayer(P player);

    /**
     *
     * @param ifPlayer The player
     * @param <P> The type of player to return
     * @return The server-specific player instance
     */
    <P> P convertFPlayerToPlayer(IFPlayer<P> ifPlayer);

    /**
     * Converts the command sender to it's IFPlayer instance
     * @return
     */
    FernCommandIssuer convertCommandSenderToAPISender(@NonNull Object commandSender);

    /**
     * Converts the command sender to it's IFPlayer instance
     * @return
     */
    IFConsole<ConsoleType> convertConsoleToAPISender(@NonNull ConsoleType commandSender);

    /**
     * Returns player from server
     * @param name Name of player
     * @return The IFPlayer instance. It never returns null, however you can check if the player is null with {@link IFPlayer#isPlayerNull()}
     */
    @NonNull
    OfflineFPlayer<PlayerType> getPlayerFromName(String name);

    /**
     * Returns player from server
     * @param uuid Name of player
     * @return The IFPlayer instance. It never returns null, however you can check if the player is null with {@link IFPlayer#isPlayerNull()}
     */
    @NonNull
    OfflineFPlayer<PlayerType> getPlayerFromUUID(UUID uuid);

    /**
     * Returns players of server in {@link IFPlayer} instance
     */
    List<IFPlayer<PlayerType>> getPlayers();

    /**
     * Returns the plugin data folder
     */
    File getDataFolder();

    /**
     * Returns the name of the player from uuid
     */
    String getNameFromPlayer(UUID uuid);

    /**
     * Returns the uuid of the player from name
     */
    UUID getUUIDFromPlayerName(String name);

    List<IFPlayer<PlayerType>> matchPlayerName(String name);
}

