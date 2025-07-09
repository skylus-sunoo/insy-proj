/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProjectINSY.java.swing.Form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Admin
 */
public class FormField extends JTextField {

    private String placeholder;
    private Color placeholderColor;
    private Color foregroundColor;

    public Color getPlaceholderColor() {
        return placeholderColor;
    }

    public void setPlaceholderColor(Color placeholderColor) {
        this.placeholderColor = placeholderColor;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public enum FieldType {
        STRING, INT, FLOAT, DATE
    }
    private FieldType fieldType;

    public FormField() {
        super();

        this.placeholder = super.getText();  // Default placeholder
        this.fieldType = FieldType.STRING;  // Default field type
        this.placeholderColor = Color.GRAY;  // Default placeholder color
        this.foregroundColor = getForeground();  // Default foreground color

        initializeField();  // Initialize field with listeners, etc.
    }

    public void setForm(String placeholder, FieldType fieldType) {
        this.placeholder = (placeholder != null) ? placeholder : "";
        this.fieldType = (fieldType != null) ? fieldType : FieldType.STRING;
        this.resetToPlaceholder();
    }

    public void setForm(String placeholder, FieldType fieldType, Color placeholderColor, Color foregroundColor) {
        this.placeholder = (placeholder != null) ? placeholder : "";
        this.fieldType = (fieldType != null) ? fieldType : FieldType.STRING;
        this.placeholderColor = (placeholderColor != null) ? placeholderColor : Color.GRAY;
        this.foregroundColor = (foregroundColor != null) ? foregroundColor : Color.BLACK;
        this.resetToPlaceholder();
    }

    // Initialize the text field with focus listeners and input validation
    private void initializeField() {
        this.resetToPlaceholder();
        setCaretPosition(0);  // Place the caret at the beginning

        // Add focus listeners to handle placeholder visibility
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                handleFocusGained();
            }

            @Override
            public void focusLost(FocusEvent e) {
                handleFocusLost();
            }
        });

        // Add key listener for enforcing input type (INT or FLOAT)
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent evt) {
                enforceFieldType(evt);
            }
        });
    }

    // Handle focus gained event - hide placeholder if necessary
    private void handleFocusGained() {
        if (isPlaceholder()) {
            setText("");
            setForeground(foregroundColor);  // Reset text color to normal when user starts typing
        }
    }

    // Handle focus lost event - restore placeholder if the field is empty
    private void handleFocusLost() {
        if (getText().isEmpty()) {
            this.resetToPlaceholder();
        }
    }

    // Enforce the input type (INT, FLOAT, or STRING)
    private void enforceFieldType(KeyEvent evt) {
        char c = evt.getKeyChar();
        String currentText = getText();

        switch (fieldType) {
            case INT ->
                enforceInteger(evt, c);
            case FLOAT ->
                enforceFloat(evt, c, currentText);
            case STRING -> {
            }
        }
        // No validation needed for strings
    }

    // Enforce integer input (only digits allowed)
    private void enforceInteger(KeyEvent evt, char c) {
        if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
            evt.consume();  // Prevent non-numeric characters
        }
    }

    // Enforce float input (only digits and one decimal point allowed)
    private void enforceFloat(KeyEvent evt, char c, String currentText) {
        // Allow digits, decimal point, and control characters
        if (c == '.' && currentText.indexOf('.') != -1) {
            evt.consume();  // Prevent multiple decimal points
        } else if (!Character.isDigit(c) && c != '.' && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
            evt.consume();  // Prevent non-numeric and non-decimal characters
        }
    }

    // Getter and Setter for placeholder text
    public void setPlaceholder() {
        this.placeholder = "";
        this.resetToPlaceholder();
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        this.resetToPlaceholder();
    }

    public void resetToPlaceholder() {
        if (fieldType == FieldType.DATE) {
            clearFieldDate();
            setForeground(foregroundColor);
        } else {
            clearField(placeholder);
            setForeground(placeholderColor);
        }
    }

    public void clearField(String defaultText) {
        setText(defaultText);
    }

    public void clearFieldDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        clearField(formattedDate);
    }

    public String getPlaceholder() {
        return placeholder;
    }

    // Getter and Setter for field type
    public void setType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public boolean isPlaceholder() {
        return getText().trim().equals(placeholder);
    }

    public boolean isValidText() {
        return !isPlaceholder() && !getText().trim().isBlank();
    }

    public String getValue() {
        String text = super.getText();

        switch (fieldType) {
            case STRING -> {
                // For STRING, just return the text as is
                return text;
            }

            case INT -> {
                // For INT, remove all non-numeric characters
                return text.replaceAll("[^0-9]", "");
            }

            case FLOAT -> {
                // For FLOAT, remove all non-numeric characters except for one decimal point
                // First, replace all non-numeric characters except the dot
                String result = text.replaceAll("[^0-9.]", "");

                // Ensure only one decimal point exists
                int decimalIndex = result.indexOf(".");
                if (decimalIndex != -1) {
                    // Remove any other decimals after the first one
                    result = result.substring(0, decimalIndex + 1) + result.substring(decimalIndex + 1).replace(".", "");
                }

                return result;
            }

            default -> {
                return text;
            }
        }
    }
}
