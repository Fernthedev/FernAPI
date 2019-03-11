package com.github.fernthedev.fernapi.server.sponge.command;

import com.github.fernthedev.fernapi.server.sponge.FernSpongeAPI;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.UniversalCommand;
import com.github.fernthedev.fernapi.universal.handlers.CommandHandler;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.*;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class SpongeCommandHandler extends CommandHandler {
    private FernSpongeAPI sponge;
    public SpongeCommandHandler(FernSpongeAPI sponge) {
        this.sponge = sponge;
    }

    @Override
    public void registerFernCommand(UniversalCommand command) {
        CommandManager cmdService = Sponge.getCommandManager();
        cmdService.register(sponge, new CommandHandler() {
            /**
             * Execute the command based on input arguments.
             *
             * <p>The implementing class must perform the necessary permission
             * checks.</p>
             *
             * @param source    The caller of the command
             * @param arguments The raw arguments for this command
             * @return The result of a command being processed
             * @throws CommandException Thrown on a command error
             */
            @Override
            public CommandResult process(CommandSource source, String arguments) throws CommandException {
                command.execute(Universal.getMethods().convertCommandSenderToAPISender(source),arguments.split(" "));
                return CommandResult.success();
            }
        }, command.getAliases());
    }

    private abstract class CommandHandler implements CommandCallable {


        /**
         * Execute the command based on input arguments.
         *
         * <p>The implementing class must perform the necessary permission
         * checks.</p>
         *
         * @param source    The caller of the command
         * @param arguments The raw arguments for this command
         * @return The result of a command being processed
         * @throws CommandException Thrown on a command error
         */
        @Override
        public abstract CommandResult process(CommandSource source, String arguments) throws CommandException;

        /**
         * Gets a list of suggestions based on input.
         *
         * <p>If a suggestion is chosen by the user, it will replace the last
         * word.</p>
         *
         * @param source         The command source
         * @param arguments      The arguments entered up to this point
         * @param targetPosition The position the source is looking at when
         *                       performing tab completion
         * @return A list of suggestions
         * @throws CommandException Thrown if there was a parsing error
         */
        @Override
        public List<String> getSuggestions(CommandSource source, String arguments, @Nullable Location<World> targetPosition) throws CommandException {
            return null;
        }

        /**
         * Test whether this command can probably be executed by the given source.
         *
         * <p>If implementations are unsure if the command can be executed by
         * the source, {@code true} should be returned. Return values of this method
         * may be used to determine whether this command is listed in command
         * listings.</p>
         *
         * @param source The caller of the command
         * @return Whether permission is (probably) granted
         */
        @Override
        public boolean testPermission(CommandSource source) {
            return false;
        }

        /**
         * Gets a short one-line description of this command.
         *
         * <p>The help system may display the description in the command list.</p>
         *
         * @param source The source of the help request
         * @return A description
         */
        @Override
        public Optional<Text> getShortDescription(CommandSource source) {
            return Optional.empty();
        }

        /**
         * Gets a longer formatted help message about this command.
         *
         * <p>It is recommended to use the default text color and style. Sections
         * with text actions (e.g. hyperlinks) should be underlined.</p>
         *
         * <p>Multi-line messages can be created by separating the lines with
         * {@code \n}.</p>
         *
         * <p>The help system may display this message when a source requests
         * detailed information about a command.</p>
         *
         * @param source The source of the help request
         * @return A help text
         */
        @Override
        public Optional<Text> getHelp(CommandSource source) {
            return Optional.empty();
        }

        /**
         * Gets the usage string of this command.
         *
         * <p>A usage string may look like
         * {@code [-w &lt;world&gt;] &lt;var1&gt; &lt;var2&gt;}.</p>
         *
         * <p>The string must not contain the command alias.</p>
         *
         * @param source The source of the help request
         * @return A usage string
         */
        @Override
        public Text getUsage(CommandSource source) {
            return null;
        }
    }
}
