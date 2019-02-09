package com.github.fernthedev.fernapi.server.bungee;

import com.github.fernthedev.fernapi.server.bungee.chat.BungeeChatHandler;
import com.github.fernthedev.fernapi.server.bungee.database.BungeeDatabase;
import com.github.fernthedev.fernapi.server.bungee.interfaces.UUIDBungee;
import com.github.fernthedev.fernapi.server.bungee.network.AskPlaceHolder;
import com.github.fernthedev.fernapi.server.bungee.network.BungeeMessageHandler;
import com.github.fernthedev.fernapi.universal.UUIDFetcher;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import net.md_5.bungee.api.plugin.Plugin;

public class FernBungeeAPI extends Plugin implements FernAPIPlugin {

    @Override
    public void onEnable() {
        BungeeMessageHandler messageHandler = new BungeeMessageHandler(this);
        Universal.getInstance().setup(new BungeeInterface(this),
                new BungeeChatHandler(),
                messageHandler,
                 new BungeeDatabase(this));
        UUIDFetcher.setFetchManager(new UUIDBungee());
        getProxy().getPluginManager().registerListener(this,messageHandler);
        Universal.getMessageHandler().registerMessageHandler(new AskPlaceHolder(this));
    }


    @Override
    public void cancelTask(int id) {
        getProxy().getScheduler().cancel(id);
    }

}
