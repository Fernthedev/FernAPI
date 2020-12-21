package com.github.fernthedev.fernapi.universal.examples.mysql;

import co.aikar.idb.DbRow;
import com.github.fernthedev.fernapi.universal.data.database.RowData;
import org.panteleyev.mysqlapi.annotations.Column;
import org.panteleyev.mysqlapi.annotations.PrimaryKey;

public class RowDataTest extends RowData {
    public RowDataTest(DbRow rowData) {
        super(rowData);
    }

    public RowDataTest(String s, String val) {
        super(new DbRow());
        thing = s;
        thing2 = val;
    }

    @PrimaryKey
    @Column("thing")
    private String thing = "test1";

    @Column("thing2")
    private String thing2 = "test2";
}
