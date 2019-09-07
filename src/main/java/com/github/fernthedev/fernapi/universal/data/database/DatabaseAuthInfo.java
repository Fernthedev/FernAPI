package com.github.fernthedev.fernapi.universal.data.database;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DatabaseAuthInfo {
    //DataBase vars.
    @NonNull
    protected String username; //Enter in your db username

    @NonNull
    protected String password; //Enter your password for the db

    @NonNull
    protected String port;

    @NonNull
    protected String urlHost;

    @NonNull
    protected String database;


    public String getUrlToDB() {
        return "jdbc:mysql://%host%:%port%/%database%".replaceAll("%host%", urlHost).replaceAll("%port%", port).replaceAll("%database%", database);
    }


}
