package com.github.fernthedev.fernapi.universal.examples.mysql;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.database.*;
import com.github.fernthedev.fernapi.universal.mysql.DatabaseListener;
import lombok.SneakyThrows;

public class DatabaseTest extends DatabaseListener {
    private TableInfo tableInfo;

    private static RowDataTemplate rowDataTemplate = new RowDataTemplate(
            new ColumnData("thing", "test"),
            new ColumnData("thing2", "testthing")
    );

    public DatabaseTest(String username,String password,String port,String URLHost,String database) {
        connect(new DatabaseAuthInfo(username,password,port,URLHost,database));
    }

    public TableInfo getTableInfo() {
        if(tableInfo == null) {
            tableInfo = new TableInfo("test_no", rowDataTemplate);
        }

        return tableInfo;
    }


    /**
     * This is called after you attempt a connection
     *
     * @param connected Returns true if successful
     * @see DatabaseListener#connect(DatabaseAuthInfo)
     */
    @Override
    public void onConnectAttempt(boolean connected) {
        if(connected) {
            Universal.getMethods().getLogger().info("Connected successfully");
        }else{
            Universal.getMethods().getLogger().warning("Unable to connect successfully");
        }
    }

    @SneakyThrows
    public void test() {

        tableInfo = new TableInfo("test_no", rowDataTemplate);

        RowData rowData = new RowData(new ColumnData("row1","value1"), new ColumnData("row2", "value2"));

        insertIntoTable(tableInfo, rowData);

        rowData = new RowData(new ColumnData("row1","value1nou"), new ColumnData("row2","value2nou"));

        insertIntoTable(tableInfo, rowData);


        createTable(tableInfo);
    }
}
