package com.github.fernthedev.fernapi.server.forge.player;

import com.github.fernthedev.fernapi.universal.data.chat.BaseMessage;
import com.github.fernthedev.fernapi.universal.data.chat.ChatColor;
import com.github.fernthedev.fernapi.universal.handlers.IFPlayer;
import lombok.Getter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

public class ForgeFPlayer extends IFPlayer {
    @Getter
    private EntityPlayer player;

    public ForgeFPlayer(EntityPlayer player) {
        super(player.getName(),player.getUniqueID());
        this.player = player;
    }

    @Override
    public void sendChatMessage(BaseMessage bas) {

        /*
                TextComponent fullMessage = new TextComponent();

        for(BaseMessage be : baseMessage.getExtra()) {
            TextComponent te = new TextComponent(ChatColor.translateAlternateColorCodes('&',be.toPlainText()));

            if (be.getClickData() != null) {
                te.setClickEvent(new ClickEvent(
                        ClickEvent.Action.valueOf(be.getClickData().getAction().toString()),
                        be.getClickData().getClickValue()));
            }

            if (be.getHoverData() != null) {
                fullMessage.setHoverEvent(new HoverEvent(
                        HoverEvent.Action.valueOf(be.getHoverData().getAction().toString()),
                        message(be.getHoverData().getHoverValue())));
            }

            fullMessage.addExtra(te);
        }

        player.spigot().sendMessage(fullMessage);
         */
        IChatComponent fullMessage = new ChatComponentText(bas.getParentText());
        for (BaseMessage be : bas.getExtra()) {
            IChatComponent te = new ChatComponentText(ChatColor.translateAlternateColorCodes('&', be.toPlainText()));


            if (be.getHoverData() != null || be.getClickData() != null) {
                ChatStyle style = new ChatStyle();

                if (be.getClickData() != null) {
                    style.setChatClickEvent(new ClickEvent(
                            ClickEvent.Action.valueOf(be.getClickData().getAction().toString()),
                            be.getClickData().getClickValue()) {
                        @Override
                        public Action getAction() {
                            //custom behavior
                            return ClickEvent.Action.valueOf(be.getClickData().getAction().toString());
                        }
                    });
                }

                if (be.getHoverData() != null) {
                    style.setChatHoverEvent(new HoverEvent(
                            HoverEvent.Action.valueOf(be.getHoverData().getAction().toString()),
                            new ChatComponentText(be.getHoverData().getHoverValue())) {
                        @Override
                        public Action getAction() {
                            //custom behavior
                            return HoverEvent.Action.valueOf(be.getHoverData().getAction().toString());
                        }
                    });

                }

                te.setChatStyle(style);
                fullMessage.appendSibling(te);
            }
        }
        player.addChatComponentMessage(fullMessage);
    }
}
