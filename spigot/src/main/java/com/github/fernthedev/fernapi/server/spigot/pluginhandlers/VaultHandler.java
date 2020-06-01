package com.github.fernthedev.fernapi.server.spigot.pluginhandlers;

import com.github.fernthedev.fernapi.server.spigot.FernSpigotAPI;
import lombok.Getter;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;

import static org.bukkit.Bukkit.getServer;


public class VaultHandler {

    public static final String VAULT_PLUGIN_NAME = "Vault";

    @Getter
    private static boolean hooked = false;

    /**
     * This method wil be called on {@link FernSpigotAPI#onEnable()} making it an internal method
     * No need to manually call it.
     */
    public void hook() {
        if (!hooked) {
            if (!setupEconomy()) {
                return;
            }
            setupPermissions();
            setupChat();
            hooked = true;
        } else throw new IllegalStateException("Already hooked to Vault API. This method will be called on the start of plugin initialization");
    }

    @Getter
    private static Economy econ = null;

    @Getter
    private static Permission permissions = null;

    @Getter
    private static Chat chat = null;

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permissions = rsp.getProvider();
        return permissions != null;
    }

}
