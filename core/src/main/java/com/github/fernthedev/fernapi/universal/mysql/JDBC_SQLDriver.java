package com.github.fernthedev.fernapi.universal.mysql;

import lombok.*;

@Setter
@ToString
public class JDBC_SQLDriver extends AbstractSQLDriver{

    public JDBC_SQLDriver(@NonNull String sqlIdentifierName, @NonNull String jdbcUrl, @NonNull String sqlDriverClassName) {
        super(sqlIdentifierName, sqlDriverClassName, jdbcUrl);
    }

    public static final JDBC_SQLDriver MYSQL_DRIVER = new JDBC_SQLDriver("mysql", "jdbc:%sql%://%host%:%port%/%database%","com.mysql.jdbc.Driver");
    public static final JDBC_SQLDriver MARIADB_DRIVER = new JDBC_SQLDriver("mariadb","jdbc:%sql%://%host%:%port%/%database%", org.mariadb.jdbc.Driver.class.getName());
}


