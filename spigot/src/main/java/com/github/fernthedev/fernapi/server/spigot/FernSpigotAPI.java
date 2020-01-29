package com.github.fernthedev.fernapi.server.spigot;

import com.github.fernthedev.fernapi.server.spigot.chat.SpigotChatHandler;
import com.github.fernthedev.fernapi.server.spigot.command.SpigotCommandHandler;
import com.github.fernthedev.fernapi.server.spigot.database.SpigotDatabase;
import com.github.fernthedev.fernapi.server.spigot.network.PlaceHolderAPI.PlaceHolderAPIResponder;
import com.github.fernthedev.fernapi.server.spigot.network.SpigotMessageHandler;
import com.github.fernthedev.fernapi.server.spigot.network.SpigotNetworkHandler;
import com.github.fernthedev.fernapi.server.spigot.pluginhandlers.VaultHandler;
import com.github.fernthedev.fernapi.server.spigot.scheduler.SpigotScheduler;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.util.ListUtil;
import com.github.fernthedev.fernapi.universal.util.network.vanish.VanishProxyCheck;
import com.github.fernthedev.fernapi.universal.handlers.FernAPIPlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class FernSpigotAPI extends JavaPlugin implements FernAPIPlugin {

    @Getter
    private VaultHandler vaultHandler;

    @Override
    public void onEnable() {
        getLogger().info("Registered FernAPI Spigot");
        Universal.getInstance().setup(new SpigotInterface(this),
                this,
                new SpigotChatHandler(),
                new SpigotMessageHandler(this),
                new SpigotDatabase(this),new SpigotCommandHandler(),
                new SpigotNetworkHandler(),
                new SpigotScheduler(this));
//        UUIDFetcher.setFetchManager(new UUIDSpigot());

        Universal.getMessageHandler().registerMessageHandler(new PlaceHolderAPIResponder(this));

        if (Bukkit.getPluginManager().isPluginEnabled(VaultHandler.VAULT_PLUGIN_NAME) && (!ListUtil.containsString(getDescription().getSoftDepend(), VaultHandler.VAULT_PLUGIN_NAME) && !ListUtil.containsString(getDescription().getDepend(), VaultHandler.VAULT_PLUGIN_NAME))) {
            Universal.debug("[WARNING/CAN BE IGNORED] Vault is enabled though not added to soft dependencies or dependencies in plugin.yml. If you want to use VaultHandler which is included in the FernSpigotAPI you must add it to your dependencies/soft dependencies.");
        }

        if(Bukkit.getPluginManager().isPluginEnabled(VaultHandler.VAULT_PLUGIN_NAME) && (ListUtil.containsString(getDescription().getSoftDepend(), VaultHandler.VAULT_PLUGIN_NAME) || ListUtil.containsString(getDescription().getDepend(), VaultHandler.VAULT_PLUGIN_NAME))) {
            vaultHandler = new VaultHandler();

            vaultHandler.hook();
        }

        Universal.getMessageHandler().registerMessageHandler(new VanishProxyCheck(this));
    }

    @Override
    public void onDisable() {
        Universal.getInstance().onDisable();
        getServer().getScheduler().cancelTasks(this);
    }


}
