package vn.edu.eaut.lab5.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
    private static final String URL =
            "jdbc:mysql://localhost:3306/minishop_db?useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "root";
    private static final String PASSWORD = "Khanh@12345";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("Ket noi CSDL thanh cong!");
            }
        } catch (SQLException e) {
            System.out.println("Ket noi CSDL that bai!");
            e.printStackTrace();
        }
    }
}
