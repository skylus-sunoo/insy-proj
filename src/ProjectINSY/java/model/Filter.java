/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProjectINSY.java.model;

import ProjectINSY.java.Main;
import javax.swing.JComboBox;
import javax.swing.JTextField;

/**
 *
 * @author admin
 */
public class Filter {

    private String filterSQL;
    private String columnName, orderString, comparatorString;
    private String defaultString = null, defaultComparatorString = null;

    public void setFilterSQL(String filter_sql) {
        this.filterSQL = filter_sql;
    }

    public String getFilterSQL() {
        return filterSQL;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDefaultString() {
        return defaultString;
    }

    public void setDefaultString(String defaultString) {
        this.defaultString = defaultString;
    }

    public enum FilterOrder {
        START, NOT_START
    }

    public enum FilterComparator {
        EQUAL, GREATER_THAN, LESSER_THAN, GREATER_THAN_DATE, LESSER_THAN_DATE
    }

    /**
     *
     * @param order
     * @param column_name
     * @param comparator
     * @param default_string
     */
    public Filter(FilterOrder order, String column_name, FilterComparator comparator, String default_string) {
        this.columnName = column_name;
        this.defaultString = default_string;

        switch (order) {
            case START ->
                orderString = "";
            case NOT_START ->
                orderString = "&& ";
            default -> {
            }
        }

        switch (comparator) {
            case EQUAL -> {
                comparatorString = " = ";
                defaultComparatorString = " IS NOT NULL ";
                filterSQL = orderString + columnName + defaultComparatorString;
            }
            case GREATER_THAN -> {
                comparatorString = " >= ";
                defaultComparatorString = Main.filterMinNumber + " ";
                filterSQL = orderString + columnName + comparatorString + defaultComparatorString;
            }
            case LESSER_THAN -> {
                comparatorString = " <= ";
                defaultComparatorString = Main.filterMaxNumber + " ";
                filterSQL = orderString + columnName + comparatorString + defaultComparatorString;
            }
            case GREATER_THAN_DATE -> {
                comparatorString = " >= ";
                defaultComparatorString = Main.filterMinDate + " ";
                filterSQL = orderString + columnName + comparatorString + defaultComparatorString;
            }
            case LESSER_THAN_DATE -> {
                comparatorString = " <= ";
                defaultComparatorString = Main.filterMaxDate + " ";
                filterSQL = orderString + columnName + comparatorString + defaultComparatorString;
            }
            default -> {
            }
        }
        
//        System.out.println(filterSQL);
    }

    public void createFilter(Object inputComponent) {
        String selectedValue = null;

        if (inputComponent instanceof JComboBox) {
            JComboBox<?> comboBox = (JComboBox<?>) inputComponent;
            Object selectedItem = comboBox.getSelectedItem();
            if (selectedItem != null) {
                selectedValue = selectedItem.toString();
            }
        } else if (inputComponent instanceof JTextField textField) {
            selectedValue = textField.getText();
        } else {
            throw new IllegalArgumentException("Unsupported input component type. Must be JComboBox or JTextField.");
        }

        if (selectedValue == null || selectedValue.trim().isEmpty() || selectedValue.equals(defaultString)) {
            if (defaultComparatorString.equals(" IS NOT NULL ")) {
                setFilterSQL(orderString + columnName + defaultComparatorString);
            } else {
                setFilterSQL(orderString + columnName + comparatorString + defaultComparatorString);
            }
        } else {
            setFilterSQL(orderString + columnName + comparatorString + "'" + selectedValue + "' ");
        }
    }
}
