package com.github.fernthedev.fernapi.server.spigot.pluginhandlers;

import lombok.Getter;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;

import static org.bukkit.Bukkit.getServer;


public class VaultHandler {

    @Getter
    private static boolean hooked = false;

    public void hook() {
        if (!setupEconomy() ) {
            return;
        }
        setupPermissions();
        setupChat();
        hooked = true;
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
