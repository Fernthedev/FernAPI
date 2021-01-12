package com.github.fernthedev.fernapi.universal.examples.mysql;

import com.github.fernthedev.fernapi.universal.Universal;
import com.github.fernthedev.fernapi.universal.data.database.DatabaseAuthInfo;
import com.github.fernthedev.fernapi.universal.data.database.TableInfo;
import com.github.fernthedev.fernapi.universal.mysql.AikarFernDatabase;
import com.github.fernthedev.fernapi.universal.mysql.DatabaseListener;
import lombok.SneakyThrows;

public class DatabaseTest extends DatabaseListener {
    private final TableInfo<RowDataTest> tableInfo = new TableInfo<>("test_no", RowDataTest.class, RowDataTest::new);

    public DatabaseTest() {
        super(AikarFernDatabase.createHikariDatabase(Universal.getPlugin(), new DatabaseAuthInfo("root","admin","3306","127.0.0.1","minecraft")));
        connect();
    }

    public TableInfo<RowDataTest> getTableInfo() {
        return tableInfo;
    }


    /**
     * This is called after you attempt a connection
     *
     * @param connected Returns true if successful
     * @see #connect()
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
        createTable(tableInfo).get();

        RowDataTest rowData = new RowDataTest("row1","value1");

        insertIntoTable(tableInfo, rowData);

        rowData = new RowDataTest("row1fern","value1nou");

        insertIntoTable(tableInfo, rowData);



    }
}
