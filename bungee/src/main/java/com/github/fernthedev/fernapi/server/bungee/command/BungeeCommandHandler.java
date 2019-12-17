package com.github.fernthedev.fernapi.server.bungee.command;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.UniversalCommand;
import com.github.fernthedev.fernapi.universal.handlers.CommandHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;

public class BungeeCommandHandler extends CommandHandler {
    private Plugin plugin;

    public BungeeCommandHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void registerFernCommand(UniversalCommand ucommand) {
        ProxyServer.getInstance().getPluginManager().registerCommand(plugin, new CommandInstance(ucommand));
    }

    private static class CommandInstance extends Command implements TabExecutor {

        private UniversalCommand ucommand;

        /**
         * Construct a new command.
         *
         */
        public CommandInstance(UniversalCommand ucommand) {
            super(ucommand.getName(),ucommand.getPermission(),ucommand.getAliases());
            this.ucommand = ucommand;
        }

        /**
         * Execute this command with the specified sender and arguments.
         *
         * @param sender the executor of this command
         * @param args   arguments used to invoke this command
         */
        @Override
        public void execute(CommandSender sender, String[] args) {
            ucommand.execute(Universal.getMethods().convertCommandSenderToAPISender(sender), args);
        }

        @Override
        public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
            return ucommand.suggest(Universal.getMethods().convertCommandSenderToAPISender(sender), args);
        }
    }
}
