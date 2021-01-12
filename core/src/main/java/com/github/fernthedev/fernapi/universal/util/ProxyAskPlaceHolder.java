package com.github.fernthedev.fernapi.universal.util;

import com.github.fernthedev.fernapi.universal.FernAPIChannels;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.data.chat.ChatColor;
import com.github.fernthedev.fernapi.universal.data.network.Channel;
import com.github.fernthedev.fernapi.universal.data.network.PluginMessageData;
import com.github.fernthedev.fernapi.universal.handlers.PluginMessageHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ProxyAskPlaceHolder extends PluginMessageHandler {

    private final ProxyPlaceholderRunnable proxyPlaceholderRunnable;
    private final CompletableFuture<PlaceHolderResponse> completableFuture;
    private IFPlayer<?> player;
    private String placeHolderValue;

    public static final String PLACEHOLDER_NOT_FOUND = "NoPlaceHolderFound";
    public static final String PLACEHOLDER_API_NOT_ENABLE = "PlaceHolderDisabled";

    private boolean responseReceived;
    private boolean placeHolderReplaced;
    private String oldPlaceValue;

    private static final Map<UUID, ProxyAskPlaceHolder> instances = new HashMap<>();

    /**
     * Internal use
     */
    @Deprecated
    public static final ProxyAskPlaceHolder LISTENER_INSTANCE = new ProxyAskPlaceHolder();


    /**
     * Internal use
     */
    private ProxyAskPlaceHolder() {
        Universal.getMethods().getAbstractLogger().info("Registered PlaceHolderAPI Listener");
        proxyPlaceholderRunnable = (player, placeHolder, isReplaced) -> {};
        completableFuture = null;
    }

    public String getPlaceHolderResult() {
        return placeHolderValue;
    }


    public ProxyAskPlaceHolder(IFPlayer<?> player, String placeHolderValue, ProxyPlaceholderRunnable proxyPlaceholderRunnable) {
        this.player = player;
        this.proxyPlaceholderRunnable = proxyPlaceholderRunnable;
        completableFuture = new CompletableFuture<>();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);

        PluginMessageData data = new PluginMessageData(stream, player.getCurrentServerName(), FernAPIChannels.getPlaceHolderResult, FernAPIChannels.PlaceHolderBungeeChannel);

        UUID uuid = UUID.randomUUID();
        if (!instances.isEmpty()) {
            while (instances.containsKey(uuid)) {
                uuid = UUID.randomUUID();
            }
        }


        Universal.debug("Current uuid is " + uuid);

        data.addData(placeHolderValue); //MESSAGE 1 (placeholder requested)
        oldPlaceValue = placeHolderValue;
        data.addData(uuid.toString()); //MESSAGE 2 (UUID)

        responseReceived = false;
        Universal.getMessageHandler().sendPluginData(player, data);
        instances.put(uuid, this);
    }

    public boolean isPlaceHolderReplaced() {
        return placeHolderReplaced;
    }

    public boolean isResponseReceived() {
        return responseReceived;
    }

    /**
     * This is the channel name that will be registered incoming and outgoing
     *
     * @return The channels that will be incoming and outgoing
     */
    @Override
    public List<Channel> getChannels() {
        List<Channel> channels = new ArrayList<>();
        channels.add(FernAPIChannels.PlaceHolderBungeeChannel);

        return channels;
    }

    @Override
    public void onMessageReceived(PluginMessageData data, Channel channel) {
        if (Universal.getNetworkHandler().isRegistered(data.getSender())) {

            ByteArrayInputStream stream = data.getInputStream();

            String channelName = data.getProxyChannelType(); // channel we delivered
            String server = data.getServer(); //Just incase
            String subchannel = data.getSubChannel(); //The channel of our custom desire


            if (channelName.equalsIgnoreCase("Forward") && subchannel.equalsIgnoreCase(FernAPIChannels.PlaceHolderValue)) {
                Queue<String> queueData = data.getExtraDataQueue();
                String placeholder = queueData.remove();

                if (placeholder.equals(PLACEHOLDER_NOT_FOUND) || placeholder.equals(PLACEHOLDER_API_NOT_ENABLE)) placeholder = null;

                UUID uuide = UUID.fromString(queueData.remove());

                ProxyAskPlaceHolder instance = instances.get(uuide);
                if (instance == null) {
                    Universal.debug("There were no instances");
                }


                if (instance != null) {

                   // getLogger().info("Found the instance");

                    instance.placeHolderValue = placeholder;

                    if (placeholder != null) {
                        instance.placeHolderReplaced = !instance.placeHolderValue.equals(instance.oldPlaceValue);
                    }

                    instance.responseReceived = true;

                    // Fire completable future complete.
                    completableFuture.complete(new PlaceHolderResponse(instance.player, instance.placeHolderValue, instance.isPlaceHolderReplaced()));

                    instance.proxyPlaceholderRunnable.onFinish(instance.player, instance.placeHolderValue, instance.isPlaceHolderReplaced());
                    instances.remove(uuide);

                } else {
                    Universal.debug(ChatColor.RED + "The incoming message was not expected. From an attacker?");
                }
            }

        }
    }

    /*
    @SuppressWarnings("Unused")
    public void sendToBukkit(String channel, String message, IServerInfo server) {
        ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(inputStream);
        try {
            out.writeUTF(channel);
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.sendData("Return", inputStream.toByteArray());
    }*/

    @FunctionalInterface
    public interface ProxyPlaceholderRunnable {

        void onFinish(IFPlayer<?> player, String placeHolder, boolean isReplaced);

    }

    public CompletableFuture<PlaceHolderResponse> getCompletableFuture() {
        return completableFuture;
    }

    public class PlaceHolderResponse {
        private final IFPlayer<?> player;
        private final String placeholder;
        private final boolean isReplaced;

        public PlaceHolderResponse(IFPlayer<?> player, String placeholder, boolean isReplaced) {
            this.player = player;
            this.placeholder = placeholder;
            this.isReplaced = isReplaced;
        }

        public IFPlayer<?> getPlayer() {
            return player;
        }

        public String getPlaceholder() {
            return placeholder;
        }

        public boolean isReplaced() {
            return isReplaced;
        }
    }
}
