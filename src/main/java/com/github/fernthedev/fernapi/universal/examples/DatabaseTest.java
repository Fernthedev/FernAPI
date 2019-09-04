package com.github.fernthedev.fernapi.universal.examples;

import com.github.fernthedev.fernapi.universal.DatabaseManager;
import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.database.DatabaseInfo;
import com.github.fernthedev.fernapi.universal.data.database.RowData;
import com.github.fernthedev.fernapi.universal.data.database.ColumnData;
import com.github.fernthedev.fernapi.universal.data.database.TableInfo;

public class DatabaseTest extends DatabaseManager {
    private TableInfo tableInfo;

    public DatabaseTest(String username,String password,String port,String URLHost,String database) {
        connect(new DatabaseInfo(username,password,port,URLHost,database));
    }

    public TableInfo getTableInfo() {
        if(tableInfo == null) {
            tableInfo = new TableInfo("test_no");
        }

        return tableInfo;
    }


    /**
     * This is called after you attempt a connection
     *
     * @param connected Returns true if successful
     * @see DatabaseManager#connect(DatabaseInfo)
     */
    @Override
    public void onConnectAttempt(boolean connected) {
        if(connected) {
            Universal.getMethods().getLogger().info("Connected successfully");
        }else{
            Universal.getMethods().getLogger().warning("Unable to connect successfully");
        }
    }

    public void test() {



        tableInfo = new TableInfo("test_no");

        RowData rowData = new RowData(new ColumnData("row1","value1"));

        rowData.addData(new ColumnData("row2","value2"));

        tableInfo.addTableInfo(rowData);

        rowData = new RowData(new ColumnData("row1","value1nou"));

        rowData.addData(new ColumnData("row2","value2nou"));

        tableInfo.addTableInfo(rowData);

        createTable(tableInfo);
    }
}
