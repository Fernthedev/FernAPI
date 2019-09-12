package com.github.fernthedev.fernapi.server.bungee;

import com.github.fernthedev.fernapi.server.bungee.chat.BungeeChatHandler;
import com.github.fernthedev.fernapi.server.bungee.command.BungeeCommandHandler;
import com.github.fernthedev.fernapi.server.bungee.database.BungeeDatabase;
import com.github.fernthedev.fernapi.server.bungee.interfaces.UUIDBungee;
import com.github.fernthedev.fernapi.server.bungee.network.BungeeMessageHandler;
import com.github.fernthedev.fernapi.server.bungee.network.BungeeNetworkHandler;
import com.github.fernthedev.fernapi.universal.ProxyAskPlaceHolder;
import com.github.fernthedev.fernapi.universal.UUIDFetcher;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import net.md_5.bungee.api.plugin.Plugin;

public class FernBungeeAPI extends Plugin implements FernAPIPlugin {

    @Override
    public void onEnable() {
        BungeeMessageHandler messageHandler = new BungeeMessageHandler(this);
        Universal.getInstance().setup(new BungeeInterface(this),
                this,
                new BungeeChatHandler(),
                messageHandler,
                 new BungeeDatabase(this),
                new BungeeCommandHandler(this),
                new BungeeNetworkHandler());
        UUIDFetcher.setFetchManager(new UUIDBungee());
        getProxy().getPluginManager().registerListener(this,messageHandler);
        Universal.getMessageHandler().registerMessageHandler(new ProxyAskPlaceHolder());
    }

    @Override
    public void onDisable() {
        Universal.getInstance().onDisable();
        getProxy().getScheduler().cancel(this);
    }

    @Override
    public void cancelTask(int id) {
        getProxy().getScheduler().cancel(id);
    }

}
