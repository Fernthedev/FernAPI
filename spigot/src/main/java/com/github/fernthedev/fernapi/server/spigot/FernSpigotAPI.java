package com.github.fernthedev.fernapi.server.spigot;

import co.aikar.commands.PaperCommandManager;
import com.github.fernthedev.fernapi.server.spigot.chat.SpigotChatHandler;
import com.github.fernthedev.fernapi.server.spigot.interfaces.SpigotPluginData;
import com.github.fernthedev.fernapi.server.spigot.network.PlaceHolderAPI.PlaceHolderAPIResponder;
import com.github.fernthedev.fernapi.server.spigot.network.SpigotMessageHandler;
import com.github.fernthedev.fernapi.server.spigot.network.SpigotNetworkHandler;
import com.github.fernthedev.fernapi.server.spigot.pluginhandlers.VaultHandler;
import com.github.fernthedev.fernapi.server.spigot.scheduler.SpigotScheduler;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import com.github.fernthedev.fernapi.universal.util.network.vanish.VanishProxyCheck;
import fr.minuskube.inv.InventoryManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public class FernSpigotAPI extends JavaPlugin implements FernAPIPlugin {

    @Getter
    private VaultHandler vaultHandler;

    @Getter
    private InventoryManager inventoryManager;

    @OverridingMethodsMustInvokeSuper
    @Override
    public void onEnable() {
        Universal.getInstance().setup(new SpigotInterface(this),
                this,
                new SpigotChatHandler(),
                new SpigotMessageHandler(this),
                new PaperCommandManager(this),
                new SpigotNetworkHandler(),
                new SpigotScheduler(this),
                new SpigotPluginData(getDescription()));
//        UUIDFetcher.setFetchManager(new UUIDSpigot());

        Universal.getMessageHandler().registerMessageHandler(new PlaceHolderAPIResponder(this));

//        if (Bukkit.getPluginManager().isPluginEnabled(VaultHandler.VAULT_PLUGIN_NAME) && (!ListUtil.containsString(getDescription().getSoftDepend(), VaultHandler.VAULT_PLUGIN_NAME) && !ListUtil.containsString(getDescription().getDepend(), VaultHandler.VAULT_PLUGIN_NAME))) {
//            Universal.debug("[WARNING/CAN BE IGNORED] Vault has been enabled though not added to soft dependencies or dependencies in plugin.yml. If you want to use VaultHandler which is included in the FernSpigotAPI you must add it to your dependencies/soft dependencies. Dependencies list: " + getDescription().getDepend() + " Soft Dependencies: " + getDescription().getSoftDepend());
//        }

        if(Bukkit.getPluginManager().isPluginEnabled(VaultHandler.VAULT_PLUGIN_NAME) /*&& (ListUtil.containsString(getDescription().getSoftDepend(), VaultHandler.VAULT_PLUGIN_NAME) || ListUtil.containsString(getDescription().getDepend(), VaultHandler.VAULT_PLUGIN_NAME))*/) {
            vaultHandler = new VaultHandler();

            Universal.debug("Initializing Vault API");

            vaultHandler.hook();
        }

        inventoryManager = new InventoryManager(this);
        inventoryManager.init();

        Universal.getMessageHandler().registerMessageHandler(VanishProxyCheck.LISTENER_INSTANCE);
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public void onDisable() {
        Universal.getInstance().onDisable();
        getServer().getScheduler().cancelTasks(this);
    }


}
