package com.github.fernthedev.fernapi.server.velocity.network;


import com.github.fernthedev.fernapi.server.velocity.FernVelocityAPI;
import com.github.fernthedev.fernapi.universal.Channels;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.chat.ChatColor;
import com.github.fernthedev.fernapi.universal.data.network.Channel;
import com.github.fernthedev.fernapi.universal.data.network.PluginMessageData;
import com.github.fernthedev.fernapi.universal.handlers.PluginMessageHandler;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor
public class AskPlaceHolder extends PluginMessageHandler {

    private Player player;
    private String placeHolderValue;

    boolean checked;
    boolean placeHolderReplaced;

    private String oldPlaceValue;

    private Timer taske;

    private MessageRunnable runnable;

    private static List<AskPlaceHolder> instances = new ArrayList<>();

    private boolean runnableset = false;
    private String uuid;

    private PluginMessageData data;
    private static FernVelocityAPI velocity;


    public AskPlaceHolder(FernVelocityAPI fernVelocityAPI) {
        velocity = fernVelocityAPI;
        getLogger().info("Registered PlaceHolderAPI Listener");
    }

    public String getPlaceHolderResult() {
        return placeHolderValue;
    }


    public AskPlaceHolder(Player player, String placeHolderValue) {
        this.player = player;


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);

        data = new PluginMessageData(stream,"Forward",player.getCurrentServer().get().getServerInfo().getName(),Channels.getPlaceHolderResult,Channels.PlaceHolderBungeeChannel);

        uuid = UUID.randomUUID().toString();
        if (!instances.isEmpty()) {
            for (AskPlaceHolder askPlaceHolder : instances) {
                if (askPlaceHolder.uuid != null) {
                    while (askPlaceHolder.uuid.equals(uuid)) {
                        uuid = UUID.randomUUID().toString();
                    }
                }
            }
        }


        getLogger().info("Current uuid is " + uuid);

        data.addData(placeHolderValue); //MESSAGE 1 (placeholder requested)
        oldPlaceValue = placeHolderValue;
        data.addData(uuid); //MESSAGE 2 (UUID)


        ServerConnection server = player.getCurrentServer().get();
        getLogger().info("Placeholder requested to " + server.getServerInfo().getName() + " for placeholder " + placeHolderValue);

        checked = false;
    }

    public void setRunnable(MessageRunnable messageRunnable) {
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
    private void removeInstance(AskPlaceHolder askPlaceHolder) {
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
            AskPlaceHolder instance = null;
            private int count = 0;

            @Override
            public void run() {
                // getLogger().info("Ran a timer");
                if (count == 0) {
                    if (instance == null) {
                        if (!instances.isEmpty()) {
                            for (AskPlaceHolder askPlaceHolder : instances) {
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
        channels.add(Channel.createChannelFromString(Channels.PlaceHolderBungeeChannel, Channel.ChannelAction.BOTH));

        return channels;
    }

    @Override
    public void onMessageReceived(PluginMessageData data, Channel channel) {
        getLogger().debug("Sender is " + data.getSender());
        if (data.getSender() instanceof RegisteredServer) {

            ByteArrayInputStream stream = data.getInputStream();
            DataInputStream in = new DataInputStream(stream);

            try {
                String channelName = data.getChannelName(); // channel we delivered
                String server = data.getServer(); //Just incase
                String subchannel = data.getSubchannel(); //The channel of our custom desire

                if (channelName.equalsIgnoreCase("Forward") && subchannel.equalsIgnoreCase(Channels.PlaceHolderValue)) {
                    String placeholder = in.readUTF();

                    if (placeholder.equals("NoPlaceHolderFound")) placeholder = null;

                    String uuide = in.readUTF();

                    AskPlaceHolder instance = null;
                    if (!instances.isEmpty()) {
                        for (AskPlaceHolder askPlaceHolder : instances) {
                            if (askPlaceHolder.uuid.equals(uuide)) {
                                instance = askPlaceHolder;
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
