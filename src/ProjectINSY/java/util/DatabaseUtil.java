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

    public static int generateNewBatch() {
        String query = "INSERT INTO " + Main.TB_ITEM_BATCH + " () VALUES ()";
        int newBatchId = 0;

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            PreparedStatement pst = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.executeUpdate();

            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    newBatchId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating batch failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            paneDatabaseError(e);
        }

        return newBatchId;
    }

    public static String getColumnValueByString(String table_name, String column_name_to_get, String column_name_to_search, String id) {
        String column_value = "";
        String query = "SELECT " + column_name_to_get + " FROM " + table_name + " WHERE " + column_name_to_search + " = ?";

        try (Connection conn = getConnection(Main.DB_NAME); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                column_value = rs.getString(column_name_to_get);
            }

        } catch (SQLException e) {
            paneDatabaseError(e);
        }

        return column_value;
    }

    public static String getColumnValueByInt(String table_name, String column_name_to_get, String column_name_to_search, int id) {
        String column_value = "";
        String query = "SELECT " + column_name_to_get + " FROM " + table_name + " WHERE " + column_name_to_search + " = ?";

        try (Connection conn = getConnection(Main.DB_NAME); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                column_value = rs.getString(column_name_to_get);
            }

        } catch (SQLException e) {
            paneDatabaseError(e);
        }

        return column_value;
    }

    public enum HistoryFrame {
        CATALOG, MANAGEMENT, TRACKER
    }

    public enum HistoryType {
        ADD, UPDATE, DELETE
    }

    public static void insertHistory(HistoryFrame HistoryFrame, HistoryType HistoryType, String history_item_code_start, String history_item_code_end, String history_desc, String history_user) {
        String query = "INSERT INTO " + Main.TB_ITEM_HISTORY + " (history_frame, history_type, history_item_code_start, history_item_code_end, history_desc, history_user) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            PreparedStatement pst = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            pst.setString(1, HistoryFrame.toString());
            pst.setString(2, HistoryType.toString());
            pst.setString(3, history_item_code_start);
            pst.setString(4, history_item_code_end);
            pst.setString(5, history_desc);
            pst.setString(6, history_user);

            pst.executeUpdate();
        } catch (SQLException e) {
            paneDatabaseError(e);
        }
    }

    public static String createHistoryDesc(String old_value, String new_value, String column_name) {
        if (!old_value.equals(new_value)) {
            return "; " + column_name + ": " + old_value + " -> " + new_value;
        }
        return "";
    }

    public static String createHistoryDesc(String new_value, String column_name) {
        return "; " + column_name + ": " + new_value;
    }

    public static String createHistoryDesc(String new_value) {
        return "; " + new_value;
    }

    public static String getColumnFromLastRow(String table_name, String last_key, String column_name_to_get) {
        String column_value = "";
        String query = "SELECT * FROM " + table_name + " ORDER BY " + last_key + " DESC LIMIT 1";

        try (Connection conn = getConnection(Main.DB_NAME); PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                column_value = rs.getString(column_name_to_get);
            }

        } catch (SQLException e) {
            paneDatabaseError(e);
        }

        return column_value;
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
