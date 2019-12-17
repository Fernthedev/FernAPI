package com.github.fernthedev.fernapi.universal.api;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import com.github.fernthedev.fernapi.universal.data.chat.ChatColor;
import com.github.fernthedev.fernapi.universal.data.chat.TextMessage;
import com.google.common.base.Preconditions;
import lombok.*;

import javax.annotation.Nullable;
import java.security.PrivilegedActionException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

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

    protected void removeArgument(Argument argument) {
        arguments.remove(argument);
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
        return new TextMessage(ChatColor.translateAlternateColorCodes('&',text));
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
     * @param source The command source
     * @param currentArgs The arguments currently provided
     * @return The arguments suggested. CANNOT BE NULL, INSTEAD RETURN AN EMPTY LIST
     */
    public List<String> suggest(CommandSender source, String[] currentArgs) {
        return new ArrayList<>();
    }

    /**
     * Allows you to make autocomplete only suggest based off what is written
     * @param arg The argument currently used
     * @param possibilities All of the possibilities
     * @return The auto-complete possibilities
     */
    public static List<String> search(String arg, List<String> possibilities) {
        List<String> newPos = new ArrayList<>();
        possibilities.forEach(s -> {
            if(s.startsWith(arg) || s.contains(arg)) {
                newPos.add(s);
            }
        });
        return newPos;
    }

    /**
     * A shortcut to {@link #handleArguments(CommandSender, List, String[])} for instances
     */
    protected ArgumentRunnable handleArguments(CommandSender sender, String[] args) {
        return handleArguments(sender, arguments, args);
    }

    /**
     *
     * @param sender The sender to check
     * @param arguments The valid arguments to use
     * @param args The arguments provided by the sender
     * @return The argument runnable that follows the specific criteria: {name of argument, permission if any required}
     *
     * @throws ArgumentNotFoundException Thrown when the argument from args in index 0 is not matched
     * to any arguments in the arguments list.
     */
    protected static ArgumentRunnable handleArguments(CommandSender sender, List<Argument> arguments, String[] args) {
        for (Argument argument : arguments) {
            if (argument.name.equalsIgnoreCase(args[0]) &&
                    (argument.permission == null || sender.hasPermission(argument.permission) /* If there is a permission required, it will check if sender has it */)) {
                return argument.getArgumentRunnable();
            }
        }
        throw new ArgumentNotFoundException("The argument {" + args[0] + "} could not be found in the list of legal arguments: " + arguments);
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
        private static ArgumentRunnable defaultArgument(List<Argument> argumentRunnables) {
            return (sender, args) -> {
                String[] argsCopy = new String[args.length - 1];

                System.arraycopy(args, 1, argsCopy, 0, args.length - 1);
                handleArguments(sender, argumentRunnables, args).run(sender, argsCopy);
            };
        }


        /**
         *
         * @param name The name of the argument
         * @param argumentRunnable The code ran when the argument is used
         * @param innerArguments Any inner arguments if you wish to use the argument as a category
         */
        public Argument(@NonNull String name, @Nullable ArgumentRunnable argumentRunnable, @Nullable Argument... innerArguments) {
            this.name = name;
            this.innerArguments = Arrays.asList(innerArguments);

            if(argumentRunnable == null) this.argumentRunnable = defaultArgument(this.innerArguments);
            else this.argumentRunnable = argumentRunnable;

        }
    }

    public static class ArgumentNotFoundException extends IllegalArgumentException {
        /**
         * Constructs an <code>IllegalArgumentException</code> with no
         * detail message.
         */
        public ArgumentNotFoundException() {
            super();
        }

        /**
         * Constructs an <code>IllegalArgumentException</code> with the
         * specified detail message.
         *
         * @param s the detail message.
         */
        public ArgumentNotFoundException(String s) {
            super(s);
        }

        /**
         * Constructs a new exception with the specified detail message and
         * cause.
         *
         * <p>Note that the detail message associated with <code>cause</code> is
         * <i>not</i> automatically incorporated in this exception's detail
         * message.
         *
         * @param message the detail message (which is saved for later retrieval
         *                by the {@link Throwable#getMessage()} method).
         * @param cause   the cause (which is saved for later retrieval by the
         *                {@link Throwable#getCause()} method).  (A {@code null} value
         *                is permitted, and indicates that the cause is nonexistent or
         *                unknown.)
         * @since 1.5
         */
        public ArgumentNotFoundException(String message, Throwable cause) {
            super(message, cause);
        }

        /**
         * Constructs a new exception with the specified cause and a detail
         * message of {@code (cause==null ? null : cause.toString())} (which
         * typically contains the class and detail message of {@code cause}).
         * This constructor is useful for exceptions that are little more than
         * wrappers for other throwables (for example, {@link
         * PrivilegedActionException}).
         *
         * @param cause the cause (which is saved for later retrieval by the
         *              {@link Throwable#getCause()} method).  (A {@code null} value is
         *              permitted, and indicates that the cause is nonexistent or
         *              unknown.)
         * @since 1.5
         */
        public ArgumentNotFoundException(Throwable cause) {
            super(cause);
        }
    }
}
