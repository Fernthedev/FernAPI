package com.github.fernthedev.fernapi.server.spigot.network;

import com.github.fernthedev.fernapi.server.spigot.FernSpigotAPI;
import com.github.fernthedev.fernapi.universal.Channels;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.JSONPlayer;
import com.github.fernthedev.fernapi.universal.data.network.*;
import com.github.fernthedev.fernapi.universal.handlers.IFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.PluginMessageHandler;
import com.google.gson.Gson;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.*;

public class SpigotMessageHandler implements IPMessageHandler, PluginMessageListener {

    private FernSpigotAPI spigot;

    public SpigotMessageHandler(FernSpigotAPI fernsp) {
        this.spigot = fernsp;
    }


    @Override
    public void registerMessageHandler(PluginMessageHandler pluginMessageHandler) {
        recievers.add(pluginMessageHandler);
        for(Channel channel : pluginMessageHandler.getChannels()) {
            if(channel.getChannelAction() == Channel.ChannelAction.INCOMING || channel.getChannelAction() == Channel.ChannelAction.BOTH) {
                Bukkit.getMessenger().registerIncomingPluginChannel(spigot, channel.getChannel(), this);
            }

            if(channel.getChannelAction() == Channel.ChannelAction.OUTCOMING || channel.getChannelAction() == Channel.ChannelAction.BOTH) {
                Bukkit.getMessenger().registerOutgoingPluginChannel(spigot, channel.getChannel());
            }
        }

    }

    @Override
    public void sendPluginData(PluginMessageData data) {
        sendPluginData(null,data);
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
            player = (Player) Universal.convertFPlayerToPlayer(fplayer);
        } else {
            player = getRandomPlayer();
        }

        if (player == null) {
            Universal.getMethods().getLogger().warning("No players online, cannot send plugin message");
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
            out.writeUTF(data.getChannelName()); //TYPE
            out.writeUTF(data.getServer()); //SERVER
            out.writeUTF(data.getSubchannel()); //SUBCHANNEL

            out.writeUTF(new Gson().toJson(new JSONPlayer(player.getName(),player.getUniqueId())));

            out.writeBoolean(data.isUseGson());
            if (data.isUseGson()) {
                out.writeUTF(new Gson().toJson(data));
            }



            for(String s : data.getExtraData()) {
                out.writeUTF(s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Bukkit.getServer().sendPluginMessage(spigot,Channels.PlaceHolderBungeeChannel,stream.toByteArray());
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
        for(PluginMessageHandler pl : recievers) {
            for(Channel channel : pl.getChannels()) {
                if (channelName.equals(channel.getChannel()) && (channel.getChannelAction() == Channel.ChannelAction.INCOMING || channel.getChannelAction() == Channel.ChannelAction.BOTH)) {


                    ByteArrayInputStream stream = new ByteArrayInputStream(message);
                    DataInputStream in = new DataInputStream(stream);

                    PluginMessageData data = new PluginMessageData(stream);

                    try {
                        String type = in.readUTF(); //TYPE
                        String server;
                        String subchannel;
                        boolean useGson;

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

                        if(in.available() > 0) {
                            JSONPlayer player1 = new Gson().fromJson(in.readUTF(),JSONPlayer.class);
                            if(Bukkit.getPlayer(player.getUniqueId()) != null) {
                                data.setPlayer(Universal.convertObjectPlayerToFPlayer(Bukkit.getPlayer(player1.getUuid())));
                            }

                            spigot.getLogger().info("Received player info");
                        }else {
                            throw new NotEnoughDataException("The player information dataInfo was not sent");
                        }

                        if (in.available() > 0) {
                            useGson = in.readBoolean();
                            spigot.getLogger().info("Gson status is: " + useGson);
                        } else {
                            throw new NotEnoughDataException("The use gson boolean dataInfo was not sent");
                        }

                        data.setChannelName(type);
                        data.setServer(server);
                        data.setSender(player);
                        data.setSubchannel(subchannel);
                        data.setUseGson(useGson);

                        if(useGson) {
                            if (in.available() > 0) {
                                data = new Gson().fromJson(in.readUTF(), PluginMessageData.class);
                            } else {
                                throw new NotEnoughDataException("The use gson json dataInfo was not sent");
                            }
                        }


                    }catch (IOException ee) {
                        ee.printStackTrace();
                    }


                    pl.onMessageReceived(data,channel);
                    break;
                }
            }
        }
    }
}
