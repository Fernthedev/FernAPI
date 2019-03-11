package com.github.fernthedev.fernapi.universal.data.database;

import lombok.Getter;
import lombok.Setter;

public class RowObject {

    @Getter
    private String row;

    @Getter
    private String value;

    @Getter
    @Setter
    private String type;

    @Getter
    private int length = -1;

    @Getter
    @Setter
    private boolean nullable = true;

    @Getter
    @Setter
    private boolean autoIncrement;

    @Getter
    @Setter
    private boolean primaryKey;

    public RowObject( String row, String value) {
        this.row = row;
        this.value = value;
    }

    public RowObject(String row, String value,int length) {
        this(row,value);
        this.length = length;
    }

}
