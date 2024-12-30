/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ProjectINSY.java.ui;

import ProjectINSY.java.Main;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import static ProjectINSY.java.util.DatabaseUtil.*;
import static ProjectINSY.java.util.GuiUtil.*;
import static ProjectINSY.java.util.MessageUtil.*;
import static ProjectINSY.java.util.SecurityUtil.*;
import static ProjectINSY.java.util.SessionUtil.*;
import java.awt.event.KeyEvent;

/**
 *
 * @author admin
 */
public class LogIn extends javax.swing.JPanel {

    private static final String EMAIL_PATTERN
            = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@(cvsu\\.edu\\.ph|cvsu-silang\\.edu\\.ph)$";

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void autoCompleteEmail(JTextField field) {
        if (!field.getText().trim().equals(PLACEHOLDER_EMAIL) && !field.getText().trim().contains("@")) {
            fieldEmail.setText(field.getText().trim() + "@cvsu.edu.ph");
        }
    }

    private final String PLACEHOLDER_EMAIL = "Enter Email Address";
    private final String PLACEHOLDER_PASSWORD = "**************";

    /**
     * Creates new form LogIn
     */
    private final Main main;

    public LogIn(Main main) {
        this.main = main;
        initComponents();

        panelQuit.setOpaque(false);
        setTransparentFrame(LogIn.this, fieldEmail, fieldPassword, btnForgotPasword, btnSignUp, btnQuit);
        btnSignUp.setText("<html>Don't have an account yet? <font color='#0C2BFF'>Sign up now.</font></html>");

        fieldEmail.getDocument().addDocumentListener(new FieldChangeListener());
        fieldPassword.getDocument().addDocumentListener(new FieldChangeListener());

//        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher((KeyEvent e) -> {
//            if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_ENTER && panelLogIn.isVisible()) {
//                if (btnLogIn.isEnabled()) {
//                    String log_email = fieldEmail.getText();
//                    String log_password = new String(fieldPassword.getPassword()).trim();
//
//                    if (isValidEmail(log_email)) {
//                        logInAccount(log_email, log_password);
//                    } else {
//                        paneInvalidEmail();
//                    }
//                }
//                return true;
//            }
//            return false;
//        });
    }

    private class FieldChangeListener implements DocumentListener, ActionListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            checkFields();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            checkFields();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            checkFields();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            checkFields();
        }

        private void checkFields() {
            String user_email = fieldEmail.getText().trim();
            String user_password = new String(fieldPassword.getPassword()).trim();

            btnLogIn.setEnabled(!user_email.isEmpty()
                    && !user_email.equals(PLACEHOLDER_EMAIL)
                    && !user_password.isEmpty()
                    && !user_password.equals(PLACEHOLDER_PASSWORD));
        }
    }

    private void logInAccount(String log_email, String log_password) {

//        byte[] employee_salt = generateSalt(16);
//        String user_email = "johnpatrick.skidmore@cvsu.edu.ph";
//        String user_password = toHash("123", employee_salt);
//        String confirm_password = toHash("123", employee_salt);
//
//        if (!user_password.equals(confirm_password)) {
//            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
//        } else {
//            String user_saltString = bytetoString(employee_salt);
//            String query = "INSERT INTO " + Main.TB_USER + " (user_email, user_salt, user_password, user_fname, user_lname)\n"
//                        + "VALUES (?, ?, ?, ?, ?)";
//
//            try (Connection conn = getConnection(Main.DB_NAME)) {
//                executeUpdate(conn, query, user_email, user_saltString, user_password, "John Patrick", "Skidmore");
//                JOptionPane.showMessageDialog(this, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
//
//            } catch (SQLException e) {
//                JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//                e.printStackTrace(System.out);
//            }
//        }
        byte[] user_salt;

        String saltQuery = "SELECT user_salt FROM " + Main.TB_USER + " WHERE user_email = ?";
        try (Connection conn = getConnection(Main.DB_NAME); PreparedStatement pst = conn.prepareStatement(saltQuery)) {
            pst.setString(1, log_email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    user_salt = stringToByte(rs.getString("user_salt"));
                } else {
                    JOptionPane.showMessageDialog(this, "This email does not exist!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        } catch (SQLException e) {
            paneDatabaseError(e);
            return;
        }

        String userPassword = toHash(log_password, user_salt);

        String query = "SELECT * FROM " + Main.TB_USER + " WHERE user_email = ? AND user_password = ?";

        try (Connection conn = getConnection(Main.DB_NAME); PreparedStatement pst = prepareQueryWithParameters(conn, query, log_email, userPassword); ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                int userID = rs.getInt("user_ID");
                String userEmail = rs.getString("user_email");
                updateUserSession(userID, userEmail);

                fieldEmail.setText("");
                fieldPassword.setText("");
                setDefaultField(fieldEmail, PLACEHOLDER_EMAIL, FieldFocus.LOST, Color.BLACK);
                setDefaultField(fieldPassword, PLACEHOLDER_PASSWORD, FieldFocus.LOST, Color.BLACK);

                fieldPassword.setEchoChar('*');
                setBtnIcon(btnPasswordViewer, "/ProjectINSY/resources/interface/btnPasswordView_remove.png");
                setDefaultField(fieldPassword, PLACEHOLDER_PASSWORD, FieldFocus.LOST, Color.BLACK);

                main.showMenu();
                main.setDefaultForm();
            } else {
                JOptionPane.showMessageDialog(this, "Wrong Email or Password!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException e) {
            paneDatabaseError(e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelLogIn = new javax.swing.JPanel();
        btnLogIn = new javax.swing.JButton();
        btnForgotPasword = new javax.swing.JButton();
        imageSilang = new javax.swing.JLabel();
        labelInventorySystem = new javax.swing.JLabel();
        panelLogInFields = new javax.swing.JPanel();
        btnPasswordViewer = new javax.swing.JButton();
        fieldEmail = new javax.swing.JTextField();
        fieldPassword = new javax.swing.JPasswordField();
        labelEmail = new javax.swing.JLabel();
        imageEmailBg = new javax.swing.JLabel();
        imagePasswordBg = new javax.swing.JLabel();
        labelPassword = new javax.swing.JLabel();
        btnSignUp = new javax.swing.JButton();
        panelQuit = new javax.swing.JPanel();
        labelQuit = new javax.swing.JLabel();
        btnQuit = new javax.swing.JButton();
        btnAuto = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(1840, 900));
        setMinimumSize(new java.awt.Dimension(1840, 900));
        setPreferredSize(new java.awt.Dimension(1840, 900));

        panelLogIn.setBackground(new java.awt.Color(246, 243, 237));
        panelLogIn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 102, 24)));
        panelLogIn.setMaximumSize(new java.awt.Dimension(427, 500));
        panelLogIn.setMinimumSize(new java.awt.Dimension(427, 500));

        btnLogIn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnLogIn.png"))); // NOI18N
        btnLogIn.setBorder(null);
        btnLogIn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogIn.setEnabled(false);
        btnLogIn.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnLogIn_pressed.png"))); // NOI18N
        btnLogIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogInActionPerformed(evt);
            }
        });
        btnLogIn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnLogInKeyPressed(evt);
            }
        });

        btnForgotPasword.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        btnForgotPasword.setForeground(new java.awt.Color(255, 102, 102));
        btnForgotPasword.setText("FORGOT PASSWORD?");
        btnForgotPasword.setBorder(null);
        btnForgotPasword.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnForgotPasword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnForgotPaswordActionPerformed(evt);
            }
        });

        imageSilang.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageSilang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/images/cvsu-silang-logo.png"))); // NOI18N

        labelInventorySystem.setBackground(new java.awt.Color(25, 102, 24));
        labelInventorySystem.setFont(new java.awt.Font("Bebas", 0, 64)); // NOI18N
        labelInventorySystem.setForeground(new java.awt.Color(255, 255, 255));
        labelInventorySystem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelInventorySystem.setText("inventory system");
        labelInventorySystem.setOpaque(true);

        panelLogInFields.setOpaque(false);
        panelLogInFields.setLayout(null);

        btnPasswordViewer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnPasswordView_remove.png"))); // NOI18N
        btnPasswordViewer.setBorder(null);
        btnPasswordViewer.setContentAreaFilled(false);
        btnPasswordViewer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPasswordViewer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasswordViewerActionPerformed(evt);
            }
        });
        panelLogInFields.add(btnPasswordViewer);
        btnPasswordViewer.setBounds(500, 180, 30, 40);

        fieldEmail.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        fieldEmail.setForeground(new java.awt.Color(153, 153, 153));
        fieldEmail.setText("Enter Email Address");
        fieldEmail.setBorder(null);
        fieldEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldEmailFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldEmailFocusLost(evt);
            }
        });
        panelLogInFields.add(fieldEmail);
        fieldEmail.setBounds(10, 50, 520, 40);

        fieldPassword.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        fieldPassword.setForeground(new java.awt.Color(153, 153, 153));
        fieldPassword.setText("**************");
        fieldPassword.setBorder(null);
        fieldPassword.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldPasswordFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldPasswordFocusLost(evt);
            }
        });
        panelLogInFields.add(fieldPassword);
        fieldPassword.setBounds(10, 180, 490, 40);

        labelEmail.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 30)); // NOI18N
        labelEmail.setText("Email Address");
        panelLogInFields.add(labelEmail);
        labelEmail.setBounds(0, 0, 540, 30);

        imageEmailBg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldLogIn.png"))); // NOI18N
        panelLogInFields.add(imageEmailBg);
        imageEmailBg.setBounds(0, 40, 540, 60);

        imagePasswordBg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldLogIn.png"))); // NOI18N
        panelLogInFields.add(imagePasswordBg);
        imagePasswordBg.setBounds(0, 170, 550, 60);

        labelPassword.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 30)); // NOI18N
        labelPassword.setText("Password");
        panelLogInFields.add(labelPassword);
        labelPassword.setBounds(0, 130, 540, 30);

        btnSignUp.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        btnSignUp.setText("Don't have an account yet? Sign up now.");
        btnSignUp.setBorder(null);
        btnSignUp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSignUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSignUpActionPerformed(evt);
            }
        });

        panelQuit.setLayout(null);

        labelQuit.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelQuit.setForeground(new java.awt.Color(255, 255, 255));
        labelQuit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelQuit.setText("Quit");
        labelQuit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelQuit.add(labelQuit);
        labelQuit.setBounds(30, 3, 35, 30);

        btnQuit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_red.png"))); // NOI18N
        btnQuit.setBorder(null);
        btnQuit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitActionPerformed(evt);
            }
        });
        panelQuit.add(btnQuit);
        btnQuit.setBounds(0, 0, 95, 35);

        javax.swing.GroupLayout panelLogInLayout = new javax.swing.GroupLayout(panelLogIn);
        panelLogIn.setLayout(panelLogInLayout);
        panelLogInLayout.setHorizontalGroup(
            panelLogInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(imageSilang, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
            .addComponent(labelInventorySystem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelLogInLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(panelLogInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelLogInFields, javax.swing.GroupLayout.PREFERRED_SIZE, 540, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelLogInLayout.createSequentialGroup()
                        .addGap(0, 157, Short.MAX_VALUE)
                        .addGroup(panelLogInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLogInLayout.createSequentialGroup()
                                .addComponent(btnForgotPasword)
                                .addGap(205, 205, 205))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLogInLayout.createSequentialGroup()
                                .addComponent(btnSignUp, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(181, 181, 181))))))
            .addGroup(panelLogInLayout.createSequentialGroup()
                .addGap(146, 146, 146)
                .addComponent(btnLogIn)
                .addGap(28, 146, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLogInLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelQuit, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelLogInLayout.setVerticalGroup(
            panelLogInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLogInLayout.createSequentialGroup()
                .addComponent(imageSilang)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelInventorySystem, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                .addComponent(panelLogInFields, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(btnLogIn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnForgotPasword, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSignUp)
                .addGap(9, 9, 9)
                .addComponent(panelQuit, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btnAuto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnLogIn_pressed.png"))); // NOI18N
        btnAuto.setBorder(null);
        btnAuto.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAuto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAutoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(246, Short.MAX_VALUE)
                .addComponent(btnAuto)
                .addGap(68, 68, 68)
                .addComponent(panelLogIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(620, 620, 620))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(75, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panelLogIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(75, 75, 75))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(102, 102, 102)
                        .addComponent(btnAuto)
                        .addGap(147, 147, 147))))
        );
    }// </editor-fold>//GEN-END:initComponents

    public void addEventMenu(ActionListener event) {
        btnLogIn.addActionListener(event);
    }

    private void btnLogInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogInActionPerformed
        String log_email = fieldEmail.getText();
        String log_password = new String(fieldPassword.getPassword()).trim();

        if (isValidEmail(log_email)) {
            logInAccount(log_email, log_password);
        } else {
            paneInvalidEmail();
        }
    }//GEN-LAST:event_btnLogInActionPerformed

    private void btnForgotPaswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnForgotPaswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnForgotPaswordActionPerformed

    private void fieldEmailFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldEmailFocusGained
        setDefaultField(fieldEmail, PLACEHOLDER_EMAIL, FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_fieldEmailFocusGained

    private void fieldEmailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldEmailFocusLost
        setDefaultField(fieldEmail, PLACEHOLDER_EMAIL, FieldFocus.LOST, Color.BLACK);
        autoCompleteEmail(fieldEmail);
    }//GEN-LAST:event_fieldEmailFocusLost

    private void fieldPasswordFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldPasswordFocusGained
        setDefaultField(fieldPassword, PLACEHOLDER_PASSWORD, FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_fieldPasswordFocusGained

    private void fieldPasswordFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldPasswordFocusLost
        setDefaultField(fieldPassword, PLACEHOLDER_PASSWORD, FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_fieldPasswordFocusLost

    private void btnPasswordViewerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasswordViewerActionPerformed
        if (fieldPassword.getEchoChar() == '*') {
            fieldPassword.setEchoChar((char) 0);
            setBtnIcon(btnPasswordViewer, "/ProjectINSY/resources/interface/btnPasswordView.png");
            setDefaultField(fieldPassword, PLACEHOLDER_PASSWORD, FieldFocus.GAINED, Color.BLACK);
        } else {
            fieldPassword.setEchoChar('*');
            setBtnIcon(btnPasswordViewer, "/ProjectINSY/resources/interface/btnPasswordView_remove.png");
            setDefaultField(fieldPassword, PLACEHOLDER_PASSWORD, FieldFocus.LOST, Color.BLACK);
        }
    }//GEN-LAST:event_btnPasswordViewerActionPerformed

    private void btnSignUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignUpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSignUpActionPerformed

    private void btnLogInKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnLogInKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String log_email = fieldEmail.getText();
            String log_password = new String(fieldPassword.getPassword()).trim();

            if (isValidEmail(log_email)) {
                logInAccount(log_email, log_password);
            } else {
                paneInvalidEmail();
            }
        }
    }//GEN-LAST:event_btnLogInKeyPressed

    private void btnAutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAutoActionPerformed
        String log_email = "johnpatrick.skidmore@cvsu.edu.ph";
        String log_password = "123";

        if (isValidEmail(log_email)) {
            logInAccount(log_email, log_password);
        } else {
            paneInvalidEmail();
        }
    }//GEN-LAST:event_btnAutoActionPerformed

    private void btnQuitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnQuitActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAuto;
    private javax.swing.JButton btnForgotPasword;
    private javax.swing.JButton btnLogIn;
    private javax.swing.JButton btnPasswordViewer;
    private javax.swing.JButton btnQuit;
    private javax.swing.JButton btnSignUp;
    private javax.swing.JTextField fieldEmail;
    private javax.swing.JPasswordField fieldPassword;
    private javax.swing.JLabel imageEmailBg;
    private javax.swing.JLabel imagePasswordBg;
    private javax.swing.JLabel imageSilang;
    private javax.swing.JLabel labelEmail;
    private javax.swing.JLabel labelInventorySystem;
    private javax.swing.JLabel labelPassword;
    private javax.swing.JLabel labelQuit;
    private javax.swing.JPanel panelLogIn;
    private javax.swing.JPanel panelLogInFields;
    private javax.swing.JPanel panelQuit;
    // End of variables declaration//GEN-END:variables
}
