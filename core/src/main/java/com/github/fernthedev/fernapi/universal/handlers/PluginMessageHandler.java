package com.github.fernthedev.fernapi.universal.handlers;

import com.github.fernthedev.fernapi.universal.data.network.Channel;
import com.github.fernthedev.fernapi.universal.data.network.PluginMessageData;
import com.github.fernthedev.fernapi.universal.exceptions.network.NotEnoughDataException;
import com.github.fernthedev.fernapi.universal.util.ProxyAskPlaceHolder;
import lombok.NonNull;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

public abstract class PluginMessageHandler {
    
    protected static final String reason1 = "There was not enough dataInfo to be read";
    protected static final String reason2 = "There was an error processing the dataInfo.";

    /**
     * This is the channel name that will be registered incoming and outgoing
     * This is where you specify the channels you want to listen to
     * Just make a new {@link java.util.ArrayList} with Channel instance instance and add an instance of the channel accordingly.
     * @see ProxyAskPlaceHolder as an example
     * @return The channels that will be incoming and outgoing
     */
    @NonNull
    public abstract List<Channel> getChannels();

    /**
     * The event called when message is received from the channels registered
     *
     * @param data The dataInfo received for use of the event.
     * @param channel The channel it was received from, for use of multiple channels in one listener
     */
    public abstract void onMessageReceived(PluginMessageData data, Channel channel);

    /**
     * Used to get data string with descriptive exception
     * @param in
     * @return Data
     * @throws IOException
     */
    protected String getDataString(DataInputStream in) throws IOException {
        try {
            if (in.available() > 0) {
                return in.readUTF();
            } else {
                throw new NotEnoughDataException(reason1);
            }
        } catch (IOException es) {
            throw new NotEnoughDataException(reason2, es);
        }
    }

    /**
     * Used to get data string with descriptive exception
     * @param in
     * @return Data
     * @throws IOException
     */
    protected int getDataInt(DataInputStream in) throws IOException {
        try {
            if (in.available() > 0) {
                return in.readInt();
            } else {
                throw new NotEnoughDataException(reason1);
            }
        } catch (IOException es) {
            throw new NotEnoughDataException(reason2, es);
        }
    }

    /**
     * Used to get data string with descriptive exception
     * @param in
     * @return Data
     * @throws IOException
     */
    protected Long getDataLong(DataInputStream in) throws IOException {
        try {
            if (in.available() > 0) {
                return in.readLong();
            } else {
                throw new NotEnoughDataException(reason1);
            }
        } catch (IOException es) {
            throw new NotEnoughDataException(reason2, es);
        }
    }

    /**
     * Used to get data string with descriptive exception
     * @param in
     * @return Data
     * @throws IOException
     */
    protected byte getDataBytes(DataInputStream in) throws IOException {
        try {
            if (in.available() > 0) {
                return in.readByte();
            } else {
                throw new NotEnoughDataException(reason1);
            }
        } catch (IOException es) {
            throw new NotEnoughDataException(reason2, es);
        }
    }



}
