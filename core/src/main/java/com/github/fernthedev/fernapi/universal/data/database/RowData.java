package com.github.fernthedev.fernapi.universal.data.database;

import co.aikar.idb.DbRow;
import com.google.errorprone.annotations.Immutable;
import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;
import lombok.ToString;
import org.panteleyev.mysqlapi.MySqlProxy;
import org.panteleyev.mysqlapi.annotations.Column;
import org.panteleyev.mysqlapi.annotations.PrimaryKey;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Contains the full row data of columns.
 * Each column is one data.
 */
@ToString
@Immutable
public abstract class RowData {

    private static final MySqlProxy mySqlProxy = new MySqlProxy();

    private boolean initiated = false;

    protected final RowData instance;

    private final Map<Field, ColumnData> cachedData = new LinkedHashMap<>();
    private final Map<String, ColumnData> cachedDataStr = new LinkedHashMap<>();

    /**
     * Use to instantiate Row Data with empty data.
     *
     * It is recommended to call {@link #initiateRowData()} after your values are instantiated.
     */
    public RowData() {
        validateKeys(getClass());
        instance = this;
    }

    /**
     * Must set initiated to true to avoid problems when overridden.
     *
     * Called when retrieving the data from the SQL table.
     *
     * @param rowData
     */
    @OverridingMethodsMustInvokeSuper
    protected void initiateRowData(DbRow rowData) {
        if (initiated)
            throw new IllegalStateException("Already initiated row data and is immutable.");

        initiated = true;


        for (Field field : getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                ColumnData columnData;
                try {
                    Column column = field.getAnnotation(Column.class);
                    columnData = ColumnData.fromField(field, rowData.get(column.value()).toString());
                    addField(field, columnData, rowData.get(columnData.getColumnName()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Must set initiated to true to avoid problems when overridden.
     *
     * Called when setting the data of the class manually.
     *
     */
    protected void initiateRowData() {
        if (initiated)
            throw new IllegalStateException("Already initiated row data and is immutable.");

        initiated = true;
        for (Field field : getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                ColumnData columnData;
                try {
                    field.setAccessible(true);
                    columnData = ColumnData.fromField(field, field.get(instance).toString());
                    addField(field, columnData);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Validates that the row data fields are properly configured.
     * @param rowData
     * @param <T>
     */
    public static <T> void validateKeys(Class<T> rowData) {

        boolean anyColumns = Arrays.stream(rowData.getDeclaredFields()).anyMatch(field -> field.isAnnotationPresent(Column.class));

        if (!anyColumns)
            throw new IllegalStateException("No fields have @" + Column.class.getName() + " annotation");


        boolean primaryKey = Arrays.stream(rowData.getDeclaredFields()).anyMatch(field -> field.isAnnotationPresent(PrimaryKey.class));

        if (!primaryKey)
            throw new IllegalStateException("No fields have @" + PrimaryKey.class.getName() + " annotation");

        boolean multiplePrimaryKeys = Arrays.stream(rowData.getDeclaredFields()).filter(field -> field.isAnnotationPresent(PrimaryKey.class)).count() > 1;

        if (multiplePrimaryKeys)
            throw new IllegalStateException("Too many fields have @" + PrimaryKey.class.getName() + " annotation");

    }

    private void addField(Field field, ColumnData columnData) {
        cachedData.put(field, columnData);
        cachedDataStr.put(field.getAnnotation(Column.class).value(), columnData);
    }

    private void addField(Field field, ColumnData columnData, Object value) {
        cachedData.put(field, columnData);
        cachedDataStr.put(field.getAnnotation(Column.class).value(), columnData);

        try {
            Object val = mySqlProxy.getFieldValue(field.getType(), value);

            field.setAccessible(true);

            field.set(this, val);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * Retrieves the column data
     *
     * @param sqlName The name of the column in the SQL table
     * @return The column data
     */
    public ColumnData getColumn(String sqlName) {
        if (!initiated)
            initiateRowData();

        return cachedDataStr.get(sqlName);
    }

    /**
     * Retrieves the column data
     *
     * @param field The field referencing the SQL data.
     * @return The column data
     */
    public ColumnData getColumn(Field field) {
        if (!initiated)
            initiateRowData();

        return cachedData.get(field);
    }

    /**
     * Immutable
     * @return
     */
    public Map<String, ColumnData> getDataStrCopy() {
        if (!initiated)
            initiateRowData();

        return new LinkedHashMap<>(cachedDataStr);
    }

    /**
     * Immutable
     * @return
     */
    public Map<Field, ColumnData> getDataCopy() {
        if (!initiated)
            initiateRowData();

        return new LinkedHashMap<>(cachedData);
    }
}
