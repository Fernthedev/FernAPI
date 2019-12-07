package com.github.fernthedev.fernapi.server.sponge.player;

import com.github.fernthedev.fernapi.universal.api.IFConsole;
import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import com.github.fernthedev.fernapi.universal.data.chat.ClickData;
import com.github.fernthedev.fernapi.universal.data.chat.HoverData;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.ClickAction;
import org.spongepowered.api.text.action.HoverAction;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.net.MalformedURLException;
import java.net.URL;

public class SpongeFConsole extends IFConsole {
    private CommandSource src;

    public SpongeFConsole(CommandSource src) {
        this.src = src;
    }

    /**
     * Checks if this user has the specified permission node.
     *
     * @param permission the node to check
     * @return whether they have this node
     */
    @Override
    public boolean hasPermission(String permission) {
        return src.hasPermission(permission);
    }

    @Override
    public void sendMessage(BaseMessage textMessage) {

        Text.Builder text = Text.builder();

        if (textMessage.getExtra() != null) {
            for(BaseMessage be : textMessage.getExtra()) {
                Text.Builder te = TextSerializers.FORMATTING_CODE.deserialize(be.toPlainText()).toBuilder();

                if(be.getClickData() != null) {
                    te.onClick(parseAction(be.getClickData()));
                }

                if(be.getHoverData() != null) {
                    te.onHover(parseAction(be.getHoverData()));
                }
                text.append(te.build());
            }
        }

        src.sendMessage(text.build());
    }

    private HoverAction parseAction(HoverData hoverData) throws UnsupportedOperationException {
        String value = TextSerializers.FORMATTING_CODE.deserialize(hoverData.getHoverValue()).toPlain();

        if (hoverData.getAction() == HoverData.Action.SHOW_TEXT) {
            return TextActions.showText(Text.builder(value).toText());
        }
        throw new UnsupportedOperationException("The hover action you attempted to use is currently not possible to use with Sponge side, it is recommended to use Sponge-Specific code for this checking for the ServerType from Universal.getMethods()");
    }

    private ClickAction parseAction(ClickData clickData) {
        String value = TextSerializers.FORMATTING_CODE.deserialize(clickData.getClickValue()).toPlain();

        switch (clickData.getAction()) {
            case OPEN_URL:
                try {
                    return TextActions.openUrl(new URL(value));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
            case CHANGE_PAGE:
                return TextActions.changePage(Integer.parseInt(value));
            case RUN_COMMAND:
                return TextActions.runCommand(value);
            case SUGGEST_COMMAND:
                return TextActions.suggestCommand(value);
        }
        return null;
    }

}
