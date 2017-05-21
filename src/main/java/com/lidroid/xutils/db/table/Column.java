package com.lidroid.xutils.db.table;

import android.database.Cursor;
import com.lidroid.xutils.db.converter.ColumnConverter;
import com.lidroid.xutils.db.converter.ColumnConverterFactory;
import com.lidroid.xutils.db.sqlite.ColumnDbType;
import com.lidroid.xutils.util.LogUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Column {
    protected final ColumnConverter columnConverter;
    protected final Field columnField;
    protected final String columnName;
    private final Object defaultValue;
    protected final Method getMethod;
    private int index = -1;
    protected final Method setMethod;
    private Table table;

    Column(Class<?> entityType, Field field) {
        this.columnField = field;
        this.columnConverter = ColumnConverterFactory.getColumnConverter(field.getType());
        this.columnName = ColumnUtils.getColumnNameByField(field);
        if (this.columnConverter != null) {
            this.defaultValue = this.columnConverter.getFieldValue(ColumnUtils.getColumnDefaultValue(field));
        } else {
            this.defaultValue = null;
        }
        this.getMethod = ColumnUtils.getColumnGetMethod(entityType, field);
        this.setMethod = ColumnUtils.getColumnSetMethod(entityType, field);
    }

    public void setValue2Entity(Object entity, Cursor cursor, int index) {
        this.index = index;
        Object value = this.columnConverter.getFieldValue(cursor, index);
        if (value != null || this.defaultValue != null) {
            if (this.setMethod != null) {
                try {
                    Method method = this.setMethod;
                    Object[] objArr = new Object[1];
                    if (value == null) {
                        value = this.defaultValue;
                    }
                    objArr[0] = value;
                    method.invoke(entity, objArr);
                    return;
                } catch (Throwable e) {
                    LogUtils.e(e.getMessage(), e);
                    return;
                }
            }
            try {
                this.columnField.setAccessible(true);
                Field field = this.columnField;
                if (value == null) {
                    value = this.defaultValue;
                }
                field.set(entity, value);
            } catch (Throwable e2) {
                LogUtils.e(e2.getMessage(), e2);
            }
        }
    }

    public Object getColumnValue(Object entity) {
        return this.columnConverter.fieldValue2ColumnValue(getFieldValue(entity));
    }

    public Object getFieldValue(Object entity) {
        Object fieldValue = null;
        if (entity != null) {
            if (this.getMethod != null) {
                try {
                    fieldValue = this.getMethod.invoke(entity, new Object[0]);
                } catch (Throwable e) {
                    LogUtils.e(e.getMessage(), e);
                }
            } else {
                try {
                    this.columnField.setAccessible(true);
                    fieldValue = this.columnField.get(entity);
                } catch (Throwable e2) {
                    LogUtils.e(e2.getMessage(), e2);
                }
            }
        }
        return fieldValue;
    }

    public Table getTable() {
        return this.table;
    }

    void setTable(Table table) {
        this.table = table;
    }

    public int getIndex() {
        return this.index;
    }

    public String getColumnName() {
        return this.columnName;
    }

    public Object getDefaultValue() {
        return this.defaultValue;
    }

    public Field getColumnField() {
        return this.columnField;
    }

    public ColumnConverter getColumnConverter() {
        return this.columnConverter;
    }

    public ColumnDbType getColumnDbType() {
        return this.columnConverter.getColumnDbType();
    }
}
