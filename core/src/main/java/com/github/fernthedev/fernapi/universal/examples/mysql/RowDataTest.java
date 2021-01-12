package com.github.fernthedev.fernapi.universal.examples.mysql;

import com.github.fernthedev.fernapi.universal.data.database.RowData;
import lombok.ToString;
import org.panteleyev.mysqlapi.annotations.Column;
import org.panteleyev.mysqlapi.annotations.PrimaryKey;

@ToString(callSuper = true)
public class RowDataTest extends RowData {

    public RowDataTest(String s, String val) {
        super();
        thing = s;
        thing2 = val;
        initiateRowData();
    }

    public RowDataTest() {}

    @PrimaryKey
    @Column("thing")
    private String thing = "test1";

    @Column("thing2")
    private String thing2 = "test2";


}
