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
     * This is where you specify the channels you want to listen to
     * Just make a new List<Channel> instance and add an instance of the channel accordingly.
     * @see com.github.fernthedev.fernapi.server.bungee.network.AskPlaceHolder as an example
     * @return The channels that will be incoming and outgoing
     */
    public abstract List<Channel> getChannels();

    /**
     * The event called when message is received from the channels registered
     *
     * @param data The data received for use of the event.
     * @param channel The channel it was received from, for use of multiple channels in one listener
     */
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
