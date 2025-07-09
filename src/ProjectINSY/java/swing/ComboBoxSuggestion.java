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

/**
 * A customized {@link JComboBox} that provides dynamic population and
 * association based on database queries. Useful for filtering combo box
 * selections with dependencies between combo boxes (e.g., cascading
 * selections).
 *
 * @param <E> The type of items in this combo box.
 */
public class ComboBoxSuggestion<E> extends JComboBox<E> {

    /**
     * Constructs a new {@code ComboBoxSuggestion} and sets a custom UI.
     */
    public ComboBoxSuggestion() {
        setUI(new ComboSuggestionUI());
    }

    public void clearComboBox() {
        setSelectedIndex(0);
    }

    /**
     * Checks whether the currently selected item is the default combo box item.
     *
     * @return {@code true} if the selected item equals the default item (first
     * item), otherwise {@code false}.
     */
    public boolean isDefaultComboItem() {
        if (getSelectedItem() == null) {
            return false;
        }
        return getSelectedItem().toString().equals(getItemAt(0));
    }

    /**
     * Resets the combo box to its default state by inserting a default item at
     * index 0 and selecting it.
     */
    public void resetDefaultComboItem() {
        removeItem("- - - - -");
        insertItemAt((E) "- - - - -", 0);
        setSelectedIndex(0);
    }

    /**
     * Repopulates the combo box using a SQL query. The column name is inferred
     * from the query.
     *
     * @param query the SQL query string used to retrieve data.
     */
    public void repopulateComboBox(String query) {
        String[] parts = query.split(" ");

        repopulateComboBox(parts[1], query);
    }

    /**
     * Repopulates the combo box with items fetched from the database. Ensures
     * unique, sorted, and categorized entries with special positioning for
     * "Supply Room", "N/A", and "Miscellaneous".
     *
     * @param columnName the column name to fetch data from.
     * @param query the SQL query to execute.
     */
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

    /**
     * Repopulates a child combo box based on the selection of a parent combo
     * box. The column names are inferred from the base query.
     *
     * @param parentCombo the parent combo box whose selection affects this
     * combo box.
     * @param parentColumnName the name of the parent column in the database.
     * @param baseQuery the SQL base query used to construct the WHERE clause.
     */
    public void repopulateAssociatedComboBox(ComboBoxSuggestion<E> parentCombo, String parentColumnName, String baseQuery) {
        String[] parts = baseQuery.split(" ");

        repopulateAssociatedComboBox(parentCombo, parentColumnName, parts[1], baseQuery);
    }

    /**
     * Repopulates this combo box based on the selection from a parent combo
     * box.
     *
     * @param parentCombo the parent combo box whose selection filters this
     * combo.
     * @param parentColumnName the parent column in the database.
     * @param childColumnName the child column whose values are to be populated.
     * @param baseQuery the base SQL query.
     */
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

    /**
     * Repopulates this combo box based on the selections of both a parent and
     * grandparent combo box. The column names are inferred from the base query.
     *
     * @param parentCombo the parent combo box.
     * @param grandParentCombo the grandparent combo box.
     * @param parentColumnName the parent column name.
     * @param grandParentColumnName the grandparent column name.
     * @param baseQuery the base SQL query.
     */
    public void repopulateAssociatedComboBox(ComboBoxSuggestion<E> parentCombo, ComboBoxSuggestion<E> grandParentCombo, String parentColumnName, String grandParentColumnName, String baseQuery) {
        String[] parts = baseQuery.split(" ");

        repopulateAssociatedComboBox(parentCombo, grandParentCombo, parentColumnName, grandParentColumnName, parts[1], baseQuery);
    }

    /**
     * Repopulates this combo box based on the selections of both a parent and
     * grandparent combo box. The SQL query is filtered according to the
     * selected values, depending on which combo box has a non-default
     * selection.
     *
     * @param parentCombo the parent combo box.
     * @param grandParentCombo the grandparent combo box.
     * @param parentColumnName the column corresponding to the parent.
     * @param grandParentColumnName the column corresponding to the grandparent.
     * @param childColumnName the column whose values will populate this combo
     * box.
     * @param baseQuery the base SQL query.
     */
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
