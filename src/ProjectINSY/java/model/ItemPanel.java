/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProjectINSY.java.model;

import ProjectINSY.java.Main;
import static ProjectINSY.java.util.DatabaseUtil.getConnection;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author admin
 */
public abstract class ItemPanel extends javax.swing.JPanel {

    protected String filterWHERE = "", filterHAVING = "";
    protected String currentSearchQuery = "";
    protected boolean isUpdatingComboBoxes = false;

    protected final String PLACEHOLDER_CATEGORY = "Enter Category";
    protected final String PLACEHOLDER_NAME = "Enter Name";
    protected final String PLACEHOLDER_DESC = "Enter Description";
    protected final String PLACEHOLDER_QTY = "1";
    protected final String PLACEHOLDER_LOCATION = "Enter Location";
    protected final String PLACEHOLDER_ID_CODE = "Enter Code (XXXX)";
    protected final String PLACEHOLDER_FULL_CODE = "Enter Code (XXXX)";
    protected final String PLACEHOLDER_PRICE = "Enter Price";
    protected final String PLACEHOLDER_DOD = "Enter Delivery Date";
    protected final String PLACEHOLDER_BENEFACTOR = "Enter Benefactor";

    public ItemPanel() {
    }

    public abstract void refreshItemTable();

    public void refreshItemTable_2() {
    }

    public abstract void repopulateComboBox();

    public abstract void repopulateFilterComboBox();

    public void enableUpdatingComboBoxes() {
        isUpdatingComboBoxes = false;
    }

    public void disableUpdatingComboBoxes() {
        isUpdatingComboBoxes = true;
    }

    public String getCurrentSearchQuery() {
        return currentSearchQuery;
    }

    protected static void exportSQLToCSV(String query, String fileName) {
        String userHome = System.getProperty("user.home");
        // Get timestamp
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        // Construct file name with timestamp
        String fileNameWithTime = fileName + "_" + timestamp;
        String documentsPath = userHome + File.separator + "Documents" + File.separator + fileNameWithTime + ".csv";

        try (Connection conn = getConnection(Main.DB_NAME); Statement pst = conn.createStatement(); ResultSet rs = pst.executeQuery(query); BufferedWriter writer = new BufferedWriter(new FileWriter(documentsPath))) {
            if (!rs.isBeforeFirst()) {
                System.out.println("No data found for the query: \n" + query);
                return;
            }

            int columnCount = rs.getMetaData().getColumnCount();
//            System.out.println("Column count: " + columnCount);
            for (int i = 1; i <= columnCount; i++) {
                writer.write(escapeCsv(rs.getMetaData().getColumnLabel(i)));
                if (i < columnCount) {
                    writer.write(",");
                }
            }
            writer.newLine();
//            System.out.println("Header written to file");

            while (rs.next()) {
//                System.out.println("Writing row to file...");
                for (int i = 1; i <= columnCount; i++) {
                    String value = rs.getString(i);
                    if (value != null && value.startsWith("Silang-Silang-")) {
                        value = value.substring("Silang-".length());
                    }
                    writer.write(escapeCsv(value));
                    if (i < columnCount) {
                        writer.write(",");
                    }
                }
                writer.newLine();
            }

            writer.flush();
            JOptionPane.showMessageDialog(null, "Data exported successfully to:\n\n " + documentsPath, "Export Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    protected static String escapeCsv(String value) {
        if (value == null) {
            return "";
        }

        String escapedValue = value.replace("\"", "\"\"");

        if (escapedValue.contains(",")
                || escapedValue.contains("\n")
                || escapedValue.contains("\"")) {
            return "\"" + escapedValue + "\"";
        }

        return escapedValue;
    }
}
