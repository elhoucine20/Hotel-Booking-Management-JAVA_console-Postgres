package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    public static final String url = "jdbc:postgresql://localhost:5432/hotelManagement";
    public static final String userName = "postgres";
    public static final String password = "12345678";
    // "jdbc:postgresql://localhost:5432/hotelManagement"
    // "postgres"
    // "12345678"


    public DatabaseConfig() {
    }
}
