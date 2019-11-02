package com.github.fernthedev.fernapi.server.velocity.player;

import com.github.fernthedev.fernapi.server.velocity.FernVelocityAPI;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.IFConsole;
import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import com.github.fernthedev.fernapi.universal.data.chat.ChatColor;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;

public class VelocityFConsole extends IFConsole {
    private CommandSource commandSender;

    public VelocityFConsole(CommandSource commandSender) {
        this.commandSender =commandSender;
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

        TextComponent fullMessage = TextComponent.of(ChatColor.translateAlternateColorCodes('&',baseMessage.getParentText()));

        if (baseMessage.getExtra() != null) {
            for(BaseMessage be : baseMessage.getExtra()) {
                TextComponent te = TextComponent.of(ChatColor.translateAlternateColorCodes('&',be.toPlainText()));

                if (be.getClickData() != null) {
                    te.clickEvent(ClickEvent.of(
                            ClickEvent.Action.valueOf(be.getClickData().getAction().toString()),
                            be.getClickData().getClickValue()));
                }

                if (be.getHoverData() != null) {
                    te.hoverEvent(HoverEvent.of(
                            HoverEvent.Action.valueOf(be.getHoverData().getAction().toString()),
                            message(be.getHoverData().getHoverValue())));
                }

                fullMessage.append(te);
            }
        }
        FernVelocityAPI api = (FernVelocityAPI) Universal.getMethods().getInstance();
        api.getServer().getConsoleCommandSource().sendMessage(fullMessage);
    }

    private TextComponent message(String text) {
        return TextComponent.of(ChatColor.translateAlternateColorCodes('&',text));
    }
}
