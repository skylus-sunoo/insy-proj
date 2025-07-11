/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ProjectINSY.java.ui;

import ProjectINSY.java.Main;
import ProjectINSY.java.swing.Form.FormField;
import ProjectINSY.java.util.BarcodeUtil;
import ProjectINSY.java.util.DatabaseUtil;
import static ProjectINSY.java.util.DatabaseUtil.getColumnValueByInt;
import static ProjectINSY.java.util.DatabaseUtil.getColumnValueByString;
import ProjectINSY.java.util.GuiUtil;
import static ProjectINSY.java.util.GuiUtil.setDefaultField;
import static ProjectINSY.java.util.GuiUtil.setScrollBarCustom;
import static ProjectINSY.java.util.GuiUtil.setTransparentFrame;
import ProjectINSY.java.util.MessageUtil;
import static ProjectINSY.java.util.MessageUtil.paneDatabaseError;
import ProjectINSY.java.util.TableUtil;
import com.mysql.cj.xdevapi.Statement;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.sql.Timestamp;

/**
 *
 * @author admin
 */
public class ItemOut extends javax.swing.JPanel {

    private int selectedRow = -1;

    private final String PLACEHOLDER_FULL_CODE = "0000000000000";
    private final String PLACEHOLDER_LOCATION = "Enter Location";
    private final String PLACEHOLDER_CUSTOMER = "Enter Customer Name";

    private boolean isUpdating = false;

    /**
     * Creates new form LogIn
     */
    public ItemOut() {
        initComponents();

        setTransparentFrame(btnSave, btnRemove, btnRelease, btnClear);
        btnRelease.setText(null); // No text
        btnRelease.setBorderPainted(false);
        btnRelease.setContentAreaFilled(false);
        btnRelease.setFocusPainted(false);
        btnRelease.setOpaque(false);
        btnRelease.setMargin(new Insets(0, 0, 0, 0));

        fieldCode.setForm(PLACEHOLDER_FULL_CODE, FormField.FieldType.INT);
        fieldCustomer.setForm(PLACEHOLDER_CUSTOMER, FormField.FieldType.STRING);
        fieldQuantity.setForm("0", FormField.FieldType.INT);

        fieldCode.getDocument().addDocumentListener(new FieldChangeListener());
        fieldCustomer.getDocument().addDocumentListener(new FieldChangeListener());
        fieldQuantity.getDocument().addDocumentListener(new FieldChangeListener());

        setScrollBarCustom(scrollPending);
        tablePending.setDefaultTable();
        tablePending.setColumnWidth(new int[]{50, 378, 75, 75, 100, 200});
        tablePending.setIntegerColumn(0);
        tablePending.setIntegerColumn(2);
        tablePending.setPriceColumn(4);
        tablePending.setPriceColumn(5);
        tablePending.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                selectedRow = tablePending.getSelectedRow();
                if (selectedRow >= 0) {
                    selectTablePending(selectedRow);
                }
            }
        });

        comboChannel.removeAllItems();
        comboChannel.addItem("Lazada");
        comboChannel.addItem("Shopee");
//        comboChannel.resetDefaultComboItem();
    }

    public void selectTablePending(int selectedRow) {
        String[] tableRow = TableUtil.selectTableRow(tablePending, selectedRow);
        TableUtil.linkFieldsToTable(tableRow, null, fieldItem, fieldQuantity);
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
                if (DatabaseUtil.recordExists(conn, Main.TB_CATALOG_ITEM, "code", fieldCode.getText())) {
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

            DefaultTableModel model = (DefaultTableModel) tablePending.getModel();
            btnRelease.setEnabled(fieldCustomer.isValidText() && model.getRowCount() > 0);
            btnClear.setEnabled(model.getRowCount() > 0);

            btnSave.setEnabled(fieldQuantity.isValidText() && selectedRow != -1);
            btnRemove.setEnabled(selectedRow != -1);
        }
    }

    public void repopulateSuggestions() {
        fieldCustomer.repopulateSuggestions("customer_name", "SELECT DISTINCT customer_name FROM " + Main.TB_SALES);
    }

    public void setScannerFocus() {
        fieldCode.resetToPlaceholder();
        fieldCode.setText("");
        fieldCode.requestFocusInWindow();
        fieldCode.setForeground(Color.black);
    }

    public void searchItem() throws SQLException {
        String codeText = fieldCode.getText();

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            if (codeText.isEmpty() || !DatabaseUtil.recordExists(conn, Main.TB_CATALOG_ITEM, "code", fieldCode.getText())) {
                return;
            }
        } catch (SQLException e) {
            paneDatabaseError(e);
        }

        DefaultTableModel model = (DefaultTableModel) tablePending.getModel();

        // Remove existing total row if present
//        int lastRowIndex = model.getRowCount() - 1;
//        if (lastRowIndex >= 0) {
//            Object lastRowMarker = model.getValueAt(lastRowIndex, 4); // Column 5 = index 4
//            if (lastRowMarker != null && lastRowMarker.toString().equalsIgnoreCase("Total")) {
//                model.removeRow(lastRowIndex);
//            }
//        }
        String name = getColumnValueByString(Main.TB_CATALOG_ITEM, "name", "code", codeText);
        int nameCol = 1;
        int qtyCol = 2;
        int rateCol = 4;
        int amountCol = 5;
        int pendingRow = -1;

        for (int i = 0; i < model.getRowCount(); i++) {
            Object value = model.getValueAt(i, nameCol);
            if (value.equals(name)) {
                pendingRow = i;
            }
        }

        if (pendingRow != -1) {
            int qty = (Integer) model.getValueAt(pendingRow, qtyCol);
            model.setValueAt(qty + 1, pendingRow, qtyCol);

            float rate = (float) model.getValueAt(pendingRow, rateCol);
            float amount = (float) model.getValueAt(pendingRow, amountCol);

            model.setValueAt(amount + rate, pendingRow, amountCol);
            setScannerFocus();
        } else {
            try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
                PreparedStatement pst = conn.prepareStatement("SELECT uom, price FROM " + Main.TB_CATALOG_ITEM + " WHERE code = ?");
                pst.setString(1, codeText);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    String uom = rs.getString("uom");
                    int quantity = 1;
                    float rate = rs.getFloat("price");
                    float amount = quantity * rate;
                    model.addRow(new Object[]{
                        model.getRowCount() + 1, name, quantity, uom, rate, amount
                    });
                    setScannerFocus();
                }
            } catch (SQLException e) {
                MessageUtil.paneDatabaseError(e);
            }
        }

        // Recalculate and append total row
        float totalAmount = 0f;
        for (int i = 0; i < model.getRowCount(); i++) {
            Object value = model.getValueAt(i, amountCol);
            if (value instanceof Float) {
                totalAmount += (Float) value;
            } else if (value instanceof Double) {
                totalAmount += ((Double) value).floatValue();
            } else if (value instanceof Number) {
                totalAmount += ((Number) value).floatValue();
            }
        }
        fieldTotal.setText(String.format("%.2f", totalAmount));
//        model.addRow(new Object[]{"", "", "", "", "Total", totalAmount});
    }

    private void clearFields() {
        fieldCode.resetToPlaceholder();

        fieldCustomer.resetToPlaceholder();
        fieldTotal.setText("0.00");
        setUpdateDeleteEnable();

        selectedRow = -1;
        fieldItem.setText("No Item Selected");
        fieldQuantity.resetToPlaceholder();
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
        labelCustomer = new javax.swing.JLabel();
        fieldCustomer = new ProjectINSY.java.swing.Form.FormFieldSuggestion();
        imageCustomer = new javax.swing.JLabel();
        comboChannel = new ProjectINSY.java.swing.ComboBoxSuggestion();
        labelChannel = new javax.swing.JLabel();
        imageChannel = new javax.swing.JLabel();
        separatorMain = new javax.swing.JSeparator();
        labelSelected = new javax.swing.JLabel();
        separatorSelected = new javax.swing.JSeparator();
        labelItem = new javax.swing.JLabel();
        fieldItem = new javax.swing.JLabel();
        separatorItem = new javax.swing.JSeparator();
        labelQuantity = new javax.swing.JLabel();
        fieldQuantity = new ProjectINSY.java.swing.Form.FormField();
        imageQuantity = new javax.swing.JLabel();
        labelSave = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        labelRemove = new javax.swing.JLabel();
        btnRemove = new javax.swing.JButton();
        panelFields = new javax.swing.JPanel();
        panelScan = new javax.swing.JPanel();
        labelScan = new javax.swing.JLabel();
        scrollPending = new javax.swing.JScrollPane();
        tablePending = new ProjectINSY.java.swing.Table();
        labelRelease = new javax.swing.JLabel();
        btnRelease = new javax.swing.JButton();
        labelTotal = new javax.swing.JLabel();
        separatorTotal = new javax.swing.JSeparator();
        fieldTotal = new javax.swing.JLabel();
        separatorTotal2 = new javax.swing.JSeparator();
        labelClear = new javax.swing.JLabel();
        btnClear = new javax.swing.JButton();

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
        fieldCode.setBounds(130, 130, 630, 90);

        imageCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldTextArea.png"))); // NOI18N
        panelCode.add(imageCode);
        imageCode.setBounds(110, 110, 665, 130);

        labelCustomer.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelCustomer.setText("Customer Name");
        panelCode.add(labelCustomer);
        labelCustomer.setBounds(110, 280, 260, 30);

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
        fieldCustomer.setBounds(120, 330, 640, 50);

        imageCustomer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldFull.png"))); // NOI18N
        panelCode.add(imageCustomer);
        imageCustomer.setBounds(110, 320, 665, 70);

        comboChannel.setBorder(null);
        comboChannel.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        panelCode.add(comboChannel);
        comboChannel.setBounds(450, 420, 310, 50);

        labelChannel.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelChannel.setText("Channel");
        panelCode.add(labelChannel);
        labelChannel.setBounds(300, 420, 130, 50);

        imageChannel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldHalf.png"))); // NOI18N
        panelCode.add(imageChannel);
        imageChannel.setBounds(440, 410, 340, 70);
        panelCode.add(separatorMain);
        separatorMain.setBounds(30, 520, 820, 20);

        labelSelected.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 48)); // NOI18N
        labelSelected.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelSelected.setText("Selected Item");
        panelCode.add(labelSelected);
        labelSelected.setBounds(30, 580, 290, 70);
        panelCode.add(separatorSelected);
        separatorSelected.setBounds(330, 620, 520, 20);

        labelItem.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelItem.setText("Item");
        panelCode.add(labelItem);
        labelItem.setBounds(60, 670, 80, 40);

        fieldItem.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        fieldItem.setText("No Item Selected");
        panelCode.add(fieldItem);
        fieldItem.setBounds(150, 680, 310, 30);
        panelCode.add(separatorItem);
        separatorItem.setBounds(150, 710, 310, 10);

        labelQuantity.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelQuantity.setText("Quantity");
        panelCode.add(labelQuantity);
        labelQuantity.setBounds(60, 740, 140, 50);

        fieldQuantity.setBorder(null);
        fieldQuantity.setForeground(new java.awt.Color(0, 0, 0));
        fieldQuantity.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldQuantity.setText("formField");
        fieldQuantity.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldQuantity.setPlaceholderColor(new java.awt.Color(153, 153, 153));
        fieldQuantity.setSelectionColor(new java.awt.Color(25, 102, 24));
        panelCode.add(fieldQuantity);
        fieldQuantity.setBounds(220, 750, 230, 30);

        imageQuantity.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldHalf_small.png"))); // NOI18N
        panelCode.add(imageQuantity);
        imageQuantity.setBounds(210, 730, 260, 70);

        labelSave.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelSave.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelSave.setText("Save");
        labelSave.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelCode.add(labelSave);
        labelSave.setBounds(520, 680, 310, 50);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnLong.png"))); // NOI18N
        btnSave.setBorder(null);
        btnSave.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSave.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnLong_pressed.png"))); // NOI18N
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        panelCode.add(btnSave);
        btnSave.setBounds(520, 680, 310, 50);

        labelRemove.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelRemove.setForeground(new java.awt.Color(255, 255, 255));
        labelRemove.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelRemove.setText("Remove");
        labelRemove.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelCode.add(labelRemove);
        labelRemove.setBounds(520, 740, 310, 50);

        btnRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnLong_red.png"))); // NOI18N
        btnRemove.setBorder(null);
        btnRemove.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRemove.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnLong_red_pressed.png"))); // NOI18N
        btnRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveActionPerformed(evt);
            }
        });
        panelCode.add(btnRemove);
        btnRemove.setBounds(520, 740, 310, 50);

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
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Object.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
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
        scrollPending.setBounds(20, 110, 880, 700);

        labelRelease.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelRelease.setForeground(new java.awt.Color(255, 255, 255));
        labelRelease.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelRelease.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/iconItemOut.png"))); // NOI18N
        labelRelease.setText("Release");
        labelRelease.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelFields.add(labelRelease);
        labelRelease.setBounds(390, 820, 300, 50);

        btnRelease.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnCam.png"))); // NOI18N
        btnRelease.setBorder(null);
        btnRelease.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRelease.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnCam_pressed.png"))); // NOI18N
        btnRelease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReleaseActionPerformed(evt);
            }
        });
        panelFields.add(btnRelease);
        btnRelease.setBounds(390, 820, 300, 50);

        labelTotal.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelTotal.setText("Total");
        panelFields.add(labelTotal);
        labelTotal.setBounds(40, 820, 80, 40);
        panelFields.add(separatorTotal);
        separatorTotal.setBounds(130, 860, 240, 10);

        fieldTotal.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        fieldTotal.setText("0.00");
        panelFields.add(fieldTotal);
        fieldTotal.setBounds(130, 830, 240, 30);

        separatorTotal2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        panelFields.add(separatorTotal2);
        separatorTotal2.setBounds(730, 820, 10, 50);

        labelClear.setFont(new java.awt.Font("Bahnschrift", 1, 14)); // NOI18N
        labelClear.setForeground(new java.awt.Color(255, 255, 255));
        labelClear.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelClear.setText("Clear");
        labelClear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelFields.add(labelClear);
        labelClear.setBounds(760, 820, 100, 50);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_red.png"))); // NOI18N
        btnClear.setBorder(null);
        btnClear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClear.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_red_pressed.png"))); // NOI18N
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        panelFields.add(btnClear);
        btnClear.setBounds(760, 820, 100, 50);

        javax.swing.GroupLayout panelMainLayout = new javax.swing.GroupLayout(panelMain);
        panelMain.setLayout(panelMainLayout);
        panelMainLayout.setHorizontalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelCode, javax.swing.GroupLayout.DEFAULT_SIZE, 901, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelFields, javax.swing.GroupLayout.PREFERRED_SIZE, 917, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        final String INVENTORY_TYPE_SALE = "SALE";
        final String INVENTORY_LOCATION = "MAIN SUPPLY ROOM";

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME)) {
            conn.setAutoCommit(false);  // Start transaction

            // Insert into tb_sales
            String saleQuery = "INSERT INTO " + Main.TB_SALES + " (customer_name, channel, created_by) VALUES (?, ?, ?)";
            String checkQuantityQuery = "SELECT quantity FROM " + Main.TB_INVENTORY_BALANCE + " WHERE item_id = ? AND location = ?";

            try (PreparedStatement pstSale = conn.prepareStatement(saleQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {

                pstSale.setString(1, customer);
                pstSale.setString(2, channel);
                pstSale.setInt(3, Main.getUserSessionID());
                pstSale.executeUpdate();

                ResultSet rs = pstSale.getGeneratedKeys();
                int saleId = -1;
                if (rs.next()) {
                    saleId = rs.getInt(1);
                }
                rs.close();

                if (saleId == -1) {
                    throw new SQLException("Failed to retrieve generated sale ID.");
                }

                // Prepare insert statements for sales items and inventory transactions
                String saleItemQuery = "INSERT INTO " + Main.TB_SALES_ITEMS
                        + " (sale_id, item_id, quantity, unit_price, total_price) VALUES (?, ?, ?, ?, ?)";
                String transactionQuery = "INSERT INTO " + Main.TB_INVENTORY_TRANSACTION
                        + " (item_id, location, type, quantity_change, created_by) VALUES (?, ?, ?, ?, ?)";
                String updateQuery = "UPDATE " + Main.TB_INVENTORY_BALANCE + " "
                        + "SET quantity = quantity - ?, updated_at = ? "
                        + "WHERE item_id = ? AND location = ?";

                try (PreparedStatement pstCheckQty = conn.prepareStatement(checkQuantityQuery); PreparedStatement pstSaleItem = conn.prepareStatement(saleItemQuery); PreparedStatement pstInventory = conn.prepareStatement(transactionQuery); PreparedStatement pstUpdateBalance = conn.prepareStatement(updateQuery)) {
                    for (int row = 0; row < rowCount; row++) {
                        String itemName = (String) model.getValueAt(row, 1);
                        int itemId = Integer.parseInt(getColumnValueByString(Main.TB_CATALOG_ITEM, "item_id", "name", itemName));
                        int quantity = Integer.parseInt(model.getValueAt(row, 2).toString());
                        float unitPrice = Float.parseFloat(model.getValueAt(row, 4).toString());
                        float totalPrice = Float.parseFloat(model.getValueAt(row, 5).toString());

                        // Check current quantity before proceeding
                        pstCheckQty.setInt(1, itemId);
                        pstCheckQty.setString(2, INVENTORY_LOCATION);
                        try (ResultSet rsCheck = pstCheckQty.executeQuery()) {
                            if (rsCheck.next()) {
                                int currentQuantity = rsCheck.getInt("quantity");
                                if (currentQuantity < quantity) {
                                    throw new SQLException("Insufficient inventory for item '" + itemName
                                            + "'. Available: " + currentQuantity + ", Requested: " + quantity);
                                }
                            } else {
                                throw new SQLException("No inventory record found for item '" + itemName
                                        + "' at location " + INVENTORY_LOCATION);
                            }
                        }

                        // Insert into tb_sales_items
                        pstSaleItem.setInt(1, saleId);
                        pstSaleItem.setInt(2, itemId);
                        pstSaleItem.setInt(3, quantity);
                        pstSaleItem.setFloat(4, unitPrice);
                        pstSaleItem.setFloat(5, totalPrice);
                        pstSaleItem.executeUpdate();

                        // Insert into tb_inventory_transaction
                        pstInventory.setInt(1, itemId);
                        pstInventory.setString(2, INVENTORY_LOCATION);
                        pstInventory.setString(3, INVENTORY_TYPE_SALE);
                        pstInventory.setInt(4, -quantity);  // SALE is an outbound transaction
                        pstInventory.setInt(5, Main.getUserSessionID());
                        pstInventory.executeUpdate();

                        // Update inventory balance
                        pstUpdateBalance.setInt(1, quantity); // subtracting this
                        pstUpdateBalance.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                        pstUpdateBalance.setInt(3, itemId);
                        pstUpdateBalance.setString(4, INVENTORY_LOCATION);
                        pstUpdateBalance.executeUpdate();
                    }
                }

                // Commit if all successful
                conn.commit();
                model.setRowCount(0);
                clearFields();
                JOptionPane.showMessageDialog(this, "Sale Complete!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException e) {
                conn.rollback();  // Rollback all changes on error
                throw e;          // Re-throw to be caught by outer catch
            }

        } catch (SQLException e) {
            MessageUtil.paneDatabaseError(e);
        }
    }//GEN-LAST:event_btnReleaseActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        int response = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to clear all items?",
                "Confirm Clear",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (response == JOptionPane.YES_OPTION) {
            DefaultTableModel model = (DefaultTableModel) tablePending.getModel();
            model.setRowCount(0);
            clearFields();
        }
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveActionPerformed
        ((DefaultTableModel) tablePending.getModel()).removeRow(selectedRow);

        selectedRow = -1;
        fieldItem.setText("No Item Selected");
        fieldQuantity.resetToPlaceholder();
        tablePending.clearSelection();
    }//GEN-LAST:event_btnRemoveActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        ((DefaultTableModel) tablePending.getModel()).setValueAt(fieldQuantity.getText(), selectedRow, 2);

        selectedRow = -1;
        fieldItem.setText("No Item Selected");
        fieldQuantity.resetToPlaceholder();
        tablePending.clearSelection();
    }//GEN-LAST:event_btnSaveActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnRelease;
    private javax.swing.JButton btnRemove;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSearch;
    private ProjectINSY.java.swing.ComboBoxSuggestion comboChannel;
    private ProjectINSY.java.swing.Form.FormField fieldCode;
    private ProjectINSY.java.swing.Form.FormFieldSuggestion fieldCustomer;
    private javax.swing.JLabel fieldItem;
    private ProjectINSY.java.swing.Form.FormField fieldQuantity;
    private javax.swing.JLabel fieldTotal;
    private javax.swing.JLabel imageChannel;
    private javax.swing.JLabel imageCode;
    private javax.swing.JLabel imageCustomer;
    private javax.swing.JLabel imageQuantity;
    private javax.swing.JLabel labelChannel;
    private javax.swing.JLabel labelClear;
    private javax.swing.JLabel labelCode;
    private javax.swing.JLabel labelCustomer;
    private javax.swing.JLabel labelItem;
    private javax.swing.JLabel labelQuantity;
    private javax.swing.JLabel labelRelease;
    private javax.swing.JLabel labelRemove;
    private javax.swing.JLabel labelSave;
    private javax.swing.JLabel labelScan;
    private javax.swing.JLabel labelSelected;
    private javax.swing.JLabel labelTotal;
    private javax.swing.JPanel panelCode;
    private javax.swing.JPanel panelFields;
    private javax.swing.JPanel panelMain;
    private javax.swing.JPanel panelScan;
    private javax.swing.JScrollPane scrollPending;
    private javax.swing.JSeparator separatorItem;
    private javax.swing.JSeparator separatorMain;
    private javax.swing.JSeparator separatorSelected;
    private javax.swing.JSeparator separatorTotal;
    private javax.swing.JSeparator separatorTotal2;
    private ProjectINSY.java.swing.Table tablePending;
    // End of variables declaration//GEN-END:variables
}
