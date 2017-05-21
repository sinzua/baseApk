package com.lidroid.xutils.db.converter;

import android.database.Cursor;
import android.text.TextUtils;
import com.lidroid.xutils.db.sqlite.ColumnDbType;

public class CharColumnConverter implements ColumnConverter<Character> {
    public Character getFieldValue(Cursor cursor, int index) {
        return cursor.isNull(index) ? null : Character.valueOf((char) cursor.getInt(index));
    }

    public Character getFieldValue(String fieldStringValue) {
        if (TextUtils.isEmpty(fieldStringValue)) {
            return null;
        }
        return Character.valueOf(fieldStringValue.charAt(0));
    }

    public Object fieldValue2ColumnValue(Character fieldValue) {
        if (fieldValue == null) {
            return null;
        }
        return Integer.valueOf(fieldValue.charValue());
    }

    public ColumnDbType getColumnDbType() {
        return ColumnDbType.INTEGER;
    }
}
