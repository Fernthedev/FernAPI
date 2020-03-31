package com.github.fernthedev.fernapi.server.sponge.chat;

import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import com.github.fernthedev.fernapi.universal.data.chat.ChatColor;
import com.github.fernthedev.fernapi.universal.data.chat.ClickData;
import com.github.fernthedev.fernapi.universal.data.chat.HoverData;
import com.github.fernthedev.fernapi.universal.handlers.IChatHandler;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.ClickAction;
import org.spongepowered.api.text.action.HoverAction;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.net.MalformedURLException;
import java.net.URL;

public class SpongeChatHandler implements IChatHandler<Text> {


    @Override
    public Text parseComponent(BaseMessage baseMessage) {

        Text.Builder builder = Text.builder();

        builder.append(TextSerializers.FORMATTING_CODE.deserialize(baseMessage.selfPlainText()));

        if (baseMessage.getColor() != null) {
            TextColor color = getTextColor(baseMessage.getColor());
            TextStyle style = getStyle(baseMessage.getColor());
            if(color != null) builder.color(color);
            if(style != null) builder.style(style);
        }

        if(baseMessage.getClickData() != null) {
            builder.onClick(parseAction(baseMessage.getClickData()));
        }

        if(baseMessage.getHoverData() != null) {
            builder.onHover(parseHover(baseMessage.getHoverData()));
        }

        if (baseMessage.getExtra() != null)
            for (BaseMessage extra : baseMessage.getExtra()) {
                builder.append(parseComponent(extra));
            }

        return builder.build();
    }

    private HoverAction<?> parseHover(HoverData hoverData) throws UnsupportedOperationException {
        Text hoverMessage = parseComponent(hoverData.getHoverValue());

        if (hoverData.getAction() == HoverData.Action.SHOW_TEXT) {
            return TextActions.showText(hoverMessage);
        }
        throw new UnsupportedOperationException("The hover action you attempted to use is currently not possible to use with Sponge side, it is recommended to use Sponge-Specific code for this checking for the ServerType from Universal.getMethods()");

//        builder.event(new HoverEvent(
//                        HoverEvent.Action.valueOf(baseMessage.getHoverData().getAction().toString()),
//                        new ComponentBuilder(hoverMessage).create()
//                )
//        );
//
//        String value = TextSerializers.FORMATTING_CODE.deserialize(hoverData.getHoverValue().toLegacyTextAll()).toPlain();
//
//        if (hoverData.getAction() == HoverData.Action.SHOW_TEXT) {
//            return TextActions.showText(Text.builder(value).toText());
//        }
//
//        throw new UnsupportedOperationException("The hover action you attempted to use is currently not possible to use with Sponge side, it is recommended to use Sponge-Specific code for this checking for the ServerType from Universal.getMethods()");
    }

    private static ClickAction<?> parseAction(ClickData clickData) {
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
            default:
                return null;
        }
        return null;
    }

    public static TextColor getTextColor(ChatColor chatColor) {
        switch (chatColor) {
            case RED:
                return TextColors.RED;
            case DARK_RED:
                return TextColors.DARK_RED;
            case GOLD:
                return TextColors.GOLD;
            case YELLOW:
                return TextColors.YELLOW;
            case GREEN:
                return TextColors.GREEN;
            case DARK_GREEN:
                return TextColors.DARK_GREEN;
            case BLUE:
                return TextColors.BLUE;
            case AQUA:
                return TextColors.AQUA;
            case DARK_AQUA:
                return TextColors.DARK_AQUA;
            case DARK_BLUE:
                return TextColors.DARK_BLUE;
            case LIGHT_PURPLE:
                return TextColors.LIGHT_PURPLE;
            case DARK_PURPLE:
                return TextColors.DARK_PURPLE;
            case GRAY:
                return TextColors.GRAY;
            case DARK_GRAY:
                return TextColors.DARK_GRAY;
            case BLACK:
                return TextColors.BLACK;
            case WHITE:
                return TextColors.WHITE;
            case RESET:
                return TextColors.RESET;
            default:
                return null;
        }
    }

    public static TextStyle getStyle(ChatColor chatColor) {
        switch (chatColor) {
            case MAGIC:
                return TextStyles.OBFUSCATED;
            case ITALIC:
                return TextStyles.ITALIC;
            case BOLD:
                return TextStyles.BOLD;
            case STRIKETHROUGH:
                return TextStyles.STRIKETHROUGH;
            case UNDERLINE:
                return TextStyles.UNDERLINE;
            case RESET:
                return TextStyles.RESET;
            default:
                return null;
        }
    }
}
