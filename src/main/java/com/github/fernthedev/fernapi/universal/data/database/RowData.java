package com.github.fernthedev.fernapi.universal.data.database;

import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contains the full row data of columns.
 * Each column is one data.
 */
public class RowData {

    @Getter
    private List<ColumnData> objects = new ArrayList<>();

    public RowData(@NonNull ColumnData columnData, @NonNull ColumnData... objects) {
        this.objects.add(columnData);
        this.objects.addAll(Arrays.asList(objects));
    }

    public RowData(@NonNull ColumnData object) {
        this.objects.add(object);
    }

    public void addData(ColumnData data) {
        objects.add(data);
    }
}
