/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ProjectINSY.java.ui;

import ProjectINSY.java.Main;
import ProjectINSY.java.model.ItemPanel;
import ProjectINSY.java.swing.TableHighlighter;
import ProjectINSY.java.util.DatabaseUtil;
import static ProjectINSY.java.util.DatabaseUtil.createHistoryDesc;
import static ProjectINSY.java.util.DatabaseUtil.insertHistory;
import static ProjectINSY.java.util.DatabaseUtil.getColumnValueByInt;
import static ProjectINSY.java.util.DatabaseUtil.getColumnValueByString;
import ProjectINSY.java.util.GuiUtil;
import ProjectINSY.java.util.GuiUtil.FieldFocus;
import static ProjectINSY.java.util.GuiUtil.enforceDigits;
import static ProjectINSY.java.util.GuiUtil.resetBtnEnability;
import static ProjectINSY.java.util.GuiUtil.setDefaultField;
import static ProjectINSY.java.util.GuiUtil.setScrollBarCustom;
import static ProjectINSY.java.util.GuiUtil.setTransparentFrame;
import ProjectINSY.java.util.MessageUtil;
import static ProjectINSY.java.util.MessageUtil.paneDatabaseError;
import ProjectINSY.java.util.TableUtil;
import ProjectINSY.java.util.TableUtil.EnumAlignment;
import static ProjectINSY.java.util.TableUtil.defaultTable;
import static ProjectINSY.java.util.GuiUtil.fieldHasValue;
import static ProjectINSY.java.util.TableUtil.fixedColumnAll;
import static ProjectINSY.java.util.GuiUtil.getComboSelected;
import static ProjectINSY.java.util.GuiUtil.getFieldString;
import static ProjectINSY.java.util.GuiUtil.isDefaultComboItem;
import static ProjectINSY.java.util.TableUtil.setColumnHorizontalAligment;
import static ProjectINSY.java.util.TableUtil.sorterNumbers;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author admin
 */
public class ItemRequest extends ItemPanel {

    TableHighlighter TableHighlighter = new TableHighlighter(5);

    /**
     * Creates new form LogIn
     */
    public ItemRequest() {
        initComponents();

        currentSearchQuery = "SELECT * FROM "
                + Main.TB_ITEM_REQUEST + " WHERE 1 "
                + filterWHERE
                + " ORDER BY request_timestamp ASC";

        setScrollBarCustom(tableScroll);

        setTransparentFrame(ItemRequest.this);
        setTransparentFrame(btnCreateRequest, btnFilter, btnAdd, btnUpdate, btnClear, btnDelete, btnClearFilter);

        fieldItem.getDocument().addDocumentListener(new FieldChangeListener());
        fieldName.getDocument().addDocumentListener(new FieldChangeListener());
        fieldQuantity.getDocument().addDocumentListener(new FieldChangeListener());

        defaultTable(tableRequest);
        tableRequest.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableRequest.getSelectedRow();
                if (selectedRow >= 0) {
                    selectTableRequest(selectedRow);
                }
            }
        });

        setColumnHorizontalAligment(tableRequest, 4, EnumAlignment.LEFT);
        fixedColumnAll(tableRequest);
        sorterNumbers(tableRequest, 4);

        switchRequestForm(btnCreateRequest);
    }

    //<editor-fold defaultstate="collapsed" desc="Item Panel">
    @Override
    public void refreshItemTable() {
        filterWHERE = "";
        if (!isDefaultComboItem(searchItem)) {
            filterWHERE += "AND request_item = '" + getComboSelected(searchItem) + "' ";
        }
        if (!isDefaultComboItem(searchDesc)) {
            filterWHERE += "AND request_desc = '" + getComboSelected(searchDesc) + "' ";
        }
        if (!isDefaultComboItem(searchName)) {
            filterWHERE += "AND request_name = '" + getComboSelected(searchName) + "' ";
        }
        if (!isDefaultComboItem(searchStatus)) {
            filterWHERE += "AND request_status = '" + getComboSelected(searchStatus) + "' ";
        }
        if (fieldHasValue(searchQuantityStart)) {
            filterWHERE += "AND request_quantity >= '" + getFieldString(searchQuantityStart) + "' ";
        }
        if (fieldHasValue(searchQuantityEnd)) {
            filterWHERE += "AND request_quantity <= '" + getFieldString(searchQuantityEnd) + "' ";
        }

        currentSearchQuery = "SELECT * FROM "
                + Main.TB_ITEM_REQUEST + " WHERE 1 "
                + filterWHERE
                + " ORDER BY request_timestamp ASC";

        TableUtil.refreshTable(tableRequest, currentSearchQuery, TableUtil.TableEnum.ITEM_REQUEST);

        for (int i = 0; i < tableRequest.getColumnCount(); i++) {
            tableRequest.getColumnModel().getColumn(i).setCellRenderer(TableHighlighter);
        }
    }

    @Override
    public void repopulateFilterComboBox() {
        disableUpdatingComboBoxes();
        GuiUtil.repopulateComboBox(searchItem, "SELECT request_item FROM " + Main.TB_ITEM_REQUEST);
        GuiUtil.repopulateComboBox(searchDesc, "SELECT request_desc FROM " + Main.TB_ITEM_REQUEST);
        GuiUtil.repopulateComboBox(searchName, "SELECT request_name FROM " + Main.TB_ITEM_REQUEST);
        GuiUtil.repopulateComboBox(searchStatus, "SELECT request_status FROM " + Main.TB_ITEM_REQUEST);
        searchStatus.removeItem("PENDING");
        searchStatus.insertItemAt("PENDING", 1);
        searchStatus.setSelectedItem("PENDING");
        enableUpdatingComboBoxes();

        refreshItemTable();
    }

    @Override
    public void repopulateComboBox() {
    }
    //</editor-fold>

    public void selectTableRequest(int selectedRow) {
        String[] tableRow = TableUtil.selectTableRow(tableRequest, selectedRow);
        TableUtil.linkFieldsToTable(tableRow, fieldTimestamp, fieldItem, fieldDesc, fieldName, fieldQuantity, comboStatus);

        fieldID.setText(getColumnValueByString(Main.TB_ITEM_REQUEST, "request_id", "request_timestamp", fieldTimestamp.getText()));

        setUpdateDeleteEnableItem();
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
            btnAdd.setEnabled(!fieldItem.getText().trim().isEmpty()
                    && !fieldItem.getText().trim().equals(PLACEHOLDER_NAME)
                    && !fieldName.getText().trim().isEmpty()
                    && !fieldName.getText().trim().equals(PLACEHOLDER_NAME)
                    && !fieldQuantity.getText().trim().isEmpty());
        }
    }

    public void switchRequestForm(JComponent com) {
        if (com == btnCreateRequest) {
            tabRequest.setSelectedIndex(0);
            btnCreateRequest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnRequest_active.png")));
            btnFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnRequest.png")));
        } else {
            tabRequest.setSelectedIndex(1);
            btnFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnRequest_active.png")));
            btnCreateRequest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnRequest.png")));

            repopulateFilterComboBox();
        }
    }

    public void clearFields() {
        GuiUtil.clearField(fieldID, "");
        GuiUtil.clearField(fieldTimestamp, "");
        GuiUtil.clearField(fieldItem, PLACEHOLDER_NAME);
        GuiUtil.clearField(fieldDesc, PLACEHOLDER_DESC);
        GuiUtil.clearField(fieldName, PLACEHOLDER_NAME);
        GuiUtil.clearComboBox(comboStatus);
        TableUtil.clearSelectedTableRow(tableRequest);
        setUpdateDeleteEnableItem();
    }

    public void setUpdateDeleteEnableItem() {
        resetBtnEnability(fieldID, btnUpdate, btnDelete);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fieldID = new javax.swing.JTextField();
        fieldTimestamp = new javax.swing.JTextField();
        panelMain = new javax.swing.JPanel();
        tableScroll = new javax.swing.JScrollPane();
        tableRequest = new ProjectINSY.java.swing.Table();
        panelTab = new javax.swing.JPanel();
        tabButtons = new javax.swing.JPanel();
        labelFormLocation = new javax.swing.JLabel();
        btnCreateRequest = new javax.swing.JButton();
        labelFormLocation1 = new javax.swing.JLabel();
        btnFilter = new javax.swing.JButton();
        tabRequest = new javax.swing.JTabbedPane();
        panelBody = new javax.swing.JPanel();
        labelName = new javax.swing.JLabel();
        fieldName = new javax.swing.JTextField();
        imageName = new javax.swing.JLabel();
        labelDesc = new javax.swing.JLabel();
        fieldDesc = new javax.swing.JTextField();
        imageDesc = new javax.swing.JLabel();
        labelItem = new javax.swing.JLabel();
        fieldItem = new javax.swing.JTextField();
        imageItem = new javax.swing.JLabel();
        labelQuantity = new javax.swing.JLabel();
        fieldQuantity = new javax.swing.JTextField();
        imageQuantity = new javax.swing.JLabel();
        labelStatus = new javax.swing.JLabel();
        comboStatus = new ProjectINSY.java.swing.ComboBoxSuggestion();
        imageStatus = new javax.swing.JLabel();
        panelCRUD = new javax.swing.JPanel();
        labelAdd = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        labelUpdate = new javax.swing.JLabel();
        btnUpdate = new javax.swing.JButton();
        labelClear = new javax.swing.JLabel();
        btnClear = new javax.swing.JButton();
        labelDelete = new javax.swing.JLabel();
        btnDelete = new javax.swing.JButton();
        panelFilter = new javax.swing.JPanel();
        labelFilterItem = new javax.swing.JLabel();
        searchItem = new ProjectINSY.java.swing.ComboBoxSuggestion();
        jSeparator2 = new javax.swing.JSeparator();
        labelFilterDesc = new javax.swing.JLabel();
        searchDesc = new ProjectINSY.java.swing.ComboBoxSuggestion();
        jSeparator3 = new javax.swing.JSeparator();
        labelFilterName = new javax.swing.JLabel();
        searchName = new ProjectINSY.java.swing.ComboBoxSuggestion();
        jSeparator4 = new javax.swing.JSeparator();
        labelFilterQuantity = new javax.swing.JLabel();
        labelFilterQuantityFrom = new javax.swing.JLabel();
        searchQuantityStart = new ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion();
        labelFilterQuantityTo = new javax.swing.JLabel();
        searchQuantityEnd = new ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion();
        jSeparator5 = new javax.swing.JSeparator();
        labelFilterStatus = new javax.swing.JLabel();
        searchStatus = new ProjectINSY.java.swing.ComboBoxSuggestion();
        panelClearFilter = new javax.swing.JPanel();
        labelCleaFilter = new javax.swing.JLabel();
        btnClearFilter = new javax.swing.JButton();

        fieldID.setText("jTextField1");

        fieldTimestamp.setText("jTextField1");

        setMaximumSize(new java.awt.Dimension(1840, 900));
        setMinimumSize(new java.awt.Dimension(1840, 900));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(1840, 900));

        panelMain.setBackground(new java.awt.Color(255, 255, 255));
        panelMain.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 102, 24), 2));
        panelMain.setMaximumSize(new java.awt.Dimension(1840, 900));
        panelMain.setMinimumSize(new java.awt.Dimension(1840, 900));
        panelMain.setPreferredSize(new java.awt.Dimension(1840, 900));

        tableScroll.setBorder(null);

        tableRequest.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Timestamp", "Item", "Description", "Requestor", "Quantity", "Status in Supply Room"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class
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
        tableRequest.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        tableRequest.setGridColor(new java.awt.Color(255, 255, 255));
        tableRequest.setSelectionBackground(new java.awt.Color(25, 102, 24));
        tableScroll.setViewportView(tableRequest);

        panelTab.setBackground(new java.awt.Color(255, 255, 255));
        panelTab.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 102, 24), 2));
        panelTab.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabButtons.setBackground(new java.awt.Color(255, 255, 255));
        tabButtons.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelFormLocation.setFont(new java.awt.Font("Bebas", 0, 24)); // NOI18N
        labelFormLocation.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelFormLocation.setText("REQUEST MANAGEMENT");
        labelFormLocation.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tabButtons.add(labelFormLocation, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -1, 270, 40));

        btnCreateRequest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnRequest.png"))); // NOI18N
        btnCreateRequest.setBorder(null);
        btnCreateRequest.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCreateRequest.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnRequest_active.png"))); // NOI18N
        btnCreateRequest.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnRequest_active.png"))); // NOI18N
        btnCreateRequest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateRequestActionPerformed(evt);
            }
        });
        tabButtons.add(btnCreateRequest, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -4, -1, 50));

        labelFormLocation1.setFont(new java.awt.Font("Bebas", 0, 24)); // NOI18N
        labelFormLocation1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelFormLocation1.setText("REQUEST FILTER");
        labelFormLocation1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tabButtons.add(labelFormLocation1, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, -1, 270, 40));

        btnFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnRequest.png"))); // NOI18N
        btnFilter.setBorder(null);
        btnFilter.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFilter.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnRequest_active.png"))); // NOI18N
        btnFilter.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnRequest_active.png"))); // NOI18N
        btnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilterActionPerformed(evt);
            }
        });
        tabButtons.add(btnFilter, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, -4, -1, 50));

        panelTab.add(tabButtons, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 550, 50));

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelName.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelName.setText("Name of Requestor");
        panelBody.add(labelName, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 260, -1, -1));

        fieldName.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        fieldName.setForeground(new java.awt.Color(153, 153, 153));
        fieldName.setText("Enter Name");
        fieldName.setBorder(null);
        fieldName.setSelectionColor(new java.awt.Color(25, 102, 24));
        fieldName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldNameFocusLost(evt);
            }
        });
        panelBody.add(fieldName, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 510, 30));

        imageName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldLogIn.png"))); // NOI18N
        panelBody.add(imageName, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, 550, -1));

        labelDesc.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelDesc.setText("Item Description");
        panelBody.add(labelDesc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));

        fieldDesc.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        fieldDesc.setForeground(new java.awt.Color(153, 153, 153));
        fieldDesc.setText("Enter Description");
        fieldDesc.setBorder(null);
        fieldDesc.setSelectionColor(new java.awt.Color(25, 102, 24));
        fieldDesc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldDescFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldDescFocusLost(evt);
            }
        });
        panelBody.add(fieldDesc, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 510, 30));

        imageDesc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageDesc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldLogIn.png"))); // NOI18N
        panelBody.add(imageDesc, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 550, -1));

        labelItem.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelItem.setText("Item Name");
        panelBody.add(labelItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        fieldItem.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        fieldItem.setForeground(new java.awt.Color(153, 153, 153));
        fieldItem.setText("Enter Name");
        fieldItem.setBorder(null);
        fieldItem.setSelectionColor(new java.awt.Color(25, 102, 24));
        fieldItem.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldItemFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldItemFocusLost(evt);
            }
        });
        panelBody.add(fieldItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 510, 30));

        imageItem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldLogIn.png"))); // NOI18N
        panelBody.add(imageItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 550, -1));

        labelQuantity.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelQuantity.setText("Quantity");
        panelBody.add(labelQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, -1, -1));

        fieldQuantity.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        fieldQuantity.setForeground(new java.awt.Color(153, 153, 153));
        fieldQuantity.setText("1");
        fieldQuantity.setBorder(null);
        fieldQuantity.setSelectionColor(new java.awt.Color(25, 102, 24));
        fieldQuantity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldQuantityFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldQuantityFocusLost(evt);
            }
        });
        fieldQuantity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                fieldQuantityKeyTyped(evt);
            }
        });
        panelBody.add(fieldQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 430, 220, 30));

        imageQuantity.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageQuantity.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldHalf_small.png"))); // NOI18N
        panelBody.add(imageQuantity, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 420, 260, -1));

        labelStatus.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelStatus.setText("Status");
        panelBody.add(labelStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 380, -1, -1));

        comboStatus.setBorder(null);
        comboStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PENDING", "DENIED", "RECEIVED" }));
        comboStatus.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        panelBody.add(comboStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 430, 220, -1));

        imageStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldHalf_small.png"))); // NOI18N
        panelBody.add(imageStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 420, 260, -1));

        panelCRUD.setBackground(new java.awt.Color(255, 255, 255));
        panelCRUD.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelAdd.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelAdd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelAdd.setText("Add");
        labelAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelCRUD.add(labelAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 80, -1));

        btnAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn.png"))); // NOI18N
        btnAdd.setBorder(null);
        btnAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdd.setEnabled(false);
        btnAdd.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_pressed.png"))); // NOI18N
        btnAdd.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_pressed.png"))); // NOI18N
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        panelCRUD.add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 100, 40));

        labelUpdate.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelUpdate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelUpdate.setText("Update");
        labelUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelCRUD.add(labelUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, 80, -1));

        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn.png"))); // NOI18N
        btnUpdate.setBorder(null);
        btnUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdate.setEnabled(false);
        btnUpdate.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_pressed.png"))); // NOI18N
        btnUpdate.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_pressed.png"))); // NOI18N
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        panelCRUD.add(btnUpdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, 100, 40));

        labelClear.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelClear.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelClear.setText("Clear");
        labelClear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelCRUD.add(labelClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 40, 80, -1));

        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn.png"))); // NOI18N
        btnClear.setBorder(null);
        btnClear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClear.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_pressed.png"))); // NOI18N
        btnClear.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_pressed.png"))); // NOI18N
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        panelCRUD.add(btnClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 30, 100, 40));

        labelDelete.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelDelete.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelDelete.setText("Delete");
        labelDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelCRUD.add(labelDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 40, 80, -1));

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_red.png"))); // NOI18N
        btnDelete.setBorder(null);
        btnDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDelete.setEnabled(false);
        btnDelete.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_pressed.png"))); // NOI18N
        btnDelete.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_pressed.png"))); // NOI18N
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        panelCRUD.add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 30, 100, 40));

        panelBody.add(panelCRUD, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 490, 560, -1));

        tabRequest.addTab("tab1", panelBody);

        panelFilter.setBackground(new java.awt.Color(255, 255, 255));

        labelFilterItem.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelFilterItem.setText("Item Name");

        searchItem.setBorder(null);
        searchItem.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        searchItem.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                searchItemItemStateChanged(evt);
            }
        });

        labelFilterDesc.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelFilterDesc.setText("Description");

        searchDesc.setBorder(null);
        searchDesc.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        searchDesc.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                searchDescItemStateChanged(evt);
            }
        });

        labelFilterName.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelFilterName.setText("Requestor");

        searchName.setBorder(null);
        searchName.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        searchName.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                searchNameItemStateChanged(evt);
            }
        });

        labelFilterQuantity.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelFilterQuantity.setText("Quantity Range");

        labelFilterQuantityFrom.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        labelFilterQuantityFrom.setText("From");

        searchQuantityStart.setBorder(null);
        searchQuantityStart.setForeground(new java.awt.Color(153, 153, 153));
        searchQuantityStart.setText("1");
        searchQuantityStart.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        searchQuantityStart.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchQuantityStartFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                searchQuantityStartFocusLost(evt);
            }
        });
        searchQuantityStart.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchQuantityStartKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchQuantityStartKeyTyped(evt);
            }
        });

        labelFilterQuantityTo.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        labelFilterQuantityTo.setText("To");

        searchQuantityEnd.setBorder(null);
        searchQuantityEnd.setForeground(new java.awt.Color(153, 153, 153));
        searchQuantityEnd.setText("999999999");
        searchQuantityEnd.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        searchQuantityEnd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchQuantityEndFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                searchQuantityEndFocusLost(evt);
            }
        });
        searchQuantityEnd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchQuantityEndKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchQuantityEndKeyTyped(evt);
            }
        });

        labelFilterStatus.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelFilterStatus.setText("Status");

        searchStatus.setBorder(null);
        searchStatus.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        searchStatus.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                searchStatusItemStateChanged(evt);
            }
        });

        panelClearFilter.setBackground(new java.awt.Color(255, 255, 255));
        panelClearFilter.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelCleaFilter.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelCleaFilter.setForeground(new java.awt.Color(255, 255, 255));
        labelCleaFilter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCleaFilter.setText("Clear Filters");
        labelCleaFilter.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelClearFilter.add(labelCleaFilter, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 0, 310, 50));

        btnClearFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnLong_red.png"))); // NOI18N
        btnClearFilter.setBorder(null);
        btnClearFilter.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnLong_red_pressed.png"))); // NOI18N
        btnClearFilter.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnLong_red_pressed.png"))); // NOI18N
        btnClearFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearFilterActionPerformed(evt);
            }
        });
        panelClearFilter.add(btnClearFilter, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 0, 310, -1));

        javax.swing.GroupLayout panelFilterLayout = new javax.swing.GroupLayout(panelFilter);
        panelFilter.setLayout(panelFilterLayout);
        panelFilterLayout.setHorizontalGroup(
            panelFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFilterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addComponent(searchItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelFilterItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelFilterDesc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchDesc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator3)
                    .addComponent(labelFilterName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator4)
                    .addGroup(panelFilterLayout.createSequentialGroup()
                        .addGroup(panelFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelFilterQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelFilterLayout.createSequentialGroup()
                                .addComponent(labelFilterQuantityFrom)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchQuantityStart, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(labelFilterQuantityTo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(searchQuantityEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                        .addGroup(panelFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(searchStatus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelFilterStatus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
            .addComponent(panelClearFilter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelFilterLayout.setVerticalGroup(
            panelFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFilterLayout.createSequentialGroup()
                .addGroup(panelFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFilterLayout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(labelFilterItem)
                        .addGap(6, 6, 6)
                        .addComponent(searchItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(labelFilterDesc)
                        .addGap(6, 6, 6)
                        .addComponent(searchDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(labelFilterName)
                        .addGap(6, 6, 6)
                        .addComponent(searchName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(panelFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelFilterLayout.createSequentialGroup()
                                .addComponent(labelFilterQuantity)
                                .addGap(6, 6, 6)
                                .addGroup(panelFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelFilterQuantityFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(searchQuantityStart, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelFilterQuantityTo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelFilterLayout.createSequentialGroup()
                                .addComponent(labelFilterStatus)
                                .addGap(6, 6, 6)
                                .addComponent(searchStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelFilterLayout.createSequentialGroup()
                        .addGap(339, 339, 339)
                        .addComponent(searchQuantityEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24)
                .addComponent(panelClearFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(360, Short.MAX_VALUE))
        );

        tabRequest.addTab("tab2", panelFilter);

        tabRequest.setSelectedIndex(1);

        panelTab.add(tabRequest, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 22, 550, 850));

        javax.swing.GroupLayout panelMainLayout = new javax.swing.GroupLayout(panelMain);
        panelMain.setLayout(panelMainLayout);
        panelMainLayout.setHorizontalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelTab, javax.swing.GroupLayout.PREFERRED_SIZE, 572, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 1246, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelMainLayout.setVerticalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelTab, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
                    .addComponent(tableScroll))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void fieldNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldNameFocusGained
        setDefaultField(fieldName, PLACEHOLDER_NAME, FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_fieldNameFocusGained

    private void fieldNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldNameFocusLost
        setDefaultField(fieldName, PLACEHOLDER_NAME, FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_fieldNameFocusLost

    private void fieldDescFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldDescFocusGained
        setDefaultField(fieldDesc, PLACEHOLDER_DESC, FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_fieldDescFocusGained

    private void fieldDescFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldDescFocusLost
        setDefaultField(fieldDesc, PLACEHOLDER_DESC, FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_fieldDescFocusLost

    private void fieldItemFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldItemFocusGained
        setDefaultField(fieldItem, PLACEHOLDER_NAME, FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_fieldItemFocusGained

    private void fieldItemFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldItemFocusLost
        setDefaultField(fieldItem, PLACEHOLDER_NAME, FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_fieldItemFocusLost

    private void fieldQuantityFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldQuantityFocusGained
        setDefaultField(fieldQuantity, PLACEHOLDER_QTY, FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_fieldQuantityFocusGained

    private void fieldQuantityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldQuantityFocusLost
        setDefaultField(fieldQuantity, PLACEHOLDER_QTY, FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_fieldQuantityFocusLost

    private void btnCreateRequestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateRequestActionPerformed
        switchRequestForm(btnCreateRequest);
    }//GEN-LAST:event_btnCreateRequestActionPerformed

    private void btnFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterActionPerformed
        switchRequestForm(btnFilter);
    }//GEN-LAST:event_btnFilterActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        String request_item = fieldItem.getText();
        String request_desc = fieldDesc.getText().equals(PLACEHOLDER_DESC) ? "" : fieldDesc.getText();
        String request_quantity = fieldQuantity.getText();
        String request_name = fieldName.getText();

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            String query = "INSERT INTO " + Main.TB_ITEM_REQUEST + " (request_item, request_desc, request_quantity, request_name)\n"
                    + "VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, request_item);
            pst.setString(2, request_desc);
            pst.setString(3, request_quantity);
            pst.setString(4, request_name);

            // HISTORY : REQUEST-ADD
            String history_desc = "";
            history_desc += createHistoryDesc(request_item, "Item Name");
            history_desc += createHistoryDesc(request_desc, "Description");
            history_desc += createHistoryDesc(request_quantity, "Quantity");

            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                String request_idStr = "Request-" + rs.getInt(1);
                insertHistory(DatabaseUtil.HistoryFrame.REQUEST, DatabaseUtil.HistoryType.ADD, request_idStr, request_idStr, history_desc, request_name);
            }

            JOptionPane.showMessageDialog(this, "Request Added!", "Success", JOptionPane.INFORMATION_MESSAGE);

            clearFields();
            refreshItemTable();
        } catch (SQLException e) {
            MessageUtil.paneDatabaseError(e);
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        int request_id = Integer.parseInt(fieldID.getText());
        String request_idStr = "Request-" + request_id;
        String request_item = fieldItem.getText();
        String request_desc = fieldDesc.getText().equals(PLACEHOLDER_DESC) ? "" : fieldDesc.getText();
        String request_quantity = fieldQuantity.getText();
        String request_name = fieldName.getText();
        String request_status = comboStatus.getSelectedItem().toString();

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            String query = "UPDATE " + Main.TB_ITEM_REQUEST + " SET request_item = ?, request_desc = ?, request_quantity = ?, request_name = ?, request_status = ? WHERE request_id = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, request_item);
            pst.setString(2, request_desc);
            pst.setString(3, request_quantity);
            pst.setString(4, request_name);
            pst.setString(5, request_status);
            pst.setInt(6, request_id);

            // HISTORY : REQUEST-UPDATE
            String history_desc = "";

            String old_item = getColumnValueByInt(Main.TB_ITEM_REQUEST, "request_item", "request_id", request_id);
            String old_desc = getColumnValueByInt(Main.TB_ITEM_REQUEST, "request_desc", "request_id", request_id);
            String old_quantity = getColumnValueByInt(Main.TB_ITEM_REQUEST, "request_quantity", "request_id", request_id);
            String old_name = getColumnValueByInt(Main.TB_ITEM_REQUEST, "request_name", "request_id", request_id);
            String old_status = getColumnValueByInt(Main.TB_ITEM_REQUEST, "request_status", "request_id", request_id);

            history_desc += createHistoryDesc(old_item, request_item, "Item Name");
            history_desc += createHistoryDesc(old_desc, request_desc, "Description");
            history_desc += createHistoryDesc(old_quantity, request_quantity, "Quantity");
            history_desc += createHistoryDesc(old_name, request_name, "Requestor");
            history_desc += createHistoryDesc(old_status, request_status, "Status");

            insertHistory(DatabaseUtil.HistoryFrame.REQUEST, DatabaseUtil.HistoryType.UPDATE, request_idStr, request_idStr, history_desc, request_name);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Request Updated!", "Success", JOptionPane.INFORMATION_MESSAGE);

            clearFields();
            refreshItemTable();
        } catch (SQLException e) {
            paneDatabaseError(e);
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearFields();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int request_id = Integer.parseInt(fieldID.getText());
        String request_idStr = "Request-" + request_id;
        String request_item = fieldItem.getText();
        String request_name = fieldName.getText();

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            int warnUser = JOptionPane.showConfirmDialog(
                    null,
                    "Confirm Delete?",
                    "Warning: Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (warnUser == JOptionPane.YES_OPTION) {
                String query = "DELETE FROM " + Main.TB_ITEM_REQUEST + " WHERE request_id = ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setInt(1, request_id);

                // HISTORY : REQUEST-DELETE
                String history_desc = createHistoryDesc(request_item, "Item Name");

                insertHistory(DatabaseUtil.HistoryFrame.REQUEST, DatabaseUtil.HistoryType.DELETE, request_idStr, request_idStr, history_desc, request_name);

                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Request Deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);

                clearFields();
                refreshItemTable();
            }
        } catch (SQLException e) {
            paneDatabaseError(e);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void fieldQuantityKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldQuantityKeyTyped
        enforceDigits(evt);
    }//GEN-LAST:event_fieldQuantityKeyTyped

    private void searchQuantityStartFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchQuantityStartFocusGained
        setDefaultField(searchQuantityStart, Main.filterMinNumber, GuiUtil.FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_searchQuantityStartFocusGained

    private void searchQuantityStartFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchQuantityStartFocusLost
        setDefaultField(searchQuantityStart, Main.filterMinNumber, GuiUtil.FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_searchQuantityStartFocusLost

    private void searchQuantityStartKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchQuantityStartKeyReleased
        refreshItemTable();
    }//GEN-LAST:event_searchQuantityStartKeyReleased

    private void searchQuantityStartKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchQuantityStartKeyTyped
        enforceDigits(evt);
    }//GEN-LAST:event_searchQuantityStartKeyTyped

    private void searchQuantityEndFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchQuantityEndFocusGained
        setDefaultField(searchQuantityEnd, Main.filterMaxNumber, GuiUtil.FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_searchQuantityEndFocusGained

    private void searchQuantityEndFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchQuantityEndFocusLost
        setDefaultField(searchQuantityEnd, Main.filterMaxNumber, GuiUtil.FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_searchQuantityEndFocusLost

    private void searchQuantityEndKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchQuantityEndKeyReleased
        refreshItemTable();
    }//GEN-LAST:event_searchQuantityEndKeyReleased

    private void searchQuantityEndKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchQuantityEndKeyTyped
        enforceDigits(evt);
    }//GEN-LAST:event_searchQuantityEndKeyTyped

    private void btnClearFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearFilterActionPerformed
        searchItem.setSelectedIndex(0);
        searchName.setSelectedIndex(0);
        searchDesc.setSelectedIndex(0);
        searchStatus.setSelectedIndex(0);

        searchQuantityStart.setText("");
        setDefaultField(searchQuantityStart, Main.filterMinNumber, GuiUtil.FieldFocus.LOST, Color.BLACK);
        searchQuantityEnd.setText("");
        setDefaultField(searchQuantityEnd, Main.filterMaxNumber, GuiUtil.FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_btnClearFilterActionPerformed

    private void searchItemItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_searchItemItemStateChanged
        if (isUpdatingComboBoxes) {
            return;
        }

        disableUpdatingComboBoxes();
        GuiUtil.repopulateAssociatedComboBox(searchItem, searchDesc, "request_item", "SELECT request_desc FROM " + Main.TB_ITEM_REQUEST);
        GuiUtil.repopulateAssociatedComboBox(searchItem, searchName, "request_item", "SELECT request_name FROM " + Main.TB_ITEM_REQUEST);
        enableUpdatingComboBoxes();

        refreshItemTable();
    }//GEN-LAST:event_searchItemItemStateChanged

    private void searchDescItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_searchDescItemStateChanged
        if (isUpdatingComboBoxes) {
            return;
        }

        disableUpdatingComboBoxes();
        GuiUtil.repopulateAssociatedComboBox(searchDesc, searchName, "request_desc", "SELECT request_name FROM " + Main.TB_ITEM_REQUEST);
        enableUpdatingComboBoxes();

        refreshItemTable();
    }//GEN-LAST:event_searchDescItemStateChanged

    private void searchNameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_searchNameItemStateChanged
        if (isUpdatingComboBoxes) {
            return;
        }

        refreshItemTable();
    }//GEN-LAST:event_searchNameItemStateChanged

    private void searchStatusItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_searchStatusItemStateChanged
        if (isUpdatingComboBoxes) {
            return;
        }

        refreshItemTable();
    }//GEN-LAST:event_searchStatusItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnClearFilter;
    private javax.swing.JButton btnCreateRequest;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFilter;
    private javax.swing.JButton btnUpdate;
    private ProjectINSY.java.swing.ComboBoxSuggestion comboStatus;
    private javax.swing.JTextField fieldDesc;
    private javax.swing.JTextField fieldID;
    private javax.swing.JTextField fieldItem;
    private javax.swing.JTextField fieldName;
    private javax.swing.JTextField fieldQuantity;
    private javax.swing.JTextField fieldTimestamp;
    private javax.swing.JLabel imageDesc;
    private javax.swing.JLabel imageItem;
    private javax.swing.JLabel imageName;
    private javax.swing.JLabel imageQuantity;
    private javax.swing.JLabel imageStatus;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JLabel labelAdd;
    private javax.swing.JLabel labelCleaFilter;
    private javax.swing.JLabel labelClear;
    private javax.swing.JLabel labelDelete;
    private javax.swing.JLabel labelDesc;
    private javax.swing.JLabel labelFilterDesc;
    private javax.swing.JLabel labelFilterItem;
    private javax.swing.JLabel labelFilterName;
    private javax.swing.JLabel labelFilterQuantity;
    private javax.swing.JLabel labelFilterQuantityFrom;
    private javax.swing.JLabel labelFilterQuantityTo;
    private javax.swing.JLabel labelFilterStatus;
    private javax.swing.JLabel labelFormLocation;
    private javax.swing.JLabel labelFormLocation1;
    private javax.swing.JLabel labelItem;
    private javax.swing.JLabel labelName;
    private javax.swing.JLabel labelQuantity;
    private javax.swing.JLabel labelStatus;
    private javax.swing.JLabel labelUpdate;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelCRUD;
    private javax.swing.JPanel panelClearFilter;
    private javax.swing.JPanel panelFilter;
    private javax.swing.JPanel panelMain;
    private javax.swing.JPanel panelTab;
    private ProjectINSY.java.swing.ComboBoxSuggestion searchDesc;
    private ProjectINSY.java.swing.ComboBoxSuggestion searchItem;
    private ProjectINSY.java.swing.ComboBoxSuggestion searchName;
    private ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion searchQuantityEnd;
    private ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion searchQuantityStart;
    private ProjectINSY.java.swing.ComboBoxSuggestion searchStatus;
    private javax.swing.JPanel tabButtons;
    private javax.swing.JTabbedPane tabRequest;
    private ProjectINSY.java.swing.Table tableRequest;
    private javax.swing.JScrollPane tableScroll;
    // End of variables declaration//GEN-END:variables
}
