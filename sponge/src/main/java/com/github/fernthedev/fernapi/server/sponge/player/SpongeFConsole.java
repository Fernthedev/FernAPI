package com.github.fernthedev.fernapi.server.sponge.player;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.IFConsole;
import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.text.Text;

public class SpongeFConsole extends IFConsole<ConsoleSource> {


    public SpongeFConsole(ConsoleSource commandSender) {
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
    public void sendMessage(BaseMessage textMessage) {

//        Text.Builder text = Text.builder();
//
//        if (textMessage.getExtra() != null) {
//            for(BaseMessage be : textMessage.getExtra()) {
//                Text.Builder te = TextSerializers.FORMATTING_CODE.deserialize(be.toPlainText()).toBuilder();
//
//                if(be.getClickData() != null) {
//                    te.onClick(parseAction(be.getClickData()));
//                }
//
//                if(be.getHoverData() != null) {
//                    te.onHover(parseAction(be.getHoverData()));
//                }
//                text.append(te.build());
//            }
//        }

        Text text = (Text) Universal.getChatHandler().parseComponent(textMessage);

        commandSender.sendMessage(text);
    }



}
