package com.github.fernthedev.fernapi.universal.data.chat;

public class HoverData {
    private BaseMessage hoverValue;
    private Action action;

    public HoverData(Action action, BaseMessage hoverValue) {
        this.hoverValue = hoverValue;
        this.action = action;

    }

    public BaseMessage getHoverValue() {
        return hoverValue;
    }

    public Action getAction() {
        return action;
    }

    public enum Action {
        SHOW_TEXT,
        SHOW_ACHIEVEMENT,
    }
}

