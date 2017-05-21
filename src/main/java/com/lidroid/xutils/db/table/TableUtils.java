package com.lidroid.xutils.db.table;

import android.text.TextUtils;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.converter.ColumnConverterFactory;
import com.lidroid.xutils.util.LogUtils;
import com.nativex.monetization.mraid.objects.ObjectNames.CalendarEntryData;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class TableUtils {
    private static ConcurrentHashMap<String, HashMap<String, Column>> entityColumnsMap = new ConcurrentHashMap();
    private static ConcurrentHashMap<String, Id> entityIdMap = new ConcurrentHashMap();

    private TableUtils() {
    }

    public static String getTableName(Class<?> entityType) {
        Table table = (Table) entityType.getAnnotation(Table.class);
        if (table == null || TextUtils.isEmpty(table.name())) {
            return entityType.getName().replace('.', '_');
        }
        return table.name();
    }

    public static String getExecAfterTableCreated(Class<?> entityType) {
        Table table = (Table) entityType.getAnnotation(Table.class);
        if (table != null) {
            return table.execAfterTableCreated();
        }
        return null;
    }

    static synchronized HashMap<String, Column> getColumnMap(Class<?> entityType) {
        HashMap<String, Column> hashMap;
        synchronized (TableUtils.class) {
            if (entityColumnsMap.containsKey(entityType.getName())) {
                hashMap = (HashMap) entityColumnsMap.get(entityType.getName());
            } else {
                HashMap<String, Column> columnMap = new HashMap();
                addColumns2Map(entityType, getPrimaryKeyFieldName(entityType), columnMap);
                entityColumnsMap.put(entityType.getName(), columnMap);
                hashMap = columnMap;
            }
        }
        return hashMap;
    }

    private static void addColumns2Map(Class<?> entityType, String primaryKeyFieldName, HashMap<String, Column> columnMap) {
        if (!Object.class.equals(entityType)) {
            try {
                for (Field field : entityType.getDeclaredFields()) {
                    if (!(ColumnUtils.isTransient(field) || Modifier.isStatic(field.getModifiers()))) {
                        if (ColumnConverterFactory.isSupportColumnConverter(field.getType())) {
                            if (!field.getName().equals(primaryKeyFieldName)) {
                                Column column = new Column(entityType, field);
                                if (!columnMap.containsKey(column.getColumnName())) {
                                    columnMap.put(column.getColumnName(), column);
                                }
                            }
                        } else if (ColumnUtils.isForeign(field)) {
                            Foreign column2 = new Foreign(entityType, field);
                            if (!columnMap.containsKey(column2.getColumnName())) {
                                columnMap.put(column2.getColumnName(), column2);
                            }
                        } else if (ColumnUtils.isFinder(field)) {
                            Finder column3 = new Finder(entityType, field);
                            if (!columnMap.containsKey(column3.getColumnName())) {
                                columnMap.put(column3.getColumnName(), column3);
                            }
                        }
                    }
                }
                if (!Object.class.equals(entityType.getSuperclass())) {
                    addColumns2Map(entityType.getSuperclass(), primaryKeyFieldName, columnMap);
                }
            } catch (Throwable e) {
                LogUtils.e(e.getMessage(), e);
            }
        }
    }

    static Column getColumnOrId(Class<?> entityType, String columnName) {
        if (getPrimaryKeyColumnName(entityType).equals(columnName)) {
            return getId(entityType);
        }
        return (Column) getColumnMap(entityType).get(columnName);
    }

    static synchronized Id getId(Class<?> entityType) {
        Id id;
        int i = 0;
        synchronized (TableUtils.class) {
            if (Object.class.equals(entityType)) {
                throw new RuntimeException("field 'id' not found");
            }
            if (entityIdMap.containsKey(entityType.getName())) {
                id = (Id) entityIdMap.get(entityType.getName());
            } else {
                Field primaryKeyField = null;
                Field[] fields = entityType.getDeclaredFields();
                if (fields != null) {
                    int length;
                    Field field;
                    for (Field field2 : fields) {
                        if (field2.getAnnotation(Id.class) != null) {
                            primaryKeyField = field2;
                            break;
                        }
                    }
                    if (primaryKeyField == null) {
                        length = fields.length;
                        while (i < length) {
                            field2 = fields[i];
                            if (CalendarEntryData.ID.equals(field2.getName()) || "_id".equals(field2.getName())) {
                                primaryKeyField = field2;
                                break;
                            }
                            i++;
                        }
                    }
                }
                if (primaryKeyField == null) {
                    id = getId(entityType.getSuperclass());
                } else {
                    Id id2 = new Id(entityType, primaryKeyField);
                    entityIdMap.put(entityType.getName(), id2);
                    id = id2;
                }
            }
        }
        return id;
    }

    private static String getPrimaryKeyFieldName(Class<?> entityType) {
        Id id = getId(entityType);
        return id == null ? null : id.getColumnField().getName();
    }

    private static String getPrimaryKeyColumnName(Class<?> entityType) {
        Id id = getId(entityType);
        return id == null ? null : id.getColumnName();
    }
}
