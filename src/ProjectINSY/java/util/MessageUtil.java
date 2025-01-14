/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProjectINSY.java.util;

/**
 *
 * @author admin
 */
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class MessageUtil {

    public static void paneDatabaseError(SQLException e) {
        if (e.getErrorCode() == 1451) {
            JOptionPane.showMessageDialog(null, "Item cannot be deleted as it is referenced in another table.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        e.printStackTrace(System.out);
    }

    public enum PaneMessage {
        INVALID_EMAIL
    };

    public static void paneInvalidEmail() {
        JOptionPane.showMessageDialog(null, "Please enter a valid email.\nOnly cvsu.edu.ph or cvsu-silang.edu.ph is allowed.", "Invalid Email", JOptionPane.ERROR_MESSAGE);
    }
}
