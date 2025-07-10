/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ProjectINSY.java.ui;

import ProjectINSY.java.Main;
import ProjectINSY.java.model.ItemPanel;
import ProjectINSY.java.swing.Form.FormField.FieldType;
import ProjectINSY.java.util.BarcodeUtil;
import static ProjectINSY.java.util.BarcodeUtil.validateBarcode;
import ProjectINSY.java.util.DatabaseUtil;
import static ProjectINSY.java.util.DatabaseUtil.getColumnValueByInt;
import static ProjectINSY.java.util.DatabaseUtil.getColumnValueByString;
import static ProjectINSY.java.util.DatabaseUtil.getConnection;
import static ProjectINSY.java.util.DatabaseUtil.prepareQueryWithParameters;
import java.sql.Timestamp;
import ProjectINSY.java.util.GuiUtil.FieldFocus;
import static ProjectINSY.java.util.GuiUtil.cleanSpaces;
import static ProjectINSY.java.util.GuiUtil.setDefaultField;
import static ProjectINSY.java.util.GuiUtil.setTransparentFrame;
import ProjectINSY.java.util.MessageUtil;
import static ProjectINSY.java.util.MessageUtil.paneDatabaseError;
import ProjectINSY.java.util.TableUtil;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import static ProjectINSY.java.util.GuiUtil.setScrollBarCustom;
import static ProjectINSY.java.util.MessageUtil.paneDatabaseError;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author admin
 */
public class ItemManagement extends ItemPanel {

    private String current_barcode = null;
    private ImageIcon barcodeIcon;
    private int batchQuantity = -1;

    private boolean isUpdating = false;

    /**
     * Creates new form LogIn
     */
    public ItemManagement() {
        initComponents();

        setScrollBarCustom(scrollStock);
        setScrollBarCustom(scrollAudit);

        setTransparentFrame(ItemManagement.this, fieldLocation, fieldQuantity, fieldLocation);
        setTransparentFrame(btnAdd, btnClear, btnStock, btnAudit);

//        tableStock.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        comboType.addItem("RECEIPT (IN)");
        comboType.addItem("SALE (OUT)");
        comboType.addItem("RETURN (IN)");
        comboType.addItem("LOST (OUT)");
        comboType.addItem("TRANSFER (IN)");
        comboType.addItem("TRANSFER (OUT)");
        comboType.addItem("ADJUSTMENT (IN)");
        comboType.addItem("ADJUSTMENT (OUT)");
        fieldCode.setForm("0000000000000", FieldType.INT);
        fieldQuantity.setForm(PLACEHOLDER_QTY, FieldType.INT);
//        fieldLocation.setForm(PLACEHOLDER_LOCATION, FieldType.STRING);

        fieldID.setForm(null, FieldType.INT);

        fieldCode.getDocument().addDocumentListener(new FieldChangeListener());
        fieldQuantity.getDocument().addDocumentListener(new FieldChangeListener());
//        fieldLocation.getDocument().addDocumentListener(new FieldChangeListener());

        tableStock.setDefaultTable();
        tableStock.setIntegerColumn(1);

        tableAudit.setDefaultTable();
        tableAudit.setIntegerColumn(3);
        tableAudit.setColumnWidth(3, 1);
        switchTable(0);
    }

    //<editor-fold defaultstate="collapsed" desc="Item Panel">
    @Override
    public void refreshItemTable() {
        filterWHERE = " ";
//        if (!searchCategory.isDefaultComboItem()) {
//            filterWHERE += "AND stock_category = '" + getComboSelected(searchCategory) + "' ";
//        }
//        if (!searchName.isDefaultComboItem()) {
//            filterWHERE += "AND stock_name = '" + getComboSelected(searchName) + "' ";
//        }
//        if (!searchDesc.isDefaultComboItem()) {
//            filterWHERE += "AND stock_desc = '" + getComboSelected(searchDesc) + "' ";
//        }
//        if (!searchBenefactor.isDefaultComboItem()) {
//            filterWHERE += "AND stock_benefactor = '" + getComboSelected(searchBenefactor) + "' ";
//        }
//        if (fieldHasValue(searchDateStart)) {
//            filterWHERE += "AND stock_dod >= '" + getFieldString(searchDateStart) + "' ";
//        }
//        if (fieldHasValue(searchDateEnd)) {
//            filterWHERE += "AND stock_dod <= '" + getFieldString(searchDateEnd) + "' ";
//        }
//
//        filterHAVING = " ";
//        if (fieldHasValue(searchPriceStart)) {
//            filterHAVING += "AND stock_price >= '" + getFieldString(searchPriceStart) + "' ";
//        }
//        if (fieldHasValue(searchPriceEnd)) {
//            filterHAVING += "AND stock_price <= '" + getFieldString(searchPriceEnd) + "' ";
//        }
//        if (fieldHasValue(searchQuantityStart)) {
//            filterHAVING += "AND stock_quantity >= '" + getFieldString(searchQuantityStart) + "' ";
//        }
//        if (fieldHasValue(searchQuantityEnd)) {
//            filterHAVING += "AND stock_quantity <= '" + getFieldString(searchQuantityEnd) + "' ";
//        }

        currentSearchQuery = "SELECT c.item_id, c.name, s.* FROM "
                + Main.TB_CATALOG_ITEM
                + " c JOIN "
                + Main.TB_INVENTORY_BALANCE
                + " s ON c.item_id = s.item_id "
                + " WHERE 1 "
                + filterWHERE
                + " ORDER BY s.updated_at DESC";

        currentSearchQuery = cleanSpaces(currentSearchQuery);
        TableUtil.refreshTable(tableStock, currentSearchQuery, TableUtil.TableEnum.INVENTORY_BALANCE);

        DefaultTableModel model = (DefaultTableModel) tableAudit.getModel();
        model.setRowCount(0);

        TableUtil.refreshTable(tableAudit, "SELECT c.name, t.type, t.quantity_change, t.timestamp, u.user_email FROM "
                + Main.TB_INVENTORY_TRANSACTION
                + " t JOIN "
                + Main.TB_CATALOG_ITEM
                + " c on t.item_id = c.item_id JOIN "
                + Main.TB_INVENTORY_BALANCE
                + " s ON t.item_id = s.item_id AND t.location = s.location JOIN "
                + Main.TB_USER
                + " u ON u.user_id = t.created_by"
                + " ORDER BY t.timestamp DESC", TableUtil.TableEnum.INVENTORY_TRANSACTION);

//        fieldLocation.repopulateSuggestions("location", "SELECT DISTINCT location FROM " + Main.TB_INVENTORY_BALANCE);
    }

    @Override
    public void repopulateFilterComboBox() {
        disableUpdatingComboBoxes();
//        searchCategory.repopulateComboBox("SELECT stock_category FROM " + Main.TB_ITEM_STOCK);
//        searchName.repopulateComboBox("SELECT stock_name FROM " + Main.TB_ITEM_STOCK);
//        searchDesc.repopulateComboBox("SELECT stock_desc FROM " + Main.TB_ITEM_STOCK);
//        searchBenefactor.repopulateComboBox("SELECT stock_benefactor FROM " + Main.TB_ITEM_STOCK);
        enableUpdatingComboBoxes();

        refreshItemTable();
    }

    @Override
    public void repopulateComboBox() {
        comboName.repopulateComboBox("SELECT name FROM " + Main.TB_CATALOG_ITEM);
    }
    //</editor-fold>

    public void switchTable(int tab) {
        tabInventory.setSelectedIndex(tab);
        if (tab == 0) {
            btnStock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnRequest_active.png")));
            btnAudit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnRequest.png")));
        } else {
            btnStock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnRequest.png")));
            btnAudit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnRequest_active.png")));
        }
    }

    public void selectTableStock(int selectedRow) {
        String[] tableRow = TableUtil.selectTableRow(tableStock, selectedRow);
        TableUtil.linkFieldsToTable(tableRow, fieldID, comboName, fieldLocation, comboType, fieldQuantity);

        fieldQuantity.resetToPlaceholder();

        current_barcode = validateBarcode(fieldID.getText());
        barcodeIcon = BarcodeUtil.generateBarcode(current_barcode);
        if (fieldLocation.getText().isEmpty()) {
            setDefaultField(fieldLocation, PLACEHOLDER_DESC, FieldFocus.LOST, Color.BLACK);
        }
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
                        searchItem();

                        isUpdating = false;
                    });
                }
            } catch (SQLException e) {
                paneDatabaseError(e);
            }

            btnAdd.setEnabled(!comboName.isDefaultComboItem() && !fieldQuantity.getText().trim().isEmpty() && fieldLocation.isValidText());

            refreshItemTable();
        }
    }

    public void clearFields() {
        current_barcode = null;
        barcodeIcon = null;
        batchQuantity = -1;

        fieldID.resetToPlaceholder();
        comboName.clearComboBox();

//        comboType.setSelectedIndex(0);
//        fieldQuantity.resetToPlaceholder();
//        fieldLocation.resetToPlaceholder();

        tableStock.clearSelectedRow();
    }

    public void searchItem() {
        String codeText = fieldCode.getText();

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            if (codeText.isEmpty() || !DatabaseUtil.recordExists(conn, Main.TB_CATALOG_ITEM, "code", codeText)) {
                return;
            }
        } catch (SQLException e) {
            paneDatabaseError(e);
        }

        if (!codeText.isEmpty()) {
            comboName.setSelectedItem(getColumnValueByString(Main.TB_CATALOG_ITEM, "name", "code", codeText));
            fieldCode.resetToPlaceholder();
            fieldCode.setText("");
            fieldCode.requestFocusInWindow();
            fieldCode.setForeground(Color.black);
        }
    }

    public void focusFieldCode() {
        fieldCode.requestFocusInWindow();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnExport = new javax.swing.JButton();
        fieldID = new ProjectINSY.java.swing.Form.FormField();
        dateDOD = new ProjectINSY.java.swing.Date.DateChooser();
        imageLocation = new javax.swing.JLabel();
        fieldLocation = new ProjectINSY.java.swing.Form.FormFieldSuggestion();
        labelLocation = new javax.swing.JLabel();
        panelMain = new javax.swing.JPanel();
        panelFields = new javax.swing.JPanel();
        panelInformation = new javax.swing.JPanel();
        labelCode = new javax.swing.JLabel();
        fieldCode = new ProjectINSY.java.swing.Form.FormField();
        imageCode = new javax.swing.JLabel();
        comboName = new ProjectINSY.java.swing.ComboBoxSuggestion();
        imageName = new javax.swing.JLabel();
        labelName = new javax.swing.JLabel();
        labelType = new javax.swing.JLabel();
        comboType = new ProjectINSY.java.swing.ComboBoxSuggestion();
        imageType = new javax.swing.JLabel();
        fieldQuantity = new ProjectINSY.java.swing.Form.FormField();
        labelQuantity = new javax.swing.JLabel();
        imageQuantity = new javax.swing.JLabel();
        panelCRUD = new javax.swing.JPanel();
        labelAdd = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        labelClear = new javax.swing.JLabel();
        btnClear = new javax.swing.JButton();
        panelTable = new javax.swing.JPanel();
        tabButtons = new javax.swing.JPanel();
        labelStock = new javax.swing.JLabel();
        btnStock = new javax.swing.JButton();
        labelAudit = new javax.swing.JLabel();
        btnAudit = new javax.swing.JButton();
        tabInventory = new javax.swing.JTabbedPane();
        panelStock = new javax.swing.JPanel();
        scrollStock = new javax.swing.JScrollPane();
        tableStock = new ProjectINSY.java.swing.Table();
        panelAudit = new javax.swing.JPanel();
        scrollAudit = new javax.swing.JScrollPane();
        tableAudit = new ProjectINSY.java.swing.Table();

        btnExport.setText("Print");
        btnExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportActionPerformed(evt);
            }
        });

        fieldID.setBorder(null);
        fieldID.setForeground(new java.awt.Color(0, 0, 0));
        fieldID.setText("formField");
        fieldID.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldID.setPlaceholderColor(new java.awt.Color(153, 153, 153));
        fieldID.setSelectionColor(new java.awt.Color(25, 102, 24));

        dateDOD.setForeground(new java.awt.Color(25, 102, 24));
        dateDOD.setDateFormat("yyyy-MM-dd");

        imageLocation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldHalf.png"))); // NOI18N

        fieldLocation.setBorder(null);
        fieldLocation.setForeground(new java.awt.Color(153, 153, 153));
        fieldLocation.setText("Enter Location");
        fieldLocation.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N

        labelLocation.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelLocation.setText("Location");

        setMaximumSize(new java.awt.Dimension(1840, 900));
        setMinimumSize(new java.awt.Dimension(1840, 900));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(1840, 900));

        panelMain.setBackground(new java.awt.Color(255, 255, 255));
        panelMain.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 102, 24), 2));

        panelFields.setBackground(new java.awt.Color(255, 255, 255));
        panelFields.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 102, 24), 2));
        panelFields.setLayout(null);

        panelInformation.setBackground(new java.awt.Color(255, 255, 255));
        panelInformation.setLayout(null);

        labelCode.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 64)); // NOI18N
        labelCode.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCode.setText("Scan Code:");
        panelInformation.add(labelCode);
        labelCode.setBounds(10, 0, 670, 90);

        fieldCode.setBorder(null);
        fieldCode.setForeground(new java.awt.Color(0, 0, 0));
        fieldCode.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldCode.setText("formField");
        fieldCode.setFont(new java.awt.Font("Bahnschrift", 1, 64)); // NOI18N
        fieldCode.setPlaceholderColor(new java.awt.Color(153, 153, 153));
        fieldCode.setSelectionColor(new java.awt.Color(25, 102, 24));
        panelInformation.add(fieldCode);
        fieldCode.setBounds(20, 120, 640, 80);

        imageCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldTextArea.png"))); // NOI18N
        panelInformation.add(imageCode);
        imageCode.setBounds(10, 90, 665, 130);

        comboName.setBorder(null);
        comboName.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        panelInformation.add(comboName);
        comboName.setBounds(20, 310, 640, 50);

        imageName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldFull.png"))); // NOI18N
        panelInformation.add(imageName);
        imageName.setBounds(10, 300, 665, 70);

        labelName.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelName.setText("Name");
        panelInformation.add(labelName);
        labelName.setBounds(10, 240, 100, 60);

        labelType.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelType.setText("Type");
        panelInformation.add(labelType);
        labelType.setBounds(10, 430, 180, 40);

        comboType.setBorder(null);
        comboType.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        panelInformation.add(comboType);
        comboType.setBounds(20, 490, 310, 50);

        imageType.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldHalf.png"))); // NOI18N
        panelInformation.add(imageType);
        imageType.setBounds(10, 480, 333, 70);

        fieldQuantity.setBorder(null);
        fieldQuantity.setForeground(new java.awt.Color(0, 0, 0));
        fieldQuantity.setText("formField");
        fieldQuantity.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldQuantity.setPlaceholderColor(new java.awt.Color(153, 153, 153));
        fieldQuantity.setSelectionColor(new java.awt.Color(25, 102, 24));
        panelInformation.add(fieldQuantity);
        fieldQuantity.setBounds(360, 490, 310, 50);

        labelQuantity.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelQuantity.setText("Quantity Change");
        panelInformation.add(labelQuantity);
        labelQuantity.setBounds(350, 430, 260, 50);

        imageQuantity.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldHalf.png"))); // NOI18N
        panelInformation.add(imageQuantity);
        imageQuantity.setBounds(350, 480, 333, 70);

        panelFields.add(panelInformation);
        panelInformation.setBounds(10, 20, 690, 550);

        panelCRUD.setBackground(new java.awt.Color(255, 255, 255));
        panelCRUD.setLayout(null);

        labelAdd.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelAdd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelAdd.setText("Add");
        labelAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelCRUD.add(labelAdd);
        labelAdd.setBounds(10, 10, 290, 30);

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnLong.png"))); // NOI18N
        btnAdd.setBorder(null);
        btnAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdd.setEnabled(false);
        btnAdd.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnLong_pressed.png"))); // NOI18N
        btnAdd.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnLong_pressed.png"))); // NOI18N
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        panelCRUD.add(btnAdd);
        btnAdd.setBounds(0, 0, 310, 50);

        labelClear.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelClear.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelClear.setText("Clear");
        labelClear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelCRUD.add(labelClear);
        labelClear.setBounds(370, 10, 290, 30);

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnLong.png"))); // NOI18N
        btnClear.setBorder(null);
        btnClear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClear.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnLong_pressed.png"))); // NOI18N
        btnClear.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnLong_pressed.png"))); // NOI18N
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        panelCRUD.add(btnClear);
        btnClear.setBounds(360, 0, 310, 50);

        panelFields.add(panelCRUD);
        panelCRUD.setBounds(20, 650, 670, 50);

        panelTable.setBackground(new java.awt.Color(255, 255, 255));
        panelTable.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 102, 24), 2));
        panelTable.setLayout(null);

        tabButtons.setBackground(new java.awt.Color(255, 255, 255));
        tabButtons.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelStock.setFont(new java.awt.Font("Bebas", 0, 24)); // NOI18N
        labelStock.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelStock.setText("stock quantities");
        labelStock.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tabButtons.add(labelStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -1, 520, 40));

        btnStock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnRequest.png"))); // NOI18N
        btnStock.setBorder(null);
        btnStock.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnStock.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnRequest_active.png"))); // NOI18N
        btnStock.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnRequest_active.png"))); // NOI18N
        btnStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStockActionPerformed(evt);
            }
        });
        tabButtons.add(btnStock, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -4, 520, 50));

        labelAudit.setFont(new java.awt.Font("Bebas", 0, 24)); // NOI18N
        labelAudit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelAudit.setText("view Audit trail");
        labelAudit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tabButtons.add(labelAudit, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, -1, 550, 40));

        btnAudit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnRequest.png"))); // NOI18N
        btnAudit.setBorder(null);
        btnAudit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAudit.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnRequest_active.png"))); // NOI18N
        btnAudit.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnRequest_active.png"))); // NOI18N
        btnAudit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAuditActionPerformed(evt);
            }
        });
        tabButtons.add(btnAudit, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, -4, 560, 50));

        panelTable.add(tabButtons);
        tabButtons.setBounds(10, 10, 1080, 46);

        panelStock.setBackground(new java.awt.Color(255, 255, 255));

        scrollStock.setBorder(null);

        tableStock.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Name", "Quantity", "Last Updated"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class
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
        tableStock.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        tableStock.setGridColor(new java.awt.Color(255, 255, 255));
        tableStock.setSelectionBackground(new java.awt.Color(25, 102, 24));
        scrollStock.setViewportView(tableStock);

        javax.swing.GroupLayout panelStockLayout = new javax.swing.GroupLayout(panelStock);
        panelStock.setLayout(panelStockLayout);
        panelStockLayout.setHorizontalGroup(
            panelStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1080, Short.MAX_VALUE)
            .addGroup(panelStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelStockLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(scrollStock, javax.swing.GroupLayout.DEFAULT_SIZE, 1074, Short.MAX_VALUE)))
        );
        panelStockLayout.setVerticalGroup(
            panelStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 825, Short.MAX_VALUE)
            .addGroup(panelStockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelStockLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(scrollStock, javax.swing.GroupLayout.DEFAULT_SIZE, 813, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        tabInventory.addTab("tab1", panelStock);

        panelAudit.setBackground(new java.awt.Color(255, 255, 255));

        scrollAudit.setBorder(null);

        tableAudit.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Timestamp", "Type", "Name", "Quantity Change", "Created by"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableAudit.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        tableAudit.setGridColor(new java.awt.Color(255, 255, 255));
        tableAudit.setSelectionBackground(new java.awt.Color(25, 102, 24));
        scrollAudit.setViewportView(tableAudit);

        javax.swing.GroupLayout panelAuditLayout = new javax.swing.GroupLayout(panelAudit);
        panelAudit.setLayout(panelAuditLayout);
        panelAuditLayout.setHorizontalGroup(
            panelAuditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1087, Short.MAX_VALUE)
            .addGroup(panelAuditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelAuditLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(scrollAudit, javax.swing.GroupLayout.PREFERRED_SIZE, 1075, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        panelAuditLayout.setVerticalGroup(
            panelAuditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 825, Short.MAX_VALUE)
            .addGroup(panelAuditLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelAuditLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(scrollAudit, javax.swing.GroupLayout.PREFERRED_SIZE, 792, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(27, Short.MAX_VALUE)))
        );

        tabInventory.addTab("tab1", panelAudit);

        panelTable.add(tabInventory);
        tabInventory.setBounds(10, 17, 1080, 860);

        javax.swing.GroupLayout panelMainLayout = new javax.swing.GroupLayout(panelMain);
        panelMain.setLayout(panelMainLayout);
        panelMainLayout.setHorizontalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelFields, javax.swing.GroupLayout.PREFERRED_SIZE, 713, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelMainLayout.setVerticalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelTable, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
                    .addComponent(panelFields, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearFields();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        String stock_name = comboName.getSelectedItem().toString();
        String stock_type = comboType.getSelectedItem().toString();
        int stock_quantity = Integer.parseInt(fieldQuantity.getText());
        String stock_location = "MAIN SUPPLY ROOM";

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            // Get item_id based on stock_name
            int item_id = Integer.parseInt(getColumnValueByString(Main.TB_CATALOG_ITEM, "item_id", "name", stock_name));

            // Get current timestamp
            Timestamp now = new Timestamp(System.currentTimeMillis());

            // 1. Insert into tb_inventory_transaction
            String query = "INSERT INTO " + Main.TB_INVENTORY_TRANSACTION + " (item_id, location, type, quantity_change, created_by)\n"
                    + "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);

            if (comboType.getSelectedItem().toString().contains("(OUT)")) {
                stock_quantity *= -1;
            }

            pst.setInt(1, item_id);
            pst.setString(2, stock_location);
            pst.setString(3, stock_type);
            pst.setInt(4, stock_quantity);
            pst.setInt(5, Main.getUserSessionID());
            pst.executeUpdate();

            // 2. Update or insert into tb_inventory_balance
            if (DatabaseUtil.recordExists(conn, Main.TB_INVENTORY_BALANCE, new String[]{"item_id", "location"}, new Object[]{item_id, stock_location})) {
                query = "UPDATE " + Main.TB_INVENTORY_BALANCE + " "
                        + "SET quantity = quantity + ?, updated_at = ? "
                        + "WHERE item_id = ? AND location = ?";
                pst = conn.prepareStatement(query);

                pst.setInt(1, stock_quantity);       // Increment quantity
                pst.setTimestamp(2, now); // New update time
                pst.setInt(3, item_id);              // item_id condition
                pst.setString(4, stock_location);    // location condition
                pst.executeUpdate();
            } else {
                query = "INSERT INTO " + Main.TB_INVENTORY_BALANCE + " (item_id, location, quantity, updated_at)\n"
                        + "VALUES (?, ?, ?, ?)";
                pst = conn.prepareStatement(query);

                pst.setInt(1, item_id);
                pst.setString(2, stock_location);
                pst.setInt(3, stock_quantity);
                pst.setTimestamp(4, now);
                pst.executeUpdate();
            }

            // HISTORY : MANAGEMENT-ADD
//            String stock_code = getColumnFromLastRow(Main.TB_ITEM_STOCK, "stock_timestamp", "stock_code");
//
//            String stock_code_end = stock_code;
//
//            if (stock_code_end.contains("-")) {
//                String[] parts = stock_code_end.split("-");
//                int qty = Integer.parseInt(parts[2]) + stock_quantity - 1;
//                stock_code_end = parts[0] + "-" + parts[1] + "-" + qty;
//            }
//
//            String history_desc = "";
//
//            history_desc += createHistoryDesc(stock_name, "Name");
//            if (!stock_desc.isEmpty()) {
//                history_desc += createHistoryDesc(stock_desc, "Description");
//            }
//            history_desc += createHistoryDesc(stock_price, "Price");
//            history_desc += createHistoryDesc(String.valueOf(stock_quantity), "Quantity");
//            history_desc += createHistoryDesc(stock_deliveryDate, "DOD");
//            history_desc += createHistoryDesc(stock_benefactor, "Benefactor");
//
//            insertHistory(DatabaseUtil.HistoryFrame.MANAGEMENT, DatabaseUtil.HistoryType.ADD, stock_code, stock_code_end, history_desc, "N/A");
            JOptionPane.showMessageDialog(this, "(" + stock_quantity + ") Stock/s Added!", "Success", JOptionPane.INFORMATION_MESSAGE);

            clearFields();
            refreshItemTable();
        } catch (SQLException e) {
            MessageUtil.paneDatabaseError(e);
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
        exportSQLToCSV(currentSearchQuery);
    }//GEN-LAST:event_btnExportActionPerformed

    private void btnStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStockActionPerformed
        switchTable(0);
    }//GEN-LAST:event_btnStockActionPerformed

    private void btnAuditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAuditActionPerformed
        switchTable(1);
    }//GEN-LAST:event_btnAuditActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAudit;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnStock;
    private ProjectINSY.java.swing.ComboBoxSuggestion comboName;
    private ProjectINSY.java.swing.ComboBoxSuggestion comboType;
    private ProjectINSY.java.swing.Date.DateChooser dateDOD;
    private ProjectINSY.java.swing.Form.FormField fieldCode;
    private ProjectINSY.java.swing.Form.FormField fieldID;
    private ProjectINSY.java.swing.Form.FormFieldSuggestion fieldLocation;
    private ProjectINSY.java.swing.Form.FormField fieldQuantity;
    private javax.swing.JLabel imageCode;
    private javax.swing.JLabel imageLocation;
    private javax.swing.JLabel imageName;
    private javax.swing.JLabel imageQuantity;
    private javax.swing.JLabel imageType;
    private javax.swing.JLabel labelAdd;
    private javax.swing.JLabel labelAudit;
    private javax.swing.JLabel labelClear;
    private javax.swing.JLabel labelCode;
    private javax.swing.JLabel labelLocation;
    private javax.swing.JLabel labelName;
    private javax.swing.JLabel labelQuantity;
    private javax.swing.JLabel labelStock;
    private javax.swing.JLabel labelType;
    private javax.swing.JPanel panelAudit;
    private javax.swing.JPanel panelCRUD;
    private javax.swing.JPanel panelFields;
    private javax.swing.JPanel panelInformation;
    private javax.swing.JPanel panelMain;
    private javax.swing.JPanel panelStock;
    private javax.swing.JPanel panelTable;
    private javax.swing.JScrollPane scrollAudit;
    private javax.swing.JScrollPane scrollStock;
    private javax.swing.JPanel tabButtons;
    private javax.swing.JTabbedPane tabInventory;
    private ProjectINSY.java.swing.Table tableAudit;
    private ProjectINSY.java.swing.Table tableStock;
    // End of variables declaration//GEN-END:variables
}
