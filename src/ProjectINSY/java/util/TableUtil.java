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
import com.mysql.cj.jdbc.Blob;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import static java.awt.image.ImageObserver.ERROR;
import static java.awt.image.ImageObserver.WIDTH;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class TableUtil {

    public enum TableEnum {
        STOCK_DISTINCT,
        STOCK_DELIVERY,
        CATALOG_CATEGORY,
        CATALOG_ITEM,
        SUPPLY_HISTORY,
        ITEM_LIST,
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

    public static void defaultTable(JTable tableName) {
        tableName.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        tableName.isCellEditable(ERROR, WIDTH);
        tableName.setDefaultEditor(Object.class, null);
        tableName.setAutoCreateRowSorter(true);
        tableName.setFillsViewportHeight(true);
    }

    public static void fixedColumnAll(JTable tableName) {
        for (int i = 0; i < tableName.getColumnCount(); i++) {
            tableName.getColumnModel().getColumn(i).setResizable(false);
        }
    }

    public enum EnumAlignment {
        CENTER, LEFT, RIGHT
    }

    public static void setColumnHorizontalAligment(JTable tableName, int columnIndex, EnumAlignment enumAlignment) {
        DefaultTableCellRenderer alignment = new DefaultTableCellRenderer();
        if (null != enumAlignment) switch (enumAlignment) {
            case CENTER -> alignment.setHorizontalAlignment(JLabel.CENTER);
            case LEFT -> alignment.setHorizontalAlignment(JLabel.LEFT);
            case RIGHT -> alignment.setHorizontalAlignment(JLabel.RIGHT);
            default -> {
            }
        }
        tableName.getColumnModel().getColumn(columnIndex).setCellRenderer(alignment);
    }

    public static void sorterNumbers(JTable tableName, int columnIndex) {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableName.getModel());
        tableName.setRowSorter(sorter);

        sorter.setComparator(columnIndex, (String o1, String o2) -> {
            try {
                Float float1 = Float.valueOf(o1);
                Float float2 = Float.valueOf(o2);
                return Float.compare(float1, float2);
            } catch (NumberFormatException e) {
                return o1.compareTo(o2);
            }
        });
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
                            //<editor-fold defaultstate="collapsed" desc="STOCK DISTINCT">
                            case STOCK_DISTINCT: {
                                String category = rs.getString("stock_category");
                                String name = rs.getString("stock_name");
                                String quantity = rs.getString("stock_quantity");
                                model.addRow(new Object[]{
                                    category, name, quantity
                                });
                                break;
                            }
                            //</editor-fold>
                            //<editor-fold defaultstate="collapsed" desc="STOCK DELIVERY">
                            case STOCK_DELIVERY: {
                                String code = rs.getString("stock_code");
//                                String category = rs.getString("stock_category");
                                String name = rs.getString("stock_name");
                                String desc = rs.getString("stock_desc");
                                int quantity = 1;
                                float price = rs.getFloat("stock_price");
                                Date deliveryDate = rs.getDate("stock_dod");
                                String holder = rs.getString("stock_user");

                                if (ItemManagement.isGroupedByBatches()) {
                                    String[] parts = code.split("-");
                                    code = String.join("-", Arrays.copyOfRange(parts, 1, parts.length));
                                    quantity = rs.getInt("stock_quantity");
                                }

                                model.addRow(new Object[]{
                                    code, name, desc, price, quantity, deliveryDate, holder
                                });
                                break;
                            }
                            //</editor-fold>
                            //<editor-fold defaultstate="collapsed" desc="CATALOG CATEGORY">
                            case CATALOG_CATEGORY: {
                                String category_name = rs.getString("category_name");
                                if (!category_name.equals("N/A")) {
                                    model.addRow(new Object[]{
                                        category_name
                                    });
                                }
                                break;
                            }
                            //</editor-fold>
                            //<editor-fold defaultstate="collapsed" desc="CATALOG ITEM">
                            case CATALOG_ITEM: {
                                String item_name = rs.getString("item_name");
                                String item_category = rs.getString("item_category");
                                String item_uom = rs.getString("item_uom");
                                model.addRow(new Object[]{
                                    item_name, item_category, item_uom
                                });
                                break;
                            }
                            //</editor-fold>
                            default:
                                break;
                        }
                    }
                } catch (SQLException e) {
                    MessageUtil.paneDatabaseError(e);
                }
            }

        } catch (SQLException e) {
            MessageUtil.paneDatabaseError(e);
        }
    }

    public static void clearSelectedTableRow(JTable table) {
        table.getSelectionModel().clearSelection();
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
        // image cell renderers should always be the last column, and not included in the list of components!
        for (int i = 0; i < components.length; i++) {
            JComponent component = components[i];
            if (component instanceof JTextField jTextField) {
                jTextField.setText(tableRow[i]);
                jTextField.setForeground(new Color(0, 0, 0));
            } else if (component instanceof JComboBox jComboBox) {
                jComboBox.setSelectedItem(tableRow[i]);
            }
        }
    }
}
