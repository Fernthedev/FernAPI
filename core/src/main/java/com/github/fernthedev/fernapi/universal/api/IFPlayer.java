package com.github.fernthedev.fernapi.universal.api;

import com.github.fernthedev.fernapi.universal.data.network.IServerInfo;
import com.github.fernthedev.fernapi.universal.exceptions.FernRuntimeException;
import com.github.fernthedev.fernapi.universal.exceptions.network.PluginTimeoutException;
import com.github.fernthedev.fernapi.universal.util.UUIDFetcher;
import com.github.fernthedev.fernapi.universal.util.network.vanish.VanishProxyCheck;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
public abstract class IFPlayer<T> implements FernCommandIssuer {


    public static final List<String> VANISH_PERMISSIONS = new ArrayList<>(
            Arrays.asList("sv.see", "acf.seevanish"));

    protected String name = null;


    protected UUID uuid = null;

    @Getter
    protected T player;

    public IFPlayer(String name, UUID uuid, T player) {
        this.name = name;
        this.uuid = uuid;
        this.player = player;
    }

    @Getter(AccessLevel.NONE)
    protected boolean checkedUUID = false;

    @Getter(AccessLevel.NONE)
    protected boolean checkedName = false;

    private IFPlayer() {}

    public abstract InetSocketAddress getAddress();

    public abstract long getPing();

    public String getCurrentServerName() {
        return getServerInfo() != null ? getServerInfo().getName() : null;
    }

    /**
     * Gets the uuid instance
     * If UUID is null, it will attempt to retrieve it using the name.
     *
     *
     * @return
     */
    @Nullable
    public String getName() {
        if (name == null && uuid != null && !checkedName) {

            this.name = UUIDFetcher.getName(uuid);

            checkedName = true;
        }

        return name;
    }

    /**
     * Gets the uuid instance
     * If UUID is null, it will attempt to retrieve it using the name.
     *
     *
     * @return
     */
    @Nullable
    @Override
    public UUID getUniqueId() {
        if (uuid == null && name != null && !checkedUUID) {

            uuid = UUIDFetcher.getUUID(name);

            checkedUUID = true;
        }

        return uuid;
    }


    public abstract IServerInfo getServerInfo();

    /**
     * Returns true if any of all data is null
     * If player is null but name and uuid aren't, this returns false
     * @return
     */
    public boolean isPlayerNull() {
        boolean isPlayer = player == null; // Player must be null
        boolean isDataNull = name == null || uuid == null; // Name and UUID both must be null

        return isPlayer || isDataNull;
    }

    public boolean isVanished() {
        final boolean[] vanished = new boolean[1];
        try {
            new VanishProxyCheck(this, (player, isVanished, timedOut) -> {
                if (timedOut) throw new PluginTimeoutException("The vanish check timed out. The server must have FernAPI enabled and registered");

                vanished[0] = isVanished;
            }).awaitVanishResponse(20);
        } catch (InterruptedException e) {
            throw new FernRuntimeException("Interrupted", e);
        }

        return vanished[0];
    }

    public boolean canSee(IFPlayer<?> player) {
        return player.isVanished() && hasVanishPermission("acf.seevanish");
    }



    public static boolean canSee(FernCommandIssuer commandIssuer, IFPlayer<?> player) {
        if (!commandIssuer.isPlayer()) return true;

        boolean vanished = player.isVanished();

        return !vanished || commandIssuer.hasVanishPermission();
    }

    /**
     * Gets the issuer in the platforms native object
     *
     * @return
     */
    @Override
    public T getIssuer() {
        return player;
    }

    /**
     * Has permission node
     *
     * @param permission
     * @return
     */
    @Override
    public boolean hasPermission(String permission) {
        return false;
    }

    /**
     * Is this issue a player, or server/console sender
     *
     * @return
     */
    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public String toString() {
        return "IFPlayer{" +
                "name='" + name + '\'' +
                ", uuid=" + uuid +
                ", player=" + player +
                '}';
    }


}
