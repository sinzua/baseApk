package com.lidroid.xutils.db.sqlite;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.table.Column;
import com.lidroid.xutils.db.table.ColumnUtils;
import com.lidroid.xutils.db.table.Finder;
import com.lidroid.xutils.db.table.Id;
import com.lidroid.xutils.db.table.KeyValue;
import com.lidroid.xutils.db.table.Table;
import com.lidroid.xutils.db.table.TableUtils;
import com.lidroid.xutils.exception.DbException;
import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class SqlInfoBuilder {
    private SqlInfoBuilder() {
    }

    public static SqlInfo buildInsertSqlInfo(DbUtils db, Object entity) throws DbException {
        List<KeyValue> keyValueList = entity2KeyValueList(db, entity);
        if (keyValueList.size() == 0) {
            return null;
        }
        SqlInfo result = new SqlInfo();
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("INSERT INTO ");
        sqlBuffer.append(TableUtils.getTableName(entity.getClass()));
        sqlBuffer.append(" (");
        for (KeyValue kv : keyValueList) {
            sqlBuffer.append(kv.key).append(",");
            result.addBindArgWithoutConverter(kv.value);
        }
        sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
        sqlBuffer.append(") VALUES (");
        int length = keyValueList.size();
        for (int i = 0; i < length; i++) {
            sqlBuffer.append("?,");
        }
        sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
        sqlBuffer.append(")");
        result.setSql(sqlBuffer.toString());
        return result;
    }

    public static SqlInfo buildReplaceSqlInfo(DbUtils db, Object entity) throws DbException {
        List<KeyValue> keyValueList = entity2KeyValueList(db, entity);
        if (keyValueList.size() == 0) {
            return null;
        }
        SqlInfo result = new SqlInfo();
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("REPLACE INTO ");
        sqlBuffer.append(TableUtils.getTableName(entity.getClass()));
        sqlBuffer.append(" (");
        for (KeyValue kv : keyValueList) {
            sqlBuffer.append(kv.key).append(",");
            result.addBindArgWithoutConverter(kv.value);
        }
        sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
        sqlBuffer.append(") VALUES (");
        int length = keyValueList.size();
        for (int i = 0; i < length; i++) {
            sqlBuffer.append("?,");
        }
        sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
        sqlBuffer.append(")");
        result.setSql(sqlBuffer.toString());
        return result;
    }

    private static String buildDeleteSqlByTableName(String tableName) {
        return "DELETE FROM " + tableName;
    }

    public static SqlInfo buildDeleteSqlInfo(DbUtils db, Object entity) throws DbException {
        SqlInfo result = new SqlInfo();
        Table table = Table.get(db, entity.getClass());
        Id id = table.id;
        Object idValue = id.getColumnValue(entity);
        if (idValue == null) {
            throw new DbException("this entity[" + entity.getClass() + "]'s id value is null");
        }
        StringBuilder sb = new StringBuilder(buildDeleteSqlByTableName(table.tableName));
        sb.append(" WHERE ").append(WhereBuilder.b(id.getColumnName(), RequestParameters.EQUAL, idValue));
        result.setSql(sb.toString());
        return result;
    }

    public static SqlInfo buildDeleteSqlInfo(DbUtils db, Class<?> entityType, Object idValue) throws DbException {
        SqlInfo result = new SqlInfo();
        Table table = Table.get(db, entityType);
        Id id = table.id;
        if (idValue == null) {
            throw new DbException("this entity[" + entityType + "]'s id value is null");
        }
        StringBuilder sb = new StringBuilder(buildDeleteSqlByTableName(table.tableName));
        sb.append(" WHERE ").append(WhereBuilder.b(id.getColumnName(), RequestParameters.EQUAL, idValue));
        result.setSql(sb.toString());
        return result;
    }

    public static SqlInfo buildDeleteSqlInfo(DbUtils db, Class<?> entityType, WhereBuilder whereBuilder) throws DbException {
        StringBuilder sb = new StringBuilder(buildDeleteSqlByTableName(Table.get(db, entityType).tableName));
        if (whereBuilder != null && whereBuilder.getWhereItemSize() > 0) {
            sb.append(" WHERE ").append(whereBuilder.toString());
        }
        return new SqlInfo(sb.toString());
    }

    public static SqlInfo buildUpdateSqlInfo(DbUtils db, Object entity, String... updateColumnNames) throws DbException {
        List<KeyValue> keyValueList = entity2KeyValueList(db, entity);
        if (keyValueList.size() == 0) {
            return null;
        }
        HashSet<String> updateColumnNameSet = null;
        if (updateColumnNames != null && updateColumnNames.length > 0) {
            updateColumnNameSet = new HashSet(updateColumnNames.length);
            Collections.addAll(updateColumnNameSet, updateColumnNames);
        }
        Table table = Table.get(db, entity.getClass());
        Id id = table.id;
        Object idValue = id.getColumnValue(entity);
        if (idValue == null) {
            throw new DbException("this entity[" + entity.getClass() + "]'s id value is null");
        }
        SqlInfo result = new SqlInfo();
        StringBuffer sqlBuffer = new StringBuffer("UPDATE ");
        sqlBuffer.append(table.tableName);
        sqlBuffer.append(" SET ");
        for (KeyValue kv : keyValueList) {
            if (updateColumnNameSet == null || updateColumnNameSet.contains(kv.key)) {
                sqlBuffer.append(kv.key).append("=?,");
                result.addBindArgWithoutConverter(kv.value);
            }
        }
        sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
        sqlBuffer.append(" WHERE ").append(WhereBuilder.b(id.getColumnName(), RequestParameters.EQUAL, idValue));
        result.setSql(sqlBuffer.toString());
        return result;
    }

    public static SqlInfo buildUpdateSqlInfo(DbUtils db, Object entity, WhereBuilder whereBuilder, String... updateColumnNames) throws DbException {
        List<KeyValue> keyValueList = entity2KeyValueList(db, entity);
        if (keyValueList.size() == 0) {
            return null;
        }
        HashSet<String> updateColumnNameSet = null;
        if (updateColumnNames != null && updateColumnNames.length > 0) {
            updateColumnNameSet = new HashSet(updateColumnNames.length);
            Collections.addAll(updateColumnNameSet, updateColumnNames);
        }
        String tableName = TableUtils.getTableName(entity.getClass());
        SqlInfo result = new SqlInfo();
        StringBuffer sqlBuffer = new StringBuffer("UPDATE ");
        sqlBuffer.append(tableName);
        sqlBuffer.append(" SET ");
        for (KeyValue kv : keyValueList) {
            if (updateColumnNameSet == null || updateColumnNameSet.contains(kv.key)) {
                sqlBuffer.append(kv.key).append("=?,");
                result.addBindArgWithoutConverter(kv.value);
            }
        }
        sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
        if (whereBuilder != null && whereBuilder.getWhereItemSize() > 0) {
            sqlBuffer.append(" WHERE ").append(whereBuilder.toString());
        }
        result.setSql(sqlBuffer.toString());
        return result;
    }

    public static SqlInfo buildCreateTableSqlInfo(DbUtils db, Class<?> entityType) throws DbException {
        Table table = Table.get(db, entityType);
        Id id = table.id;
        StringBuffer sqlBuffer = new StringBuffer();
        sqlBuffer.append("CREATE TABLE IF NOT EXISTS ");
        sqlBuffer.append(table.tableName);
        sqlBuffer.append(" ( ");
        if (id.isAutoIncrement()) {
            sqlBuffer.append("\"").append(id.getColumnName()).append("\"  ").append("INTEGER PRIMARY KEY AUTOINCREMENT,");
        } else {
            sqlBuffer.append("\"").append(id.getColumnName()).append("\"  ").append(id.getColumnDbType()).append(" PRIMARY KEY,");
        }
        for (Column column : table.columnMap.values()) {
            if (!(column instanceof Finder)) {
                sqlBuffer.append("\"").append(column.getColumnName()).append("\"  ");
                sqlBuffer.append(column.getColumnDbType());
                if (ColumnUtils.isUnique(column.getColumnField())) {
                    sqlBuffer.append(" UNIQUE");
                }
                if (ColumnUtils.isNotNull(column.getColumnField())) {
                    sqlBuffer.append(" NOT NULL");
                }
                String check = ColumnUtils.getCheck(column.getColumnField());
                if (check != null) {
                    sqlBuffer.append(" CHECK(").append(check).append(")");
                }
                sqlBuffer.append(",");
            }
        }
        sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
        sqlBuffer.append(" )");
        return new SqlInfo(sqlBuffer.toString());
    }

    private static KeyValue column2KeyValue(Object entity, Column column) {
        String key = column.getColumnName();
        if (key == null) {
            return null;
        }
        Object value = column.getColumnValue(entity);
        if (value == null) {
            value = column.getDefaultValue();
        }
        return new KeyValue(key, value);
    }

    public static List<KeyValue> entity2KeyValueList(DbUtils db, Object entity) {
        List<KeyValue> keyValueList = new ArrayList();
        Table table = Table.get(db, entity.getClass());
        Id id = table.id;
        if (!id.isAutoIncrement()) {
            keyValueList.add(new KeyValue(id.getColumnName(), id.getColumnValue(entity)));
        }
        for (Column column : table.columnMap.values()) {
            if (!(column instanceof Finder)) {
                KeyValue kv = column2KeyValue(entity, column);
                if (kv != null) {
                    keyValueList.add(kv);
                }
            }
        }
        return keyValueList;
    }
}
