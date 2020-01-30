package com.github.fernthedev.fernapi.server.bungee.chat;

import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import com.github.fernthedev.fernapi.universal.handlers.IChatHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;

public class BungeeChatHandler implements IChatHandler<BaseComponent> {

    @Override
    public BaseComponent parseComponent(BaseMessage baseMessage) {
        BaseComponent builder = new TextComponent(ChatColor.translateAlternateColorCodes('&', baseMessage.toLegacyText()));

        if (baseMessage.getColor() != null) builder.setColor(ChatColor.getByChar(baseMessage.getColor().getCode()));

        if (baseMessage.getClickData() != null) {
            builder.setClickEvent(new ClickEvent(
                    ClickEvent.Action.valueOf(baseMessage.getClickData().getAction().toString()),
                    baseMessage.getClickData().getClickValue()));
        }

        if (baseMessage.getHoverData() != null) {
            BaseComponent hoverMessage = parseComponent(baseMessage);

            builder.setHoverEvent(new HoverEvent(
                    HoverEvent.Action.valueOf(baseMessage.getHoverData().getAction().toString()),
                    new ComponentBuilder(hoverMessage).create()
                    )
            );
        }

        if (baseMessage.getExtra() != null)
        for (BaseMessage extra : baseMessage.getExtra()) {
            builder.addExtra(parseComponent(extra));
        }

//        new RuntimeException(builder.toString(), new RuntimeException(baseMessage.toString())).printStackTrace();

        return builder;
    }
}
