package com.github.fernthedev.fernapi.universal.data.network;

import com.github.fernthedev.fernapi.universal.handlers.IFPlayer;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PluginMessageData {

    protected Object sender;
    protected String channelName; // channel we delivered or type
    protected String server;
    protected String subchannel;

    protected String messageChannel;

    protected boolean useGson = false;

    protected List<String> extraData = new ArrayList<>();

    protected IFPlayer player;

    public PluginMessageData(@NonNull ByteArrayInputStream inputStream) {
        this.inputStream = inputStream;
        this.in = new DataInputStream(inputStream);
    }

    public PluginMessageData(Object channelBuf) {

    }

    public PluginMessageData(@NonNull ByteArrayOutputStream outputStream,String channelName,String server,String subchannel,String messageChannel) {
        this.outputStream = outputStream;
        this.in = new DataInputStream(inputStream);
        this.channelName = channelName;
        this.server = server;
        this.subchannel = subchannel;
        this.messageChannel = messageChannel;
    }

    /**
     * This will add extra dataInfo, you can also send an instance of this class through gson if you extend it.
     * @param s The object, use gson if possible for objects.
     */
    public void addData(String s) {
        extraData.add(s);
    }

    protected ByteArrayInputStream inputStream;
    protected DataInputStream in;

    protected ByteArrayOutputStream outputStream;
    protected DataOutputStream out;



}
