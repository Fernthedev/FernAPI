package com.github.fernthedev.fernapi.universal.data.database;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * The column is a piece of data.
 */
@ToString
public class ColumnData {

    @Getter
    @NonNull
    private String columnName;

    /**
     * Will return null if it is SQL NULL
     */
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

    public ColumnData(@NonNull String columnName, String value) {
        this.columnName = columnName;
        this.value = value;
    }

    public ColumnData(@NonNull String columnName, String value, int length) {
        this(columnName,value);
        this.length = length;
    }

}
