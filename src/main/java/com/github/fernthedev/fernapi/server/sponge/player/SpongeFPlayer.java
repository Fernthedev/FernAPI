package com.github.fernthedev.fernapi.server.sponge.player;

import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import com.github.fernthedev.fernapi.universal.data.chat.ClickData;
import com.github.fernthedev.fernapi.universal.data.chat.HoverData;
import com.github.fernthedev.fernapi.universal.handlers.IFPlayer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.ClickAction;
import org.spongepowered.api.text.action.HoverAction;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

public class SpongeFPlayer extends IFPlayer {
    private Player player;

    public SpongeFPlayer(Player player) {
        this.player = player;
    }

    /**
     * Checks if this user has the specified permission node.
     *
     * @param permission the node to check
     * @return whether they have this node
     */
    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    /**
     * Set a permission node for this user.
     *
     * @param permission the node to set
     * @param value      the value of the node
     */
    @Override
    public void setPermission(String permission, boolean value) {
        return;
    }

    /**
     * Get all Permissions which this CommandSender has
     *
     * @return a unmodifiable Collection of Strings which represent their
     * permissions
     */
    @Override
    public Collection<String> getPermissions() {
        return null;
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

        player.sendMessage(text.build());
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

    @Override
    public long getPing() {
        return player.getConnection().getLatency();
    }

    @Override
    public String getCurrentServerName() {
        return Sponge.getServer().getDefaultWorldName();
    }

    @Override
    public InetSocketAddress getAddress() {
        return player.getConnection().getAddress();
    }
}
