package com.github.fernthedev.fernapi.universal.api;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import com.github.fernthedev.fernapi.universal.data.chat.ChatColor;
import com.github.fernthedev.fernapi.universal.data.chat.TextMessage;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.logging.Logger;
@Data
@RequiredArgsConstructor(access = AccessLevel.NONE)
public abstract class UniversalCommand {

    private final String name;

    private final String permission;

    private final String[] aliases;

    /**
     * Construct a new command with no permissions or aliases.
     *
     * @param name the name of this command
     */
    public UniversalCommand(String name)
    {
        this( name, null );
    }

    /**
     * Construct a new command.
     *
     * @param name primary name of this command
     * @param permission the permission node required to execute this command,
     * null or empty string allows it to be executed by everyone
     * @param aliases aliases which map back to this command
     */
    public UniversalCommand(String name, String permission, String... aliases) {
        Preconditions.checkArgument( name != null, "name" );
        this.name = name;
        this.permission = permission;
        this.aliases = aliases;
    }

    protected BaseMessage message(String text) {
        return new TextMessage(ChatColor.translateAlternateColorCodes('&',text));
    }

    protected Logger getLogger() {
        return Universal.getMethods().getLogger();
    }

    protected void sendMessage(CommandSender player, String message) {
        player.sendMessage(message(message));
    }

    public abstract void execute(CommandSender sender, String[] args);
}
