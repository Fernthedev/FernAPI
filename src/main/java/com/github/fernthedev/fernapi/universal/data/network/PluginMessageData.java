package com.github.fernthedev.fernapi.universal.data.network;

import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Getter
@Setter
public class PluginMessageData {

    protected Object sender;


    protected String proxyChannelType = "Forward"; // channel we delivered or type

    /**
     * No need to define the bungee channel type
     */
    @Deprecated
    public void setProxyChannelType(String proxyChannelType) {
        this.proxyChannelType = proxyChannelType;
    }

    protected String server;


    public void setServer(String server) {
        this.server = server;
    }

    public PluginMessageData setServer(IServerInfo server) {
        this.server = server.getName();
        return this;
    }

    protected String subChannel;

    protected Channel messageChannel;

    protected boolean useGson = false;

    protected ByteArrayInputStream inputStream;
    protected DataInputStream in;

    protected ByteArrayOutputStream outputStream;
    protected DataOutputStream out;



    protected List<String> extraData = new ArrayList<>();

    /**
     * Returns a new instance each time. Save it as a variable.
     */
    public Queue<String> getExtraDataQueue() {
        return new LinkedList<>(extraData);
    }

    protected IFPlayer player;

    public PluginMessageData(@NonNull ByteArrayInputStream inputStream) {
        this.inputStream = inputStream;
        this.in = new DataInputStream(inputStream);
    }

    public PluginMessageData(Object channelBuf) {

    }

    /**
     * @deprecated Use {@link PluginMessageData#PluginMessageData(ByteArrayOutputStream, String, String, Channel)}
     */
    @Deprecated
    public PluginMessageData(@NonNull ByteArrayOutputStream outputStream, String proxyChannelType, String server, String subChannel, String pluginChannel) {
        this.outputStream = outputStream;
        this.in = new DataInputStream(inputStream);
        this.proxyChannelType = proxyChannelType;
        this.server = server;
        this.subChannel = subChannel;
        this.messageChannel = Channel.createChannelFromString(pluginChannel, Channel.ChannelAction.BOTH);
    }

    /**
     * @deprecated Use {@link PluginMessageData#PluginMessageData(ByteArrayOutputStream, String, String, Channel)}
     */
    public PluginMessageData(@NonNull ByteArrayOutputStream outputStream, String server, String subChannel, String pluginChannel) {
        this.outputStream = outputStream;
        this.in = new DataInputStream(inputStream);
        this.server = server;
        this.subChannel = subChannel;
        this.messageChannel = Channel.createChannelFromString(pluginChannel, Channel.ChannelAction.BOTH);
    }

    /**
     *
     * @param outputStream The stream with data
     * @param server The server to send to. Use server name or "ALL"
     * @param subChannel The SubChannel to send to.
     * @param channel The Plugin channel
     */
    public PluginMessageData(@NonNull ByteArrayOutputStream outputStream, String server, String subChannel, Channel channel) {
        this.outputStream = outputStream;
        this.in = new DataInputStream(inputStream);
        this.server = server;
        this.subChannel = subChannel;
        this.messageChannel = channel;
    }

    /**
     *
     * @param outputStream The stream with data
     * @param server The server to send to. Use server name or "ALL"
     * @param subChannel The SubChannel to send to.
     * @param channel The Plugin channel
     */
    public PluginMessageData(@NonNull ByteArrayOutputStream outputStream, IServerInfo server, String subChannel, Channel channel) {
        this(outputStream, server.getName(), subChannel, channel);
    }


    /**
     * This will add extra dataInfo, you can also send an instance of this class through gson if you extend it.
     * @param s The object, use gson if possible for objects.
     */
    public void addData(String s) {
        extraData.add(s);
    }




}
