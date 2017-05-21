package com.lidroid.xutils.db.table;

import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.util.LogUtils;
import java.lang.reflect.Field;
import java.util.HashSet;

public class Id extends Column {
    private static final HashSet<String> AUTO_INCREMENT_TYPES = new HashSet(4);
    private static final HashSet<String> INTEGER_TYPES = new HashSet(2);
    private String columnFieldClassName = this.columnField.getType().getName();
    private boolean isAutoIncrement = false;
    private boolean isAutoIncrementChecked = false;

    Id(Class<?> entityType, Field field) {
        super(entityType, field);
    }

    public boolean isAutoIncrement() {
        boolean z = true;
        if (!this.isAutoIncrementChecked) {
            this.isAutoIncrementChecked = true;
            if (!(this.columnField.getAnnotation(NoAutoIncrement.class) == null && AUTO_INCREMENT_TYPES.contains(this.columnFieldClassName))) {
                z = false;
            }
            this.isAutoIncrement = z;
        }
        return this.isAutoIncrement;
    }

    public void setAutoIncrementId(Object entity, long value) {
        Object idValue = Long.valueOf(value);
        if (INTEGER_TYPES.contains(this.columnFieldClassName)) {
            idValue = Integer.valueOf((int) value);
        }
        if (this.setMethod != null) {
            try {
                this.setMethod.invoke(entity, new Object[]{idValue});
                return;
            } catch (Throwable e) {
                LogUtils.e(e.getMessage(), e);
                return;
            }
        }
        try {
            this.columnField.setAccessible(true);
            this.columnField.set(entity, idValue);
        } catch (Throwable e2) {
            LogUtils.e(e2.getMessage(), e2);
        }
    }

    public Object getColumnValue(Object entity) {
        Object idValue = super.getColumnValue(entity);
        if (idValue == null) {
            return null;
        }
        if (!isAutoIncrement()) {
            return idValue;
        }
        if (idValue.equals(Integer.valueOf(0)) || idValue.equals(Long.valueOf(0))) {
            return null;
        }
        return idValue;
    }

    static {
        INTEGER_TYPES.add(Integer.TYPE.getName());
        INTEGER_TYPES.add(Integer.class.getName());
        AUTO_INCREMENT_TYPES.addAll(INTEGER_TYPES);
        AUTO_INCREMENT_TYPES.add(Long.TYPE.getName());
        AUTO_INCREMENT_TYPES.add(Long.class.getName());
    }
}
