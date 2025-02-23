package com.querybuilder.builder;

import com.querybuilder.Enum.Operator;
import com.querybuilder.Enum.user.TableColumn;
import com.querybuilder.core.database.Condition;
import com.querybuilder.core.database.InsertColumnValue;
import com.querybuilder.core.database.OrderByClause;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QueryBuilder<T extends Enum<T> & TableColumn> {
    private String table;
    private final List<Condition> conditions = new ArrayList<>();
    private final List<String> columnsToSelect = new ArrayList<>();
    private final List<OrderByClause> orderByClauses = new ArrayList<>();
    private final List<String> joins = new ArrayList<>();
    private final List<InsertColumnValue> insertColumns = new ArrayList<>();
    private int limit = -1;

    private Class<?> modelClass;
    private String tableName;

    public QueryBuilder(Class<?> modelClass) {
        this.modelClass = modelClass;
        this.table = modelClass.getSimpleName().toLowerCase();
    }

    public QueryBuilder(String tableName) {
        this.table = tableName;
    }

    private String getTableName() {
        if (table != null) {
            return table;
        }
        if (tableName != null) {
            return tableName;
        }
        throw new IllegalStateException("Table name must be specified.");
    }

    public QueryBuilder<T> select(String... columns) {
        for (String column : columns) {
            columnsToSelect.add(column.toLowerCase());
        }
        return this;
    }

    public QueryBuilder<T> where(String column, Operator operator, Object value) {
        conditions.add(new Condition(column, operator, value));
        return this;
    }

    public QueryBuilder<T> and(String column, Operator operator, Object value) {
        return where(column, operator, value);
    }

    public QueryBuilder<T> orderBy(T column, boolean ascending) {
        orderByClauses.add(new OrderByClause(column, ascending));
        return this;
    }

    public QueryBuilder<T> is_null(T column) {
        conditions.add(new Condition(column.toString().toLowerCase(), Operator.IS_NULL, null));
        return this;
    }

    public QueryBuilder<T> limit(int limit) {
        this.limit = limit;
        return this;
    }

    public QueryBuilder<T> join(Class<?> joinTable, String baseColumn, String joinColumn) {
        String joinTableName = joinTable.getSimpleName().toLowerCase();
        joins.add("INNER JOIN " + joinTableName + " ON " + baseColumn + " = " + joinColumn);
        return this;
    }

    public QueryBuilder<T> insertInto(String table) {
        this.table = table;
        return this;
    }

    public QueryBuilder<T> set(String column, Object value) {
        insertColumns.add(new InsertColumnValue(column, value));
        return this;
    }

    public QueryBuilder<T> offset(int offset) {
        this.limit = offset;
        return this;
    }

    public String build() {
        String tableName = getTableName();
        if (!insertColumns.isEmpty()) {
            StringBuilder query = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
            List<String> columnNames = new ArrayList<>();
            List<String> placeholders = new ArrayList<>();
            for (InsertColumnValue columnValue : insertColumns) {
                columnNames.add(columnValue.getColumn());
                placeholders.add("?");
            }

            query.append(String.join(", ", columnNames)).append(") ");
            query.append("VALUES (").append(String.join(", ", placeholders)).append(")");

            return query.toString();
        }

        StringBuilder query = new StringBuilder("SELECT ");
        if (columnsToSelect.isEmpty()) {
            query.append("*");
        } else {
            query.append(String.join(", ", columnsToSelect));
        }

        query.append(" FROM ").append(table);

        if (!joins.isEmpty()) {
            query.append(" ").append(String.join(" ", joins));
        }

        if (!conditions.isEmpty()) {
            query.append(" WHERE ");
            List<String> conditionStrings = new ArrayList<>();
            for (Condition condition : conditions) {
                String columnValue = condition.getColumn().toString();
                if (condition.getOperator() == Operator.IN) {
                    conditionStrings.add(columnValue + " " + condition.getOperator().getSymbol() + " (?)");
                } else {
                    conditionStrings.add(columnValue + " " + condition.getOperator().getSymbol() + " ?");
                }
            }
            query.append(String.join(" AND ", conditionStrings));
        }

        if (!orderByClauses.isEmpty()) {
            query.append(" ORDER BY ");
            List<String> orderByStrings = new ArrayList<>();
            for (OrderByClause orderBy : orderByClauses) {
                orderByStrings.add(orderBy.getColumn().toString() + (orderBy.isAscending() ? " ASC" : " DESC"));
            }
            query.append(String.join(", ", orderByStrings));
        }

        if (limit > -1) {
            query.append(" LIMIT ").append(limit);
        }

        return query.toString();
    }

    public PreparedStatement buildPreparedStatement(Connection conn) throws SQLException {
        String sql = build();
        PreparedStatement pstmt = conn.prepareStatement(sql);

        int paramIndex = 1;
        for (Condition condition : conditions) {
            if (condition.getOperator() == Operator.IN) {
                pstmt.setObject(paramIndex++, condition.getValue());
            } else {
                pstmt.setObject(paramIndex++, condition.getValue());
            }
        }

        return pstmt;
    }

    public String buildCreateTable() {
        String tableName = getTableName();
        StringBuilder query = new StringBuilder("CREATE TABLE ").append(tableName).append(" (");
        var fields = modelClass.getDeclaredFields();
        List<String> columnDefinitions = new ArrayList<>();
        for (var field : fields) {
            String columnName = field.getName();
            String columnType = getSqlType(field.getType());

            columnDefinitions.add(columnName + " " + columnType);
        }

        query.append(String.join(", ", columnDefinitions));
        query.append(")");

        return query.toString();
    }

    private String getSqlType(Class<?> fieldType) {
        if (fieldType == String.class) {
            return "VARCHAR(255)";
        } else if (fieldType == int.class || fieldType == Integer.class) {
            return "INT";
        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
            return "BOOLEAN";
        } else if (fieldType == double.class || fieldType == Double.class) {
            return "DOUBLE";
        } else if (fieldType == float.class || fieldType == Float.class) {
            return "FLOAT";
        } else if (fieldType == java.util.Date.class) {
            return "DATE";
        } else if (fieldType == Long.class) {
            return "BIGINT";
        }
        return "VARCHAR(255)";
    }

    public QueryBuilder<T> update(T column, Object value) {
        insertColumns.add(new InsertColumnValue(column.toString().toLowerCase(), value));
        return this;
    }

    public String buildUpdateQuery(Object objectToUpdate, Object originalObject) {
        String tableName = getTableName();
        StringBuilder query = new StringBuilder("UPDATE ").append(tableName).append(" SET ");

        List<String> updatedColumns = new ArrayList<>();
        List<Object> updatedValues = new ArrayList<>();
        for (var field : objectToUpdate.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object newValue = field.get(objectToUpdate);
                Object originalValue = field.get(originalObject);
                if (newValue != null && !newValue.equals(originalValue)) {
                    updatedColumns.add(field.getName().toLowerCase() + " = ?");
                    updatedValues.add(newValue);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (updatedColumns.isEmpty()) {
            throw new IllegalStateException("No fields have been updated.");
        }

        query.append(String.join(", ", updatedColumns));
        query.append(" WHERE id = ?");
        updatedValues.add(getId(objectToUpdate));

        return query.toString();
    }

    private Object getId(Object object) {
        try {
            var field = object.getClass().getDeclaredField("id");
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException("ID field not found or inaccessible.", e);
        }
    }

    public PreparedStatement buildUpdatePreparedStatement(Connection conn, Object objectToUpdate, Object originalObject) throws SQLException {
        String sql = buildUpdateQuery(objectToUpdate, originalObject);
        PreparedStatement pstmt = conn.prepareStatement(sql);

        List<Object> updatedValues = new ArrayList<>();
        for (var field : objectToUpdate.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object newValue = field.get(objectToUpdate);
                Object originalValue = field.get(originalObject);
                if (newValue != null && !newValue.equals(originalValue)) {
                    updatedValues.add(newValue);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        updatedValues.add(getId(objectToUpdate));

        int paramIndex = 1;
        for (Object value : updatedValues) {
            pstmt.setObject(paramIndex++, value);
        }

        return pstmt;
    }
}