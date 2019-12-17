package com.github.fernthedev.fernapi.server.velocity.network;


import com.github.fernthedev.fernapi.server.velocity.FernVelocityAPI;
import com.github.fernthedev.fernapi.universal.Channels;
import com.github.fernthedev.fernapi.universal.ProxyAskPlaceHolder;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.chat.ChatColor;
import com.github.fernthedev.fernapi.universal.data.network.Channel;
import com.github.fernthedev.fernapi.universal.data.network.PluginMessageData;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.PluginMessageHandler;
import org.slf4j.Logger;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @deprecated Use {@link ProxyAskPlaceHolder}
 */
@Deprecated
public class VelocityAskPlaceHolder extends PluginMessageHandler {

    private IFPlayer player;
    private String placeHolderValue;

    boolean checked;
    boolean placeHolderReplaced;

    private String oldPlaceValue;

    private Timer taske;

    private Runnable runnable;

    private static List<VelocityAskPlaceHolder> instances = new ArrayList<>();

    private boolean runnableset = false;
    private String uuid;

    private PluginMessageData data;
    private static FernVelocityAPI velocity;


    public VelocityAskPlaceHolder(FernVelocityAPI fernVelocityAPI) {
        velocity = fernVelocityAPI;
        getLogger().info("Registered PlaceHolderAPI Listener");
    }

    public String getPlaceHolderResult() {
        return placeHolderValue;
    }


    public VelocityAskPlaceHolder(IFPlayer player, String placeHolderValue) {
        this.player = player;


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);

        data = new PluginMessageData(stream, player.getCurrentServerName(),Channels.getPlaceHolderResult,Channels.PlaceHolderBungeeChannel);

        uuid = UUID.randomUUID().toString();
        if (!instances.isEmpty()) {
            for (VelocityAskPlaceHolder velocityAskPlaceHolder : instances) {
                if (velocityAskPlaceHolder.uuid != null) {
                    while (velocityAskPlaceHolder.uuid.equals(uuid)) {
                        uuid = UUID.randomUUID().toString();
                    }
                }
            }
        }


        getLogger().info("Current uuid is " + uuid);

        data.addData(placeHolderValue); //MESSAGE 1 (placeholder requested)
        oldPlaceValue = placeHolderValue;
        data.addData(uuid); //MESSAGE 2 (UUID)


//        ServerConnection server = player.getCurrentServer().get();
//        getLogger().info("Placeholder requested to " + server.getServerInfo().getName() + " for placeholder " + placeHolderValue);

        checked = false;
    }

    public void setRunnable(Runnable messageRunnable) {
        runnableset = true;
        this.runnable = messageRunnable;
        instances.add(this);

        Universal.getMessageHandler().sendPluginData(Universal.convertObjectPlayerToFPlayer(player),data);

       // getLogger().info("Runnable has now been initialized");
    }

    public boolean isPlaceHolderReplaced() {
        return placeHolderReplaced;
    }


    @Deprecated
    private void removeInstance(VelocityAskPlaceHolder velocityAskPlaceHolder) {
        //  getLogger().info("Removed an instance from list");
        instances.remove(velocityAskPlaceHolder);
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
            VelocityAskPlaceHolder instance = null;
            private int count = 0;

            @Override
            public void run() {
                // getLogger().info("Ran a timer");
                if (count == 0) {
                    if (instance == null) {
                        if (!instances.isEmpty()) {
                            for (VelocityAskPlaceHolder velocityAskPlaceHolder : instances) {
                                if (velocityAskPlaceHolder.uuid.equals(uuid)) {
                                    instance = velocityAskPlaceHolder;
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


    private Logger getLogger() {
        return velocity.getLogger();
    }

    /**
     * This is the channel name that will be registered incoming and outcoming
     *
     * @return The channels that will be incoming and outcoming
     */
    @Override
    public List<Channel> getChannels() {
        List<Channel> channels = new ArrayList<>();
        channels.add(Channels.PlaceHolderBungeeChannel);

        return channels;
    }

    @Override
    public void onMessageReceived(PluginMessageData data, Channel channel) {
        getLogger().debug("Sender is " + data.getSender());
        if (Universal.getNetworkHandler().isRegistered(data.getSender()) ) {

            ByteArrayInputStream stream = data.getInputStream();
            DataInputStream in = new DataInputStream(stream);

            try {
                String channelName = data.getProxyChannelType(); // channel we delivered
                String server = data.getServer(); //Just incase
                String subchannel = data.getSubChannel(); //The channel of our custom desire

                if (channelName.equalsIgnoreCase("Forward") && subchannel.equalsIgnoreCase(Channels.PlaceHolderValue)) {
                    String placeholder = in.readUTF();

                    if (placeholder.equals("NoPlaceHolderFound")) placeholder = null;

                    String uuide = in.readUTF();

                    VelocityAskPlaceHolder instance = null;
                    if (!instances.isEmpty()) {
                        for (VelocityAskPlaceHolder velocityAskPlaceHolder : instances) {
                            if (velocityAskPlaceHolder.uuid.equals(uuide)) {
                                instance = velocityAskPlaceHolder;
                            }
                        }
                    } else {
                        getLogger().info("There were no instances");
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
                            getLogger().info("Runnable is not set because we checked");
                        }
                        instance.runTask();

                    } else {
                        getLogger().info(ChatColor.RED + "The incoming message was not expected. From an attacker?");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
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
}
