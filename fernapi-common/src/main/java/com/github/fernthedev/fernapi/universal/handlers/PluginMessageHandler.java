package com.github.fernthedev.fernapi.universal.handlers;

import com.github.fernthedev.fernapi.universal.data.network.Channel;
import com.github.fernthedev.fernapi.universal.data.network.NotEnoughDataException;
import com.github.fernthedev.fernapi.universal.data.network.PluginMessageData;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

public abstract class PluginMessageHandler {
    
    protected static String reason1 = "There was not enough data to be read";
    protected static String reason2 = "There was an error processing the data.";

    /**
     * This is the channel name that will be registered incoming and outcoming
     * @return The channels that will be incoming and outcoming
     */
    public abstract List<Channel> getChannels();

    public abstract void onMessageReceived(PluginMessageData data, Channel channel);

    protected String getDataString(DataInputStream in) throws IOException {
        try {
            if (in.available() > 0) {
                return in.readUTF();
            } else {
                throw new NotEnoughDataException(reason1);
            }
        } catch (IOException es) {
            throw new NotEnoughDataException(reason2,new IOException());
        }
    }

    protected int getDataInt(DataInputStream in) throws IOException {
        try {
            if (in.available() > 0) {
                return in.readInt();
            } else {
                throw new NotEnoughDataException(reason1);
            }
        } catch (IOException es) {
            throw new NotEnoughDataException(reason2,new IOException());
        }
    }

    protected Long getDataLong(DataInputStream in) throws IOException {
        try {
            if (in.available() > 0) {
                return in.readLong();
            } else {
                throw new NotEnoughDataException(reason1);
            }
        } catch (IOException es) {
            throw new NotEnoughDataException(reason2,new IOException());
        }
    }

    protected byte getDataBytes(DataInputStream in) throws IOException {
        try {
            if (in.available() > 0) {
                return in.readByte();
            } else {
                throw new NotEnoughDataException(reason1);
            }
        } catch (IOException es) {
            throw new NotEnoughDataException(reason2, new IOException());
        }
    }



}
