package ProjectINSY.java.swing;

import ProjectINSY.java.Main;
import ProjectINSY.java.util.DatabaseUtil;
import static ProjectINSY.java.util.MessageUtil.paneDatabaseError;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JComboBox;

public class ComboBoxSuggestion<E> extends JComboBox<E> {

    public ComboBoxSuggestion() {
        setUI(new ComboSuggestionUI());
    }

    public boolean isDefaultComboItem() {
        if (getSelectedItem() == null) {
            return false;
        }
        return getSelectedItem().toString().equals(getItemAt(0));
    }

    public void resetDefaultComboItem() {
        removeItem("- - - - -");
        insertItemAt((E) "- - - - -", 0);
        setSelectedIndex(0);
    }

    public void repopulateComboBox(String query) {
        String[] parts = query.split(" ");

        repopulateComboBox(parts[1], query);
    }

    public void repopulateComboBox(String columnName, String query) {
        Set<String> uniqueItems = new HashSet<>();
        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME); PreparedStatement pst = DatabaseUtil.prepareQuery(conn, query); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString(columnName);
                if (name != null && !name.trim().isEmpty()) {
                    uniqueItems.add(name);
                }
            }

            List<String> sortedItems = new ArrayList<>(uniqueItems);
            boolean hasNA = sortedItems.contains("N/A");
            if (hasNA) {
                sortedItems.remove("N/A");
            }
            boolean hasMisc = sortedItems.contains("Miscellaneous");
            if (hasMisc) {
                sortedItems.remove("Miscellaneous");
            }
            boolean hasSupplyRoom = sortedItems.contains("Supply Room");
            if (hasSupplyRoom) {
                sortedItems.remove("Supply Room");
            }

            Collections.sort(sortedItems, String.CASE_INSENSITIVE_ORDER);
            if (hasMisc) {
                sortedItems.add(0, "Miscellaneous"); // ensures Miscellaneous is index 1
            }
            if (hasNA) {
                sortedItems.add(0, "N/A"); // ensures N/A is index 0
            }
            if (hasSupplyRoom) {
                sortedItems.add(0, "Supply Room");
            }

            removeAllItems();
            for (String item : sortedItems) {
                addItem((E) item); // ⚠️ Unchecked cast
//                System.out.println(item);
            }

            if (!sortedItems.isEmpty()) {
                setSelectedIndex(0);
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (SQLException e) {
            paneDatabaseError(e);
        }

        resetDefaultComboItem();
    }

    public void repopulateAssociatedComboBox(ComboBoxSuggestion<E> parentCombo, String parentColumnName, String baseQuery) {
        String[] parts = baseQuery.split(" ");

        repopulateAssociatedComboBox(parentCombo, parentColumnName, parts[1], baseQuery);
    }

    public void repopulateAssociatedComboBox(ComboBoxSuggestion<E> parentCombo, String parentColumnName, String childColumnName, String baseQuery) {
        Object selectedItemObj = parentCombo.getSelectedItem();
        if (selectedItemObj == null || selectedItemObj.toString().trim().isEmpty()) {
            return;
        }

        String selectedItem = parentCombo.getSelectedItem().toString();

        if (!parentCombo.isDefaultComboItem()) {
            baseQuery += " WHERE " + parentColumnName + " = '" + selectedItem + "'";
        }

        repopulateComboBox(childColumnName, baseQuery);

        resetDefaultComboItem();
    }

    public void repopulateAssociatedComboBox(ComboBoxSuggestion<E> parentCombo, ComboBoxSuggestion<E> grandParentCombo, String parentColumnName, String grandParentColumnName, String baseQuery) {
        String[] parts = baseQuery.split(" ");

        repopulateAssociatedComboBox(parentCombo, grandParentCombo, parentColumnName, grandParentColumnName, parts[1], baseQuery);
    }

    public void repopulateAssociatedComboBox(ComboBoxSuggestion<E> parentCombo, ComboBoxSuggestion<E> grandParentCombo, String parentColumnName, String grandParentColumnName, String childColumnName, String baseQuery) {
        Object selectedItemObj = parentCombo.getSelectedItem();
        if (selectedItemObj == null || selectedItemObj.toString().trim().isEmpty()) {
            return;
        }

        String selectedItem = parentCombo.getSelectedItem().toString();
        String selectedItemGrand = grandParentCombo.getSelectedItem().toString();

        if (!grandParentCombo.isDefaultComboItem() && parentCombo.isDefaultComboItem()) {
            baseQuery += " WHERE " + grandParentColumnName + " = '" + selectedItemGrand + "'";
        } else if (!parentCombo.isDefaultComboItem()) {
            baseQuery += " WHERE " + parentColumnName + " = '" + selectedItem + "'";
        }

        repopulateComboBox(childColumnName, baseQuery);

        resetDefaultComboItem();
    }
}
