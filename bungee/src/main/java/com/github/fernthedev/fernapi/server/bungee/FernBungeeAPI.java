package com.github.fernthedev.fernapi.server.bungee;

import co.aikar.commands.BungeeCommandManager;
import com.github.fernthedev.fernapi.server.bungee.chat.BungeeChatHandler;
import com.github.fernthedev.fernapi.server.bungee.database.BungeeDatabase;
import com.github.fernthedev.fernapi.server.bungee.interfaces.BungeePluginData;
import com.github.fernthedev.fernapi.server.bungee.network.BungeeMessageHandler;
import com.github.fernthedev.fernapi.server.bungee.network.BungeeNetworkHandler;
import com.github.fernthedev.fernapi.server.bungee.scheduler.BungeeScheduler;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.util.ProxyAskPlaceHolder;
import com.github.fernthedev.fernapi.universal.util.network.vanish.VanishProxyCheck;
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
                new BungeeCommandManager(this),
                new BungeeNetworkHandler(),
                new BungeeScheduler(this),
                new BungeePluginData(getDescription()));
        getProxy().getPluginManager().registerListener(this, messageHandler);
        Universal.getMessageHandler().registerMessageHandler(new ProxyAskPlaceHolder());
        Universal.getMessageHandler().registerMessageHandler(new VanishProxyCheck(this));
    }

    @Override
    public void onDisable() {
        Universal.getInstance().onDisable();
        getProxy().getScheduler().cancel(this);
    }



}
