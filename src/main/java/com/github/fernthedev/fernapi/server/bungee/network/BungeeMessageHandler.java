package com.github.fernthedev.fernapi.server.bungee.network;

import com.github.fernthedev.fernapi.server.bungee.FernBungeeAPI;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.JSONPlayer;
import com.github.fernthedev.fernapi.universal.data.network.Channel;
import com.github.fernthedev.fernapi.universal.data.network.IPMessageHandler;
import com.github.fernthedev.fernapi.universal.data.network.PluginMessageData;
import com.github.fernthedev.fernapi.universal.exceptions.network.NotEnoughDataException;
import com.github.fernthedev.fernapi.universal.exceptions.network.ServerDoesNotExistException;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.handlers.PluginMessageHandler;
import com.google.gson.Gson;
import lombok.NonNull;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.*;

public class BungeeMessageHandler implements Listener, IPMessageHandler {

    private FernBungeeAPI bungee;

    public BungeeMessageHandler(FernBungeeAPI fernBungeeAPI) {
        this.bungee = fernBungeeAPI;
    }



    @EventHandler
    public void onPluginMessage(PluginMessageEvent ev) {
        for (PluginMessageHandler pl : recievers) {
            for (Channel channel : pl.getChannels()) {
//                System.out.println(ev.getTag() + " " + channel.getFullChannelName() + " " + (ev.getTag().equals(channel.getFullChannelName())));
                if (ev.getTag().equals(channel.getFullChannelName()) && (channel.getChannelAction() == Channel.ChannelAction.INCOMING || channel.getChannelAction() == Channel.ChannelAction.BOTH)) {

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

//                        if(in.available() > 0) {
//                            messageChannel = in.readUTF();
//                        } else {
//                            throw new NotEnoughDataException("The message channel data info was not sent");
//                        }

                        if(in.available() > 0) {
                            String dataS = in.readUTF();

                            JSONPlayer ifPlayer = new Gson().fromJson(dataS, JSONPlayer.class);

                            if(ifPlayer != null && ifPlayer.getUuid() != null) {
                                IFPlayer correctPlayer = Universal.getMethods().getPlayerFromUUID(ifPlayer.getUuid());
                                data.setPlayer(correctPlayer);
                            } else {
                                data.setPlayer(null);
                            }

                        } else {
                            throw new NotEnoughDataException("The player information dataInfo was not sent");
                        }

                        if (in.available() > 0) {
                            useGson = in.readBoolean();
                        } else {
                            throw new NotEnoughDataException("The use gson boolean dataInfo was not sent");
                        }

                        data.setBungeeChannelType(channelName);
//                        data.setMessageChannel(Channel.createChannelFromString(messageChannel, Channel.ChannelAction.BOTH));
                        data.setMessageChannel(channel);
                        data.setSender(ev.getSender());
                        data.setServer(server);
                        data.setSubChannel(subchannel);
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
            ProxyServer.getInstance().registerChannel(channel.getFullChannelName());
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
            player = (ProxiedPlayer) Universal.getMethods().convertFPlayerToPlayer(fplayer);
        } else {
            player = getRandomPlayer();
        }

//        if (player == null) {
//            Universal.getMethods().getLogger().warning("No players online, cannot send plugin message");
//            try {
//                throw new NoPlayersOnlineException("Players are required to send plugin messages through bungee to other servers.");
//            } catch (NoPlayersOnlineException e) {
//                e.printStackTrace();
//            }
//            return;
//        }

        data.setPlayer(fplayer);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);

        try {
            out.writeUTF(data.getBungeeChannelType()); //TYPE
            out.writeUTF(data.getServer()); //SERVER
            out.writeUTF(data.getSubChannel()); //SUBCHANNEL

//            out.writeUTF(data.getMessageChannel().getFullChannelName());

            if(player != null) {
                out.writeUTF(new Gson().toJson(new JSONPlayer(player.getName(), player.getUniqueId())));
            } else {
                out.writeUTF(new Gson().toJson(new JSONPlayer()));
            }
            out.writeBoolean(data.isUseGson());

            Universal.debug("Use gson status: " + data.isUseGson());


            if (data.isUseGson()) {
                out.writeUTF(new Gson().toJson(data));
            }

            if (!data.isUseGson()) {
                for (String s : data.getExtraData()) {
                    out.writeUTF(s);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (data.getServer().equalsIgnoreCase("all")) {
            for (ServerInfo server : ProxyServer.getInstance().getServers().values()) {
                server.sendData(data.getMessageChannel().getFullChannelName(), stream.toByteArray());
                Universal.debug(server.getName() + " " + server);
            }
        } else {
            Universal.debug("fplayer == null" + (fplayer == null));
            if (fplayer == null) {
                ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(data.getServer());
                if(serverInfo == null) {
                    throw new ServerDoesNotExistException("The specified server " + data.getServer() + " does not exist");
                }

                ProxyServer.getInstance().getScheduler().runAsync(bungee, () -> serverInfo.sendData(data.getMessageChannel().getFullChannelName(), stream.toByteArray(), true));

                Universal.debug(serverInfo.getName() + " " + serverInfo + " sent");
            } else {
                Server serverInfo = player.getServer();
                ProxyServer.getInstance().getScheduler().runAsync(bungee, () -> serverInfo.sendData(data.getMessageChannel().getFullChannelName(), stream.toByteArray()));

                Universal.debug(serverInfo.getInfo().getName() + " " + serverInfo.getInfo() + " sent");
            }
        }
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
