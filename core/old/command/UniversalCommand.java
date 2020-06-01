package com.github.fernthedev.fernapi.universal.api.command;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.chat.ChatColor;
import com.github.fernthedev.fernapi.universal.data.chat.TextMessage;
import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.logging.Logger;

/**
 * TODO: New argument system
 */
@Data
@RequiredArgsConstructor(access = AccessLevel.NONE)
public abstract class UniversalCommand {

    private final String name;

    private final String permission;

    private final String[] aliases;

    protected Map<AbstractArgument<?>, List<AbstractArgument<?>>> arguments = new HashMap<>();
    private int argumentLength;

    protected void addArgument(AbstractArgument<?> argument, AbstractArgument<?>... argumentsArg) {
        addArgumentMap(argument);
        Arrays.stream(argumentsArg).forEach(this::addArgumentMap);
        updateArgumentsLength();
    }

    protected void removeArgument(AbstractArgument<?> argument, AbstractArgument<?>... argumentsArg) {
        arguments.remove(argument);
        Arrays.stream(argumentsArg).forEach(this::removeArgumentMap);
        updateArgumentsLength();
    }

    protected void addArgumentMap(AbstractArgument<?> abstractArgument) {
        if (abstractArgument.isMultiArgument()) {
            arguments.put(abstractArgument, (List<AbstractArgument<?>>) abstractArgument.getPossibleValues());
        } else {
            arguments.put(abstractArgument, null);
        }
    }

    protected void removeArgumentMap(AbstractArgument<?> abstractArgument) {
        arguments.remove(abstractArgument);
    }


    /**
     * Construct a new command with no permissions or aliases.
     *
     * @param name the name of this command
     */
    public UniversalCommand(String name)
    {
        this( name, null);
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

    protected TextMessage message(String text) {
        return new TextMessage(ChatColor.translateAlternateColorCodes('&', text));
    }

    protected Logger getLogger() {
        return Universal.getMethods().getLogger();
    }

    /**
     * Sends a message to the player and converts the color codes to their respective colors
     * @param player The source to send
     * @param message The message
     */
    protected void sendMessage(CommandSender player, String message) {
        player.sendMessage(message(message));
    }

    /**
     * Is used for tab completion. DO NOT RETURN NULL, INSTEAD RETURN AN EMPTY LIST
     *
     * By default it returns a list of all the arguments
     *
     * @param source The command source
     * @param currentArgs The arguments currently provided
     * @return The arguments suggested. CANNOT BE NULL, INSTEAD RETURN AN EMPTY LIST
     */
    @NonNull
    public List<String> suggest(CommandSender source, String[] currentArgs) {
        if (currentArgs.length <= argumentLength) {
            AbstractArgument<?> argument = arguments.get(currentArgs.length - 1);

            if (argument.getPermission() != null && !source.hasPermission(permission)) return Collections.emptyList();

            return argument.getSuggester().getSuggestions(source, )
        }else {
            return Collections.emptyList();
        }
    }

    private int updateArgumentsLength() {
        return argumentLength = arguments.parallelStream().mapToInt(AbstractArgument::getCountOfArgs).sum();
    }


    /**
     * Called when executing the command
     * @param sender The source
     * @param args The arguments provided
     */
    public abstract void execute(CommandSender sender, String[] args);


}
