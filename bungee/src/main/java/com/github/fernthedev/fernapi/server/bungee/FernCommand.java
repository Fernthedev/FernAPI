package com.github.fernthedev.fernapi.server.bungee;

import com.github.fernthedev.fernapi.universal.Universal;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class FernCommand extends Command {
    /**
     * Construct a new command with no permissions or aliases.
     *
     * @param name the name of this command
     */
    public FernCommand(String name) {
        super(name);
    }

    /**
     * Construct a new command.
     *
     * @param name       primary name of this command
     * @param permission the permission node required to execute this command,
     *                   null or empty string allows it to be executed by everyone
     * @param aliases    aliases which map back to this command
     */
    public FernCommand(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    protected BaseComponent message(String text) {
        return new TextComponent(ChatColor.translateAlternateColorCodes('&',text));
    }

    protected Logger logger() {
        return Universal.getMethods().getAbstractLogger();
    }

    protected void sendMessage(CommandSender player, String message) {
        player.sendMessage(message(message));
    }


    /**
     * Allows you to make autocomplete only suggest based off what is written
     * @param arg The argument currently used
     * @param possibilities All of the possibilities
     * @return The auto-complete possibilities
     */
    public List<String> search(String arg, List<String> possibilities) {
        List<String> newPos = new ArrayList<>();
        possibilities.forEach(s -> {
            if(s.startsWith(arg) || s.contains(arg)) {
                newPos.add(s);
            }
        });
        return newPos;
    }

    @Override
    public abstract void execute(CommandSender sender, String[] args);
}
