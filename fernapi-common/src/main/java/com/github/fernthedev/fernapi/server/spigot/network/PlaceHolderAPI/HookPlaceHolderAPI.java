package com.github.fernthedev.fernapi.server.spigot.network.PlaceHolderAPI;

import com.github.fernthedev.fernapi.server.spigot.FernSpigotAPI;
import com.github.fernthedev.fernapi.universal.Channels;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.network.Channel;
import com.github.fernthedev.fernapi.universal.data.network.PluginMessageData;
import com.github.fernthedev.fernapi.universal.handlers.PluginMessageHandler;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class HookPlaceHolderAPI extends PluginMessageHandler {


    public HookPlaceHolderAPI() {
        getLogger().info("Initiated PlaceHolderAPI plugin message listener");
    }

    private FernSpigotAPI spigotAPI;

    public HookPlaceHolderAPI(FernSpigotAPI fernSpigotAPI) {
        this.spigotAPI = fernSpigotAPI;
    }

    private Logger getLogger() {
        return spigotAPI.getLogger();
    }

    /**
     * This is the channel name that will be registered incoming and outcoming
     *
     * @return The channels that will be incoming and outcoming
     */
    @Override
    public List<Channel> getChannels() {
        List<Channel> channels = new ArrayList<>();
        channels.add(new Channel(Channels.PlaceHolderBungeeChannel, Channel.ChannelAction.BOTH));

        return channels;
    }

    @Override
    public void onMessageReceived(PluginMessageData data, Channel channel) {
        DataInputStream in = data.getIn();
        try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                PluginMessageData newData = new PluginMessageData(stream,"Forward","ALL",Channels.PlaceHolderValue);

                String placeholderOld = in.readUTF(); //PLACEHOLDER
                String uuid = in.readUTF(); //UUID OF SENDER

                Player player = (Player) Universal.convertFPlayerToPlayer(data.getPlayer());

                String placeholder = PlaceholderAPI.setPlaceholders(player, placeholderOld);

                if (placeholder.equalsIgnoreCase(placeholderOld)) {
                    newData.addData("NoPlaceHolderFound");
                } else {
                    newData.addData(placeholder);
                }

                newData.addData(uuid);

                Universal.getMessageHandler().sendPluginData(newData);

               // Bukkit.getServer().sendPluginMessage(spigotAPI.getPlugin(spigotAPI.getClass()), Channels.PlaceHolderBungeeChannel, stream.toByteArray());


        } catch (IOException e) {
            e.fillInStackTrace();
        }
    }
}
