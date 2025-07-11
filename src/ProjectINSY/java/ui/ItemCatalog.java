/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ProjectINSY.java.ui;

import ProjectINSY.java.Main;
import ProjectINSY.java.model.ItemPanel;
import ProjectINSY.java.swing.Form.FormField.FieldType;
import ProjectINSY.java.util.BarcodeUtil;
import static ProjectINSY.java.util.BarcodeUtil.generateCode;
import ProjectINSY.java.util.DatabaseUtil;
import static ProjectINSY.java.util.DatabaseUtil.getColumnValueByInt;
import static ProjectINSY.java.util.DatabaseUtil.getColumnValueByString;
import static ProjectINSY.java.util.DatabaseUtil.getTimestampNow;
import ProjectINSY.java.util.GuiUtil;
import java.sql.ResultSet;
import static ProjectINSY.java.util.GuiUtil.resetBtnEnability;
import static ProjectINSY.java.util.GuiUtil.setScrollBarCustom;
import static ProjectINSY.java.util.GuiUtil.setTransparentFrame;
import ProjectINSY.java.util.MessageUtil;
import static ProjectINSY.java.util.MessageUtil.paneDatabaseError;
import ProjectINSY.java.util.TableUtil;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author admin
 */
public class ItemCatalog extends ItemPanel {

    private String current_barcode = null;

    /**
     * Creates new form LogIn
     */
    public ItemCatalog() {
        initComponents();

        setScrollBarCustom(scrollItem);
        setScrollBarCustom(scrollQueue);

        setTransparentFrame(ItemCatalog.this, fieldName, fieldAmount);
        setTransparentFrame(btnAddItem, btnUpdateItem, btnClearItem, btnDeleteItem,
                btnAddQueue, btnRemoveQueue, btnPrint);

        fieldItemID.setPlaceholder();

        fieldName.setForm(PLACEHOLDER_NAME, FieldType.STRING);
        fieldName.getDocument().addDocumentListener(new FieldChangeListener());

        fieldPrice.setForm(PLACEHOLDER_PRICE, FieldType.FLOAT);
        fieldPrice.getDocument().addDocumentListener(new FieldChangeListener());

        tableItem.setDefaultTable();
        tableItem.setPriceColumn(1);
        tableItem.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableItem.getSelectedRow();
                if (selectedRow >= 0) {
                    selectTableItem(selectedRow);
                }
            }
        });

        comboUOM.addItem("PIECE");
        comboUOM.addItem("SET");
        comboUOM.addItem("UNIT");

        fieldAmount.setForm("1", FieldType.INT);
        fieldAmount.getDocument().addDocumentListener(new FieldChangeListener());

        tableQueue.setDefaultTable();
        tableQueue.setIntegerColumn(0);
        tableQueue.setIntegerColumn(2);
        tableQueue.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableQueue.getSelectedRow();
                if (selectedRow >= 0) {
                    selectTableQueue(selectedRow);
                }
            }
        });

        DefaultTableModel model = (DefaultTableModel) tableQueue.getModel();
        model.setRowCount(0);
    }

    //<editor-fold defaultstate="collapsed" desc="Item Panel">
    @Override
    public void refreshItemTable() {
        TableUtil.refreshTable(tableItem, "SELECT * FROM " + Main.TB_CATALOG_ITEM + " ORDER BY updated_at DESC", TableUtil.TableEnum.CATALOG_ITEM);
//        System.out.println("SELECT * FROM " + Main.TB_CATALOG_ITEM + " ORDER BY updated_at DESC");
    }

    @Override
    public void repopulateFilterComboBox() {
    }

    @Override
    public void repopulateComboBox() {
//        comboCategory.repopulateComboBox("SELECT category_name FROM " + Main.TB_CATALOG_CATEGORY);
    }
    //</editor-fold>

    public void selectTableItem(int selectedRow) {
        String[] tableRow = TableUtil.selectTableRow(tableItem, selectedRow);
        TableUtil.linkFieldsToTable(tableRow, fieldName, fieldPrice, comboUOM);

        fieldItemID.setText(fieldName.getText());

        String query = "SELECT code FROM " + Main.TB_CATALOG_ITEM + " WHERE name = ?";
        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME); PreparedStatement pst = conn.prepareStatement(query)) {

            pst.setString(1, fieldName.getText()); // Replace itemName with your actual variable

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    current_barcode = rs.getString("code");
                    imgBarcode.setIcon(BarcodeUtil.generateBarcode(current_barcode));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        btnAddQueue.setEnabled(current_barcode != null
                && !fieldAmount.getText().trim().isEmpty()
                && Integer.parseInt(fieldAmount.getText()) > 0);
        setUpdateDeleteEnableItem();
    }

    public void selectTableQueue(int selectedRow) {
        String[] tableRow = TableUtil.selectTableRow(tableQueue, selectedRow);
        TableUtil.linkFieldsToTable(tableRow, null, null, fieldAmount);

        current_barcode = tableRow[0];
        imgBarcode.setIcon(BarcodeUtil.generateBarcode(current_barcode));
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
            btnAddItem.setEnabled(fieldName.isValidText() && fieldPrice.isValidText());
            btnAddQueue.setEnabled(current_barcode != null
                    && !fieldAmount.getText().trim().isEmpty()
                    && Integer.parseInt(fieldAmount.getText()) > 0);
            btnRemoveQueue.setEnabled(current_barcode != null);
        }
    }

    public void setUpdateDeleteEnableItem() {
        resetBtnEnability(fieldItemID, btnUpdateItem, btnDeleteItem);
    }

    public void clearItemFields() {
        fieldItemID.resetToPlaceholder();
        fieldName.resetToPlaceholder();
        fieldPrice.resetToPlaceholder();
        comboUOM.setSelectedIndex(0);
        tableItem.clearSelectedRow();
        setUpdateDeleteEnableItem();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fieldItemID = new ProjectINSY.java.swing.Form.FormField();
        panelBody = new javax.swing.JPanel();
        panelItem = new javax.swing.JPanel();
        labelCatalogItem = new javax.swing.JLabel();
        scrollItem = new javax.swing.JScrollPane();
        tableItem = new ProjectINSY.java.swing.Table();
        panelItemFields = new javax.swing.JPanel();
        labelName = new javax.swing.JLabel();
        fieldName = new ProjectINSY.java.swing.Form.FormField();
        imageName = new javax.swing.JLabel();
        fieldPrice = new ProjectINSY.java.swing.Form.FormField();
        imagePrice = new javax.swing.JLabel();
        labelPrice = new javax.swing.JLabel();
        labelUOM = new javax.swing.JLabel();
        comboUOM = new ProjectINSY.java.swing.ComboBoxSuggestion();
        imageUOM = new javax.swing.JLabel();
        separatorItem = new javax.swing.JSeparator();
        labelAddItem = new javax.swing.JLabel();
        btnAddItem = new javax.swing.JButton();
        labelUpdateItem = new javax.swing.JLabel();
        btnUpdateItem = new javax.swing.JButton();
        labelClearItem = new javax.swing.JLabel();
        btnClearItem = new javax.swing.JButton();
        labelDeleteItem = new javax.swing.JLabel();
        btnDeleteItem = new javax.swing.JButton();
        panelPrint = new javax.swing.JPanel();
        panelBarcode = new javax.swing.JPanel();
        imgBarcode = new javax.swing.JLabel();
        fieldAmount = new ProjectINSY.java.swing.Form.FormField();
        imageAmount = new javax.swing.JLabel();
        labelAmount = new javax.swing.JLabel();
        labelRemoveQueue = new javax.swing.JLabel();
        btnRemoveQueue = new javax.swing.JButton();
        labelAddQueue = new javax.swing.JLabel();
        btnAddQueue = new javax.swing.JButton();
        labelBarcode = new javax.swing.JLabel();
        scrollQueue = new javax.swing.JScrollPane();
        tableQueue = new ProjectINSY.java.swing.Table();
        labelPrint = new javax.swing.JLabel();
        btnPrint = new javax.swing.JButton();

        fieldItemID.setBorder(null);
        fieldItemID.setForeground(new java.awt.Color(0, 0, 0));
        fieldItemID.setText("formField");
        fieldItemID.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        fieldItemID.setPlaceholderColor(new java.awt.Color(153, 153, 153));
        fieldItemID.setSelectionColor(new java.awt.Color(25, 102, 24));

        setMaximumSize(new java.awt.Dimension(1840, 900));
        setMinimumSize(new java.awt.Dimension(1840, 900));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(1840, 900));

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 102, 24), 2));

        panelItem.setBackground(new java.awt.Color(255, 255, 255));
        panelItem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 102, 24), 2));

        labelCatalogItem.setBackground(new java.awt.Color(25, 102, 24));
        labelCatalogItem.setFont(new java.awt.Font("Bebas", 0, 64)); // NOI18N
        labelCatalogItem.setForeground(new java.awt.Color(255, 255, 255));
        labelCatalogItem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCatalogItem.setText("ITEMS CATALOG");
        labelCatalogItem.setOpaque(true);

        scrollItem.setBorder(null);

        tableItem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Item", "Price", "Unit of Measure", "Last Updated"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Float.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableItem.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        tableItem.setGridColor(new java.awt.Color(255, 255, 255));
        tableItem.setSelectionBackground(new java.awt.Color(25, 102, 24));
        scrollItem.setViewportView(tableItem);

        panelItemFields.setBackground(new java.awt.Color(255, 255, 255));
        panelItemFields.setLayout(null);

        labelName.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelName.setText("Name");
        panelItemFields.add(labelName);
        labelName.setBounds(10, 0, 173, 30);

        fieldName.setBorder(null);
        fieldName.setForeground(new java.awt.Color(0, 0, 0));
        fieldName.setText("formField");
        fieldName.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        fieldName.setPlaceholderColor(new java.awt.Color(153, 153, 153));
        fieldName.setSelectionColor(new java.awt.Color(25, 102, 24));
        panelItemFields.add(fieldName);
        fieldName.setBounds(20, 40, 520, 40);

        imageName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldLogIn.png"))); // NOI18N
        panelItemFields.add(imageName);
        imageName.setBounds(10, 30, 540, 60);

        fieldPrice.setBorder(null);
        fieldPrice.setForeground(new java.awt.Color(0, 0, 0));
        fieldPrice.setText("formField");
        fieldPrice.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        fieldPrice.setPlaceholderColor(new java.awt.Color(153, 153, 153));
        fieldPrice.setSelectionColor(new java.awt.Color(25, 102, 24));
        panelItemFields.add(fieldPrice);
        fieldPrice.setBounds(650, 40, 520, 40);

        imagePrice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldLogIn.png"))); // NOI18N
        panelItemFields.add(imagePrice);
        imagePrice.setBounds(640, 30, 540, 60);

        labelPrice.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelPrice.setText("Price");
        panelItemFields.add(labelPrice);
        labelPrice.setBounds(640, 0, 173, 30);

        labelUOM.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelUOM.setText("Unit of Measure");
        panelItemFields.add(labelUOM);
        labelUOM.setBounds(10, 110, 173, 30);

        comboUOM.setBorder(null);
        comboUOM.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        panelItemFields.add(comboUOM);
        comboUOM.setBounds(20, 150, 520, 40);

        imageUOM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldLogIn.png"))); // NOI18N
        panelItemFields.add(imageUOM);
        imageUOM.setBounds(10, 140, 540, 60);

        separatorItem.setOrientation(javax.swing.SwingConstants.VERTICAL);
        panelItemFields.add(separatorItem);
        separatorItem.setBounds(850, 130, 10, 60);

        labelAddItem.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelAddItem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelAddItem.setText("Add");
        labelAddItem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelItemFields.add(labelAddItem);
        labelAddItem.setBounds(740, 150, 80, 23);

        btnAddItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn.png"))); // NOI18N
        btnAddItem.setBorder(null);
        btnAddItem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddItem.setEnabled(false);
        btnAddItem.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_pressed.png"))); // NOI18N
        btnAddItem.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_pressed.png"))); // NOI18N
        btnAddItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddItemActionPerformed(evt);
            }
        });
        panelItemFields.add(btnAddItem);
        btnAddItem.setBounds(730, 140, 100, 40);

        labelUpdateItem.setFont(new java.awt.Font("Bahnschrift", 1, 14)); // NOI18N
        labelUpdateItem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelUpdateItem.setText("Update");
        labelUpdateItem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        labelUpdateItem.setFocusable(false);
        panelItemFields.add(labelUpdateItem);
        labelUpdateItem.setBounds(860, 170, 60, 20);

        btnUpdateItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnSave.png"))); // NOI18N
        btnUpdateItem.setBorder(null);
        btnUpdateItem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdateItem.setEnabled(false);
        btnUpdateItem.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnSave.png"))); // NOI18N
        btnUpdateItem.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnSave.png"))); // NOI18N
        btnUpdateItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateItemActionPerformed(evt);
            }
        });
        panelItemFields.add(btnUpdateItem);
        btnUpdateItem.setBounds(870, 130, 40, 40);

        labelClearItem.setFont(new java.awt.Font("Bahnschrift", 1, 14)); // NOI18N
        labelClearItem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelClearItem.setText("Clear");
        labelClearItem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelItemFields.add(labelClearItem);
        labelClearItem.setBounds(930, 170, 60, 20);

        btnClearItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnBack.png"))); // NOI18N
        btnClearItem.setBorder(null);
        btnClearItem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClearItem.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnBack.png"))); // NOI18N
        btnClearItem.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnBack.png"))); // NOI18N
        btnClearItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearItemActionPerformed(evt);
            }
        });
        panelItemFields.add(btnClearItem);
        btnClearItem.setBounds(940, 130, 40, 40);

        labelDeleteItem.setFont(new java.awt.Font("Bahnschrift", 1, 14)); // NOI18N
        labelDeleteItem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelDeleteItem.setText("Delete");
        labelDeleteItem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        labelDeleteItem.setFocusable(false);
        panelItemFields.add(labelDeleteItem);
        labelDeleteItem.setBounds(1000, 170, 60, 20);

        btnDeleteItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnDelete.png"))); // NOI18N
        btnDeleteItem.setBorder(null);
        btnDeleteItem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDeleteItem.setEnabled(false);
        btnDeleteItem.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnDelete.png"))); // NOI18N
        btnDeleteItem.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnDelete.png"))); // NOI18N
        btnDeleteItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteItemActionPerformed(evt);
            }
        });
        panelItemFields.add(btnDeleteItem);
        btnDeleteItem.setBounds(1010, 130, 40, 40);

        javax.swing.GroupLayout panelItemLayout = new javax.swing.GroupLayout(panelItem);
        panelItem.setLayout(panelItemLayout);
        panelItemLayout.setHorizontalGroup(
            panelItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelCatalogItem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelItemLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelItemFields, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(scrollItem, javax.swing.GroupLayout.DEFAULT_SIZE, 1192, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelItemLayout.setVerticalGroup(
            panelItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelItemLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelCatalogItem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelItemFields, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollItem, javax.swing.GroupLayout.PREFERRED_SIZE, 562, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelPrint.setBackground(new java.awt.Color(255, 255, 255));
        panelPrint.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 102, 24), 2));

        panelBarcode.setBackground(new java.awt.Color(255, 255, 255));
        panelBarcode.setLayout(null);

        imgBarcode.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        panelBarcode.add(imgBarcode);
        imgBarcode.setBounds(340, 40, 200, 80);

        fieldAmount.setBorder(null);
        fieldAmount.setForeground(new java.awt.Color(0, 0, 0));
        fieldAmount.setText("formField");
        fieldAmount.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        fieldAmount.setPlaceholderColor(new java.awt.Color(153, 153, 153));
        fieldAmount.setSelectionColor(new java.awt.Color(25, 102, 24));
        panelBarcode.add(fieldAmount);
        fieldAmount.setBounds(30, 40, 220, 40);

        imageAmount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldHalf_small.png"))); // NOI18N
        panelBarcode.add(imageAmount);
        imageAmount.setBounds(20, 30, 250, 60);

        labelAmount.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelAmount.setText("Amount to Print");
        panelBarcode.add(labelAmount);
        labelAmount.setBounds(20, 0, 170, 30);

        labelRemoveQueue.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelRemoveQueue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelRemoveQueue.setText("Remove");
        labelRemoveQueue.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelBarcode.add(labelRemoveQueue);
        labelRemoveQueue.setBounds(170, 110, 80, 23);

        btnRemoveQueue.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_red.png"))); // NOI18N
        btnRemoveQueue.setBorder(null);
        btnRemoveQueue.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRemoveQueue.setEnabled(false);
        btnRemoveQueue.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_pressed.png"))); // NOI18N
        btnRemoveQueue.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_pressed.png"))); // NOI18N
        btnRemoveQueue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveQueueActionPerformed(evt);
            }
        });
        panelBarcode.add(btnRemoveQueue);
        btnRemoveQueue.setBounds(160, 100, 100, 40);

        labelAddQueue.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelAddQueue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelAddQueue.setText("Add");
        labelAddQueue.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelBarcode.add(labelAddQueue);
        labelAddQueue.setBounds(40, 110, 80, 23);

        btnAddQueue.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn.png"))); // NOI18N
        btnAddQueue.setBorder(null);
        btnAddQueue.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddQueue.setEnabled(false);
        btnAddQueue.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_pressed.png"))); // NOI18N
        btnAddQueue.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_pressed.png"))); // NOI18N
        btnAddQueue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddQueueActionPerformed(evt);
            }
        });
        panelBarcode.add(btnAddQueue);
        btnAddQueue.setBounds(30, 100, 100, 40);

        labelBarcode.setBackground(new java.awt.Color(25, 102, 24));
        labelBarcode.setFont(new java.awt.Font("Bebas", 0, 64)); // NOI18N
        labelBarcode.setForeground(new java.awt.Color(255, 255, 255));
        labelBarcode.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelBarcode.setText("Print queue");
        labelBarcode.setOpaque(true);

        scrollQueue.setBorder(null);

        tableQueue.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "", "Item", "Amount to Print"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableQueue.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        tableQueue.setGridColor(new java.awt.Color(255, 255, 255));
        tableQueue.setSelectionBackground(new java.awt.Color(25, 102, 24));
        scrollQueue.setViewportView(tableQueue);

        labelPrint.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelPrint.setForeground(new java.awt.Color(255, 255, 255));
        labelPrint.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/iconPrint.png"))); // NOI18N
        labelPrint.setText("Print");
        labelPrint.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnPrint.png"))); // NOI18N
        btnPrint.setBorder(null);
        btnPrint.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPrint.setEnabled(false);
        btnPrint.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnPrint_pressed.png"))); // NOI18N
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelPrintLayout = new javax.swing.GroupLayout(panelPrint);
        panelPrint.setLayout(panelPrintLayout);
        panelPrintLayout.setHorizontalGroup(
            panelPrintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelBarcode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelPrintLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelPrintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelBarcode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelPrintLayout.createSequentialGroup()
                        .addComponent(scrollQueue, javax.swing.GroupLayout.PREFERRED_SIZE, 599, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelPrintLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelPrintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrint))
                .addContainerGap())
        );
        panelPrintLayout.setVerticalGroup(
            panelPrintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelPrintLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelBarcode)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollQueue, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelPrintLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                .addContainerGap(1221, Short.MAX_VALUE)
                .addComponent(panelPrint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelBodyLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(panelItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(622, Short.MAX_VALUE)))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelPrint, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelBodyLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(panelItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddItemActionPerformed
        String name = fieldName.getText();
        String uom = comboUOM.getSelectedItem().toString();
        Float price = Float.valueOf(fieldPrice.getText());

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            if (!DatabaseUtil.recordExists(conn, Main.TB_CATALOG_ITEM, "name", name)) {
                String query = "INSERT INTO " + Main.TB_CATALOG_ITEM + " (code, name, uom, price)\n"
                        + "VALUES (?, ?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setString(1, generateCode());
                pst.setString(2, name);
                pst.setString(3, uom);
                pst.setFloat(4, price);

                // HISTORY : CATALOG-ADD
//                String history_desc = "";
//
//                history_desc += createHistoryDesc(item_name, "Item Name");
//                history_desc += createHistoryDesc(item_category, "Category");
//                history_desc += createHistoryDesc(item_uom, "UOM");
//
                pst.executeUpdate();
//
//                String item_idStr = createObjectCode(pst.getGeneratedKeys(), "Catalog-I-", Main.TB_CATALOG_ITEM, "item_code", "item_id");
//
//                insertHistory(DatabaseUtil.HistoryFrame.CATALOG, DatabaseUtil.HistoryType.ADD, item_idStr, item_idStr, history_desc, "");

                JOptionPane.showMessageDialog(this, "Item Added!", "Success", JOptionPane.INFORMATION_MESSAGE);

                clearItemFields();
                refreshItemTable();
            } else {
                JOptionPane.showMessageDialog(this, "This item already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            MessageUtil.paneDatabaseError(e);
        }
    }//GEN-LAST:event_btnAddItemActionPerformed

    private void btnUpdateItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateItemActionPerformed
        String name_original = fieldItemID.getText();
        String name = fieldName.getText();
        String uom = comboUOM.getSelectedItem().toString();
        Float price = Float.valueOf(fieldPrice.getText());

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            int warnUser = JOptionPane.showConfirmDialog(
                    null,
                    "Updating this Item's name will also update the corresponding item name in other related tables. Do you want to proceed?",
                    "Warning: Item Update",
                    JOptionPane.YES_NO_OPTION
            );

            if (warnUser == JOptionPane.YES_OPTION) {
                String query = "UPDATE " + Main.TB_CATALOG_ITEM + " SET name = ?, uom = ?, price = ?, updated_at = ? WHERE name = ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, name);
                pst.setString(2, uom);
                pst.setFloat(3, price);
                pst.setString(4, getTimestampNow());
                pst.setString(5, name_original);

                // HISTORY : CATALOG-UPDATE
//                String history_desc = "";
//
//                String old_category = getColumnValueByString(Main.TB_CATALOG_ITEM, "item_category", "item_name", item_name_original);
//                String old_uom = getColumnValueByString(Main.TB_CATALOG_ITEM, "item_uom", "item_name", item_name_original);
//
//                history_desc += createHistoryDesc(item_name_original, item_name, "Item Name");
//                history_desc += createHistoryDesc(old_category, item_category, "Category");
//                history_desc += createHistoryDesc(old_uom, item_uom, "UOM");
//
//                if (!old_category.equals(item_category)) {
//                    int category_id = Integer.parseInt(getColumnValueByString(Main.TB_CATALOG_CATEGORY, "category_id", "category_name", item_category));
//                    String item_idStr = getColumnValueByString(Main.TB_CATALOG_ITEM, "item_code", "item_name", item_name_original);
//
//                    if (item_idStr.contains("-")) {
//                        String[] parts = item_idStr.split("-");
//                        item_idStr = parts[0] + "-" + parts[1] + "-" + category_id + "-" + parts[3];
//                    }
//
//                    setColumnValueByString(Main.TB_CATALOG_ITEM, "item_code", "item_name", item_idStr, item_name_original);
//                }
//
//                String item_idStr = getColumnValueByString(Main.TB_CATALOG_ITEM, "item_code", "item_name", item_name_original);
//                insertHistory(DatabaseUtil.HistoryFrame.CATALOG, DatabaseUtil.HistoryType.UPDATE, item_idStr, item_idStr, history_desc, "");
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Item Updated!", "Success", JOptionPane.INFORMATION_MESSAGE);

                clearItemFields();
                refreshItemTable();
            }
        } catch (SQLException e) {
            paneDatabaseError(e);
        }
    }//GEN-LAST:event_btnUpdateItemActionPerformed

    private void btnDeleteItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteItemActionPerformed
        String item_name = fieldItemID.getText();

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            int warnUser = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure you want to delete '" + fieldName.getText() + "'?",
                    "Warning: Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (warnUser == JOptionPane.YES_OPTION) {
                String query = "DELETE FROM " + Main.TB_CATALOG_ITEM + " WHERE name = ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, item_name);

                // HISTORY : CATALOG-DELETE
//                String history_desc = createHistoryDesc(item_name, "Item Name");
//
//                String item_idStr = getColumnValueByString(Main.TB_CATALOG_ITEM, "item_code", "item_name", item_name);
//
                pst.executeUpdate();
//
//                insertHistory(DatabaseUtil.HistoryFrame.CATALOG, DatabaseUtil.HistoryType.DELETE, item_idStr, item_idStr, history_desc, "");
                JOptionPane.showMessageDialog(this, "Item Deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);

                clearItemFields();
                refreshItemTable();
            }
        } catch (SQLException e) {
            paneDatabaseError(e);
        }
    }//GEN-LAST:event_btnDeleteItemActionPerformed

    private void btnClearItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearItemActionPerformed
        clearItemFields();
    }//GEN-LAST:event_btnClearItemActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        if (current_barcode != null) {
            DefaultTableModel model = (DefaultTableModel) tableQueue.getModel();
            List<BufferedImage> barcodeImages = new ArrayList<>();

            String file_name = "barcode";
            for (int row = 0; row < model.getRowCount(); row++) {
                String code = model.getValueAt(row, 0).toString();
                int amount = Integer.parseInt(model.getValueAt(row, 2).toString());

                for (int i = 0; i < amount; i++) {
                    ImageIcon barcodeIcon = BarcodeUtil.generateBarcode(code);

                    BufferedImage bufferedImage = (BufferedImage) barcodeIcon.getImage();
                    barcodeImages.add(bufferedImage);
                }
            }
            try {
                BarcodeUtil.generateFileFromBarcodes(barcodeImages, BarcodeUtil.FileType.PDF, file_name);
                model.setRowCount(0);
            } catch (IOException ex) {
                Logger.getLogger(ItemManagement.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Cannot save PDF file. Please close any open PDF viewers and try again.", "File Access Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No Barcode Selected", "Print Failed", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnRemoveQueueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveQueueActionPerformed
        DefaultTableModel model = (DefaultTableModel) tableQueue.getModel();
        int codeCol = 0; // column index where barcode/code is stored

        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            Object value = model.getValueAt(i, codeCol);
            if (value != null && value.equals(current_barcode)) {
                model.removeRow(i);
                break; // remove only the first matching row
            }
        }

        fieldAmount.resetToPlaceholder();
        tableQueue.clearSelectedRow();

        btnPrint.setEnabled(model.getRowCount() > 0);
    }//GEN-LAST:event_btnRemoveQueueActionPerformed

    private void btnAddQueueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddQueueActionPerformed
        DefaultTableModel model = (DefaultTableModel) tableQueue.getModel();

        int codeCol = 0;
        int amountCol = 2;
        int pendingRow = 0;
        boolean alreadyPending = false;

        for (int i = 0; i < model.getRowCount(); i++) {
            Object value = model.getValueAt(i, codeCol);
            if (value.equals(current_barcode)) {
                alreadyPending = true;
                pendingRow = i;
            }
        }

        if (alreadyPending) {
            int qty = Integer.parseInt(model.getValueAt(pendingRow, amountCol).toString());
            model.setValueAt(qty + Integer.parseInt(fieldAmount.getText()), pendingRow, amountCol);
        } else {
            String name = getColumnValueByString(Main.TB_CATALOG_ITEM, "name", "code", current_barcode);
            model.addRow(new Object[]{
                current_barcode, name, fieldAmount.getText()
            });
        }

//        GuiUtil.resetIcon(imgBarcode);
//        current_barcode = null;
        fieldAmount.resetToPlaceholder();
        tableQueue.clearSelectedRow();

        btnPrint.setEnabled(model.getRowCount() > 0);
    }//GEN-LAST:event_btnAddQueueActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddItem;
    private javax.swing.JButton btnAddQueue;
    private javax.swing.JButton btnClearItem;
    private javax.swing.JButton btnDeleteItem;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnRemoveQueue;
    private javax.swing.JButton btnUpdateItem;
    private ProjectINSY.java.swing.ComboBoxSuggestion comboUOM;
    private ProjectINSY.java.swing.Form.FormField fieldAmount;
    private ProjectINSY.java.swing.Form.FormField fieldItemID;
    private ProjectINSY.java.swing.Form.FormField fieldName;
    private ProjectINSY.java.swing.Form.FormField fieldPrice;
    private javax.swing.JLabel imageAmount;
    private javax.swing.JLabel imageName;
    private javax.swing.JLabel imagePrice;
    private javax.swing.JLabel imageUOM;
    private javax.swing.JLabel imgBarcode;
    private javax.swing.JLabel labelAddItem;
    private javax.swing.JLabel labelAddQueue;
    private javax.swing.JLabel labelAmount;
    private javax.swing.JLabel labelBarcode;
    private javax.swing.JLabel labelCatalogItem;
    private javax.swing.JLabel labelClearItem;
    private javax.swing.JLabel labelDeleteItem;
    private javax.swing.JLabel labelName;
    private javax.swing.JLabel labelPrice;
    private javax.swing.JLabel labelPrint;
    private javax.swing.JLabel labelRemoveQueue;
    private javax.swing.JLabel labelUOM;
    private javax.swing.JLabel labelUpdateItem;
    private javax.swing.JPanel panelBarcode;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelItem;
    private javax.swing.JPanel panelItemFields;
    private javax.swing.JPanel panelPrint;
    private javax.swing.JScrollPane scrollItem;
    private javax.swing.JScrollPane scrollQueue;
    private javax.swing.JSeparator separatorItem;
    private ProjectINSY.java.swing.Table tableItem;
    private ProjectINSY.java.swing.Table tableQueue;
    // End of variables declaration//GEN-END:variables
}
