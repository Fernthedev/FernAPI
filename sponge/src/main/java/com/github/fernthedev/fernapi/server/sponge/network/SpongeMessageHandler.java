package com.github.fernthedev.fernapi.server.sponge.network;

import com.github.fernthedev.fernapi.server.sponge.FernSpongeAPI;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.JSONPlayer;
import com.github.fernthedev.fernapi.universal.data.network.*;
import com.github.fernthedev.fernapi.universal.exceptions.network.NoPlayersOnlineException;
import com.github.fernthedev.fernapi.universal.exceptions.network.NotEnoughDataException;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.PluginMessageHandler;
import com.google.gson.Gson;
import org.spongepowered.api.Game;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.network.ChannelBinding;
import org.spongepowered.api.network.ChannelBuf;
import org.spongepowered.api.scheduler.Task;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
public class SpongeMessageHandler extends IPMessageHandler {

    public SpongeMessageHandler(FernSpongeAPI sponge) {
        SpongeMessageHandler.sponge = sponge;
    }

    Game game;
    Task task;
    private static FernSpongeAPI sponge;

    private Map<Channel, PluginMessageHandler> channelPluginMessageHandlerHashMap = new HashMap<>();

    /**
     * Needs to be rewritten.
     * @param pluginMessageHandler
     */
    @Override
    public void registerMessageHandler(PluginMessageHandler pluginMessageHandler) {
        receivers.add(pluginMessageHandler);
        for(Channel channelPlugin : pluginMessageHandler.getChannels()) {
            ChannelBinding.RawDataChannel spongeChannel = game.getChannelRegistrar().getOrCreateRaw(sponge, channelPlugin.getFullChannelName());

            spongeChannel.addListener(Platform.Type.SERVER, (data, connection, side) -> {
                for(PluginMessageHandler pl : receivers) {
                    for(Channel channel : pl.getChannels()) {
                        if (channelPlugin.getFullChannelName().equals(channel.getFullChannelName()) && (channel.getChannelAction() == Channel.ChannelAction.INCOMING || channel.getChannelAction() == Channel.ChannelAction.BOTH)) {

                            Player player = null;
                            ChannelBuf in = data;

                            PluginMessageData pdata = new PluginMessageData(data);

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

//                                if (in.available() > 0) {
//                                    messageChannel = in.readUTF();
//                                } else {
//                                    throw new NotEnoughDataException("The message channel dataInfo was not sent");
//                                }

                                if(in.available() > 0) {
                                    JSONPlayer player1 = new Gson().fromJson(in.readUTF(),JSONPlayer.class);
                                    if(Sponge.getServer().getPlayer(player1.getUniqueId()).isPresent()) {
                                        pdata.setPlayer(Universal.getMethods().getPlayerFromUUID(player1.getUniqueId()));
                                        player = Sponge.getServer().getPlayer(player1.getUniqueId()).get();
                                    }

                                    sponge.getLogger().debug("Received player info");
                                }else {
                                    throw new NotEnoughDataException("The player information dataInfo was not sent");
                                }

                                if (in.available() > 0) {
                                    useGson = in.readBoolean();
                                    sponge.getLogger().debug("Gson status is: " + useGson);
                                } else {
                                    throw new NotEnoughDataException("The use gson boolean dataInfo was not sent");
                                }

                                if (in.available() > 0) {
                                    gsonDataName = in.readUTF();
                                } else {
                                    throw new NotEnoughDataException("The gson data name was not sent");
                                }

                                pdata.setProxyChannelType(type);
                                pdata.setServer(server);
                                pdata.setMessageChannel(channel);
                                pdata.setSender(player);
                                pdata.setSubChannel(subchannel);
                                pdata.setUseGson(useGson);
                                pdata.setGsonName(gsonDataName);

                                if(useGson) {
                                    if (in.available() > 0) {
                                        pdata = Universal.getMessageHandler().getPacketParser(gsonDataName).parse(in.readUTF());
                                    } else {
                                        throw new NotEnoughDataException("The use gson json dataInfo was not sent");
                                    }
                                } else {
                                    while(in.available() > 0) {
                                        pdata.addData(in.readUTF());
                                    }
                                }


                            } catch (IOException ee) {
                                ee.printStackTrace();
                            }


                            pl.onMessageReceived(pdata, channel);
                            break;
                        }
                    }
                }
            });
            channelPluginMessageHandlerHashMap.put(channelPlugin, pluginMessageHandler);
        }
    }

    @Override
    public void sendPluginData(PluginMessageData data) {
        sendPluginData(null,data);
    }


    /**
     * @return A random Player (/ the first player in the Player Collection)
     */
    private static Player getRandomPlayer() {
        if (!Sponge.getServer().getOnlinePlayers().isEmpty())
            return Sponge.getServer().getOnlinePlayers().iterator().next();
        else
            return null;
    }

    @Override
    public void sendPluginData(IFPlayer<?> fplayer, PluginMessageData data) {
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


        Consumer<ChannelBuf> channelBuf = out -> {
            out.writeUTF(data.getProxyChannelType()); //TYPE
            out.writeUTF(data.getServer()); //SERVER
            out.writeUTF(data.getSubChannel()); //SUBCHANNEL

            out.writeUTF(data.getMessageChannel().getFullChannelName());

            out.writeUTF(new Gson().toJson(new JSONPlayer(player.getName(), player.getUniqueId())));

            out.writeBoolean(data.isUseGson());
            out.writeUTF(data.getGsonName());
            if (data.isUseGson()) {
                out.writeUTF(new Gson().toJson(data));
            }


            for (String s : data.getExtraData()) {
                out.writeUTF(s);
            }
        };


        ChannelBinding.RawDataChannel channele = game.getChannelRegistrar().getOrCreateRaw(sponge, data.getMessageChannel().getFullChannelName());

        channele.sendTo(player,channelBuf);

/*
        channel.sendTo(player,channelBuf -> {

            channelBuf.writeUTF(data.getChannelName()).writeUTF(data.getServer())
                    .writeUTF(data.getSubchannel()).writeUTF(new Gson().toJson(new JSONPlayer(player.getName(),player.getUniqueId())))
                    .writeBoolean(data.isUseGson());
            if(data.isUseGson()) {
                channelBuf.writeUTF(new Gson().toJson(data));
            }

            for(String s : data.getExtraData()) {
                channelBuf.writeUTF(s);
            }
        });*/

    }
}
