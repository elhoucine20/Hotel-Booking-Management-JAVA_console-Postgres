package config;

import db.DatabaseConnection;
import org.flywaydb.core.Flyway;

import java.sql.Connection;

public class DatabaseInitializer {


    public static void initialize(){

        //Connection connection = DatabaseConnection.getInstance().getConnection();

        Flyway flyway = Flyway.configure().
                dataSource(DatabaseConfig.url,DatabaseConfig.userName,DatabaseConfig.password).load();
        flyway.migrate();
        System.out.println("database migrate avec succes");
    }
}
