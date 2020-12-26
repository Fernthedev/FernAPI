package com.github.fernthedev.fernapi.universal.mysql;

import co.aikar.idb.Database;
import co.aikar.idb.DbRow;
import co.aikar.idb.DbStatement;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.chat.ChatColor;
import com.github.fernthedev.fernapi.universal.data.database.*;
import com.github.fernthedev.fernapi.universal.exceptions.database.DatabaseException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.intellij.lang.annotations.Language;
import org.panteleyev.mysqlapi.MySqlProxy;
import org.panteleyev.mysqlapi.annotations.Column;
import org.panteleyev.mysqlapi.annotations.ForeignKey;
import org.panteleyev.mysqlapi.annotations.Index;
import org.panteleyev.mysqlapi.annotations.PrimaryKey;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.panteleyev.mysqlapi.DataTypes.TYPE_ENUM;

public abstract class DatabaseListener {

    @Setter(AccessLevel.PACKAGE)
    protected boolean firstConnect = false;

    private static final MySqlProxy mysqlProxy = new MySqlProxy();

    @Setter
    @Getter
    private DatabaseAuthInfo databaseAuthInfo;

    @Getter
    @Setter
    @NonNull
    protected Database database;

    public DatabaseListener(@NonNull Database database) {
        this.database = database;
    }

    private final Queue<Runnable> runOnConnectQueue = new LinkedList<>();
    private final Queue<Runnable> runOnConnectAsync = new LinkedList<>();

    protected static void validateNonMainThread() {
        if (Universal.getMethods().isMainThread()) throw new IllegalStateException("Cannot run SQL methods on main thread. Use Universal.getScheduler() and Universal.getMethods().isMainThread()");
    }

    public boolean isConnected() throws SQLException {
        try (Connection connection = getConnection()) {
            return !connection.isClosed();
        }
    }

    public Connection getConnection() throws SQLException {
        validateNonMainThread();

        return database.getConnection();
    }

    /**
     * This is called after you attempt a connection
     * @see #connect()
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
     * @see DatabaseListener#onConnectAttempt(boolean) Called after attempted
     */
    public void connect() {
        Runnable runnable = () -> {

            try (Connection connection = database.getConnection()) {
                firstConnect = connection != null;

                if (firstConnect) {
                    Universal.getScheduler().runAsync(() -> {
                        while (!runOnConnectAsync.isEmpty()) {
                            Universal.getScheduler().runAsync(() -> runOnConnectAsync.remove().run());
                        }
                    });

                    while (!runOnConnectQueue.isEmpty()) {
                        runOnConnectQueue.remove().run();
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
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
     * @return The table
     */
    public CompletableFuture<List<DbRow>> getTableRows(String name) {
        @Language("SQL") String sql = "SELECT * FROM " + name + ";";
        Universal.debug(ChatColor.GREEN + "Executing {}", sql);

        return database.getResultsAsync(sql);
    }

    /**
     * Removes the row if columnName contains value
     * @param tableInfo The table
     * @param columnName The columnName
     * @param value The value needed to remove from the columnName to be true to remove row
     * @return
     */
    public <T extends RowData> CompletableFuture<Void> removeRowIfColumnContainsValue(TableInfo<T> tableInfo, String columnName, String value) {
        @Language("SQL") String sql = "DELETE FROM " + tableInfo.getTableName() + " WHERE ? = ?;";



        return database.queryAsync(sql).thenAccept(dbStatement -> {
            try (DbStatement statement = dbStatement) {
                statement.execute(columnName, value);
                Universal.debug("Executing {} {} {} {}",sql, tableInfo.getTableName(), columnName, value);
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
     * @return
     */
    public <T extends RowData> CompletableFuture<Integer> insertIntoTable(TableInfo<T> tableInfo, T rowData) {


        String columnName = getColumnName(tableInfo);
        String columnValues = getColumnValues(rowData);

        @Language("SQL") String sql = "INSERT INTO " + tableInfo.getTableName() + "(" + columnName + ") VALUES (" + columnValues + ");";

        Universal.debug("Executing {}", sql);

        return database.executeUpdateAsync(sql)
                .thenApply(dbStatement -> {
                    Universal.debug(ChatColor.GREEN + "Fully Executed {}", sql);
                    try {
                        tableInfo.loadFromDB(this).get();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    return dbStatement;
                });

    }

    /**
     * Updates the row from a table
     * @param tableInfo The table
     * @param newRow The new row replacing the old
     * @param conditionKey What column
     * @param conditionValue What value is required from the column to update
     * @return
     */
    public <T extends RowData> CompletableFuture<Integer> updateRow(TableInfo<T> tableInfo, T newRow, String conditionKey, String conditionValue) {
        @Language("SQL") String sql = "UPDATE " + tableInfo + " SET ? WHERE ?=?;";

        String columnValues = getColumnValues(newRow);

        Universal.debug("Executing {} ({}) {} {}", sql, columnValues, conditionKey, conditionValue);

        return database.executeUpdateAsync(sql, columnValues, conditionKey, conditionValue)
                .thenApply(integer -> {
                    Universal.debug(ChatColor.GREEN + "Fully Executed {} {} ({}) {} {}", sql, tableInfo.getTableName(), columnValues, conditionKey, conditionValue);
                    tableInfo.loadFromDB(DatabaseListener.this);
                    return integer;
                });


    }

    /**
     * Deletes the table
     * @param tableInfo The table info
     * @return
     */
    public <T extends RowData> CompletableFuture<Integer> removeTable(TableInfo<T> tableInfo) throws DatabaseException {
        @Language("SQL") String sql = "DROP TABLE IF EXISTS " + tableInfo.getTableName() + ";";

        Universal.debug("Executing {}", sql);

        return database.executeUpdateAsync(sql);
    }

    /**
     * This is just to get column values from row data
     * @param rowData The row data variable
     * @return the values
     */
    public String getColumnValues(RowData rowData) {
//        StringBuilder sql = new StringBuilder();
//        StringBuilder append = new StringBuilder();
//
//        int index = 0;
//
        Map<Field, ColumnData> dataCopy = rowData.getDataCopy();

//        Collection<ColumnData> values = dataCopy.values();
//        for(ColumnData columnData : values) {
//            index++;
//
//
//            append.append("'")
//                    .append(columnData.getValue())
//                    .append("'");
//
//            if(index > 0 && index <= values.size() - 1) {
//                append.append(',');
//            }
//
//
//            sql.append(append);
//        }



        int fCount = 0;

        Set<Field> keySet = dataCopy.keySet();
        StringBuilder valueString = new StringBuilder();
        try {
            for (Field field : keySet) {
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    if (fCount != 0) {
                        valueString.append(",");
                    }
                    String value = dataCopy.get(field).getValue();

                    if (value == null) value = "null";

                    valueString.append(mysqlProxy.getInsertColumnPattern(field)
                            .replace("?", "'" + value + "'" )
                    );
                    fCount++;
                }
            }
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        }

        if (fCount == 0) {
            throw new IllegalStateException("No fields");
        }

        return valueString.toString();
    }

    public <T extends RowData> String getColumnName(@NonNull TableInfo<T> tableDataInfo) {
        StringBuilder append = new StringBuilder();

        RowDataTemplate<T> rd = tableDataInfo.getRowDataTemplate();
        int index = 0;
        Collection<ColumnData> values = rd.getDataStrCopy().values();

        for (ColumnData object : values) {
            index++;

            append.append(object.getColumnName());

            if (index > 0 && index <= values.size() - 1) {
                append.append(',');
            }

        }


        return append.toString();
    }


    /**
     * Creates the table
     * @param tableDataInfo The table with the data
     * @return
     */
    public <T extends RowData> CompletableFuture<Void> createTable(@NonNull TableInfo<T> tableDataInfo) {
        @Language("SQL") String q = "CREATE TABLE IF NOT EXISTS " + tableDataInfo .getTableName() + " (";

        StringBuilder sql = new StringBuilder(q);

        //CREATE TABLE IF NOT EXISTS fern_nicks(PLAYERUUID varchar(200), NICK varchar(40));
        //CREATE TABLE IF NOT EXISTS test_no(row1 TEXTrow2 TEXT);

        RowDataTemplate<T> rd = tableDataInfo.getRowDataTemplate();

        Set<Field> keySet = rd.getDataCopy().keySet();

        List<String> constraints = new ArrayList<>();
        Set<Field> indexed = new HashSet<>();

        boolean first = true;
        for (Field field : keySet) {

            Column column = field.getAnnotation(Column.class);
            String fName = column.value();

            Class<?> getterType = field.getType();
            String typeName = getterType.isEnum() ?
                    TYPE_ENUM : getterType.getTypeName();

            if (!first) {
                sql.append(",");
            }
            first = false;

            sql.append(fName).append(" ")
                    .append(mysqlProxy.getColumnString(column, field.getAnnotation(PrimaryKey.class),
                            field.getAnnotation(ForeignKey.class), typeName, constraints));

            if (field.isAnnotationPresent(Index.class)) {
                indexed.add(field);
            }
        }

        if (!constraints.isEmpty()) {
            sql.append(",");
            sql.append(String.join(",", constraints));
        }

        sql.append(");");

        Universal.debug("Executing {}", sql.toString());

        return database.executeUpdateAsync(sql.toString()).thenRun(() -> {
            Universal.debug(ChatColor.GREEN + "Fully Executed {} {}", sql, tableDataInfo.getTableName());
            // Create indexes
            for (Field field : indexed) {
                try {
                    @Language("SQL") String indexSql = mysqlProxy.buildIndex(tableDataInfo.getTableName(), field);

                    Universal.debug("Executing {}", indexSql);

                    database.executeUpdate(indexSql);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

            tableDataInfo.loadFromDB(DatabaseListener.this);
        });


    }

    protected Logger getLogger() {
        return Universal.getMethods().getAbstractLogger();
    }

}
