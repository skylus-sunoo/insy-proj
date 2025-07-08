package ProjectINSY.java.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.text.DecimalFormat;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import static javax.swing.SwingConstants.LEFT;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class Table extends JTable {

    public Table() {
        setShowHorizontalLines(true);
        setGridColor(new Color(230, 230, 230));
        setRowHeight(40);
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable jtable, Object o, boolean bln, boolean bln1, int i, int i1) {
                TableHeader header = new TableHeader(o + "");
                return header;
            }
        });
    }

    public void addRow(Object[] row) {
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.addRow(row);
    }

    public void fixColumnWidth(int[] widths) {
        if (getColumnCount() == widths.length) {
            for (int i = 0; i < widths.length; i++) {
                getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
            }
        } else {
            System.err.println("Width array length does not match table column count.");
        }
    }

    // PriceCellRenderer for formatting price with 2 decimal places
    public static class PriceCellRenderer extends DefaultTableCellRenderer {

        private static final DecimalFormat dfTwoDecimals = new DecimalFormat("0.00");

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            // Get the default renderer component
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (value instanceof Float || value instanceof Double) {
                // Check if the number of decimals is 1 or more than 2
                String stringValue = value.toString();
                int dotIndex = stringValue.indexOf(".");

                if (dotIndex != -1) {
                    // Extract the part after the decimal point
                    String decimalPart = stringValue.substring(dotIndex + 1);

                    // If there's only one decimal place, format it to two
                    if (decimalPart.length() == 1) {
                        value = dfTwoDecimals.format(value);
                    } // If there's more than two decimal places, keep the original value
                    else if (decimalPart.length() > 2) {
                        setText(value.toString());
                        return c;  // Return immediately with original value
                    }
                }
            }

            // Set the formatted value to be displayed in the cell
            if (value == null) {
                setText("");  // Or set some default value, like "N/A"
            } else {
                setText(value.toString());
            }

            return c;
        }
    }

    // QuantityCellRenderer for left alignment
    public static class QuantityCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            // Get the default renderer component
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Set text alignment to left
            setHorizontalAlignment(LEFT);

            return c;
        }
    }

    public void setPriceColumn(int col) {
        getColumnModel().getColumn(col).setCellRenderer(new Table.PriceCellRenderer());
    }

    public void setIntegerColumn(int col) {
        getColumnModel().getColumn(col).setCellRenderer(new Table.QuantityCellRenderer());
    }
}
