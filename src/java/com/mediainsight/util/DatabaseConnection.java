package com.mediainsight.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3307/media_insight_db?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "db";

    private static boolean driverLoaded = false;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            driverLoaded = true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        if (!driverLoaded) {
            throw new SQLException("MySQL JDBC Driver not loaded");
        }

        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("Connection created: " + conn.getMetaData().getDatabaseProductName() +
                        " version " + conn.getMetaData().getDatabaseProductVersion());
            return conn;
        } catch (SQLException e) {
                System.err.println("Connection error:");
                System.err.println("URL: " + DB_URL);
                System.err.println("User: " + DB_USER);
                System.err.println("Error code: " + e.getErrorCode());
                System.err.println("SQL State: " + e.getSQLState());
            throw e;
        }
    }
}