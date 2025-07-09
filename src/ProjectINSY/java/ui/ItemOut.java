/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ProjectINSY.java.ui;

import ProjectINSY.java.Main;
import ProjectINSY.java.swing.Form.FormField;
import ProjectINSY.java.util.DatabaseUtil;
import static ProjectINSY.java.util.DatabaseUtil.getColumnValueByInt;
import ProjectINSY.java.util.GuiUtil;
import static ProjectINSY.java.util.GuiUtil.setDefaultField;
import static ProjectINSY.java.util.GuiUtil.setScrollBarCustom;
import static ProjectINSY.java.util.GuiUtil.setTransparentFrame;
import ProjectINSY.java.util.MessageUtil;
import static ProjectINSY.java.util.MessageUtil.paneDatabaseError;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author admin
 */
public class ItemOut extends javax.swing.JPanel {

    private String selectedCode = null;
    private int selectedCodeID = -1;

    private final String PLACEHOLDER_FULL_CODE = "Catalog-I-00-00";
    private final String PLACEHOLDER_LOCATION = "Enter Location";
    private final String PLACEHOLDER_CUSTOMER = "Enter Customer Name";

    private boolean isUpdating = false;

    /**
     * Creates new form LogIn
     */
    public ItemOut() {
        initComponents();

        setTransparentFrame(btnRelease, btnClear);
        btnRelease.setText(null); // No text
        btnRelease.setBorderPainted(false);
        btnRelease.setContentAreaFilled(false);
        btnRelease.setFocusPainted(false);
        btnRelease.setOpaque(false);
        btnRelease.setMargin(new Insets(0, 0, 0, 0));

        fieldCode.setForm(PLACEHOLDER_FULL_CODE, FormField.FieldType.STRING);
        fieldCustomer.setForm(PLACEHOLDER_CUSTOMER, FormField.FieldType.STRING);
        fieldCustomer.getDocument().addDocumentListener(new FieldChangeListener());

        fieldCode.getDocument().addDocumentListener(new FieldChangeListener());
        fieldCustomer.getDocument().addDocumentListener(new FieldChangeListener());

        setScrollBarCustom(scrollPending);
        tablePending.setDefaultTable();
        tablePending.setColumnWidth(new int[]{50, 378, 75, 75, 100, 200});
        tablePending.setIntegerColumn(0);
        tablePending.setIntegerColumn(2);
        tablePending.setPriceColumn(4);
        tablePending.setPriceColumn(5);

        comboChannel.removeAllItems();
        comboChannel.addItem("Lazada");
        comboChannel.addItem("Shopee");
//        comboChannel.resetDefaultComboItem();
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
            if (isUpdating) {
                return;
            }

            try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
                if (DatabaseUtil.recordExists(conn, Main.TB_CATALOG_ITEM, "item_code", fieldCode.getText())) {
                    isUpdating = true;

                    SwingUtilities.invokeLater(() -> {
                        try {
                            searchItem();
                        } catch (SQLException ex) {
                            Logger.getLogger(ItemOut.class.getName()).log(Level.SEVERE, null, ex);
                        }

                        isUpdating = false;
                    });
                }
            } catch (SQLException e) {
                paneDatabaseError(e);
            }

            btnRelease.setEnabled(fieldCustomer.isValidText());
        }
    }

    public void repopulateSuggestions() {
        fieldCustomer.repopulateSuggestions("out_customer", "SELECT DISTINCT out_customer FROM " + Main.TB_ITEM_TRANSACTION);
    }

    public void setScannerFocus() {
        fieldCode.requestFocusInWindow();
    }

    public void searchItem() throws SQLException {
        String codeText = fieldCode.getText();

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            if (codeText.isEmpty() || !DatabaseUtil.recordExists(conn, Main.TB_CATALOG_ITEM, "item_code", fieldCode.getText())) {
                return;
            }
        } catch (SQLException e) {
            paneDatabaseError(e);
        }

        String[] parts = codeText.split("-");
        selectedCodeID = Integer.parseInt(parts[parts.length - 1]);
        selectedCode = codeText;

        DefaultTableModel model = (DefaultTableModel) tablePending.getModel();

        String name = getColumnValueByInt(Main.TB_CATALOG_ITEM, "item_name", "item_id", selectedCodeID);
        int nameCol = 1;
        int qtyCol = 2;
        int rateCol = 4;
        int amountCol = 5;
        int pendingRow = 0;
        boolean alreadyPending = false;

        for (int i = 0; i < model.getRowCount(); i++) {
            Object value = model.getValueAt(i, nameCol);
            if (value.equals(name)) {
                alreadyPending = true;
                pendingRow = i;
            }
        }

        if (alreadyPending) {
            int qty = (Integer) model.getValueAt(pendingRow, qtyCol);
            model.setValueAt(qty + 1, pendingRow, qtyCol);

            float rate = (Float) model.getValueAt(pendingRow, rateCol);
            float amount = (Float) model.getValueAt(pendingRow, amountCol);

            model.setValueAt(amount + rate, pendingRow, amountCol);

            fieldCode.resetToPlaceholder();
            fieldCode.setText("");
            setScannerFocus();
            fieldCode.setForeground(Color.black);
        } else {
            try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
                PreparedStatement pst = conn.prepareStatement("SELECT * FROM " + Main.TB_CATALOG_ITEM + " WHERE item_id = ?");
                pst.setInt(1, selectedCodeID);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    String uom = rs.getString("item_uom");
                    int quantity = 1;
                    float rate = 20;
                    float amount = quantity * rate;
                    model.addRow(new Object[]{
                        model.getRowCount() + 1, name, quantity, uom, rate, amount
                    });

                    fieldCode.resetToPlaceholder();
                    fieldCode.setText("");
                    setScannerFocus();
                    fieldCode.setForeground(Color.black);
                }
            } catch (SQLException e) {
                MessageUtil.paneDatabaseError(e);
            }
        }
    }

    private void clearFields() {
        selectedCode = null;
        selectedCodeID = -1;

        fieldCode.resetToPlaceholder();

        fieldCustomer.resetToPlaceholder();

        setUpdateDeleteEnable();
    }

    public void setUpdateDeleteEnable() {
//        resetBtnEnability(fieldName, btnUpdate);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSearch = new javax.swing.JButton();
        panelMain = new javax.swing.JPanel();
        panelCode = new javax.swing.JPanel();
        labelCode = new javax.swing.JLabel();
        fieldCode = new ProjectINSY.java.swing.Form.FormField();
        imageCode = new javax.swing.JLabel();
        labelScanInfo = new javax.swing.JLabel();
        labelScanInfo1 = new javax.swing.JLabel();
        labelCustomer = new javax.swing.JLabel();
        fieldCustomer = new ProjectINSY.java.swing.Form.FormFieldSuggestion();
        imageCustomer = new javax.swing.JLabel();
        comboChannel = new ProjectINSY.java.swing.ComboBoxSuggestion();
        labelChannel = new javax.swing.JLabel();
        imageChannel = new javax.swing.JLabel();
        labelRelease = new javax.swing.JLabel();
        btnRelease = new javax.swing.JButton();
        labelClear = new javax.swing.JLabel();
        btnClear = new javax.swing.JButton();
        panelFields = new javax.swing.JPanel();
        panelScan = new javax.swing.JPanel();
        labelScan = new javax.swing.JLabel();
        scrollPending = new javax.swing.JScrollPane();
        tablePending = new ProjectINSY.java.swing.Table();

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnSearch.png"))); // NOI18N
        btnSearch.setBorder(null);
        btnSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearch.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnSearch_pressed.png"))); // NOI18N
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        setMaximumSize(new java.awt.Dimension(1840, 900));
        setMinimumSize(new java.awt.Dimension(1840, 900));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(1840, 900));

        panelMain.setBackground(new java.awt.Color(255, 255, 255));
        panelMain.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 102, 24), 2));

        panelCode.setBackground(new java.awt.Color(255, 255, 255));
        panelCode.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 102, 24), 2));
        panelCode.setLayout(null);

        labelCode.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 64)); // NOI18N
        labelCode.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCode.setText("Enter or Scan Code:");
        panelCode.add(labelCode);
        labelCode.setBounds(10, 10, 880, 90);

        fieldCode.setBorder(null);
        fieldCode.setForeground(new java.awt.Color(0, 0, 0));
        fieldCode.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldCode.setText("formField");
        fieldCode.setFont(new java.awt.Font("Bahnschrift", 1, 64)); // NOI18N
        fieldCode.setPlaceholderColor(new java.awt.Color(153, 153, 153));
        fieldCode.setSelectionColor(new java.awt.Color(25, 102, 24));
        panelCode.add(fieldCode);
        fieldCode.setBounds(130, 140, 630, 80);

        imageCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldTextArea.png"))); // NOI18N
        panelCode.add(imageCode);
        imageCode.setBounds(110, 110, 665, 130);

        labelScanInfo.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelScanInfo.setText("that the field above is selected and empty.");
        panelCode.add(labelScanInfo);
        labelScanInfo.setBounds(20, 310, 660, 45);

        labelScanInfo1.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelScanInfo1.setText("When scanning a new barcode, ensure");
        panelCode.add(labelScanInfo1);
        labelScanInfo1.setBounds(20, 260, 660, 45);

        labelCustomer.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelCustomer.setText("Customer Name");
        panelCode.add(labelCustomer);
        labelCustomer.setBounds(110, 400, 260, 30);

        fieldCustomer.setBorder(null);
        fieldCustomer.setForeground(new java.awt.Color(153, 153, 153));
        fieldCustomer.setText("Enter Customer Name");
        fieldCustomer.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldCustomer.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldCustomerFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldCustomerFocusLost(evt);
            }
        });
        panelCode.add(fieldCustomer);
        fieldCustomer.setBounds(120, 450, 640, 50);

        imageCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldFull.png"))); // NOI18N
        panelCode.add(imageCustomer);
        imageCustomer.setBounds(110, 440, 665, 70);

        comboChannel.setBorder(null);
        comboChannel.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        panelCode.add(comboChannel);
        comboChannel.setBounds(120, 580, 310, 50);

        labelChannel.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelChannel.setText("Channel");
        panelCode.add(labelChannel);
        labelChannel.setBounds(110, 530, 173, 30);

        imageChannel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldHalf.png"))); // NOI18N
        panelCode.add(imageChannel);
        imageChannel.setBounds(110, 570, 340, 70);

        labelRelease.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelRelease.setForeground(new java.awt.Color(255, 255, 255));
        labelRelease.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelRelease.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/iconItemOut.png"))); // NOI18N
        labelRelease.setText("Release");
        labelRelease.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelCode.add(labelRelease);
        labelRelease.setBounds(40, 670, 300, 50);

        btnRelease.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnCam.png"))); // NOI18N
        btnRelease.setBorder(null);
        btnRelease.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRelease.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnCam_pressed.png"))); // NOI18N
        btnRelease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReleaseActionPerformed(evt);
            }
        });
        panelCode.add(btnRelease);
        btnRelease.setBounds(40, 670, 300, 50);

        labelClear.setFont(new java.awt.Font("Bahnschrift", 1, 14)); // NOI18N
        labelClear.setForeground(new java.awt.Color(255, 255, 255));
        labelClear.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelClear.setText("Clear");
        labelClear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelCode.add(labelClear);
        labelClear.setBounds(370, 680, 100, 40);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_red.png"))); // NOI18N
        btnClear.setBorder(null);
        btnClear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClear.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_red_pressed.png"))); // NOI18N
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        panelCode.add(btnClear);
        btnClear.setBounds(370, 680, 100, 40);

        panelFields.setBackground(new java.awt.Color(255, 255, 255));
        panelFields.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 102, 24), 2));
        panelFields.setLayout(null);

        panelScan.setBackground(new java.awt.Color(25, 102, 24));

        labelScan.setBackground(new java.awt.Color(25, 102, 24));
        labelScan.setFont(new java.awt.Font("Bebas", 0, 64)); // NOI18N
        labelScan.setForeground(new java.awt.Color(255, 255, 255));
        labelScan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelScan.setText("items to release");
        labelScan.setOpaque(true);

        javax.swing.GroupLayout panelScanLayout = new javax.swing.GroupLayout(panelScan);
        panelScan.setLayout(panelScanLayout);
        panelScanLayout.setHorizontalGroup(
            panelScanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 910, Short.MAX_VALUE)
            .addGroup(panelScanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelScanLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(labelScan)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        panelScanLayout.setVerticalGroup(
            panelScanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 80, Short.MAX_VALUE)
            .addGroup(panelScanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelScanLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(labelScan)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        panelFields.add(panelScan);
        panelScan.setBounds(0, 10, 910, 80);

        scrollPending.setBorder(null);

        tablePending.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "", "Item", "Quantity", "Unit", "Rate", "Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Float.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablePending.setSelectionBackground(new java.awt.Color(25, 102, 24));
        scrollPending.setViewportView(tablePending);

        panelFields.add(scrollPending);
        scrollPending.setBounds(10, 100, 880, 770);

        javax.swing.GroupLayout panelMainLayout = new javax.swing.GroupLayout(panelMain);
        panelMain.setLayout(panelMainLayout);
        panelMainLayout.setHorizontalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelCode, javax.swing.GroupLayout.DEFAULT_SIZE, 901, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(panelFields, javax.swing.GroupLayout.PREFERRED_SIZE, 905, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelMainLayout.setVerticalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelFields, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
                    .addComponent(panelCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void fieldCustomerFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldCustomerFocusLost
        setDefaultField(fieldCustomer, PLACEHOLDER_LOCATION, GuiUtil.FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_fieldCustomerFocusLost

    private void fieldCustomerFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldCustomerFocusGained
        setDefaultField(fieldCustomer, PLACEHOLDER_LOCATION, GuiUtil.FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_fieldCustomerFocusGained

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        try {
            searchItem();
        } catch (SQLException ex) {
            Logger.getLogger(ItemOut.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnReleaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReleaseActionPerformed
        DefaultTableModel model = (DefaultTableModel) tablePending.getModel();

        int rowCount = model.getRowCount();

        String channel = comboChannel.getSelectedItem().toString();
        String customer = fieldCustomer.getText();

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            String query = "INSERT INTO " + Main.TB_ITEM_TRANSACTION + " (out_name, out_quantity, out_price, out_channel, out_customer) "
                    + "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);

            for (int row = 0; row < rowCount; row++) {
                pst.setString(1, (String) model.getValueAt(row, 1));
                pst.setInt(2, (Integer) model.getValueAt(row, 2));
                pst.setFloat(3, (Float) model.getValueAt(row, 5));
                pst.setString(4, channel);
                pst.setString(5, customer);
                pst.executeUpdate();
            }

            model.setRowCount(0);
            clearFields();
        } catch (SQLException e) {
            MessageUtil.paneDatabaseError(e);
        }
    }//GEN-LAST:event_btnReleaseActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        DefaultTableModel model = (DefaultTableModel) tablePending.getModel();
        model.setRowCount(0);
    }//GEN-LAST:event_btnClearActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnRelease;
    private javax.swing.JButton btnSearch;
    private ProjectINSY.java.swing.ComboBoxSuggestion comboChannel;
    private ProjectINSY.java.swing.Form.FormField fieldCode;
    private ProjectINSY.java.swing.Form.FormFieldSuggestion fieldCustomer;
    private javax.swing.JLabel imageChannel;
    private javax.swing.JLabel imageCode;
    private javax.swing.JLabel imageCustomer;
    private javax.swing.JLabel labelChannel;
    private javax.swing.JLabel labelClear;
    private javax.swing.JLabel labelCode;
    private javax.swing.JLabel labelCustomer;
    private javax.swing.JLabel labelRelease;
    private javax.swing.JLabel labelScan;
    private javax.swing.JLabel labelScanInfo;
    private javax.swing.JLabel labelScanInfo1;
    private javax.swing.JPanel panelCode;
    private javax.swing.JPanel panelFields;
    private javax.swing.JPanel panelMain;
    private javax.swing.JPanel panelScan;
    private javax.swing.JScrollPane scrollPending;
    private ProjectINSY.java.swing.Table tablePending;
    // End of variables declaration//GEN-END:variables
}
