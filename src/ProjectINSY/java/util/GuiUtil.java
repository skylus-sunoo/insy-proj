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
import ProjectINSY.java.swing.ScrollBarCustom;
import ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion;
import static ProjectINSY.java.util.MessageUtil.paneDatabaseError;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;

public class GuiUtil {

    public static void setTransparentFrame(JComponent... components) {
        for (JComponent component : components) {
            component.setBackground(new Color(0, 0, 0, 0));
        }
    }

    public enum FieldFocus {
        GAINED, LOST
    };

    public static void setDefaultField(JTextField text_field, String default_text, FieldFocus field_focus, Color color) {
        if (field_focus == FieldFocus.GAINED) {
            if (text_field.getText().equals(default_text)) {
                text_field.setText("");
                text_field.setForeground(color);
            }
        } else if (field_focus == FieldFocus.LOST) {
            if (text_field.getText().equals("")) {
                text_field.setText(default_text);
                text_field.setForeground(new Color(153, 153, 153));
            }
        }
    }

    public static void setBtnIcon(JButton button, String location) {
        java.net.URL imgURL = GuiUtil.class.getResource(location);
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            button.setIcon(icon);
        } else {
            System.err.println("Error: Image not found at " + location);
        }

        button.setHorizontalTextPosition(SwingConstants.CENTER);
    }

    public static void resetBtnIcon(JButton button) {
        ImageIcon transparentIcon = new ImageIcon(new ImageIcon(new byte[]{}).getImage().getScaledInstance(1, 1, Image.SCALE_DEFAULT));

        button.setIcon(transparentIcon);
    }

    public static void resetIcon(JLabel label) {
        ImageIcon transparentIcon = new ImageIcon(new ImageIcon(new byte[]{}).getImage().getScaledInstance(1, 1, Image.SCALE_DEFAULT));

        label.setIcon(transparentIcon);
    }

    public static void setForm(JPanel panel, JComponent com) {
        panel.removeAll();
        panel.add(com);
        panel.repaint();
        panel.revalidate();

//        switch (com) {
//            case SearchComboBox search -> search.repopulateComboBox(search.selectedSearch);
//            case SearchComboBoxTwo search -> search.repopulateComboBox(search.selectedSearch);
//            case SearchComboBoxField search -> search.repopulateComboBox(search.selectedSearch);
//            default -> {}
//        }
    }

    public static void setScrollBarCustom(JScrollPane tableScroll) {
        tableScroll.setVerticalScrollBar(new ScrollBarCustom());
        ScrollBarCustom sp = new ScrollBarCustom();
        sp.setOrientation(JScrollBar.HORIZONTAL);
        tableScroll.setHorizontalScrollBar(sp);
    }

    public static void repopulateComboBox(JComboBox comboBox, String columnName, String query) {
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

            comboBox.removeAllItems();
            for (String item : sortedItems) {
                comboBox.addItem(item);
//                System.out.println(item);
            }

            if (!sortedItems.isEmpty()) {
                comboBox.setSelectedIndex(0);
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (SQLException e) {
            paneDatabaseError(e);
        }

        resetDefaultComboItem(comboBox);
    }

    public static void repopulateSuggestions(TextFieldSuggestion field, String columnName, String query) {
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

            field.clearItemSuggestion();
            for (String item : sortedItems) {
                field.addItemSuggestion(item);
            }

            rs.close();
            pst.close();
            conn.close();

        } catch (SQLException e) {
            paneDatabaseError(e);
        }
    }

    public static void enforceDigits(KeyEvent evt) {
        char c = evt.getKeyChar();

        if (!Character.isDigit(c) && c != '.' && c != KeyEvent.VK_BACK_SPACE) {
            evt.consume();
        }
    }

    public static void enforceCharacterAmount(KeyEvent evt, int amount) {
        char c = evt.getKeyChar();

        String currentText = ((javax.swing.JTextField) evt.getSource()).getText();
        if (currentText.length() >= amount && c != KeyEvent.VK_BACK_SPACE) {
            evt.consume();
        }
    }

    public static void enforceDigits(DocumentEvent e) {
        JTextField textField = (JTextField) e.getDocument().getProperty("owner");
        String text = textField.getText();

        String validText = text.replaceAll("[^0-9.]", "");

        int decimalCount = validText.length() - validText.replace(".", "").length();
        if (decimalCount > 1) {
            validText = validText.substring(0, validText.indexOf('.', validText.indexOf('.') + 1));
        }

        if (!text.equals(validText)) {
            textField.setText(validText);
        }
    }

    public static void clearLabelImage(JLabel label) {
        label.setIcon(null);
        clearLabel(label, null);
    }

    public static void clearLabel(JLabel label, String defaultText) {
        label.setText(defaultText);
        label.setForeground(new Color(153, 153, 153));
    }

    public static void clearFieldDate(JTextField field) {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        clearField(field, formattedDate);
    }

    public static void clearField(JTextField field, String defaultText) {
        field.setText(defaultText);
        field.setForeground(new Color(153, 153, 153));
    }

    public static void clearComboBox(JComboBox... comboBox) {
        for (JComboBox combo : comboBox) {
            combo.setSelectedIndex(0);
        }
    }

    public static void resetBtnEnability(JComponent component, JButton... buttons) {
        boolean enable = false;

        switch (component) {
            case JTextField jTextField ->
                enable = !jTextField.getText().equals("");
            case JLabel jLabel ->
                enable = !jLabel.getText().equals("");
            default -> {
            }
        }

        for (JButton button : buttons) {
            button.setEnabled(enable);
        }
    }

    public static String cleanSpaces(String input) {
        StringBuilder result = new StringBuilder();

        boolean previousIsSpace = false;

        for (char c : input.toCharArray()) {
            if (c == ' ') {
                if (!previousIsSpace) {
                    result.append(c);
                    previousIsSpace = true;
                }
            } else {
                result.append(c);
                previousIsSpace = false;
            }
        }

        return result.toString();
    }

    public static String getComboSelected(JComboBox combo) {
        if (combo.getSelectedItem() == null) {
            return "";
        }
        return combo.getSelectedItem().toString();
    }

    public static boolean isDefaultComboItem(JComboBox combo) {
        if (combo.getSelectedItem() == null) {
            return false;
        }
        return combo.getSelectedItem().toString().equals(combo.getItemAt(0));
    }

    public static void resetDefaultComboItem(JComboBox combo) {
        combo.removeItem("- - - - -");
        combo.insertItemAt("- - - - -", 0);
        combo.setSelectedIndex(0);
    }

    public static String getFieldString(JTextField field) {
        return field.getText().trim();
    }

    public static boolean fieldHasValue(JTextField field) {
        return !field.getText().trim().isEmpty();
    }

    public static void repopulateAssociatedComboBox(JComboBox parentCombo, JComboBox childCombo, String parentColumnName, String childColumnName, String baseQuery) {
        Object selectedItemObj = parentCombo.getSelectedItem();
        if (selectedItemObj == null || selectedItemObj.toString().trim().isEmpty()) {
            return;
        }

        String selectedItem = parentCombo.getSelectedItem().toString();

        if (!isDefaultComboItem(parentCombo)) {
            baseQuery += " WHERE " + parentColumnName + " = '" + selectedItem + "'";
        }

        repopulateComboBox(childCombo, childColumnName, baseQuery);

        resetDefaultComboItem(childCombo);
    }

    public static void repopulateAssociatedComboBox(JComboBox parentCombo, JComboBox grandParentCombo, JComboBox childCombo, String parentColumnName, String grandParentColumnName, String childColumnName, String baseQuery) {
        Object selectedItemObj = parentCombo.getSelectedItem();
        if (selectedItemObj == null || selectedItemObj.toString().trim().isEmpty()) {
            return;
        }

        String selectedItem = parentCombo.getSelectedItem().toString();
        String selectedItemGrand = grandParentCombo.getSelectedItem().toString();

        if (!isDefaultComboItem(grandParentCombo) && isDefaultComboItem(parentCombo)) {
            baseQuery += " WHERE " + grandParentColumnName + " = '" + selectedItemGrand + "'";
        } else if (!isDefaultComboItem(parentCombo)) {
            baseQuery += " WHERE " + parentColumnName + " = '" + selectedItem + "'";
        }

        repopulateComboBox(childCombo, childColumnName, baseQuery);

        resetDefaultComboItem(childCombo);
    }
}
