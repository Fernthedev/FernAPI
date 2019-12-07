package com.github.fernthedev.fernapi.server.velocity.network;

import com.github.fernthedev.fernapi.server.velocity.FernVelocityAPI;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.JSONPlayer;
import com.github.fernthedev.fernapi.universal.data.network.Channel;
import com.github.fernthedev.fernapi.universal.data.network.IPMessageHandler;
import com.github.fernthedev.fernapi.universal.data.network.PluginMessageData;
import com.github.fernthedev.fernapi.universal.exceptions.network.NoPlayersOnlineException;
import com.github.fernthedev.fernapi.universal.exceptions.network.NotEnoughDataException;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.PluginMessageHandler;
import com.google.gson.Gson;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import lombok.NonNull;

import java.io.*;
import java.util.List;

public class VelocityMessageHandler implements IPMessageHandler {

    private FernVelocityAPI velocity;

    public List<PluginMessageHandler> getRecievers() {
        return recievers;
    }

    public VelocityMessageHandler(FernVelocityAPI fernVelocityAPI) {
        this.velocity = fernVelocityAPI;
    }



    @Subscribe
    public void onPluginMessage(PluginMessageEvent ev) {
        for (PluginMessageHandler pl : recievers) {
            for (Channel channel : pl.getChannels()) {
                if (ev.getIdentifier().getId().equals(channel.getFullChannelName()) && (channel.getChannelAction() == Channel.ChannelAction.INCOMING || channel.getChannelAction() == Channel.ChannelAction.BOTH)) {

                    ByteArrayInputStream stream = new ByteArrayInputStream(ev.getData());
                    DataInputStream in = new DataInputStream(stream);

                    PluginMessageData data = new PluginMessageData(stream);

                    try {
                        String bungeeChannelName = in.readUTF(); // channel we delivered
                        String server;
                        String subChannel;
                        boolean useGson;

                        if (in.available() > 0) {
                            server = in.readUTF();
                        } else {
                            throw new NotEnoughDataException("The server dataInfo was not sent");
                        }

                        if (in.available() > 0) {
                            subChannel = in.readUTF();
                        } else {
                            throw new NotEnoughDataException("The subChannel dataInfo was not sent");
                        }

//                        if(in.available() > 0) {
//                            messageChannel = in.readUTF();
//                        } else {
//                            throw new NotEnoughDataException("The message channel data info was not sent");
//                        }

                        if(in.available() > 0) {
                            JSONPlayer ifPlayer = new Gson().fromJson(in.readUTF(),JSONPlayer.class);
                            IFPlayer correctPlayer = Universal.getMethods().getPlayerFromUUID(ifPlayer.getUuid());
                            data.setPlayer(correctPlayer);
                        }else {
                            throw new NotEnoughDataException("The player information dataInfo was not sent");
                        }

                        if (in.available() > 0) {
                            useGson = in.readBoolean();
                        } else {
                            throw new NotEnoughDataException("The use gson boolean dataInfo was not sent");
                        }

                        data.setBungeeChannelType(bungeeChannelName);
                        data.setMessageChannel(channel);
                        data.setSender(ev.getSource());
                        data.setServer(server);
                        data.setSubChannel(subChannel);
                        data.setUseGson(useGson);

                        if(useGson) {
                            if (in.available() > 0) {
                                data = new Gson().fromJson(in.readUTF(), PluginMessageData.class);
                            } else {
                                throw new NotEnoughDataException("The use gson json dataInfo was not sent");
                            }
                        } else {
                            while(in.available() > 0) {
                                data.addData(in.readUTF());
                            }
                        }




                    } catch (IOException ee) {
                        ee.printStackTrace();
                    }

                    pl.onMessageReceived(data, channel);
                    break;
                }
            }
        }
    }

    @Override
    public void registerMessageHandler(PluginMessageHandler pluginMessageHandler) {
        recievers.add(pluginMessageHandler);
        for(Channel channel : pluginMessageHandler.getChannels()) {
            MinecraftChannelIdentifier channelIdentifier = MinecraftChannelIdentifier.create(channel.getNamespace(), channel.getChannelName());
            velocity.getServer().getChannelRegistrar().register(channelIdentifier);
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
                throw new NoPlayersOnlineException("Players are required to send plugin messages through bungee/velocity to other servers.");
            } catch (NoPlayersOnlineException e) {
                e.printStackTrace();
            }
            return;
        }

        data.setPlayer(fplayer);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);

        try {
            out.writeUTF(data.getBungeeChannelType()); //TYPE
            out.writeUTF(data.getServer()); //SERVER
            out.writeUTF(data.getSubChannel()); //SUBCHANNEL

            out.writeUTF(data.getMessageChannel().getFullChannelName());

            out.writeUTF(new Gson().toJson(new JSONPlayer(player.getUsername(),player.getUniqueId())));

            out.writeBoolean(data.isUseGson());
            Universal.debug("Use gson status: " + data.isUseGson());


            if (data.isUseGson()) {
                out.writeUTF(new Gson().toJson(data));
            }



            for(String s : data.getExtraData()) {
                out.writeUTF(s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Channel channel = Channel.createChannelFromString(data.getMessageChannel().getFullChannelName(), Channel.ChannelAction.BOTH);
        ChannelIdentifier channelIdentifier = MinecraftChannelIdentifier.create(channel.getNamespace(), channel.getChannelName());

        player.getCurrentServer().get().getServer().sendPluginMessage(channelIdentifier, stream.toByteArray());
    }

    /**
     * @return A random Player (/ the first player in the Player Collection)
     */
    private Player getRandomPlayer() {
        if (!velocity.getServer().getAllPlayers().isEmpty())
            return velocity.getServer().getAllPlayers().iterator().next();
        else
            return null;
    }
}