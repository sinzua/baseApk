package com.lidroid.xutils.db.table;

import android.database.Cursor;
import com.lidroid.xutils.db.converter.ColumnConverter;
import com.lidroid.xutils.db.converter.ColumnConverterFactory;
import com.lidroid.xutils.db.sqlite.ColumnDbType;
import com.lidroid.xutils.db.sqlite.ForeignLazyLoader;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import java.lang.reflect.Field;
import java.util.List;

public class Foreign extends Column {
    private final ColumnConverter foreignColumnConverter = ColumnConverterFactory.getColumnConverter(TableUtils.getColumnOrId(getForeignEntityType(), this.foreignColumnName).columnField.getType());
    private final String foreignColumnName;

    Foreign(Class<?> entityType, Field field) {
        super(entityType, field);
        this.foreignColumnName = ColumnUtils.getForeignColumnNameByField(field);
    }

    public String getForeignColumnName() {
        return this.foreignColumnName;
    }

    public Class<?> getForeignEntityType() {
        return ColumnUtils.getForeignEntityType(this);
    }

    public void setValue2Entity(Object entity, Cursor cursor, int index) {
        Object fieldValue = this.foreignColumnConverter.getFieldValue(cursor, index);
        if (fieldValue != null) {
            Object obj = null;
            Class<?> columnType = this.columnField.getType();
            if (columnType.equals(ForeignLazyLoader.class)) {
                obj = new ForeignLazyLoader(this, fieldValue);
            } else if (columnType.equals(List.class)) {
                try {
                    obj = new ForeignLazyLoader(this, fieldValue).getAllFromDb();
                } catch (DbException e) {
                    LogUtils.e(e.getMessage(), e);
                }
            } else {
                try {
                    obj = new ForeignLazyLoader(this, fieldValue).getFirstFromDb();
                } catch (DbException e2) {
                    LogUtils.e(e2.getMessage(), e2);
                }
            }
            if (this.setMethod != null) {
                try {
                    this.setMethod.invoke(entity, new Object[]{obj});
                    return;
                } catch (Throwable e3) {
                    LogUtils.e(e3.getMessage(), e3);
                    return;
                }
            }
            try {
                this.columnField.setAccessible(true);
                this.columnField.set(entity, obj);
            } catch (Throwable e32) {
                LogUtils.e(e32.getMessage(), e32);
            }
        }
    }

    public Object getColumnValue(Object entity) {
        Object fieldValue = getFieldValue(entity);
        if (fieldValue == null) {
            return null;
        }
        Class<?> columnType = this.columnField.getType();
        if (columnType.equals(ForeignLazyLoader.class)) {
            return ((ForeignLazyLoader) fieldValue).getColumnValue();
        }
        Column column;
        Table table;
        if (columnType.equals(List.class)) {
            try {
                List<?> foreignEntities = (List) fieldValue;
                if (foreignEntities.size() <= 0) {
                    return null;
                }
                column = TableUtils.getColumnOrId(ColumnUtils.getForeignEntityType(this), this.foreignColumnName);
                Object columnValue = column.getColumnValue(foreignEntities.get(0));
                table = getTable();
                if (table != null && (column instanceof Id)) {
                    for (Object foreignObj : foreignEntities) {
                        if (column.getColumnValue(foreignObj) == null) {
                            table.db.saveOrUpdate(foreignObj);
                        }
                    }
                }
                return column.getColumnValue(foreignEntities.get(0));
            } catch (Throwable e) {
                LogUtils.e(e.getMessage(), e);
                return null;
            }
        }
        try {
            column = TableUtils.getColumnOrId(columnType, this.foreignColumnName);
            columnValue = column.getColumnValue(fieldValue);
            table = getTable();
            if (table != null && columnValue == null && (column instanceof Id)) {
                table.db.saveOrUpdate(fieldValue);
            }
            return column.getColumnValue(fieldValue);
        } catch (Throwable e2) {
            LogUtils.e(e2.getMessage(), e2);
            return null;
        }
    }

    public ColumnDbType getColumnDbType() {
        return this.foreignColumnConverter.getColumnDbType();
    }

    public Object getDefaultValue() {
        return null;
    }
}
