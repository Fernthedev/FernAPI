package com.github.fernthedev.fernapi.universal;

import com.github.fernthedev.fernapi.universal.data.database.ColumnData;
import com.github.fernthedev.fernapi.universal.data.database.DatabaseAuthInfo;
import com.github.fernthedev.fernapi.universal.data.database.RowData;
import com.github.fernthedev.fernapi.universal.data.database.TableInfo;
import com.github.fernthedev.fernapi.universal.exceptions.database.DatabaseNotConnectedException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.sql.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Logger;

public abstract class DatabaseManager {

    @Getter
    @Setter
    private boolean isSetup;

    @Setter
    protected Connection connection;

    protected boolean connected = false;
    public  boolean isConnected() {
        return connected;
    }

    private Queue<Runnable> runOnConnectQueue = new LinkedList<>();
    private Queue<Runnable> runOnConnectAsync = new LinkedList<>();

    public Statement statement() {
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Connection getConnection() throws DatabaseNotConnectedException {
        if(connection == null) {
            throw new DatabaseNotConnectedException("You must call the connect(); method before calling any methods affecting the database.", new NullPointerException());
        }

        return connection;
    }

    /**
     * This is called after you attempt a connection
     * @see DatabaseManager#connect(DatabaseAuthInfo)
     * @param connected Returns true if successful
     */
    public abstract void onConnectAttempt(boolean connected);

    /**
     * Runs the code in an async thread but in a queue blocking order of runnable list.
     * @param runnable The code to run
     */
    public void runOnConnect(Runnable runnable) {
        if(!connected) {
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
        if (!connected) {
            runOnConnectAsync.add(runnable);
        } else {
            Universal.getMethods().runAsync(runnable);
        }
    }


    /**
     * Attempts to make a connection to the database
     * @param data The data required for login
     * @see DatabaseManager#onConnectAttempt(boolean) Called after attempted
     */
    public void connect(DatabaseAuthInfo data) {
        Universal.getDatabaseHandler().registerDatabase(data,this);
        try {
            connected = Universal.getDatabaseHandler().openConnection(data);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (connected) {
            Universal.getMethods().runAsync(() -> {
                while(!runOnConnectQueue.isEmpty()) {
                    runOnConnectQueue.remove().run();
                }
            });

            Universal.getMethods().runAsync(() -> {
                while(!runOnConnectAsync.isEmpty()) {
                    Universal.getMethods().runAsync(() -> runOnConnectAsync.remove().run());
                }
            });
        }
    }

    /**
     * Gets the table and it's rows
     * Note: This won't give you all the information given from the instance it was created from
     * Use this to get data, not to create tables exactly as the first instance.
     * @param name The name of the table
     * @return The table
     */
    public TableInfo getTable(String name) {
        String sql = "SELECT * FROM " + name + ";";

        ResultSet result = runSqlStatement(sql);

        TableInfo tableInfo = new TableInfo(name);



        try {
            while (result.next()) {
                RowData rowData = null;

                ResultSetMetaData rsMetaData = result.getMetaData();
                int count = rsMetaData.getColumnCount();

                int time = 0;

                for(int i = 0; i < count; i++) {
                    time++;

                    ColumnData columnData = new ColumnData(
                            rsMetaData.getColumnName(time),
                            result.getString(time),
                            rsMetaData.getColumnDisplaySize(time)
                    );

                    columnData.setType(rsMetaData.getColumnTypeName(time));
                    columnData.setAutoIncrement(rsMetaData.isAutoIncrement(time));

                    if(rowData == null) {
                        rowData = new RowData(columnData);
                    }else {
                        rowData.addData(columnData);
                    }
                }
                
                tableInfo.addTableInfo(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return tableInfo;
    }

    /**
     * Removes the row if columnName contains value
     * @param tableInfo The table
     * @param columnName The columnName
     * @param value The value needed to remove from the columnName to be true to remove row
     */
    public void removeRowIfColumnContainsValue(TableInfo tableInfo,String columnName,String value) {
        String sql = "DELETE FROM " + tableInfo.getTableName() + " WHERE " + columnName + "='" + value + "';";

        runSqlStatement(sql);

        tableInfo.getFromDatabase(this);
    }

    /**
     * Insert new rows into table
     * @param tableInfo The table
     * @param rowData The row
     */
    public void insertIntoTable(TableInfo tableInfo, RowData rowData) {
        tableInfo.addTableInfo(rowData);

        //INSERT INTO test_no(row1,row2) VALUES (value1test,value2test);
        String sql = "INSERT INTO " + tableInfo.getTableName() + "(" + getColumnName(tableInfo) + ") VALUES (" + getColumnValues(rowData) + ");";

        runSqlStatement(sql);

        tableInfo.getFromDatabase(this);
    }

    /**
     * Updates the row from a table
     * @param tableInfo The table
     * @param newRow The new row replacing the old
     * @param conditionKey What column
     * @param conditionValue What value is required from the column to update
     */
    public void updateRow(TableInfo tableInfo,RowData newRow,String conditionKey,String conditionValue) {
        //UPDATE table_name
        //SET column1 = value1, column2 = value2, ...
        //WHERE condition;

        // "UPDATE fern_nicks SET PLAYERUUID='" + player.getUniqueId().toString().replaceAll("-", "") + "NICK='" + args[0] + "' WHERE PLAYERUUID='" + player.getUniqueId().toString().replaceAll("-", "") + "';";

        // UPDATE fern_nicks SET 'f99a5767-0aae-48ca-a8a8-6b56e6a8c470','&cF&ee&ar&bn' WHERE PLAYERUUID=f99a57670aae48caa8a86b56e6a8c470;

        String sql = "UPDATE " + tableInfo.getTableName() + " SET " + getColumnValues(newRow) + " WHERE " + conditionKey + "='" + conditionValue + "';";

        runSqlStatement(sql);

        tableInfo.getFromDatabase(this);
    }


    /**
     * Deletes the table
     * @param tableInfo The table info
     */
    public void removeTable(TableInfo tableInfo) {
        String sql = "DROP TABLE IF EXISTS " + tableInfo.getTableName() + ";";

        runSqlStatement(sql);
    }

    /**
     * This is just to get column values from row data
     * @param rowData The row data variable
     * @return the values
     */
    public String getColumnValues(RowData rowData) {
        StringBuilder sql = new StringBuilder();
        String append;

        int times = 0;

        for(ColumnData columnData : rowData.getColumnDataList()) {
            times++;


            append = columnData.getColumnName() + "='" + columnData.getValue();

            append += "'";

            if(times > 0 && times <= rowData.getColumnDataList().size() - 1) {
                append += ',';
            }


            sql.append(append);
        }

        return sql.toString();
    }

    public String getColumnName(@NonNull TableInfo tableDataInfo) {
        StringBuilder sql = new StringBuilder();

        //CREATE TABLE IF NOT EXISTS fern_nicks(PLAYERUUID varchar(200), NICK varchar(40));
        //CREATE TABLE IF NOT EXISTS test_no(row1 TEXTrow2 TEXT);

        String append;

        if(!tableDataInfo.getRowDataList().isEmpty()) {
            RowData rd = tableDataInfo.getRowDataList().iterator().next();
            int time = 0;
            for (ColumnData object : rd.getColumnDataList()) {
                time++;

                append = object.getColumnName();

                if (time > 0 && time <= rd.getColumnDataList().size() - 1) {
                    append += ',';
                }

                sql.append(append);
            }
        }

        return sql.toString();
    }

    /**
     * Runs a sql statement
     * @param sql The statement
     * @return The result
     */
    public ResultSet runSqlStatement(String sql) {
        PreparedStatement stmt;

        Statement statement = statement();

        if(statement != null) {
            try {
                stmt = getConnection().prepareStatement(sql);

                Universal.debug("Running {" + sql+ "}");

                if(sql.startsWith("SELECT ")) {
                    return stmt.executeQuery();
                }else{
                    stmt.executeUpdate();
                    return null;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * Creates the table
     * @param tableDataInfo The table with the data
     */
    public void createTable(@NonNull TableInfo tableDataInfo) {
        StringBuilder sql = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableDataInfo.getTableName() + "(");

        //CREATE TABLE IF NOT EXISTS fern_nicks(PLAYERUUID varchar(200), NICK varchar(40));
        //CREATE TABLE IF NOT EXISTS test_no(row1 TEXTrow2 TEXT);

        String append;

        if(!tableDataInfo.getRowDataList().isEmpty()) {
            RowData rd = tableDataInfo.getRowDataList().iterator().next();
            int time = 0;
            for (ColumnData object : rd.getColumnDataList()) {
                time++;

                String type = "varchar(" + object.getLength() + ")";

                if (object.getLength() <= 0) {
                    type = "TEXT";
                }
                if (object.isPrimaryKey()) {
                    type = "INT PRIMARY KEY";
                }

                append = object.getColumnName() + " " + type;

                if (object.isAutoIncrement()) {
                    append += " AUTO_INCREMENT";
                }

                if (!object.isNullable() && !object.isPrimaryKey()) {
                    append += " NOT NULL";
                }

                if (time > 0 && time <= rd.getColumnDataList().size() - 1) {
                    append += ',';
                }

                sql.append(append);
            }
        }


        sql.append(");");

        runSqlStatement(sql.toString());
        tableDataInfo.getFromDatabase(this);
    }

    protected Logger getLogger() {
        return Universal.getMethods().getLogger();
    }

}
