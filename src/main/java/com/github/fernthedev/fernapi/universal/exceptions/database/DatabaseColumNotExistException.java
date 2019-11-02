package com.github.fernthedev.fernapi.universal.exceptions.database;

public class DatabaseColumNotExistException extends DatabaseException  {
    public DatabaseColumNotExistException(String s) {
        super(s);
    }

    public DatabaseColumNotExistException(String s, NullPointerException e) {
        super(s, e);
    }
}
