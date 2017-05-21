package com.lidroid.xutils.db.converter;

import android.database.Cursor;
import android.text.TextUtils;
import com.lidroid.xutils.db.sqlite.ColumnDbType;

public class BooleanColumnConverter implements ColumnConverter<Boolean> {
    public Boolean getFieldValue(Cursor cursor, int index) {
        boolean z = true;
        if (cursor.isNull(index)) {
            return null;
        }
        if (cursor.getInt(index) != 1) {
            z = false;
        }
        return Boolean.valueOf(z);
    }

    public Boolean getFieldValue(String fieldStringValue) {
        if (TextUtils.isEmpty(fieldStringValue)) {
            return null;
        }
        return Boolean.valueOf(fieldStringValue.length() == 1 ? "1".equals(fieldStringValue) : Boolean.valueOf(fieldStringValue).booleanValue());
    }

    public Object fieldValue2ColumnValue(Boolean fieldValue) {
        if (fieldValue == null) {
            return null;
        }
        return Integer.valueOf(fieldValue.booleanValue() ? 1 : 0);
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}
