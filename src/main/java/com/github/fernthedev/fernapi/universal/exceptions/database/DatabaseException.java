package com.github.fernthedev.fernapi.universal.exceptions.database;

import java.sql.SQLException;

public class DatabaseException extends SQLException {
    public DatabaseException(String s) {
        super(s);
    }

    public DatabaseException(String s, NullPointerException e) {
        super(s,e);
    }
}
