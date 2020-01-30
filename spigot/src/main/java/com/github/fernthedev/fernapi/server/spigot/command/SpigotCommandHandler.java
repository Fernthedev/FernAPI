package com.github.fernthedev.fernapi.server.spigot.command;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.UniversalCommand;
import com.github.fernthedev.fernapi.universal.exceptions.command.PluginYMLMustDefineCommand;
import com.github.fernthedev.fernapi.universal.handlers.CommandHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public class SpigotCommandHandler extends CommandHandler {
    @Override
    public void registerFernCommand(UniversalCommand ucommand) {
        if(Bukkit.getPluginCommand(ucommand.getName()) == null) throw new PluginYMLMustDefineCommand("Null exception is usually caused by plugin.yml not defining the command which in this case is " + ucommand.getName() + ". You must have it defined in your plugin.yml for it to work.");


        Bukkit.getPluginCommand(ucommand.getName()).setExecutor(new CommandInstance(ucommand));
    }


    private static class CommandInstance implements CommandExecutor, TabExecutor {
        private UniversalCommand ucommand;

        public CommandInstance(UniversalCommand ucommand) {
            this.ucommand = ucommand;
        }

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if(sender.hasPermission(ucommand.getPermission())) {
                ucommand.execute(Universal.getMethods().convertCommandSenderToAPISender(sender), args);
            } else {
                sender.sendMessage(
                        ChatColor.translateAlternateColorCodes('&', Universal.getLocale().noPermission(ucommand)
                        ));
            }
            return true;
        }

        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
            return ucommand.suggest(Universal.getMethods().convertCommandSenderToAPISender(sender), args);
        }
    }
}
