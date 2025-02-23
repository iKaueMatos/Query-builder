package com.querybuilder;

import com.querybuilder.core.environment.DatabaseInitializer;

import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws SQLException {
        DatabaseInitializer.initialize();
    }
}
