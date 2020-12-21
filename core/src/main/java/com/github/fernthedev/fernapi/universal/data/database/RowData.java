package com.github.fernthedev.fernapi.universal.data.database;

import co.aikar.idb.DbRow;
import com.google.errorprone.annotations.Immutable;
import lombok.ToString;
import org.panteleyev.mysqlapi.annotations.Column;
import org.panteleyev.mysqlapi.annotations.PrimaryKey;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains the full row data of columns.
 * Each column is one data.
 */
@ToString
@Immutable
public abstract class RowData {

    private final Map<Field, ColumnData> cachedData = new HashMap<>();
    private final Map<String, ColumnData> cachedDataStr = new HashMap<>();

    public RowData(DbRow rowData) {
        validateKeys(getClass());

        Arrays.stream(getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .forEach(field -> {
            ColumnData columnData;
            try {
                Column column = field.getAnnotation(Column.class);
                columnData = ColumnData.fromField(field, rowData.get(column.value()).toString());
                addField(field, columnData);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    public static <T> void validateKeys(Class<T> rowData) {

        boolean anyColumns = Arrays.stream(rowData.getFields()).anyMatch(field -> field.isAnnotationPresent(Column.class));

        if (!anyColumns)
            throw new IllegalStateException("No fields have @" + Column.class.toString() + " annotation");


        boolean primaryKey = Arrays.stream(rowData.getFields()).anyMatch(field -> field.isAnnotationPresent(PrimaryKey.class));

        if (!primaryKey)
            throw new IllegalStateException("No fields have @" + PrimaryKey.class.toString() + " annotation");

        boolean multiplePrimaryKeys = Arrays.stream(rowData.getFields()).filter(field -> field.isAnnotationPresent(PrimaryKey.class)).count() > 1;

        if (multiplePrimaryKeys)
            throw new IllegalStateException("Too many fields have @" + PrimaryKey.class.toString() + " annotation");

    }

    private void addField(Field field, ColumnData columnData) {
        cachedData.put(field, columnData);
        cachedDataStr.put(field.getAnnotation(Column.class).value(), columnData);
    }


    public ColumnData getColumn(String sqlName) {
        return cachedDataStr.get(sqlName);
    }

    public ColumnData getColumn(Field field) {
        return cachedData.get(field);
    }

    /**
     * Immutable
     * @return
     */
    public Map<String, ColumnData> getDataCopy() {
        return new HashMap<>(cachedDataStr);
    }
}
