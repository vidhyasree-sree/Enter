package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/employee_attendance";

    private static final String USER = "root";

    private static final String PASSWORD = "MySQL@1234";

    public static Connection getConnection() {

        try {
            Connection connection =
                    DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("MySQL Database Connected Successfully!");

            return connection;

        } catch (SQLException e) {

            System.out.println("Database Connection Failed!");
            e.printStackTrace();

            return null;
        }
    }

    public static void main(String[] args) {

        Connection connection = getConnection();

        if (connection != null) {
            System.out.println("Connection Test Successful!");

            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}