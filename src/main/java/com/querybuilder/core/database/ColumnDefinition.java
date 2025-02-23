package com.querybuilder.core.database;

public class ColumnDefinition {
  private final String name;
  private final String type;
  private final boolean notNull;
  private final boolean primaryKey;

  public ColumnDefinition(String name, String type, boolean notNull, boolean primaryKey) {
      this.name = name;
      this.type = type;
      this.notNull = notNull;
      this.primaryKey = primaryKey;
  }

  public String getName() {
      return name;
  }

  public String getType() {
      return type;
  }

  public boolean isNotNull() {
      return notNull;
  }

  public boolean isPrimaryKey() {
      return primaryKey;
  }
}
