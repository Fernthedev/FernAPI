package com.github.fernthedev.fernapi.universal.util.network.vanish;

import com.github.fernthedev.fernapi.universal.FernAPIChannels;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.data.network.Channel;
import com.github.fernthedev.fernapi.universal.data.network.PluginMessageData;
import com.github.fernthedev.fernapi.universal.exceptions.FernRuntimeException;
import com.github.fernthedev.fernapi.universal.handlers.PluginMessageHandler;
import com.github.fernthedev.fernapi.universal.handlers.ServerType;
import com.github.fernthedev.fernapi.universal.util.ProxyAskPlaceHolder;
import lombok.NonNull;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class VanishProxyCheck extends PluginMessageHandler {

    private static Object o;

    private final CompletableFuture<VanishProxyResponse> completableFuture;
    private final VanishFunction vanishFunction;
    private final IFPlayer<?> player;

    private static final Map<UUID, VanishProxyCheck> instances = new HashMap<>();

    private final UUID uuid;

    /**
     * Internal use
     */
    @Deprecated
    public static final VanishProxyCheck LISTENER_INSTANCE = new VanishProxyCheck();

    /**
     * Must be called manually to work
     *
     * @param timeout  The amount of time to wait
     * @param timeUnit The unit of time
     */
    public VanishProxyCheck setTimeout(long timeout, TimeUnit timeUnit) {
        VanishProxyCheck vanishProxyCheck = this;

        Universal.getScheduler().runSchedule(() -> {
            if (instances.containsKey(vanishProxyCheck.uuid)) {
                completableFuture.complete(new VanishProxyResponse(player, false, true));
                vanishFunction.run(player, false, true);
                instances.remove(vanishProxyCheck.uuid);

            }
        }, timeout, timeUnit);

        return this;
    }


    /**
     * Internal use
     */
    @Deprecated
    private VanishProxyCheck() {
        if (o != null) throw new FernRuntimeException("VanishProxyCheck is already registered");

        o = new Object();
        Universal.getMethods().getAbstractLogger().info("Registered VanishProxyCheck Listener");
        completableFuture = null;
        player = null;
        vanishFunction = null;
        uuid = null;
    }

    /**
     * Use for getting vanish
     *
     * @param player         the player
     * @param vanishFunction the code to run
     */
    public VanishProxyCheck(@NonNull IFPlayer<?> player, @NonNull VanishFunction vanishFunction) {
        this.vanishFunction = vanishFunction;
        this.player = player;
        uuid = UUID.randomUUID();
        completableFuture = new CompletableFuture<>();

        instances.put(uuid, this);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        DataOutputStream out = new DataOutputStream(stream);

        PluginMessageData data = new PluginMessageData(stream, player.getCurrentServerName(), FernAPIChannels.VANISH_SUBCHANNEL, FernAPIChannels.VANISH_CHANNEL);

        data.addData(uuid.toString()); //MESSAGE 2 (UUID)

        Universal.getMessageHandler().sendPluginData(player, data);
    }

    /**
     * This is the channel name that will be registered incoming and outgoing
     * This is where you specify the channels you want to listen to
     * Just make a new {@link ArrayList} with Channel instance instance and add an instance of the channel accordingly.
     *
     * @return The channels that will be incoming and outgoing
     * @see ProxyAskPlaceHolder as an example
     */
    @Override
    public @NonNull List<Channel> getChannels() {
        List<Channel> channels = new ArrayList<>();

        channels.add(FernAPIChannels.VANISH_CHANNEL);

        return channels;
    }

    /**
     * The event called when message is received from the channels registered
     *
     * @param data    The dataInfo received for use of the event.
     * @param channel The channel it was received from, for use of multiple channels in one listener
     */
    @Override
    public void onMessageReceived(PluginMessageData data, Channel channel) {
        Queue<String> dataQueue = data.getExtraDataQueue();
        UUID uuidCheck = UUID.fromString(dataQueue.remove());

        Universal.debug(() -> Universal.getMethods().getServerType().isProxy() + " proxy " + Universal.getMethods().getServerType().equals(ServerType.BUKKIT) + " bukkit");

        if (Universal.getMethods().getServerType().isProxy()) {
            boolean vanished = Boolean.parseBoolean(dataQueue.remove());

            Universal.debug(() -> vanished + " vanished");


            VanishProxyCheck vanishProxyCheck = instances.get(uuidCheck);
            if (vanishProxyCheck != null) {
                vanishProxyCheck.completableFuture.complete(new VanishProxyResponse(vanishProxyCheck.player, vanished, false));
                Universal.getScheduler().runAsync(() -> vanishProxyCheck.vanishFunction.run(vanishProxyCheck.player, vanished, false));

                instances.remove(vanishProxyCheck.uuid);
            }
        }
    }

    /**
     * Synchronise to await message response
     *
     * @param millis The amount of time to wait
     *               
     * @deprecated Use {@link CompletableFuture#join()} with {@link #completableFuture}
     *               
     * @throws InterruptedException
     */
    public void awaitVanishResponse(int millis) throws InterruptedException {
        while (instances.containsKey(uuid)) {
            Thread.sleep(millis);
        }
    }

    public CompletableFuture<VanishProxyResponse> getCompletableFuture() {
        return completableFuture;
    }
}
