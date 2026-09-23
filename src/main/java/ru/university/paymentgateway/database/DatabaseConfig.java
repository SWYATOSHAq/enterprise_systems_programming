package ru.university.paymentgateway.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private final String url;
    private final String user;
    private final String password;

    public DatabaseConfig() {
        this.url = read_value("DB_URL", "jdbc:postgresql://localhost:5432/payment_gateway");
        this.user = read_value("DB_USER", "postgres");
        this.password = System.getenv("DB_PASSWORD");

        if (password == null || password.isBlank()) {
            throw new DatabaseException("Не задана переменная окружения DB_PASSWORD.");
        }
    }

    public Connection get_connection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    private String read_value(String name, String default_value) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            return default_value;
        }
        return value;
    }
}
