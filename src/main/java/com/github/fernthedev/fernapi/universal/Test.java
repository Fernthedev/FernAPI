package com.github.fernthedev.fernapi.universal;

import com.github.fernthedev.fernapi.universal.mysql.DatabaseManager;

public class Test extends DatabaseManager {
    /**
     * This is called after you attempt a connection
     *
     * @param connected
     */
    @Override
    public void onConnectAttempt(boolean connected) {

    }
}
