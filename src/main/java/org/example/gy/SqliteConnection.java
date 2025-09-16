package org.example.gy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteConnection {
    private static Connection instance = null;

    private SqliteConnection() {
        String url = "jdbc:sqlite:users.db";
        try {
            instance = DriverManager.getConnection(url);
            System.out.println("Connected to SQLite database");
        } catch (SQLException sqlEx) {
            System.err.println("Failed to connect to SQLite database");
        }
    }

    public static Connection getInstance() {
        if (instance == null) {
            new SqliteConnection();
        }
        return instance;
    }

    public static void closeConnection() {
        if (instance != null) {
            try {
                instance.close();
                instance = null;
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing connection");
            }
        }
    }
}