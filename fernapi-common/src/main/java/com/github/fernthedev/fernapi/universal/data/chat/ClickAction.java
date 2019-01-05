package com.github.fernthedev.fernapi.universal.data.chat;

public enum  ClickAction {
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
