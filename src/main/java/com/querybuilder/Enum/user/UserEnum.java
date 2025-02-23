package com.querybuilder.Enum.user;

public enum UserEnum implements TableColumn {
    ID("id"),
    USERNAME("username"),
    PASSWORD("password"),
    EMAIL("email"),
    CREATED_AT("created_at"),
    UPDATED_AT("updated_at"),
    IS_ACTIVE("is_active"),
    TOKEN("token"),
    COMPANY_ID("company_id"),
    ALL_COLUMN("*");

    private final String value;

    UserEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getOperatorSymbol() {
        return "";
    }
}
