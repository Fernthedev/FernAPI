package com.github.fernthedev.fernapi.server.sponge.player;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.api.IFPlayer;
import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import com.github.fernthedev.fernapi.universal.data.network.IServerInfo;
import com.github.fernthedev.fernapi.universal.exceptions.FernRuntimeException;
import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.net.InetSocketAddress;
import java.util.UUID;

public class SpongeFPlayer extends IFPlayer<Player> {

    public SpongeFPlayer(Player player, Audience audience) {
        this(player, player.getName(), player.getUniqueId(), audience);
    }

    public SpongeFPlayer(Player player, @Nullable String name, @Nullable UUID uuid, Audience audience) {
        super(player == null ? name : player.getName(), player == null ? uuid : player.getUniqueId(), player, audience);
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

    @Override
    public void sendMessage(BaseMessage textMessage) {
        Text text = (Text) Universal.getChatHandler().parseComponent(textMessage);

        player.sendMessage(text);
    }

//    private HoverAction parseAction(HoverData hoverData) throws UnsupportedOperationException {
//        String value = TextSerializers.FORMATTING_CODE.deserialize(hoverData.getHoverValue()).toPlain();
//
//        if (hoverData.getAction() == HoverData.Action.SHOW_TEXT) {
//            return TextActions.showText(Text.builder(value).toText());
//        }
//        throw new UnsupportedOperationException("The hover action you attempted to use is currently not possible to use with Sponge side, it is recommended to use Sponge-Specific code for this checking for the ServerType from Universal.getMethods()");
//    }
//
//    private ClickAction parseAction(ClickData clickData) {
//        String value = TextSerializers.FORMATTING_CODE.deserialize(clickData.getClickValue()).toPlain();
//
//        switch (clickData.getAction()) {
//            case OPEN_URL:
//                try {
//                    return TextActions.openUrl(new URL(value));
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
//                break;
//            case CHANGE_PAGE:
//                return TextActions.changePage(Integer.parseInt(value));
//            case RUN_COMMAND:
//                return TextActions.runCommand(value);
//            case SUGGEST_COMMAND:
//                return TextActions.suggestCommand(value);
//        }
//        return null;
//    }

    @Override
    public long getPing() {
        return player.getConnection().getLatency();
    }

    @Override
    public String getCurrentServerName() {
        return Sponge.getServer().getDefaultWorldName();
    }

    @Override
    public IServerInfo getServerInfo() {
        return Universal.getNetworkHandler().toServer(null);
    }

    @Override
    public boolean isVanished() {
        throw new FernRuntimeException("This method does not work on Sponge yet. Use Universal.getMethods().getServerType() to check for Sponge");
    }

    @Override
    public InetSocketAddress getAddress() {
        return player.getConnection().getAddress();
    }
}
