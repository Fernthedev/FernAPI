package com.github.fernthedev.fernapi.server.bungee.network;

import com.github.fernthedev.fernapi.server.bungee.FernBungeeAPI;
import com.github.fernthedev.fernapi.universal.Channels;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.JSONPlayer;
import com.github.fernthedev.fernapi.universal.data.network.*;
import com.github.fernthedev.fernapi.universal.handlers.IFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.PluginMessageHandler;
import com.google.gson.Gson;
import lombok.NonNull;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.*;
import java.util.List;

public class BungeeMessageHandler implements Listener, IPMessageHandler {

    private FernBungeeAPI bungee;

    public List<PluginMessageHandler> getRecievers() {
        return recievers;
    }

    public BungeeMessageHandler(FernBungeeAPI fernBungeeAPI) {
        this.bungee = fernBungeeAPI;
    }



    @EventHandler
    public void onPluginMessage(PluginMessageEvent ev) {
        for (PluginMessageHandler pl : recievers) {
            for (Channel channel : pl.getChannels()) {
                if (ev.getTag().equals(channel.getChannel()) && (channel.getChannelAction() == Channel.ChannelAction.INCOMING || channel.getChannelAction() == Channel.ChannelAction.BOTH)) {

                    ByteArrayInputStream stream = new ByteArrayInputStream(ev.getData());
                    DataInputStream in = new DataInputStream(stream);

                    PluginMessageData data = new PluginMessageData(stream);

                    try {
                        String channelName = in.readUTF(); // channel we delivered
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
                            JSONPlayer ifPlayer = new Gson().fromJson(in.readUTF(),JSONPlayer.class);
                            IFPlayer correctPlayer = Universal.convertObjectPlayerToFPlayer(Universal.convertFPlayerToPlayer(ifPlayer));
                            data.setPlayer(correctPlayer);
                        }else {
                            throw new NotEnoughDataException("The player information dataInfo was not sent");
                        }

                        if (in.available() > 0) {
                            useGson = in.readBoolean();
                        } else {
                            throw new NotEnoughDataException("The use gson boolean dataInfo was not sent");
                        }

                        data.setChannelName(channelName);
                        data.setSender(ev.getSender());
                        data.setServer(server);
                        data.setSubchannel(subchannel);
                        data.setUseGson(useGson);

                        if(useGson) {
                            if (in.available() > 0) {
                                data = new Gson().fromJson(in.readUTF(), PluginMessageData.class);
                            } else {
                                throw new NotEnoughDataException("The use gson json dataInfo was not sent");
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
            ProxyServer.getInstance().registerChannel(channel.getChannel());
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
        ProxiedPlayer player;
        if (fplayer != null) {
            player = (ProxiedPlayer) Universal.convertFPlayerToPlayer(fplayer);
        } else {
            player = getRandomPlayer();
        }

        if (player == null) {
            Universal.getMethods().getLogger().warning("No players online, cannot send plugin message");
            try {
                throw new NoPlayersOnlineException("Players are required to send plugin messages through bungee to other servers.");
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
            bungee.getLogger().info("Use gson status: " + data.isUseGson());


            if (data.isUseGson()) {
                out.writeUTF(new Gson().toJson(data));
            }



            for(String s : data.getExtraData()) {
                out.writeUTF(s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        player.getServer().sendData(Channels.PlaceHolderBungeeChannel,stream.toByteArray());
    }

    /**
     * @return A random Player (/ the first player in the Player Collection)
     */
    private static ProxiedPlayer getRandomPlayer() {
        if (!ProxyServer.getInstance().getPlayers().isEmpty())
            return ProxyServer.getInstance().getPlayers().iterator().next();
        else
            return null;
    }
}
