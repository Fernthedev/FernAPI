package com.github.fernthedev.fernapi.universal.examples.mysql;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.database.*;
import com.github.fernthedev.fernapi.universal.mysql.DatabaseListener;
import lombok.SneakyThrows;

public class DatabaseTest extends DatabaseListener {
    private TableInfo<RowDataTest> tableInfo;

    public DatabaseTest(String username,String password,String port,String URLHost,String database) {
        connect(new DatabaseAuthInfo(username,password,port,URLHost,database));
    }

    public TableInfo<RowDataTest> getTableInfo() {
        if(tableInfo == null) {
            tableInfo = new TableInfo<>("test_no", RowDataTest.class);
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
            Universal.getMethods().getAbstractLogger().info("Connected successfully");
        }else{
            Universal.getMethods().getAbstractLogger().warn("Unable to connect successfully");
        }
    }

    @SneakyThrows
    public void test() {

        tableInfo = new TableInfo<>("test_no", RowDataTest.class);

        RowDataTest rowData = new RowDataTest("row1","value1");

        insertIntoTable(tableInfo, rowData);

        rowData = new RowDataTest("row1fern","value1nou");

        insertIntoTable(tableInfo, rowData);


        createTable(tableInfo);
    }
}
