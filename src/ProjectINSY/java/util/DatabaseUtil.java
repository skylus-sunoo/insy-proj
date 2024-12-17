/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProjectINSY.java.util;

/**
 *
 * @author admin
 */
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class DatabaseUtil {

    public static Connection getConnection(String dbName) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/" + dbName;
        String username = "root";
        String password = "";

        return DriverManager.getConnection(url, username, password);
    }

    public static PreparedStatement prepareQuery(Connection conn, String query) throws SQLException {
        return conn.prepareStatement(query);
    }

    public static PreparedStatement prepareQueryWithParameters(Connection conn, String query, String... params) throws SQLException {
        PreparedStatement pst = conn.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            pst.setString(i + 1, params[i]);
        }
        return pst;
    }
    
    public static boolean recordExists(Connection conn, String query, String... params) throws SQLException {
        try (PreparedStatement pst = prepareQueryWithParameters(conn, query, params); ResultSet rs = pst.executeQuery()) {
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    public static void executeUpdate(Connection conn, String query, String... params) throws SQLException {
        try (PreparedStatement pst = prepareQueryWithParameters(conn, query, params)) {
            pst.executeUpdate();
        }
    }
}
