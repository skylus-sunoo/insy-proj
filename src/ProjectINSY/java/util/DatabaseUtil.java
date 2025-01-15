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
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public static void setColumnValueByString(String table_name, String column_name_to_set, String column_name_to_search, String update, String id) {
        String query = "UPDATE " + table_name + " SET " + column_name_to_set + " = ? WHERE " + column_name_to_search + " = ?";

        try (Connection conn = getConnection(Main.DB_NAME); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, update);
            ps.setString(2, id);
            
            ps.executeUpdate();
        } catch (SQLException e) {
            paneDatabaseError(e);
        }
    }

    public static void setColumnValueByInt(String table_name, String column_name_to_set, String column_name_to_search, String update, int id) {
        String query = "UPDATE " + table_name + " SET " + column_name_to_set + " = ? WHERE " + column_name_to_search + " = ?";

        try (Connection conn = getConnection(Main.DB_NAME); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, update);
            ps.setInt(2, id);
            
            ps.executeUpdate();
        } catch (SQLException e) {
            paneDatabaseError(e);
        }
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
        REQUEST, CATALOG, MANAGEMENT, TRACKER
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

    public static String createObjectCode(ResultSet rs, String objectStr, String dbName, String objectCodeColumn, String objectGeneratedKeyColumn) {
        try {
            if (rs.next()) {
                String objectCode = objectStr;
                if (objectCode.equals("Catalog-I-")) {
                    objectCode += getColumnValueByString(Main.TB_CATALOG_CATEGORY, "category_id", "category_name", getColumnValueByInt(Main.TB_CATALOG_ITEM, "item_category", "item_id", rs.getInt(1))) + "-" + rs.getInt(1);
                } else {
                    objectCode += rs.getInt(1);
                }
                try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
                    String query = "UPDATE " + dbName + " SET " + objectCodeColumn + " = ? WHERE " + objectGeneratedKeyColumn + " = ?";
                    PreparedStatement pst = conn.prepareStatement(query);
                    pst.setString(1, objectCode);
                    pst.setInt(2, rs.getInt(1));

                    pst.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(DatabaseUtil.class.getName()).log(Level.SEVERE, null, ex);
                }

                return objectCode;
            }
        } catch (SQLException e) {
            MessageUtil.paneDatabaseError(e);
        }

        return objectStr;
    }

    public static void resetObjectCode() {
        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            PreparedStatement pst = conn.prepareStatement("""
                                                          UPDATE tb_catalog_item i 
                                                          JOIN tb_catalog_category c 
                                                          ON i.item_category = c.category_name 
                                                          SET i.item_code = CONCAT('Catalog-I-', c.category_id, '-', i.item_id)
                                                          """);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            PreparedStatement pst = conn.prepareStatement("""
                                                          UPDATE tb_catalog_category 
                                                          SET category_code = CONCAT('Catalog-C-', category_id);
                                                          """);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void normalizeID() {
        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            PreparedStatement pst = conn.prepareStatement("""
                                                          SET @max_id = (SELECT MAX(item_id) FROM tb_catalog_item);
                                                          UPDATE tb_catalog_item SET item_id = item_id + @max_id;
                                                          SET @counter = 0;
                                                          UPDATE tb_catalog_item 
                                                          JOIN ( 
                                                              SELECT item_id, @counter := @counter + 1 AS new_item_id 
                                                              FROM tb_catalog_item 
                                                              ORDER BY item_id
                                                          ) AS temp 
                                                          ON tb_catalog_item.item_id = temp.item_id 
                                                          SET tb_catalog_item.item_id = temp.new_item_id;
                                                          
                                                          SET @new_auto_increment = @counter + 1;
                                                          
                                                          SET @sql = CONCAT('ALTER TABLE tb_catalog_item AUTO_INCREMENT = ', @new_auto_increment);
                                                          PREPARE stmt FROM @sql;
                                                          EXECUTE stmt;
                                                          DEALLOCATE PREPARE stmt;
                                                          """);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            PreparedStatement pst = conn.prepareStatement("""
                                                          SET @max_category_id = (SELECT MAX(category_id) FROM tb_catalog_category);
                                                          UPDATE tb_catalog_category SET category_id = category_id + @max_category_id;
                                                          SET @counter = 0;
                                                          UPDATE tb_catalog_category 
                                                          JOIN ( 
                                                              SELECT category_id, @counter := @counter + 1 AS new_category_id 
                                                              FROM tb_catalog_category 
                                                              ORDER BY category_id
                                                          ) AS temp 
                                                          ON tb_catalog_category.category_id = temp.category_id 
                                                          SET tb_catalog_category.category_id = temp.new_category_id;
                                                          
                                                          SET @new_auto_increment = @counter + 1;
                                                          
                                                          SET @sql = CONCAT('ALTER TABLE tb_catalog_category AUTO_INCREMENT = ', @new_auto_increment);
                                                          PREPARE stmt FROM @sql;
                                                          EXECUTE stmt;
                                                          DEALLOCATE PREPARE stmt;
                                                          """);
            pst.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        resetObjectCode();
    }

//    public void DisplayData() {
//        DefaultTableModel model;
//        try {
//            Connection con = Connector.getConnection();
//            String sql = "SELECT * FROM guestmanage";
//            PreparedStatement st = con.prepareStatement(sql);
//            ResultSet rs = st.executeQuery(sql);
//
//            while (rs.next()) {
//                int Guestid = rs.getInt("guestid");
//                int IdNumber = rs.getInt("idnum");
//                String Name = rs.getString("guestname");
//                int Contact = rs.getInt("contact");
//                String Email = rs.getString("email");
//                String Address = rs.getString("address");
//                Object[] obj = {Guestid, IdNumber, Name, Contact, Email, Address};
//                model = (DefaultTableModel) guesttable.getModel();
//                model.addRow(obj);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
