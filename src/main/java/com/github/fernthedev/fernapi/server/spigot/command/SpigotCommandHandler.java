package com.github.fernthedev.fernapi.server.spigot.command;

import com.github.fernthedev.fernapi.server.spigot.player.SpigotFConsole;
import com.github.fernthedev.fernapi.universal.api.UniversalCommand;
import com.github.fernthedev.fernapi.universal.handlers.CommandHandler;
import org.bukkit.Bukkit;

public class SpigotCommandHandler extends CommandHandler {
    @Override
    public void registerFernCommand(UniversalCommand ucommand) {
        Bukkit.getPluginCommand(ucommand.getName()).setExecutor((sender, command, label, args) -> {
            ucommand.execute(new SpigotFConsole(sender),args);
            return true;
        });
    }
}
