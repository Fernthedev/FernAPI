package com.github.fernthedev.fernapi.universal.data.chat;

public class HoverData {
    private String hoverValue;
    private Action action;

    public HoverData(Action action, String hoverValue) {
        this.hoverValue = hoverValue;
        this.action = action;
    }

    public String getHoverValue() {
        return hoverValue;
    }

    public Action getAction() {
        return action;
    }

    public static enum Action {
        SHOW_TEXT,
        SHOW_ACHIEVEMENT,
    }
}

