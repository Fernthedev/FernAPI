package com.github.fernthedev.fernapi.universal.data.chat;

public class ChatMessage {

    private String message;
    private ClickData clickData;
    private HoverData hoverData;


    public ChatMessage(String message) {
        this.message = message;
    }

    public ClickData getClickData() {
        return clickData;
    }

    public void setClickData(ClickData clickData) {
        this.clickData = clickData;
    }

    public HoverData getHoverData() {
        return hoverData;
    }

    public void setHoverData(HoverData hoverData) {
        this.hoverData = hoverData;
    }

    public String getMessage() {
        return message;
    }

    public static class ClickData {
        private String clickValue;
        private ClickAction clickAction;

        public ClickData(ClickAction clickAction,String clickValue) {
            this.clickValue = clickValue;
            this.clickAction = clickAction;
        }

        public String getClickValue() {
            return clickValue;
        }

        public ClickAction getClickAction() {
            return clickAction;
        }
    }

    public static class HoverData {
        private String hoverValue;
        private HoverAction hoverAction;

        public HoverData(HoverAction hoverAction,String hoverValue) {
            this.hoverValue = hoverValue;
            this.hoverAction = hoverAction;
        }

        public String getHoverValue() {
            return hoverValue;
        }

        public HoverAction getHoverAction() {
            return hoverAction;
        }
    }

}
