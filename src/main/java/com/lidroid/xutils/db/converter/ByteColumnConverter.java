package com.lidroid.xutils.db.converter;

import android.database.Cursor;
import android.text.TextUtils;
import com.lidroid.xutils.db.sqlite.ColumnDbType;

public class ByteColumnConverter implements ColumnConverter<Byte> {
    public Byte getFieldValue(Cursor cursor, int index) {
        return cursor.isNull(index) ? null : Byte.valueOf((byte) cursor.getInt(index));
    }

    public Byte getFieldValue(String fieldStringValue) {
        if (TextUtils.isEmpty(fieldStringValue)) {
            return null;
        }
        return Byte.valueOf(fieldStringValue);
    }

    public Object fieldValue2ColumnValue(Byte fieldValue) {
        return fieldValue;
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}
