package com.github.fernthedev.fernapi.server.velocity.player;

import com.github.fernthedev.fernapi.server.velocity.FernVelocityAPI;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.IFConsole;
import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import com.github.fernthedev.fernapi.universal.data.chat.ChatColor;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import net.kyori.text.TextComponent;

public class VelocityFConsole extends IFConsole<ConsoleCommandSource> {

    public VelocityFConsole(ConsoleCommandSource commandSender) {
        super(commandSender);
    }


    /**
     * Checks if this user has the specified permission node.
     *
     * @param permission the node to check
     * @return whether they have this node
     */
    @Override
    public boolean hasPermission(String permission) {
        return commandSender.hasPermission(permission);
    }

    @Override
    public void sendMessage(BaseMessage baseMessage) {
        TextComponent fullMessage = (TextComponent) Universal.getChatHandler().parseComponent(baseMessage);
        FernVelocityAPI api = (FernVelocityAPI) Universal.getMethods().getInstance();
        api.getServer().getConsoleCommandSource().sendMessage(fullMessage);
    }



    private TextComponent message(String text) {
        return TextComponent.of(ChatColor.translateAlternateColorCodes('&',text));
    }
}
