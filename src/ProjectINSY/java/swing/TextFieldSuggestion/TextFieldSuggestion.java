package ProjectINSY.java.swing.TextFieldSuggestion;

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
import javax.swing.JTextField;

public class TextFieldSuggestion extends JTextField {

    private TextFieldSuggestionUI textUI;

    public TextFieldSuggestion() {
        textUI = new TextFieldSuggestionUI(this);
        setUI(textUI);
    }

    public void addItemSuggestion(String text) {
        textUI.getItems().add(text);
    }

    public void removeItemSuggestion(String text) {
        textUI.getItems().remove(text);
    }

    public void clearItemSuggestion() {
        textUI.getItems().clear();
    }

    public void setRound(int round) {
        textUI.setRound(round);
    }

    public int getRound() {
        return textUI.getRound();
    }

    /**
     * Repopulates the auto-complete suggestions for this text field using data
     * from a specified database column and query.
     * <p>
     * This method fetches distinct, non-empty values from the given column,
     * sorts them alphabetically (case-insensitive), and gives priority
     * positioning to the special entry "Supply Room" by placing it at the top.
     * <p>
     * After fetching and sorting, it clears any existing suggestions and
     * updates the list with the new results.
     *
     * @param columnName the name of the column in the result set whose values
     * are used for suggestions.
     * @param query the SQL query to execute for retrieving the suggestion
     * values.
     */
    public void repopulateSuggestions(String columnName, String query) {
        Set<String> uniqueItems = new HashSet<>();
        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME); PreparedStatement pst = DatabaseUtil.prepareQuery(conn, query); ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString(columnName);
                if (name != null && !name.trim().isEmpty()) {
                    uniqueItems.add(name);
                }
            }

            List<String> sortedItems = new ArrayList<>(uniqueItems);
            boolean hasSupplyRoom = sortedItems.contains("Supply Room");
            if (hasSupplyRoom) {
                sortedItems.remove("Supply Room");
            }

            Collections.sort(sortedItems, String.CASE_INSENSITIVE_ORDER);
            if (hasSupplyRoom) {
                sortedItems.add(0, "Supply Room");
            }

            clearItemSuggestion();
            for (String item : sortedItems) {
                addItemSuggestion(item);
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (SQLException e) {
            paneDatabaseError(e);
        }
    }

}
