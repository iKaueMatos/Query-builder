package com.querybuilder.core.environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManagerDev {
    private static final String SQLITE_DB_URL = "jdbc:sqlite:dev-database.sqlite";

    public static Connection connect() throws SQLException {
        if (!DatabaseManager.isDevelopment()) {
            throw new IllegalStateException("Modo de desenvolvimento desabilitado. Não é possível usar SQLite.");
        }
        return DriverManager.getConnection(SQLITE_DB_URL);
    }

    public static void initializeDatabase() {
        if (DatabaseManager.isDevelopment()) {
            try (Connection connection = connect()) {
                initializeTables(connection);
                System.out.println("Tabelas criadas ou já existem no banco SQLite.");
            } catch (SQLException e) {
                System.err.println("Erro ao inicializar o banco de dados SQLite.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Modo de desenvolvimento desabilitado. Nenhuma inicialização do banco SQLite foi feita.");
        }
    }

    private static void initializeTables(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.execute("");
        } catch (SQLException e) {
            System.err.println("Erro ao criar as tabelas no banco SQLite.");
            e.printStackTrace();
        }
    }
}
