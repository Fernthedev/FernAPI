package com.github.fernthedev.fernapi.universal.exceptions.database;

public class DatabaseNotConnectedException extends DatabaseException {
    public DatabaseNotConnectedException(String s) {
        super(s);
    }

    public DatabaseNotConnectedException(String s, NullPointerException e) {
        super(s, e);
    }
}
