package com.querybuilder.core.database;

public class InsertColumnValue {
    private final String column;
    private final Object value;

    public InsertColumnValue(String column, Object value) {
        this.column = column;
        this.value = value;
    }

    public String getColumn() {
        return column;
    }

    public Object getValue() {
        return value;
    }
}
