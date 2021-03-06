package com.github.fernthedev.fernapi.server.spigot.network;

import com.github.fernthedev.fernapi.server.spigot.FernSpigotAPI;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.JSONPlayer;
import com.github.fernthedev.fernapi.universal.data.network.*;
import com.github.fernthedev.fernapi.universal.exceptions.network.IllegalChannelState;
import com.github.fernthedev.fernapi.universal.exceptions.network.NoPlayersOnlineException;
import com.github.fernthedev.fernapi.universal.exceptions.network.NotEnoughDataException;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.PluginMessageHandler;
import com.google.gson.Gson;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;

public class SpigotMessageHandler extends IPMessageHandler implements PluginMessageListener {

    private FernSpigotAPI spigot;

    public SpigotMessageHandler(FernSpigotAPI fernsp) {
        this.spigot = fernsp;
    }


    @Override
    public void registerMessageHandler(PluginMessageHandler pluginMessageHandler) {
        receivers.add(pluginMessageHandler);
        for(Channel channel : pluginMessageHandler.getChannels()) {
            try {
                if (channel.getChannelAction() == Channel.ChannelAction.INCOMING || channel.getChannelAction() == Channel.ChannelAction.BOTH) {
                    Bukkit.getMessenger().registerIncomingPluginChannel(spigot, channel.getFullChannelName(), this);
                }

                if (channel.getChannelAction() == Channel.ChannelAction.OUTGOING || channel.getChannelAction() == Channel.ChannelAction.BOTH) {
                    Bukkit.getMessenger().registerOutgoingPluginChannel(spigot, channel.getFullChannelName());
                }
            } catch (Exception e) {
                throw new IllegalChannelState("Channel name: " + channel.getNamespace() + ":" + channel.getChannelName() + " {" + channel.getChannelAction() + "}", e);
            }
        }

    }

    @Override
    public void sendPluginData(PluginMessageData data) {
        sendPluginData(null, data);
    }

    /**
     * This sends plugin dataInfo.
     * @param fplayer The player can be null, not necessary
     * @param data The dataInfo to be sent, player will be specified added automatically
     */

    @Override
    public void sendPluginData(IFPlayer fplayer, @NonNull PluginMessageData data) {
        Player player;
        if (fplayer != null) {
            player = (Player) Universal.getMethods().convertFPlayerToPlayer(fplayer);
        } else {
            player = getRandomPlayer();
        }

        if (player == null) {
            Universal.getMethods().getAbstractLogger().warn("No players online, cannot send plugin message");
            try {
                throw new NoPlayersOnlineException("Players are required to send plugin messages through spigot to other servers.");
            } catch (NoPlayersOnlineException e) {
                e.printStackTrace();
            }
            return;
        }

        data.setPlayer(fplayer);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);


        try {
            out.writeUTF(data.getProxyChannelType()); //TYPE
            out.writeUTF(data.getServer()); //SERVER
            out.writeUTF(data.getSubChannel()); //SUBCHANNEL

//            out.writeUTF(data.getMessageChannel().getFullChannelName());

            out.writeUTF(new Gson().toJson(new JSONPlayer(player.getName(),player.getUniqueId())));

            out.writeBoolean(data.isUseGson());
            out.writeUTF(data.getGsonName());

            if (data.isUseGson()) {
                out.writeUTF(new Gson().toJson(data));
            }



            for(String s : data.getExtraData()) {
                out.writeUTF(s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Universal.debug(() -> "{} {}",() -> new Object[]{data.getMessageChannel().getFullChannelName(), data});

        player.sendPluginMessage(spigot, data.getMessageChannel().getFullChannelName(), stream.toByteArray());
    }

    /**
     * @return A random Player (/ the first player in the Player Collection)
     */
    private static Player getRandomPlayer() {
        if (!Bukkit.getOnlinePlayers().isEmpty())
            return Bukkit.getOnlinePlayers().iterator().next();
        else
            return null;
    }

    /**
     * A method that will be thrown when a PluginMessageSource sends a plugin
     * message on a registered channelName.
     *
     * @param channelName Channel that the message was sent through.
     * @param player  Source of the message.
     * @param message The raw message that was sent.
     */
    @Override
    public void onPluginMessageReceived(String channelName, Player player, byte[] message) {
        for(PluginMessageHandler pl : receivers) {
            for(Channel channel : pl.getChannels()) {
//                System.out.println(channelName + " " + channel.getFullChannelName() + " " + channelName.equals(channel.getFullChannelName()) );
                if (channelName.equals(channel.getFullChannelName()) && (channel.getChannelAction() == Channel.ChannelAction.INCOMING || channel.getChannelAction() == Channel.ChannelAction.BOTH)) {


                    ByteArrayInputStream stream = new ByteArrayInputStream(message);
                    DataInputStream in = new DataInputStream(stream);

                    PluginMessageData data = new PluginMessageData(stream);

                    try {
                        String type = in.readUTF(); //TYPE
                        String server;
                        String subchannel;
                        boolean useGson;
                        String gsonDataName;

                        if (in.available() > 0) {
                            server = in.readUTF();
                        } else {
                            throw new NotEnoughDataException("The server dataInfo was not sent");
                        }

                        if (in.available() > 0) {
                            subchannel = in.readUTF();
                        } else {
                            throw new NotEnoughDataException("The subchannel dataInfo was not sent");
                        }

//                        if (in.available() > 0) {
//                            messageChannel = in.readUTF();
//                        } else {
//                            throw new NotEnoughDataException("The message channel dataInfo was not sent");
//                        }

                        if(in.available() > 0) {
                            JSONPlayer player1 = new Gson().fromJson(in.readUTF(),JSONPlayer.class);
                            if(Bukkit.getPlayer(player.getUniqueId()) != null) {
                                data.setPlayer(Universal.getMethods().getPlayerFromUUID(player1.getUniqueId()));
                            }

                            Universal.debug(() -> "Received player info");
                        }else {
                            throw new NotEnoughDataException("The player information dataInfo was not sent");
                        }

                        if (in.available() > 0) {
                            useGson = in.readBoolean();
                            Universal.debug(() -> "Gson status is: " + useGson);
                        } else {
                            throw new NotEnoughDataException("The use gson boolean dataInfo was not sent");
                        }

                        if (in.available() > 0) {
                            gsonDataName = in.readUTF();
                        } else {
                            throw new NotEnoughDataException("The gson data name was not sent");
                        }

                        data.setProxyChannelType(type);
                        data.setServer(server);
//                        data.setMessageChannel(Channel.createChannelFromString(messageChannel, Channel.ChannelAction.BOTH));
                        data.setMessageChannel(channel);
                        data.setSender(player);
                        data.setSubChannel(subchannel);
                        data.setUseGson(useGson);
                        data.setGsonName(gsonDataName);

                        if(useGson) {
                            if (in.available() > 0) {
                                data = Universal.getMessageHandler().getPacketParser(gsonDataName).parse(in.readUTF());
                            } else {
                                throw new NotEnoughDataException("The use gson json dataInfo was not sent");
                            }
                        } else {
                            while(in.available() > 0) {
                                data.addData(in.readUTF());
                            }
                        }


                    }catch (IOException ee) {
                        ee.printStackTrace();
                    }


                    pl.onMessageReceived(data, channel);
                    break;
                }
            }
        }
    }
}
