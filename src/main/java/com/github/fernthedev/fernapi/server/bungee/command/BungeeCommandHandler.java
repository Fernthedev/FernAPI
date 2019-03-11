package com.github.fernthedev.fernapi.server.bungee.command;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.UniversalCommand;
import com.github.fernthedev.fernapi.universal.handlers.CommandHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeCommandHandler extends CommandHandler {
    private Plugin plugin;

    public BungeeCommandHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void registerFernCommand(UniversalCommand ucommand) {
        ProxyServer.getInstance().getPluginManager().registerCommand(plugin, new Command(ucommand.getName(),ucommand.getPermission(),ucommand.getAliases()) {
            @Override
            public void execute(CommandSender sender, String[] args) {
                ucommand.execute(Universal.getMethods().convertCommandSenderToAPISender(sender),args);
            }
        });
    }
}
