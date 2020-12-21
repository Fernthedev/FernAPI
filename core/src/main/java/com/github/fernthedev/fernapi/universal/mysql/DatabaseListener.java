package com.github.fernthedev.fernapi.universal.mysql;

import co.aikar.idb.Database;
import co.aikar.idb.DbRow;
import co.aikar.idb.DbStatement;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.database.*;
import com.github.fernthedev.fernapi.universal.exceptions.database.DatabaseException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

public abstract class DatabaseListener {

    @Setter(AccessLevel.PACKAGE)
    protected boolean firstConnect = false;



    @Setter
    @Getter
    private DatabaseAuthInfo databaseAuthInfo;

    @Getter
    @Setter
    protected Database database;

    private final Queue<Runnable> runOnConnectQueue = new LinkedList<>();
    private final Queue<Runnable> runOnConnectAsync = new LinkedList<>();

    protected static void validateNonMainThread() {
        if (Universal.getMethods().isMainThread()) throw new IllegalStateException("Cannot run SQL methods on main thread. Use Universal.getScheduler() and Universal.getMethods().isMainThread()");
    }

    @Nullable
    private static ResultSet sqlRun(@NonNull PreparedStatement stmt, @NonNull String sql) throws SQLException {
        validateNonMainThread();
        if (sql.startsWith("SELECT ")) {
            return stmt.executeQuery();
        } else {
            stmt.executeUpdate();
            return null;
        }
    }

    public boolean isConnected() throws SQLException {
        return !getConnection().isClosed();
    }

    public Connection getConnection() throws SQLException {
        validateNonMainThread();

        return database.getConnection();
    }

    /**
     * This is called after you attempt a connection
     * @see DatabaseListener#connect(DatabaseAuthInfo)
     * @param connected Returns true if successful
     */
    public abstract void onConnectAttempt(boolean connected);

    /**
     * Runs the code in an async thread but in a queue blocking order of runnable list.
     * @param runnable The code to run
     */
    public void runOnConnect(Runnable runnable) {
        if(!firstConnect) {
            runOnConnectQueue.add(runnable);
        } else {
            runnable.run();
        }
    }

    /**
     * Runs the code in an async thread from the other scheduled runnable code.
     * @param runnable The code to run
     */
    public void runOnConnectAsync(Runnable runnable) {
        if (!firstConnect) {
            runOnConnectAsync.add(runnable);
        } else {
            Universal.getScheduler().runAsync(runnable);
        }
    }

    /**
     * Attempts to make a connection to the database
     * @param data The data required for login
     * @see DatabaseListener#onConnectAttempt(boolean) Called after attempted
     */
    public void connect(DatabaseAuthInfo data) {
        Runnable runnable = () -> {

            try {
                firstConnect = database.getConnection() != null;
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (firstConnect) {
                Universal.getScheduler().runAsync(() -> {
                    while(!runOnConnectAsync.isEmpty()) {
                        Universal.getScheduler().runAsync(() -> runOnConnectAsync.remove().run());
                    }
                });

                while(!runOnConnectQueue.isEmpty()) {
                    runOnConnectQueue.remove().run();
                }
            }
        };

        if (Universal.getMethods().isMainThread()) {
            Universal.getScheduler().runAsync(runnable);
        } else {
            runnable.run();
        }


    }

    /**
     * Gets the table and it's rows
     * Note: This won't give you all the information given from the instance it was created from
     * Use this to get data, not to create tables exactly as the first instance.
     * @param name The name of the table
     * @param rowClass The row data template. Used to define table parameters when creating.
     * @return The table
     */
    public <T extends RowData> CompletableFuture<TableInfo<T>> getTable(String name, Class<T> rowClass) throws DatabaseException {
        TableInfo<T> tableInfo = new TableInfo<>(name, rowClass);

        return getTableRows(name, rowClass).thenApply(dbRows -> {

            dbRows.forEach(tableInfo::addRow);

            return tableInfo;
        });
    }

    /**
     * Gets the table and it's rows
     * Note: This won't give you all the information given from the instance it was created from
     * Use this to get data, not to create tables exactly as the first instance.
     * @param rowClass The row data template. Used to define table parameters when creating.
     * @param name The name of the table
     * @return The table
     */
    public <T extends RowData> CompletableFuture<List<DbRow>> getTableRows(String name, Class<T> rowClass) throws DatabaseException {
        return database.getResultsAsync("SELECT * FROM ?;", name);
    }

    /**
     * Removes the row if columnName contains value
     * @param tableInfo The table
     * @param columnName The columnName
     * @param value The value needed to remove from the columnName to be true to remove row
     */
    public <T extends RowData> void removeRowIfColumnContainsValue(TableInfo<T> tableInfo, String columnName, String value) {
        @Language("SQL") String sql = "DELETE FROM ? WHERE ?='?';";



        database.queryAsync(sql).thenAccept(dbStatement -> {
            try (DbStatement statement = dbStatement) {
                statement.execute(tableInfo.getTableName(), columnName, value);
                tableInfo.loadFromDB(this);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

    }

    /**
     * Insert new rows into table
     * @param tableInfo The table
     * @param rowData The row
     */
    public <T extends RowData> void insertIntoTable(TableInfo<T> tableInfo, T rowData) throws DatabaseException {
        @Language("SQL") String sql = "INSERT INTO ? (?) VALUES (?);";

        database.executeUpdateAsync(sql, tableInfo.getTableName(), getColumnName(tableInfo), getColumnValues(rowData)).thenAccept(dbStatement -> {
            tableInfo.addRow(rowData);
        });

        tableInfo.loadFromDB(this);
    }

    /**
     * Updates the row from a table
     * @param tableInfo The table
     * @param newRow The new row replacing the old
     * @param conditionKey What column
     * @param conditionValue What value is required from the column to update
     */
    public <T extends RowData> void updateRow(TableInfo<T> tableInfo, T newRow, String conditionKey, String conditionValue) throws DatabaseException {
        @Language("SQL") String sql = "UPDATE ? SET ? WHERE ?='?';";

        database.executeUpdateAsync(sql, tableInfo.getTableName(), getColumnValues(newRow), conditionKey, conditionValue);

        tableInfo.loadFromDB(this);
    }

    /**
     * Deletes the table
     * @param tableInfo The table info
     */
    public <T extends RowData> void removeTable(TableInfo<T> tableInfo) throws DatabaseException {
        @Language("SQL") String sql = "DROP TABLE IF EXISTS ?;";

        database.executeUpdateAsync(sql, tableInfo.getTableName());
    }

    /**
     * This is just to get column values from row data
     * @param rowData The row data variable
     * @return the values
     */
    public String getColumnValues(RowData rowData) {
        StringBuilder sql = new StringBuilder();
        StringBuilder append = new StringBuilder();

        int index = 0;

        Collection<ColumnData> values = rowData.getDataCopy().values();
        for(ColumnData columnData : values) {
            index++;


            append.append("'")
                    .append(columnData.getValue())
                    .append("'");

            if(index > 0 && index <= values.size() - 1) {
                append.append(',');
            }


            sql.append(append);
        }

        return sql.toString();
    }

    public <T extends RowData> String getColumnName(@NonNull TableInfo<T> tableDataInfo) {
        StringBuilder sql = new StringBuilder();

        StringBuilder append = new StringBuilder();

        if(!tableDataInfo.getRowDataListCopy().isEmpty()) {
            RowDataTemplate<T> rd = tableDataInfo.getRowDataTemplate();
            int index = 0;
            Collection<ColumnData> values = rd.getDataCopy().values();
            for (ColumnData object : values) {
                index++;

                append.append(object.getColumnName());

                if (index > 0 && index <= values.size() - 1) {
                    append.append(',');
                }

                sql.append(append);
            }
        }

        return sql.toString();
    }


    /**
     * Creates the table
     * @param tableDataInfo The table with the data
     */
    public <T extends RowData> void createTable(@NonNull TableInfo<T> tableDataInfo) throws DatabaseException {
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS ? (");

        //CREATE TABLE IF NOT EXISTS fern_nicks(PLAYERUUID varchar(200), NICK varchar(40));
        //CREATE TABLE IF NOT EXISTS test_no(row1 TEXTrow2 TEXT);

        StringBuilder append;

        RowDataTemplate<T> rd = tableDataInfo.getRowDataTemplate();

        Collection<ColumnData> values = rd.getDataCopy().values();

        int time = 0;
        for (ColumnData object : values) {
            time++;

            String type = "varchar(" + object.getLength() + ")";

            if (object.getLength() <= 0) {
                type = "TEXT";
            }

            if (object.isPrimaryKey()) {
                type = "INT PRIMARY KEY";
            }

            append = new StringBuilder(object.getColumnName() + " " + type);

            if (object.isAutoIncrement()) {
                append.append(" AUTO_INCREMENT");
            }

            if (!object.isNullable() && !object.isPrimaryKey()) {
                append.append(" NOT NULL");
            }

            if (time > 0 && time <= values.size() - 1) {
                append.append(',');
            }

            sql.append(append);
        }


        sql.append(");");

        database.executeUpdateAsync(sql.toString(), tableDataInfo.getTableName()).thenRun(() -> {
            try {
                tableDataInfo.loadFromDB(DatabaseListener.this);
            } catch (DatabaseException throwables) {
                throwables.printStackTrace();
            }
        });


    }

    protected Logger getLogger() {
        return Universal.getMethods().getAbstractLogger();
    }

}
