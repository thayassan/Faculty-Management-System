package com.faculty.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Central place for obtaining a JDBC connection to the MySQL database.
 *
 * IMPORTANT: Update DB_URL / DB_USER / DB_PASSWORD to match your local
 * MySQL setup before running the application. You must also add the
 * MySQL Connector/J jar to your classpath (mysql-connector-j-x.x.x.jar).
 */
public class DBConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/faculty_management_system?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Add mysql-connector-j to your classpath.");
        }
    }

    private DBConnection() {
        // utility class
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
