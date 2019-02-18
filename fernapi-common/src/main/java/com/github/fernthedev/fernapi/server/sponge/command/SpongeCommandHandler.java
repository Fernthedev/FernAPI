package com.github.fernthedev.fernapi.server.sponge.command;

import com.github.fernthedev.fernapi.server.sponge.FernSpongeAPI;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.UniversalCommand;
import com.github.fernthedev.fernapi.universal.handlers.CommandHandler;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.spec.CommandSpec;

public class SpongeCommandHandler extends CommandHandler {
    private FernSpongeAPI sponge;
    public SpongeCommandHandler(FernSpongeAPI sponge) {
        this.sponge = sponge;
    }

    @Override
    public void registerFernCommand(UniversalCommand command) {
        CommandSpec myCommandSpec = CommandSpec.builder()
                .permission(command.getPermission())
                .executor((src, args) -> {
                    command.execute(Universal.getMethods().convertCommandSenderToAPISender(src),args.);
                    return CommandResult.success();
                })
                .build();

        Sponge.getCommandManager().register(sponge, myCommandSpec,command.getAliases());
    }
}
