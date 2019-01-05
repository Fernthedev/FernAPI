package com.github.fernthedev.fernapi.server.forge;

import com.github.fernthedev.fernapi.universal.data.chat.ChatMessage;
import com.github.fernthedev.fernapi.universal.handlers.IFPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

public class ForgeFPlayer implements IFPlayer{
    private EntityPlayer player;
    public ForgeFPlayer(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public void sendChatMessage(ChatMessage chatMessage) {
        /*TextComponent prefix = new TextComponent(
                ChatColor.translateAlternateColorCodes('&',chatMessage.getMessage())
        );
        
        if(chatMessage.getClickData() != null)
        prefix.setClickEvent(new ClickEvent(
                ClickEvent.Action.valueOf(chatMessage.getClickData().getClickAction().toString()),
                chatMessage.getClickData().getClickValue()));
        
        if(chatMessage.getHoverData() != null) {

            prefix.setHoverEvent(new HoverEvent(
                    HoverEvent.Action.valueOf(chatMessage.getHoverData().getHoverAction().toString()),
                    message(chatMessage.getHoverData().getHoverValue())));
        }
        
        player.spigot().sendMessage(prefix);*/
        IChatComponent comp = new ChatComponentText(translateAlternateColorCodes('&',chatMessage.getMessage()));
        if(chatMessage.getHoverData() != null || chatMessage.getClickData() != null) {
            ChatStyle style = new ChatStyle();

            if (chatMessage.getClickData() != null) {
                style.setChatClickEvent(new ClickEvent(
                        ClickEvent.Action.valueOf(chatMessage.getClickData().getClickAction().toString()),
                        chatMessage.getClickData().getClickValue()) {
                    @Override
                    public Action getAction() {
                        //custom behavior
                        return ClickEvent.Action.valueOf(chatMessage.getClickData().getClickAction().toString());
                    }
                });
            }

            if(chatMessage.getHoverData() != null) {

                style.setChatHoverEvent(new HoverEvent(
                        HoverEvent.Action.valueOf(chatMessage.getHoverData().getHoverAction().toString()),
                        new ChatComponentText(chatMessage.getHoverData().getHoverValue())) {
                    @Override
                    public Action getAction() {
                        //custom behavior
                        return HoverEvent.Action.valueOf(chatMessage.getHoverData().getHoverAction().toString());
                    }
                });
                
            }


            comp.setChatStyle(style);
        }

        player.addChatComponentMessage(comp);
    }

    /**
     * The special character which prefixes all chat colour codes. Use this if
     * you need to dynamically convert colour codes from your custom format.
     */
    public static final char COLOR_CHAR = '\u00A7';


    /**
     * Translates a string using an alternate color code character into a
     * string that uses the internal ChatColor.COLOR_CODE color code
     * character. The alternate color code character will only be replaced if
     * it is immediately followed by 0-9, A-F, a-f, K-O, k-o, R or r.
     *
     * @param altColorChar The alternate color code character to replace. Ex: {@literal &}
     * @param textToTranslate Text containing the alternate color code character.
     * @return Text containing the ChatColor.COLOR_CODE color code character.
     */
    public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i+1]) > -1) {
                b[i] = COLOR_CHAR;
                b[i+1] = Character.toLowerCase(b[i+1]);
            }
        }
        return new String(b);
    }
}
