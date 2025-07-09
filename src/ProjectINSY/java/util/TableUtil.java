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
import ProjectINSY.java.ui.ItemManagement;
import static ProjectINSY.java.util.DatabaseUtil.getColumnValueByInt;
import com.mysql.cj.jdbc.Blob;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

public class TableUtil {

    public enum TableEnum {
        STOCK_DISTINCT,
        STOCK_DELIVERY,
        STOCK_LOCATION,
        CATALOG_CATEGORY,
        CATALOG_ITEM,
        ITEM_HISTORY,
        ITEM_LIST,
        ITEM_REQUEST,
        ITEM_REPORT,
        TRANSACTION,
        INVENTORY_BALANCE,
        INVENTORY_TRANSACTION,
    };

    public static ImageIcon blobToImage(ResultSet rs, String column_name) throws SQLException {
        Blob blob = (Blob) rs.getBlob(column_name);
        ImageIcon imageIcon = null;
        if (blob != null) {
            byte[] imageBytes = blob.getBytes(1, (int) blob.length());
            imageIcon = new ImageIcon(imageBytes);
            Image img = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(img);
        }

        return imageIcon;
    }

    public final static void refreshTableAll(JTable tableName, String tb_name, TableEnum tableEnum) {
        String query = "SELECT * FROM " + tb_name;

        refreshTable(tableName, query, tableEnum);
    }

    public static void floatFormatDecimal(JTable tableName, int columnIndex) {
        TableColumn column = tableName.getColumnModel().getColumn(columnIndex);
        DecimalFormat df = new DecimalFormat("#,##0.00");

        column.setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (value instanceof Number number) {
                    value = df.format(number);
                }

                setText(value.toString());
                return comp;
            }
        });
    }

    public final static void refreshTable(JTable tableName, String query, TableEnum tableEnum) {
        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME); PreparedStatement pst = DatabaseUtil.prepareQuery(conn, query); ResultSet rs = pst.executeQuery()) {
            DefaultTableModel model = (DefaultTableModel) tableName.getModel();
            model.setRowCount(0);
            while (rs.next()) {
                try {
                    if (null != tableEnum) {
                        switch (tableEnum) {
                            //<editor-fold defaultstate="collapsed" desc="CATALOG ITEM">
                            case CATALOG_ITEM -> {
                                String name = rs.getString("name");
                                Float price = rs.getFloat("price");
                                String uom = rs.getString("uom");
                                String updated_at = rs.getString("updated_at");
//                                System.out.println(name);
//                                System.out.println(uom);
//                                System.out.println(price);

                                model.addRow(new Object[]{
                                    name, price, uom, updated_at
                                });
                            }
//                            //</editor-fold>
                            //<editor-fold defaultstate="collapsed" desc="CATALOG ITEM">
                            case INVENTORY_BALANCE -> {
                                int id = rs.getInt("inventory_id");
                                String name = rs.getString("name");
                                String location = rs.getString("location");
                                int quantity = rs.getInt("quantity");
                                String updated_at = rs.getString("updated_at");
                                model.addRow(new Object[]{
                                    id, name, location, quantity, updated_at
                                });
                            }
//                            //</editor-fold>
//                            //<editor-fold defaultstate="collapsed" desc="TRANSACCTION">
//                            case TRANSACTION -> {
//                                String timestamp = rs.getString("out_timestamp");
//                                String name = rs.getString("out_name");
//                                int quantity = rs.getInt("out_quantity");
//                                float price = rs.getFloat("out_price");
//                                String channel = rs.getString("out_channel");
//                                String customer = rs.getString("out_customer");
//                                model.addRow(new Object[]{
//                                    timestamp, name, quantity, price, channel, customer
//                                });
//                            }
//                            //</editor-fold>
//                            //<editor-fold defaultstate="collapsed" desc="ITEM REQUEST">
//                            case ITEM_REQUEST -> {
//                                String timestamp = rs.getString("request_timestamp");
//                                String name = rs.getString("request_name");
//                                String item = rs.getString("request_item");
//                                String desc = rs.getString("request_desc");
//                                int quantity = rs.getInt("request_quantity");
//                                String status = rs.getString("request_status");
//                                model.addRow(new Object[]{
//                                    timestamp, item, desc, name, quantity, status
//                                });
//                            }
//                            //</editor-fold>
//                            //<editor-fold defaultstate="collapsed" desc="ITEM REPORT">
//                            case ITEM_REPORT -> {
//                                String code = rs.getString("report_code");
//                                String name = rs.getString("stock_name");
//                                String desc = rs.getString("stock_desc");
//                                String condition = rs.getString("report_condition");
//                                model.addRow(new Object[]{
//                                    code, name, desc, condition
//                                });
//                            }
//                            //</editor-fold>
//                            //<editor-fold defaultstate="collapsed" desc="STOCK DISTINCT">
//                            case STOCK_DISTINCT -> {
//                                String category = rs.getString("stock_category");
//                                String name = rs.getString("stock_name");
//                                int quantity = rs.getInt("stock_quantity");
//                                model.addRow(new Object[]{
//                                    category, name, quantity
//                                });
//                            }
//                            //</editor-fold>
//                            //<editor-fold defaultstate="collapsed" desc="STOCK DELIVERY">
//                            case STOCK_DELIVERY -> {
//                                String code = rs.getString("stock_code");
                            ////                                String category = rs.getString("stock_category");
//                                String name = rs.getString("stock_name");
//                                String desc = rs.getString("stock_desc");
//                                int quantity = 1;
//                                String price = floatRoundOff(rs.getFloat("stock_price"));
//                                if (ItemManagement.isGroupedByBatches()) {
//                                    if (rs.getFloat("stock_price") != rs.getFloat("stock_price_batch")) {
//                                        price = price + " / " + floatRoundOff(rs.getFloat("stock_price_batch"));
//                                    }
//                                }
//                                Date deliveryDate = rs.getDate("stock_dod");
//                                String benefactor = rs.getString("stock_benefactor");
//
//                                if (ItemManagement.isGroupedByBatches()) {
//                                    String[] parts = code.split("-");
//                                    code = String.join("-", Arrays.copyOfRange(parts, 1, parts.length));
//                                    quantity = rs.getInt("stock_quantity");
//                                }
//
//                                model.addRow(new Object[]{
//                                    code, name, desc, price, quantity, deliveryDate, benefactor
//                                });
//                            }
//                            //</editor-fold>
//                            //<editor-fold defaultstate="collapsed" desc="STOCK LOCATION">
//                            case STOCK_LOCATION -> {
//                                String code = rs.getString("stock_code");
//                                String name = rs.getString("stock_name");
//                                String desc = rs.getString("stock_desc");
//                                String location = rs.getString("stock_location");
//                                String holder = rs.getString("stock_holder");
//
//                                model.addRow(new Object[]{
//                                    code, name, desc, location, holder
//                                });
//                            }
//                            //</editor-fold>
//                            //<editor-fold defaultstate="collapsed" desc="CATALOG CATEGORY">
//                            case CATALOG_CATEGORY -> {
//                                String category_name = rs.getString("category_name");
//                                if (!category_name.equals("N/A")) {
//                                    model.addRow(new Object[]{
//                                        category_name
//                                    });
//                                }
//                            }
//                            //</editor-fold>
//                            //<editor-fold defaultstate="collapsed" desc="ITEM HISTORY">
//                            case ITEM_HISTORY -> {
//                                String timestamp = rs.getString("history_timestamp");
//                                String type = rs.getString("history_frame") + "-" + rs.getString("history_type");
//                                String code = rs.getString("history_item_code_start");
//                                if (!code.equals(rs.getString("history_item_code_end"))) {
//                                    String[] parts = rs.getString("history_item_code_end").split("-");
//                                    code += "-" + parts[2];
//                                }
//                                String desc = rs.getString("history_desc");
//                                String user = rs.getString("history_user");
//
//                                model.addRow(new Object[]{
//                                    timestamp, type, code, desc, user
//                                });
//                            }
//                            //</editor-fold>
                            default -> {
                            }
                        }
                    }
                } catch (SQLException e) {
                    MessageUtil.paneDatabaseError(e);
                }
            }

            resetTableSort(tableName);

        } catch (SQLException e) {
            MessageUtil.paneDatabaseError(e);
        }
    }

    public static String floatRoundOff(float input) {
        BigDecimal bd = new BigDecimal(input);
        bd = bd.setScale(2, RoundingMode.HALF_UP);

        return bd.toString();
    }

    public static String[] selectTableRow(JTable table, int selectedRow) {
        String[] values = new String[table.getColumnCount()];

        for (int i = 0; i < table.getColumnCount(); i++) {
            Object cellValue = table.getValueAt(selectedRow, i);
            values[i] = cellValue != null ? cellValue.toString() : "";
        }

        return values;
    }

    public static void linkFieldsToTable(String[] tableRow, JComponent... components) {
        DecimalFormat df = new DecimalFormat("0.00");  // For formatting prices to 2 decimals

        // image cell renderers should always be the last column, and not included in the list of components!
        for (int i = 0; i < components.length; i++) {
            if (components[i] == null) {
                continue;
            }

            JComponent component = components[i];
            String cellValue = tableRow[i];

            switch (component) {
                case JTextField jTextField -> {
                    // Check if the value is a number (float or double)
                    if (isFloat(cellValue)) {
                        try {
                            float value = Float.parseFloat(cellValue);  // Parse as float

                            // Check if the number has more than 2 decimals
                            if (getDecimalPlaces(value) <= 2) {
                                jTextField.setText(df.format(value));  // Format to 2 decimal places
                            } else {
                                jTextField.setText(String.valueOf(value));  // Leave as is
                            }
                        } catch (NumberFormatException e) {
                            jTextField.setText(cellValue);  // If it's not a number, just display it as is
                        }
                    } else {
                        // If not numeric, just set the value as is
                        jTextField.setText(cellValue);
                    }
                    jTextField.setForeground(new Color(0, 0, 0));  // Default text color
                }
                case JComboBox jComboBox -> {
                    // Set the selected item from the tableRow values
                    jComboBox.setSelectedItem(cellValue);
                }
                default -> {
                    // Handle other cases if needed
                }
            }
        }

        // image cell renderers should always be the last column, and not included in the list of components!
//        for (int i = 0; i < components.length; i++) {
//            JComponent component = components[i];
//            if (component instanceof JTextField jTextField) {
//                jTextField.setText(tableRow[i]);
//                jTextField.setForeground(new Color(0, 0, 0));
//            } else if (component instanceof JComboBox jComboBox) {
//                jComboBox.setSelectedItem(tableRow[i]);
//            }
//        }
    }

    /**
     * Helper method to check if the string can be parsed to a Float.
     */
    private static boolean isFloat(String str) {
        // Check if the string contains a decimal point
        if (str.contains(".")) {
            try {
                Float.valueOf(str);  // Try to parse as float
                return true;
            } catch (NumberFormatException e) {
                return false;  // Return false if it's not a valid float
            }
        }
        // If there's no decimal point, it's treated as an integer
        return false;
    }

    /**
     * Helper method to check the number of decimal places in a float.
     */
    private static int getDecimalPlaces(float value) {
        String str = String.valueOf(value);
        int decimalIndex = str.indexOf('.');

        if (decimalIndex == -1) {
            return 0;  // No decimal places
        }

        return str.length() - decimalIndex - 1;  // Return number of decimals after the dot
    }

    public static void resetTableSort(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        if (model.getRowCount() > 0) {
            Object[][] data = new Object[model.getRowCount()][model.getColumnCount()];

            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    data[i][j] = model.getValueAt(i, j);
                }
            }

            Object[][] originalData = new Object[data.length][data[0].length];
            for (int i = 0; i < data.length; i++) {
                System.arraycopy(data[i], 0, originalData[i], 0, data[i].length);
            }

            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            table.setRowSorter(sorter);
        }
    }
}
