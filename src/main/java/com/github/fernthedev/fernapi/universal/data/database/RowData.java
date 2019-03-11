package com.github.fernthedev.fernapi.universal.data.database;

import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RowData {

    @Getter
    private List<RowObject> objects = new ArrayList<>();

    public RowData(@NonNull RowObject rowObject,@NonNull RowObject... objects) {
        this.objects.add(rowObject);
        this.objects.addAll(Arrays.asList(objects));
    }

    public RowData(@NonNull RowObject object) {
        this.objects.add(object);
    }

    public void addData(RowObject data) {
        objects.add(data);
    }
}
