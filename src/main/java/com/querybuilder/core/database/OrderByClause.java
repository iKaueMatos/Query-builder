package com.querybuilder.core.database;

public class OrderByClause<T> {
    private final T column;
    private final boolean ascending;

    public OrderByClause(T column, boolean ascending) {
        this.column = column;
        this.ascending = ascending;
    }

    public T getColumn() {
        return column;
    }

    public boolean isAscending() {
        return ascending;
    }
}
