package com.querybuilder.core.environment;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseManager {
    private static Properties properties = new Properties();
    private static String dbUrl;
    private static String dbUsername;
    private static String dbPassword;
    private static boolean isDevelopment;
    private static DatabaseManager instance;

    public static DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        return instance;
    }

    public DatabaseManager() { }

    static {
        try (InputStream input = DatabaseManager.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new IllegalStateException("Não foi possível encontrar o arquivo de propriedades.");
            }
            properties.load(input);

            dbUrl = properties.getProperty("db.url");
            dbUsername = properties.getProperty("db.username");
            dbPassword = properties.getProperty("db.password");
            isDevelopment = Boolean.parseBoolean(properties.getProperty("db.dev", "false"));

            if (!isDevelopment && (dbUrl == null || dbUsername == null || dbPassword == null)) {
                throw new IllegalArgumentException("Propriedades de banco de dados de produção ausentes.");
            }

            if (isDevelopment) {
                System.out.println("Modo de desenvolvimento detectado. Inicializando banco de dados...");
                DatabaseManagerDev.initializeDatabase();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao carregar as propriedades de configuração do banco de dados.", e);
        }
    }

    public static boolean isDevelopment() {
        return isDevelopment;
    }

    public static Connection connect() throws SQLException {
        if (isDevelopment) {
            System.out.println("Ambiente de desenvolvimento habilitado. Usando SQLite.");
            return DatabaseManagerDev.connect();
        } else {
            System.out.println("Modo de produção habilitado. Conectando ao banco de produção.");
            return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        }
    }

    public static void initializeDatabase() {
        if (isDevelopment) {
            System.out.println("Inicializando banco de dados SQLite para ambiente de desenvolvimento.");
            DatabaseManagerDev.initializeDatabase();
        } else {
            System.out.println("Banco de produção detectado. Nenhuma inicialização necessária.");
        }
    }
}
