package com.github.fernthedev.fernapi.server.velocity.chat;

import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import com.github.fernthedev.fernapi.universal.data.chat.ChatColor;
import com.github.fernthedev.fernapi.universal.data.chat.ClickData;
import com.github.fernthedev.fernapi.universal.data.chat.HoverData;
import com.github.fernthedev.fernapi.universal.handlers.IChatHandler;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.format.TextDecoration;

public class VelocityChatHandler implements IChatHandler<TextComponent> {
    @Override
    public TextComponent parseComponent(BaseMessage baseMessage) {
        TextComponent builder = TextComponent.of(ChatColor.translateAlternateColorCodes('&', baseMessage.toLegacyText()));

        if (baseMessage.getColor() != null) {
            builder.color(getTextColor(baseMessage.getColor()));
        }

        if (baseMessage.getColor() != null) {
            TextColor color = getTextColor(baseMessage.getColor());
            TextDecoration style = getStyle(baseMessage.getColor());
            if(color != null) builder.color(color);
            if(style != null) builder.decoration(style);
        }

        if (baseMessage.getClickData() != null) {
            builder.clickEvent(parseAction(baseMessage.getClickData()));
        }

        if (baseMessage.getHoverData() != null) {
            builder.hoverEvent(parseHover(baseMessage.getHoverData()));
        }

        if (baseMessage.getExtra() != null)
            for (BaseMessage extra : baseMessage.getExtra()) {
                builder.append(parseComponent(extra));
            }

        return builder;
    }

    private HoverEvent parseHover(HoverData hoverData) throws UnsupportedOperationException {
        TextComponent hoverMessage = parseComponent(hoverData.getHoverValue());

        if (hoverData.getAction() == HoverData.Action.SHOW_TEXT) {
            return HoverEvent.showText(hoverMessage);
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

    private static ClickEvent parseAction(ClickData clickData) {
        String value = clickData.getClickValue();
        switch (clickData.getAction()) {
            case OPEN_URL:
                return ClickEvent.openUrl(value);
            case CHANGE_PAGE:
                return ClickEvent.changePage(Integer.parseInt(value));
            case RUN_COMMAND:
                return ClickEvent.runCommand(value);
            case SUGGEST_COMMAND:
                return ClickEvent.suggestCommand(value);
            default:
                return null;
        }
    }

    public static TextColor getTextColor(ChatColor chatColor) {
        switch (chatColor) {
            case RED:
                return TextColor.RED;
            case DARK_RED:
                return TextColor.DARK_RED;
            case GOLD:
                return TextColor.GOLD;
            case YELLOW:
                return TextColor.YELLOW;
            case GREEN:
                return TextColor.GREEN;
            case DARK_GREEN:
                return TextColor.DARK_GREEN;
            case BLUE:
                return TextColor.BLUE;
            case AQUA:
                return TextColor.AQUA;
            case DARK_AQUA:
                return TextColor.DARK_AQUA;
            case DARK_BLUE:
                return TextColor.DARK_BLUE;
            case LIGHT_PURPLE:
                return TextColor.LIGHT_PURPLE;
            case DARK_PURPLE:
                return TextColor.DARK_PURPLE;
            case GRAY:
                return TextColor.GRAY;
            case DARK_GRAY:
                return TextColor.DARK_GRAY;
            case BLACK:
                return TextColor.BLACK;
            case WHITE:
                return TextColor.WHITE;
            default:
                return null;
        }
    }

    public static TextDecoration getStyle(ChatColor chatColor) {
        switch (chatColor) {
            case MAGIC:
                return TextDecoration.OBFUSCATED;
            case ITALIC:
                return TextDecoration.ITALIC;
            case BOLD:
                return TextDecoration.BOLD;
            case STRIKETHROUGH:
                return TextDecoration.STRIKETHROUGH;
            case UNDERLINE:
                return TextDecoration.UNDERLINED;
            default:
                return null;
        }
    }
}
