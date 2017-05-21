package com.lidroid.xutils.db.table;

import android.text.TextUtils;
import com.lidroid.xutils.db.annotation.Check;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Finder;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NotNull;
import com.lidroid.xutils.db.annotation.Transient;
import com.lidroid.xutils.db.annotation.Unique;
import com.lidroid.xutils.db.converter.ColumnConverter;
import com.lidroid.xutils.db.converter.ColumnConverterFactory;
import com.lidroid.xutils.db.sqlite.FinderLazyLoader;
import com.lidroid.xutils.db.sqlite.ForeignLazyLoader;
import com.lidroid.xutils.util.LogUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.List;

public class ColumnUtils {
    private static final HashSet<String> DB_PRIMITIVE_TYPES = new HashSet(14);

    private ColumnUtils() {
    }

    static {
        DB_PRIMITIVE_TYPES.add(Integer.TYPE.getName());
        DB_PRIMITIVE_TYPES.add(Long.TYPE.getName());
        DB_PRIMITIVE_TYPES.add(Short.TYPE.getName());
        DB_PRIMITIVE_TYPES.add(Byte.TYPE.getName());
        DB_PRIMITIVE_TYPES.add(Float.TYPE.getName());
        DB_PRIMITIVE_TYPES.add(Double.TYPE.getName());
        DB_PRIMITIVE_TYPES.add(Integer.class.getName());
        DB_PRIMITIVE_TYPES.add(Long.class.getName());
        DB_PRIMITIVE_TYPES.add(Short.class.getName());
        DB_PRIMITIVE_TYPES.add(Byte.class.getName());
        DB_PRIMITIVE_TYPES.add(Float.class.getName());
        DB_PRIMITIVE_TYPES.add(Double.class.getName());
        DB_PRIMITIVE_TYPES.add(String.class.getName());
        DB_PRIMITIVE_TYPES.add(byte[].class.getName());
    }

    public static boolean isDbPrimitiveType(Class<?> fieldType) {
        return DB_PRIMITIVE_TYPES.contains(fieldType.getName());
    }

    public static Method getColumnGetMethod(Class<?> entityType, Field field) {
        String fieldName = field.getName();
        Method getMethod = null;
        if (field.getType() == Boolean.TYPE) {
            getMethod = getBooleanColumnGetMethod(entityType, fieldName);
        }
        if (getMethod == null) {
            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            try {
                getMethod = entityType.getDeclaredMethod(methodName, new Class[0]);
            } catch (NoSuchMethodException e) {
                LogUtils.d(new StringBuilder(String.valueOf(methodName)).append(" not exist").toString());
            }
        }
        if (getMethod != null || Object.class.equals(entityType.getSuperclass())) {
            return getMethod;
        }
        return getColumnGetMethod(entityType.getSuperclass(), field);
    }

    public static Method getColumnSetMethod(Class<?> entityType, Field field) {
        String fieldName = field.getName();
        Method setMethod = null;
        if (field.getType() == Boolean.TYPE) {
            setMethod = getBooleanColumnSetMethod(entityType, field);
        }
        if (setMethod == null) {
            String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            try {
                setMethod = entityType.getDeclaredMethod(methodName, new Class[]{field.getType()});
            } catch (NoSuchMethodException e) {
                LogUtils.d(new StringBuilder(String.valueOf(methodName)).append(" not exist").toString());
            }
        }
        if (setMethod != null || Object.class.equals(entityType.getSuperclass())) {
            return setMethod;
        }
        return getColumnSetMethod(entityType.getSuperclass(), field);
    }

    public static String getColumnNameByField(Field field) {
        Column column = (Column) field.getAnnotation(Column.class);
        if (column != null && !TextUtils.isEmpty(column.column())) {
            return column.column();
        }
        Id id = (Id) field.getAnnotation(Id.class);
        if (id != null && !TextUtils.isEmpty(id.column())) {
            return id.column();
        }
        Foreign foreign = (Foreign) field.getAnnotation(Foreign.class);
        if (foreign != null && !TextUtils.isEmpty(foreign.column())) {
            return foreign.column();
        }
        if (((Finder) field.getAnnotation(Finder.class)) != null) {
            return field.getName();
        }
        return field.getName();
    }

    public static String getForeignColumnNameByField(Field field) {
        Foreign foreign = (Foreign) field.getAnnotation(Foreign.class);
        if (foreign != null) {
            return foreign.foreign();
        }
        return field.getName();
    }

    public static String getColumnDefaultValue(Field field) {
        Column column = (Column) field.getAnnotation(Column.class);
        if (column == null || TextUtils.isEmpty(column.defaultValue())) {
            return null;
        }
        return column.defaultValue();
    }

    public static boolean isTransient(Field field) {
        return field.getAnnotation(Transient.class) != null;
    }

    public static boolean isForeign(Field field) {
        return field.getAnnotation(Foreign.class) != null;
    }

    public static boolean isFinder(Field field) {
        return field.getAnnotation(Finder.class) != null;
    }

    public static boolean isUnique(Field field) {
        return field.getAnnotation(Unique.class) != null;
    }

    public static boolean isNotNull(Field field) {
        return field.getAnnotation(NotNull.class) != null;
    }

    public static String getCheck(Field field) {
        Check check = (Check) field.getAnnotation(Check.class);
        if (check != null) {
            return check.value();
        }
        return null;
    }

    public static Class<?> getForeignEntityType(Foreign foreignColumn) {
        Class<?> result = foreignColumn.getColumnField().getType();
        if (result.equals(ForeignLazyLoader.class) || result.equals(List.class)) {
            return ((ParameterizedType) foreignColumn.getColumnField().getGenericType()).getActualTypeArguments()[0];
        }
        return result;
    }

    public static Class<?> getFinderTargetEntityType(Finder finderColumn) {
        Class<?> result = finderColumn.getColumnField().getType();
        if (result.equals(FinderLazyLoader.class) || result.equals(List.class)) {
            return ((ParameterizedType) finderColumn.getColumnField().getGenericType()).getActualTypeArguments()[0];
        }
        return result;
    }

    public static Object convert2DbColumnValueIfNeeded(Object value) {
        Object result = value;
        if (value == null) {
            return result;
        }
        Class<?> valueType = value.getClass();
        if (isDbPrimitiveType(valueType)) {
            return result;
        }
        ColumnConverter converter = ColumnConverterFactory.getColumnConverter(valueType);
        if (converter != null) {
            return converter.fieldValue2ColumnValue(value);
        }
        return value;
    }

    private static boolean isStartWithIs(String fieldName) {
        return fieldName != null && fieldName.startsWith("is");
    }

    private static Method getBooleanColumnGetMethod(Class<?> entityType, String fieldName) {
        String methodName = "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        if (isStartWithIs(fieldName)) {
            methodName = fieldName;
        }
        try {
            return entityType.getDeclaredMethod(methodName, new Class[0]);
        } catch (NoSuchMethodException e) {
            LogUtils.d(new StringBuilder(String.valueOf(methodName)).append(" not exist").toString());
            return null;
        }
    }

    private static Method getBooleanColumnSetMethod(Class<?> entityType, Field field) {
        String methodName;
        String fieldName = field.getName();
        if (isStartWithIs(field.getName())) {
            methodName = "set" + fieldName.substring(2, 3).toUpperCase() + fieldName.substring(3);
        } else {
            methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        }
        try {
            return entityType.getDeclaredMethod(methodName, new Class[]{field.getType()});
        } catch (NoSuchMethodException e) {
            LogUtils.d(new StringBuilder(String.valueOf(methodName)).append(" not exist").toString());
            return null;
        }
    }
}
