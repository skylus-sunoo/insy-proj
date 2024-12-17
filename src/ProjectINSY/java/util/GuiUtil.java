/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProjectINSY.java.util;

/**
 *
 * @author admin
 */
import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

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
}
