package com.github.fernthedev.fernapi.universal.api;

import com.github.fernthedev.fernapi.universal.data.network.IServerInfo;
import com.github.fernthedev.fernapi.universal.exceptions.network.PluginTimeoutException;
import com.github.fernthedev.fernapi.universal.util.UUIDFetcher;
import com.github.fernthedev.fernapi.universal.util.network.vanish.VanishProxyCheck;
import lombok.AccessLevel;
import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Getter
public abstract class IFPlayer<T> implements FernCommandIssuer {


    public static final List<String> VANISH_PERMISSIONS = new ArrayList<>(
            Arrays.asList("sv.see", "acf.seevanish"));

    protected String name = null;


    protected UUID uuid = null;

    @Getter
    protected T player;

    @Getter
    protected Audience audience;

    public IFPlayer(String name, UUID uuid, T player, Audience audience) {
        this.name = name;
        this.uuid = uuid;
        this.player = player;
        this.audience = audience;
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

    @Nullable
    public abstract IServerInfo getServerInfo();

    /**
     * Returns true if any of all data is null
     * If player is null but name and uuid aren't, this returns false
     * @return
     */
    public boolean isPlayerNull() {
        boolean isPlayer = player == null || audience == null; // Player and audience must be null
        boolean isDataNull = name == null || uuid == null; // Name and UUID both must be null

        return isPlayer || isDataNull;
    }

    public CompletableFuture<Boolean> isVanished() {
        return isVanished(10, TimeUnit.SECONDS);
    }

    public CompletableFuture<Boolean> isVanished(int amount, TimeUnit timeUnit) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();

        new VanishProxyCheck(this, (player, isVanished, timedOut) -> {
            completableFuture.complete(isVanished);

            if (timedOut) throw new PluginTimeoutException("The vanish check timed out. The server must have AskPlaceHolderSpigot plugin enabled and registered");
        }).setTimeout(amount, timeUnit);

        return completableFuture;
    }

    public CompletableFuture<Boolean> canSee(IFPlayer<?> player) {
        if (hasVanishPermission()) return CompletableFuture.completedFuture(true);

        return player.isVanished().thenApply(aBoolean -> !aBoolean || hasVanishPermission());
    }



    public static CompletableFuture<Boolean> canSee(FernCommandIssuer commandIssuer, IFPlayer<?> player) {
        if (!commandIssuer.isPlayer()) return CompletableFuture.completedFuture(true);
        if (commandIssuer.hasVanishPermission("acf.seevanish")) return CompletableFuture.completedFuture(true);


        CompletableFuture<Boolean> vanished = player.isVanished();


       // Invert to see if it can be seen
        return vanished.thenApply(v -> !v);
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
