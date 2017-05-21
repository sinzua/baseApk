package com.lidroid.xutils.db.table;

import android.database.Cursor;
import com.lidroid.xutils.db.sqlite.ColumnDbType;
import com.lidroid.xutils.db.sqlite.FinderLazyLoader;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import java.lang.reflect.Field;
import java.util.List;

public class Finder extends Column {
    private final String targetColumnName;
    private final String valueColumnName;

    Finder(Class<?> entityType, Field field) {
        super(entityType, field);
        com.lidroid.xutils.db.annotation.Finder finder = (com.lidroid.xutils.db.annotation.Finder) field.getAnnotation(com.lidroid.xutils.db.annotation.Finder.class);
        this.valueColumnName = finder.valueColumn();
        this.targetColumnName = finder.targetColumn();
    }

    public Class<?> getTargetEntityType() {
        return ColumnUtils.getFinderTargetEntityType(this);
    }

    public String getTargetColumnName() {
        return this.targetColumnName;
    }

    public void setValue2Entity(Object entity, Cursor cursor, int index) {
        Object obj = null;
        Class<?> columnType = this.columnField.getType();
        Object finderValue = TableUtils.getColumnOrId(entity.getClass(), this.valueColumnName).getColumnValue(entity);
        if (columnType.equals(FinderLazyLoader.class)) {
            obj = new FinderLazyLoader(this, finderValue);
        } else if (columnType.equals(List.class)) {
            try {
                obj = new FinderLazyLoader(this, finderValue).getAllFromDb();
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
        } else {
            try {
                obj = new FinderLazyLoader(this, finderValue).getFirstFromDb();
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

    public Object getColumnValue(Object entity) {
        return null;
    }

    public Object getDefaultValue() {
        return null;
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.TEXT;
    }
}
