/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProjectINSY.java.swing;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author admin
 */
public class TableHighlighter extends DefaultTableCellRenderer {

    private final int statusColumnIndex;

    public TableHighlighter(int statusColumnIndex) {
        this.statusColumnIndex = statusColumnIndex;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        String statusValue = table.getValueAt(row, statusColumnIndex).toString();

        Color color = switch (statusValue) {
            case "PENDING" ->
                new Color(255, 255, 191, 100);
            case "RECEIVED" ->
                new Color(191, 191, 255, 100);
            case "DENIED" ->
                new Color(255, 191, 191, 100);
            case "DAMAGED" ->
                new Color(255, 191, 191, 100);
            case "IN MAINTENANCE" ->
                new Color(255, 191, 191, 100);
            case "LOST/STOLEN" ->
                new Color(255, 191, 191, 100);
            case "OBSOLETE" ->
                new Color(255, 191, 191, 100);
            case "UNSERVICABLE" ->
                new Color(255, 191, 191, 100);
            default ->
                table.getBackground();
        };

        if (isSelected) {
            component.setBackground(table.getSelectionBackground());
        } else {
            component.setBackground(color);
        }

        return component;
    }
}
