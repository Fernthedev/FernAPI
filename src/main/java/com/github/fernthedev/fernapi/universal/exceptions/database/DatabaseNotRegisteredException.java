package com.github.fernthedev.fernapi.universal.exceptions.database;


public class DatabaseNotRegisteredException extends DatabaseException {
    public DatabaseNotRegisteredException(String s, NullPointerException e) {
        super(s,e);
    }

    public DatabaseNotRegisteredException(String s) {
        super(s);
    }
}
