package com.github.fernthedev.fernapi.universal.api;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import com.github.fernthedev.fernapi.universal.data.chat.ChatColor;
import com.github.fernthedev.fernapi.universal.data.chat.TextMessage;
import com.github.fernthedev.fernapi.universal.exceptions.command.ArgumentNotFoundException;
import com.google.common.base.Preconditions;
import lombok.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor(access = AccessLevel.NONE)
public abstract class UniversalCommand {

    private final String name;

    private final String permission;

    private final String[] aliases;

    protected List<Argument> arguments = new ArrayList<>();

    protected void addArgument(Argument argument, Argument... argumentsArg) {
        arguments.add(argument);
        arguments.addAll(Arrays.asList(argumentsArg));
    }

    protected void removeArgument(Argument argument, Argument... argumentsArg) {
        arguments.remove(argument);
        arguments.removeAll(Arrays.asList(argumentsArg));
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

    protected BaseMessage message(String text) {
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
        return searchArguments(source, currentArgs).stream().map(Argument::getName).collect(Collectors.toList());
    }

    /**
     * Allows you to make autocomplete only suggest based off what is written
     * @param arg The argument currently used
     * @param possibilities All of the possibilities
     * @return The auto-complete possibilities
     */
    public static List<String> search(String arg, List<String> possibilities) {
        return possibilities.stream().filter(s -> s.contains(arg) || s.startsWith(arg)).collect(Collectors.toList());
    }

    /**
     * Just an example or a default usage of executing arguments.
     * Use it as a base or just run the code
     * @param sender
     * @param args
     */
    protected void executeArguments(CommandSender sender, String[] args){
        try {
            Argument argument = handleArguments(sender, args[0]);
            argument.getArgumentRunnable().run(sender, Arrays.copyOfRange(args, 1, args.length));
        } catch (ArgumentNotFoundException e) {

            String parentArg = e.getArgumentInfo().getParentArgument() == null ? null : e.getArgumentInfo().getParentArgument().getName();

            sender.sendMessage(message("&cWrong arguments received \""
                            + e.getArgumentInfo().getProvidedArg() + "\" is not valid " + (
                            parentArg != null ? ("for " + parentArg) : ""
                    ) + ". "
                            + e.getArgumentInfo().getPossibleArguments().stream().map(Argument::getName).collect(Collectors.toList()))
            );
        }
    }

    /**
     * A shortcut to {@link #handleArguments(CommandSender, List, String)} for instances
     */
    protected Argument handleArguments(CommandSender sender, String arg) {
        return handleArguments(sender, arguments, arg);
    }

    /**
     * Returns the argument that matches arg
     * You can then call {@link Argument#execute(CommandSender, String[])} to execute the argument though
     * it is recommended you skip the first argument in the array if
     * using the same array to prevent {@link StackOverflowError}
     *
     * @param sender The sender to check
     * @param arguments The valid arguments to use
     * @param arg The arguments provided by the sender
     * @return The argument runnable that follows the specific criteria: {name of argument, permission if any required}
     *
     * @throws ArgumentNotFoundException Thrown when arg is not matched
     * to any arguments in the arguments list.
     */
    protected static Argument handleArguments(CommandSender sender, List<Argument> arguments, String arg) {
        for (Argument argument : arguments) {
            if (argument.name.equalsIgnoreCase(arg) &&
                    (argument.permission == null || sender.hasPermission(argument.permission) /* If there is a permission required, it will check if sender has it */)) {
                return argument;
            }
        }
        throw new ArgumentNotFoundException(new ArgumentNotFoundException.ArgumentInfo(null, arguments, arg), "The argument {" + arg + "} could not be found in the list of legal arguments: " + arguments);
    }

    /**
     * Searches for arguments
     * @param sender
     * @param args
     * @return
     */
    protected List<Argument> searchArguments(CommandSender sender, String[] args) {
        return searchArguments(sender, args, getArguments(), true);
    }

    /**
     *
     * @param sender
     * @param args
     * @param checkArguments The arguments to list if no suggestions are available yet or to get form {@link #handleArguments(CommandSender, List, String)}
     * @param autoComplete Whether the arguments should contain the argument being typed
     * @return
     */
    protected List<Argument> searchArguments(CommandSender sender, String[] args, List<Argument> checkArguments, boolean autoComplete) {
        if (args.length == 0) return new ArrayList<>(); // Return empty suggestions if no arguments are given

        try {
            Argument argument = handleArguments(sender, checkArguments, args[0]);


            // Return all the inner argument names if they exist
            if (argument.getInnerArguments() != null && argument.getInnerArguments().size() > 0) {
                return searchArguments(sender, Arrays.copyOfRange(args, 1, args.length), argument.getInnerArguments(), autoComplete);
            }


            // Return no suggestions if there are none to give
            return new ArrayList<>();
        } catch (ArgumentNotFoundException e) {
            // Return the arguments given since there are no valid ones yet
            List<Argument> filterArguments = checkArguments;

            if (autoComplete) filterArguments = filterArguments.stream().filter(argument ->
                    argument.getName().startsWith(args[0]) || argument.getName().contains(args[0]))
                    .collect(Collectors.toList());

            return filterArguments;
        }
    }

    protected List<String> toStringList(List<Argument> arguments) {
        return arguments.stream().map(Argument::getName).collect(Collectors.toList());
    }

    /**
     * Called when executing the command
     * @param sender The source
     * @param args The arguments provided
     */
    public abstract void execute(CommandSender sender, String[] args);

    @FunctionalInterface
    public interface ArgumentRunnable {
        void run(CommandSender sender, String[] args);
    }

    @Getter
    @ToString
    public static class Argument {

        private final String name;
        private final ArgumentRunnable argumentRunnable;
        private final List<Argument> innerArguments;

        @Setter
        private String permission = null;

        /**
         * Used only if ArgumentRunnable is null
         * Is used to allow usage of argument as categories into other argument runnables
         *
         * Example:
         * - main
         *      - foo
         *      - bar
         *      - thing
         *          - another
         *          - another2
         * - other
         * - misc
         *
         *
         * It can also be used as an example to have a hybrid category/argument style
         *
         * Such as:
         * main -> "foo"
         * main bar -> "bar"
         */
        private ArgumentRunnable defaultArgument(List<Argument> argumentRunnables) {
            return (sender, args) -> {
                String arg = args.length == 0 ? "" : args[0];
                try {
                    String[] argCopy = args.length == 0 ? new String[0] : Arrays.copyOfRange(args, 1, args.length);
                    handleArguments(sender, argumentRunnables, arg).getArgumentRunnable().run(sender, argCopy);
                } catch (ArgumentNotFoundException e) {
                    e.setArgumentInfo(new ArgumentNotFoundException.ArgumentInfo(this, innerArguments, arg));
                    throw e;
                }
            };
        }


        /**
         *
         * @param name The name of the argument
         * @param argumentRunnable The code ran when the argument is used. Null if you want the default behaviour of using innerArguments. {@link #defaultArgument(List)}
         * @param innerArguments Any inner arguments if you wish to use the argument as a category
         */
        public Argument(@NonNull String name, @Nullable ArgumentRunnable argumentRunnable, @Nullable Argument... innerArguments) {
            this.name = name;
            this.innerArguments = Arrays.asList(innerArguments != null ? innerArguments : new Argument[0]);

            if (argumentRunnable == null && innerArguments == null) throw new NullPointerException("Both argumentRunnable and innerArguments cannot be null." +
                    " Provide inner arguments to make a category or provide an argument runnable to run code when argument is called");

            if(argumentRunnable == null) this.argumentRunnable = defaultArgument(this.innerArguments);
            else this.argumentRunnable = argumentRunnable;

        }
    }


}
