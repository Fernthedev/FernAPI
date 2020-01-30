package com.github.fernthedev.fernapi.server.velocity.command;

import com.github.fernthedev.fernapi.server.velocity.FernVelocityAPI;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.UniversalCommand;
import com.github.fernthedev.fernapi.universal.data.chat.TextMessage;
import com.github.fernthedev.fernapi.universal.handlers.CommandHandler;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class VelocityCommandHandler extends CommandHandler {
    private FernVelocityAPI plugin;

    public VelocityCommandHandler(FernVelocityAPI plugin) {
        this.plugin = plugin;
    }

    @Override
    public void registerFernCommand(UniversalCommand ucommand) {
        plugin.getServer().getCommandManager().register(ucommand.getName(), new Command() {
            /**
             * Executes the command for the specified {@link CommandSource}.
             *
             * @param source the source of this command
             * @param args   the arguments for this command
             */
            @Override
            public void execute(CommandSource source, @NonNull String[] args) {
                if(source.hasPermission(ucommand.getPermission())) {
                    ucommand.execute(Universal.getMethods().convertCommandSenderToAPISender(source), args);
                } else {
                    TextMessage textMessage = new TextMessage(Universal.getLocale().noPermission(ucommand));
                    source.sendMessage((Component) Universal.getChatHandler().parseComponent(textMessage));
                }
            }

            /**
             * Provides tab complete suggestions for a command for a specified {@link CommandSource}.
             *
             * @param source      the source to run the command for
             * @param currentArgs the current, partial arguments for this command
             * @return tab complete suggestions
             */
            @Override
            public List<String> suggest(CommandSource source, @NonNull String[] currentArgs) {
                return ucommand.suggest(Universal.getMethods().convertCommandSenderToAPISender(source), currentArgs);
            }

            /**
             * Tests to check if the {@code source} has permission to use this command with the provided
             * {@code args}.
             *
             * <p>If this method returns false, the handling will be forwarded onto
             * the players current server.</p>
             *
             * @param source the source of the command
             * @param args   the arguments for this command
             * @return whether the source has permission
             */
            @Override
            public boolean hasPermission(CommandSource source, @NonNull String[] args) {
                return source.hasPermission(ucommand.getPermission());
            }
        });
    }
}
