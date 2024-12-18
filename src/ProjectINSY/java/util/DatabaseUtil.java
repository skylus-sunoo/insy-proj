/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProjectINSY.java.util;

/**
 *
 * @author admin
 */
import ProjectINSY.java.Main;
import static ProjectINSY.java.util.MessageUtil.paneDatabaseError;
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

    public static boolean recordExists(Connection conn, String table, String column, String... params) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + table + " WHERE " + column + " = ?";
        
        try (PreparedStatement pst = prepareQueryWithParameters(conn, query, params); ResultSet rs = pst.executeQuery()) {
//            if (rs.next() && rs.getInt(1) > 0) {
//                JOptionPane.showMessageDialog(null, "Value already exists: " + params[0], "Error", JOptionPane.ERROR_MESSAGE);
//            }
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    public static void executeUpdate(Connection conn, String query, String... params) throws SQLException {
        try (PreparedStatement pst = prepareQueryWithParameters(conn, query, params)) {
            pst.executeUpdate();
        }
    }

    public static String getCategoryByItem(String item_name) {
        String item_category = "";
        String query = "SELECT item_category FROM " + Main.TB_CATALOG_ITEM + " WHERE item_name = ?";

        
        try (Connection conn = getConnection(Main.DB_NAME); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, item_name);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                item_category = rs.getString("item_category");
            }

        } catch (SQLException e) {
            paneDatabaseError(e);
        }

        return item_category;
    }
}

//    -- Step 1: Get the highest category_id value
//    SET @max_id = (SELECT MAX(category_id) FROM tb_catalog_category);
//
//    -- Step 2: Update category_id values by adding the highest category_id value to each
//    UPDATE tb_catalog_category SET category_id = category_id + @max_id;
//
//    -- Step 3: Reset the category_id values according to alphabetical order of category_name
//    SET @row_num = 0;
//    UPDATE tb_catalog_category t
//    JOIN (
//        SELECT category_id, @row_num := @row_num + 1 AS new_category_id
//        FROM tb_catalog_category
//        ORDER BY category_name
//    ) AS ordered_table
//    ON t.category_id = ordered_table.category_id
//    SET t.category_id = ordered_table.new_category_id;
//
//    -- Step 4: Alter the AUTO_INCREMENT value to match the number of rows in the table
//    SET @count = (SELECT COUNT(*) FROM tb_catalog_category);
//    SET @auto_increment_value = @count + 1;
//    ALTER TABLE tb_catalog_category AUTO_INCREMENT = @auto_increment_value;
