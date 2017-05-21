package com.lidroid.xutils.db.sqlite;

import android.text.TextUtils;
import com.lidroid.xutils.db.converter.ColumnConverterFactory;
import com.lidroid.xutils.db.table.ColumnUtils;
import com.supersonicads.sdk.utils.Constants.RequestParameters;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class WhereBuilder {
    private final List<String> whereItems = new ArrayList();

    private WhereBuilder() {
    }

    public static WhereBuilder b() {
        return new WhereBuilder();
    }

    public static WhereBuilder b(String columnName, String op, Object value) {
        WhereBuilder result = new WhereBuilder();
        result.appendCondition(null, columnName, op, value);
        return result;
    }

    public WhereBuilder and(String columnName, String op, Object value) {
        appendCondition(this.whereItems.size() == 0 ? null : "AND", columnName, op, value);
        return this;
    }

    public WhereBuilder or(String columnName, String op, Object value) {
        appendCondition(this.whereItems.size() == 0 ? null : "OR", columnName, op, value);
        return this;
    }

    public WhereBuilder expr(String expr) {
        this.whereItems.add(new StringBuilder(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(expr).toString());
        return this;
    }

    public WhereBuilder expr(String columnName, String op, Object value) {
        appendCondition(null, columnName, op, value);
        return this;
    }

    public int getWhereItemSize() {
        return this.whereItems.size();
    }

    public String toString() {
        if (this.whereItems.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String item : this.whereItems) {
            sb.append(item);
        }
        return sb.toString();
    }

    private void appendCondition(String conj, String columnName, String op, Object value) {
        StringBuilder sqlSb = new StringBuilder();
        if (this.whereItems.size() > 0) {
            sqlSb.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        }
        if (!TextUtils.isEmpty(conj)) {
            sqlSb.append(new StringBuilder(String.valueOf(conj)).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).toString());
        }
        sqlSb.append(columnName);
        if ("!=".equals(op)) {
            op = "<>";
        } else if ("==".equals(op)) {
            op = RequestParameters.EQUAL;
        }
        if (value != null) {
            sqlSb.append(new StringBuilder(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(op).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).toString());
            Iterable<?> items;
            ArrayList<Object> arrayList;
            int len;
            int i;
            Object items2;
            String valueStr;
            if ("IN".equalsIgnoreCase(op)) {
                items = null;
                if (value instanceof Iterable) {
                    items = (Iterable) value;
                } else if (value.getClass().isArray()) {
                    arrayList = new ArrayList();
                    len = Array.getLength(value);
                    for (i = 0; i < len; i++) {
                        arrayList.add(Array.get(value, i));
                    }
                    items2 = arrayList;
                }
                if (items != null) {
                    StringBuffer stringBuffer = new StringBuffer("(");
                    for (Object item : items) {
                        Object itemColValue = ColumnUtils.convert2DbColumnValueIfNeeded(item);
                        if (ColumnDbType.TEXT.equals(ColumnConverterFactory.getDbColumnType(itemColValue.getClass()))) {
                            valueStr = itemColValue.toString();
                            if (valueStr.indexOf(39) != -1) {
                                valueStr = valueStr.replace("'", "''");
                            }
                            stringBuffer.append("'" + valueStr + "'");
                        } else {
                            stringBuffer.append(itemColValue);
                        }
                        stringBuffer.append(",");
                    }
                    stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                    stringBuffer.append(")");
                    sqlSb.append(stringBuffer.toString());
                } else {
                    throw new IllegalArgumentException("value must be an Array or an Iterable.");
                }
            } else if ("BETWEEN".equalsIgnoreCase(op)) {
                items = null;
                if (value instanceof Iterable) {
                    items = (Iterable) value;
                } else if (value.getClass().isArray()) {
                    arrayList = new ArrayList();
                    len = Array.getLength(value);
                    for (i = 0; i < len; i++) {
                        arrayList.add(Array.get(value, i));
                    }
                    items2 = arrayList;
                }
                if (items != null) {
                    Iterator<?> iterator = items.iterator();
                    if (iterator.hasNext()) {
                        Object start = iterator.next();
                        if (iterator.hasNext()) {
                            Object end = iterator.next();
                            Object startColValue = ColumnUtils.convert2DbColumnValueIfNeeded(start);
                            Object endColValue = ColumnUtils.convert2DbColumnValueIfNeeded(end);
                            if (ColumnDbType.TEXT.equals(ColumnConverterFactory.getDbColumnType(startColValue.getClass()))) {
                                String startStr = startColValue.toString();
                                if (startStr.indexOf(39) != -1) {
                                    startStr = startStr.replace("'", "''");
                                }
                                String endStr = endColValue.toString();
                                if (endStr.indexOf(39) != -1) {
                                    endStr = endStr.replace("'", "''");
                                }
                                sqlSb.append("'" + startStr + "'");
                                sqlSb.append(" AND ");
                                sqlSb.append("'" + endStr + "'");
                            } else {
                                sqlSb.append(startColValue);
                                sqlSb.append(" AND ");
                                sqlSb.append(endColValue);
                            }
                        } else {
                            throw new IllegalArgumentException("value must have tow items.");
                        }
                    }
                    throw new IllegalArgumentException("value must have tow items.");
                }
                throw new IllegalArgumentException("value must be an Array or an Iterable.");
            } else {
                value = ColumnUtils.convert2DbColumnValueIfNeeded(value);
                if (ColumnDbType.TEXT.equals(ColumnConverterFactory.getDbColumnType(value.getClass()))) {
                    valueStr = value.toString();
                    if (valueStr.indexOf(39) != -1) {
                        valueStr = valueStr.replace("'", "''");
                    }
                    sqlSb.append("'" + valueStr + "'");
                } else {
                    sqlSb.append(value);
                }
            }
        } else if (RequestParameters.EQUAL.equals(op)) {
            sqlSb.append(" IS NULL");
        } else if ("<>".equals(op)) {
            sqlSb.append(" IS NOT NULL");
        } else {
            sqlSb.append(new StringBuilder(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(op).append(" NULL").toString());
        }
        this.whereItems.add(sqlSb.toString());
    }
}
