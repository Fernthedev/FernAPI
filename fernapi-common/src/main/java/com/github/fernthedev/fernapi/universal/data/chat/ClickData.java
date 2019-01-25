package com.github.fernthedev.fernapi.universal.data.chat;

public class ClickData {
    private String clickValue;
    private Action clickAction;

    public ClickData(Action clickAction,String clickValue) {
        this.clickValue = clickValue;
        this.clickAction = clickAction;
    }

    public String getClickValue() {
        return clickValue;
    }

    public Action getAction() {
        return clickAction;
    }

    public enum Action {
        /**
         * Open a url at the path given
         */
        OPEN_URL,
        /**
         * Open a file at the path given
         */
        OPEN_FILE,
        /**
         * Run the command given
         */
        RUN_COMMAND,
        /**
         * Inserts the string given into the players
         * text box
         */
        SUGGEST_COMMAND,
        /**
         * Change to the page number given in a book
         */
        CHANGE_PAGE
    }
}
