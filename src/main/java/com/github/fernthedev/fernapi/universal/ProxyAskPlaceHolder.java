package com.github.fernthedev.fernapi.universal;

import com.github.fernthedev.fernapi.universal.data.chat.ChatColor;
import com.github.fernthedev.fernapi.universal.data.network.Channel;
import com.github.fernthedev.fernapi.universal.data.network.PluginMessageData;
import com.github.fernthedev.fernapi.universal.handlers.IFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.PluginMessageHandler;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ProxyAskPlaceHolder extends PluginMessageHandler {

    private IFPlayer player;
    private String placeHolderValue;

    public static final String PLACEHOLDER_NOT_FOUND = "NoPlaceHolderFound";
    public static final String PLACEHOLDER_API_NOT_ENABLE = "PlaceHolderDisabled";

    boolean checked;
    boolean placeHolderReplaced;

    private String oldPlaceValue;

    private Timer taske;

    private Runnable runnable;

    private static List<ProxyAskPlaceHolder> instances = new ArrayList<>();

    private boolean runnableset = false;
    private String uuid;

    private PluginMessageData data;


    public ProxyAskPlaceHolder() {
        Universal.getMethods().getLogger().info("Registered PlaceHolderAPI Listener");
    }

    public String getPlaceHolderResult() {
        return placeHolderValue;
    }


    public ProxyAskPlaceHolder(IFPlayer player, String placeHolderValue) {
        this.player = player;


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);

        data = new PluginMessageData(stream, player.getCurrentServerName(), Channels.getPlaceHolderResult, Channels.PlaceHolderBungeeChannel);

        uuid = UUID.randomUUID().toString();
        if (!instances.isEmpty()) {
            for (ProxyAskPlaceHolder askPlaceHolder : instances) {
                if (askPlaceHolder.uuid != null) {
                    while (askPlaceHolder.uuid.equals(uuid)) {
                        uuid = UUID.randomUUID().toString();
                    }
                }
            }
        }


        Universal.debug("Current uuid is " + uuid);

        data.addData(placeHolderValue); //MESSAGE 1 (placeholder requested)
        oldPlaceValue = placeHolderValue;
        data.addData(uuid); //MESSAGE 2 (UUID)

        checked = false;
    }

    /**
     * Must be called in order for the message to be sent
     * It is called on another thread
     * @param messageRunnable The action to run when the placeholder is received
     */
    public void setRunnable(Runnable messageRunnable) {
        runnableset = true;
        this.runnable = messageRunnable;
        instances.add(this);

        Universal.getMessageHandler().sendPluginData(player, data);

        Universal.debug("Placeholder requested to " + player.getCurrentServerName() + " for placeholder " + oldPlaceValue);

        // getLogger().info("Runnable has now been initialized");
    }

    public boolean isPlaceHolderReplaced() {
        return placeHolderReplaced;
    }


    @Deprecated
    private void removeInstance(ProxyAskPlaceHolder askPlaceHolder) {
        //  getLogger().info("Removed an instance from list");
        instances.remove(askPlaceHolder);
    }

    @SuppressWarnings("unused")
    private void removeInstance() {
        // getLogger().info("Removed an instance from themselves to the list");
        instances.remove(this);
    }

    private void cancelTask() {
        //   getLogger().info("Task cancelled");
        //     getLogger().info("Cancelled instance with uuid of " + uuid);
        removeInstance();
        taske.cancel();
        taske.purge();
        taske = new Timer();
    }

    private void runTask() {
        taske = new Timer();

        //getLogger().info("This instance uuid is " + uuid);

        taske.schedule(new TimerTask() {
            ProxyAskPlaceHolder instance = null;
            private int count = 0;

            @Override
            public void run() {
                // getLogger().info("Ran a timer");
                if (count == 0) {
                    if (instance == null) {
                        if (!instances.isEmpty()) {
                            for (ProxyAskPlaceHolder askPlaceHolder : instances) {
                                if (askPlaceHolder.uuid.equals(uuid)) {
                                    instance = askPlaceHolder;
                                    //          getLogger().info("Found on the list an instance with uuid " + askPlaceHolder.uuid);
                                    //           getLogger().info("It is equal to current uuid " + uuid);
                                }
                            }
                        } else {
                            //    getLogger().info("There are no instances while running timer");
                            count++;
                            cancelTask();
                        }
                    } else {
                        runnable.run();
                        instance.cancelTask();
                        count++;
                    }

                 /*
                    if (instance.player != null) {
                        instance.player.sendMessage(FernCommands.getInstance().message("&cThere was an error trying to run this command."));
                    }
                    getLogger().info("There was an error trying to run this command.");*/
                }
            }

        }, TimeUnit.SECONDS.toMillis(2), TimeUnit.SECONDS.toMillis(2));


    }

    /**
     * This is the channel name that will be registered incoming and outgoing
     *
     * @return The channels that will be incoming and outgoing
     */
    @Override
    public List<Channel> getChannels() {
        List<Channel> channels = new ArrayList<>();
        channels.add(Channels.PlaceHolderBungeeChannel);

        return channels;
    }

    @Override
    public void onMessageReceived(PluginMessageData data, Channel channel) {
        if (Universal.getNetworkHandler().isRegistered(data.getSender())) {

            ByteArrayInputStream stream = data.getInputStream();

            String channelName = data.getBungeeChannelType(); // channel we delivered
            String server = data.getServer(); //Just incase
            String subchannel = data.getSubChannel(); //The channel of our custom desire


            if (channelName.equalsIgnoreCase("Forward") && subchannel.equalsIgnoreCase(Channels.PlaceHolderValue)) {
                Queue<String> queueData = data.getExtraDataQueue();
                String placeholder = queueData.remove();

                if (placeholder.equals(PLACEHOLDER_NOT_FOUND) || placeholder.equals(PLACEHOLDER_API_NOT_ENABLE)) placeholder = null;

                String uuide = queueData.remove();

                ProxyAskPlaceHolder instance = null;
                if (!instances.isEmpty()) {
                    for (ProxyAskPlaceHolder askPlaceHolder : instances) {
                        if (askPlaceHolder.uuid.equals(uuide)) {
                            instance = askPlaceHolder;
                            break;
                        }
                    }
                } else {
                    Universal.debug("There were no instances");
                }


                if (instance != null) {

                   // getLogger().info("Found the instance");

                    instance.placeHolderValue = placeholder;

                    if (placeholder != null) {
                        instance.placeHolderReplaced = !instance.placeHolderValue.equals(instance.oldPlaceValue);
                    }

                    instance.checked = true;
                    if (instance.runnableset) {
                      //   getLogger().info("Runnable is set because we checked");
                    } else {
                        Universal.debug("Runnable is not set because we checked");
                    }
                    instance.runTask();

                } else {
                    Universal.debug(ChatColor.RED + "The incoming message was not expected. From an attacker?");
                }
            }

        }
    }

    /*
    @SuppressWarnings("Unused")
    public void sendToBukkit(String channel, String message, ServerInfo server) {
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
}