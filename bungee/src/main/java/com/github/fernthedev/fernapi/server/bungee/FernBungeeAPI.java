package com.github.fernthedev.fernapi.server.bungee;

import co.aikar.commands.BungeeCommandManager;
import com.github.fernthedev.fernapi.server.bungee.chat.BungeeChatHandler;
import com.github.fernthedev.fernapi.server.bungee.interfaces.BungeePluginData;
import com.github.fernthedev.fernapi.server.bungee.network.BungeeMessageHandler;
import com.github.fernthedev.fernapi.server.bungee.network.BungeeNetworkHandler;
import com.github.fernthedev.fernapi.server.bungee.scheduler.BungeeScheduler;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.util.ProxyAskPlaceHolder;
import com.github.fernthedev.fernapi.universal.util.network.vanish.VanishProxyCheck;
import net.md_5.bungee.api.plugin.Plugin;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public class FernBungeeAPI extends Plugin implements FernAPIPlugin {

    @OverridingMethodsMustInvokeSuper
    @Override
    public void onEnable() {
        BungeeMessageHandler messageHandler = new BungeeMessageHandler(this);
        Universal.getInstance().setup(new BungeeInterface(this),
                this,
                new BungeeChatHandler(),
                messageHandler,
                new BungeeCommandManager(this),
                new BungeeNetworkHandler(),
                new BungeeScheduler(this),
                new BungeePluginData(getDescription()));
        getProxy().getPluginManager().registerListener(this, messageHandler);
        Universal.getMessageHandler().registerMessageHandler(ProxyAskPlaceHolder.LISTENER_INSTANCE);
        Universal.getMessageHandler().registerMessageHandler(VanishProxyCheck.LISTENER_INSTANCE);
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public void onDisable() {
        Universal.getInstance().onDisable();
        getProxy().getScheduler().cancel(this);
    }



}
