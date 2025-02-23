package com.querybuilder.core.environment;


import java.sql.SQLException;

public class DatabaseInitializer {
    public static void initialize() throws SQLException {
        DatabaseManager.getInstance().connect();
    }
}
