/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ProjectINSY.java.ui;

import ProjectINSY.java.Main;
import ProjectINSY.java.model.ItemPanel;
import ProjectINSY.java.swing.Form.FormField.FieldType;
import ProjectINSY.java.swing.TableHighlighter;
import ProjectINSY.java.util.DatabaseUtil;
import static ProjectINSY.java.util.DatabaseUtil.createHistoryDesc;
import static ProjectINSY.java.util.DatabaseUtil.getColumnValueByInt;
import static ProjectINSY.java.util.DatabaseUtil.insertHistory;
import static ProjectINSY.java.util.DatabaseUtil.getColumnValueByString;
import ProjectINSY.java.util.GuiUtil;
import ProjectINSY.java.util.GuiUtil.FieldFocus;
import static ProjectINSY.java.util.GuiUtil.getComboSelected;
import static ProjectINSY.java.util.GuiUtil.resetBtnEnability;
import static ProjectINSY.java.util.GuiUtil.setDefaultField;
import static ProjectINSY.java.util.GuiUtil.setScrollBarCustom;
import static ProjectINSY.java.util.GuiUtil.setTransparentFrame;
import ProjectINSY.java.util.MessageUtil;
import ProjectINSY.java.util.TableUtil;
import static ProjectINSY.java.util.MessageUtil.paneDatabaseError;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author admin
 */
public class ItemReport extends ItemPanel {

    TableHighlighter TableHighlighter = new TableHighlighter(3);

    private final String PLACEHOLDER_FULL_CODE = "Silang-00-000000";

    /**
     * Creates new form LogIn
     */
    public ItemReport() {
        initComponents();

        setScrollBarCustom(tableScroll);

        setTransparentFrame(ItemReport.this);
        setTransparentFrame(btnCreateReport, btnFilter, btnAdd, btnUpdate, btnClear, btnDelete, btnClearFilter);

        fieldID.setForm(null, FieldType.STRING);
        fieldTimestamp.setForm(null, FieldType.STRING);
        fieldCode.setForm(PLACEHOLDER_FULL_CODE, FieldType.STRING);
        
        fieldName.setForm(null, FieldType.STRING);
        fieldName.getDocument().addDocumentListener(new FieldChangeListener());

        tableRequest.setDefaultTable();
        tableRequest.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableRequest.getSelectedRow();
                if (selectedRow >= 0) {
                    selectTableRequest(selectedRow);
                }
            }
        });

        switchReportForm(btnCreateReport);
    }

    //<editor-fold defaultstate="collapsed" desc="Item Panel">
    @Override
    public void refreshItemTable() {
        filterWHERE = "";
        if (!searchName.isDefaultComboItem()) {
            filterWHERE += "AND s.stock_name = '" + getComboSelected(searchName) + "' ";
        }
        if (!searchDesc.isDefaultComboItem()) {
            filterWHERE += "AND s.stock_desc = '" + getComboSelected(searchDesc) + "' ";
        }
        if (!searchCondition.isDefaultComboItem()) {
            filterWHERE += "AND r.report_condition = '" + getComboSelected(searchCondition) + "' ";
        }

        currentSearchQuery = "SELECT r.report_code, s.stock_name, s.stock_desc, r.report_condition FROM "
                + Main.TB_ITEM_REPORT
                + " r JOIN "
                + Main.TB_ITEM_STOCK
                + " s ON r.report_code = s.stock_code "
                + " WHERE 1 "
                + filterWHERE
                + " ORDER BY report_timestamp DESC";

//        System.out.println(currentSearchQuery);
//        TableUtil.refreshTable(tableRequest, currentSearchQuery, TableUtil.TableEnum.ITEM_REPORT);

        for (int i = 0; i < tableRequest.getColumnCount(); i++) {
            tableRequest.getColumnModel().getColumn(i).setCellRenderer(TableHighlighter);
        }
    }

    @Override
    public void repopulateFilterComboBox() {
        disableUpdatingComboBoxes();
        searchName.repopulateComboBox("SELECT s.stock_name FROM "
                + Main.TB_ITEM_REPORT
                + " r JOIN "
                + Main.TB_ITEM_STOCK
                + " s ON r.report_code = s.stock_code");
        searchDesc.repopulateComboBox("SELECT s.stock_desc FROM "
                + Main.TB_ITEM_REPORT
                + " r JOIN "
                + Main.TB_ITEM_STOCK
                + " s ON r.report_code = s.stock_code");
        searchCondition.repopulateComboBox("SELECT report_condition FROM " + Main.TB_ITEM_REPORT);
        enableUpdatingComboBoxes();

        refreshItemTable();
    }

    @Override
    public void repopulateComboBox() {
    }
    //</editor-fold>

    public void selectTableRequest(int selectedRow) {
        String[] tableRow = TableUtil.selectTableRow(tableRequest, selectedRow);
        TableUtil.linkFieldsToTable(tableRow, fieldSelectedCode, fieldName, fieldDesc, comboCondition);

        String codeText = fieldSelectedCode.getText();
        fieldID.setText(getColumnValueByString(Main.TB_ITEM_REPORT, "report_id", "report_code", codeText));

        setUpdateDeleteEnableItem();
    }

    public void searchItem() {
        String codeText = fieldCode.getText();
        fieldSelectedCode.setText("None");

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            if (codeText.isEmpty() || !DatabaseUtil.recordExists(conn, Main.TB_ITEM_STOCK, "stock_code", codeText)) {
                return;
            }
        } catch (SQLException e) {
            paneDatabaseError(e);
        }

        String name = getColumnValueByString(Main.TB_ITEM_STOCK, "stock_name", "stock_code", codeText);
        String desc = getColumnValueByString(Main.TB_ITEM_STOCK, "stock_desc", "stock_code", codeText);

        fieldID.setText(getColumnValueByString(Main.TB_ITEM_STOCK, "stock_id", "stock_code", codeText));
        fieldSelectedCode.setText(codeText);

        fieldName.setText(name);
        fieldDesc.setText(desc);
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
            btnAdd.setEnabled(fieldName.isValidText());
        }
    }

    public void switchReportForm(JComponent com) {
        if (com == btnCreateReport) {
            tabRequest.setSelectedIndex(0);
            btnCreateReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnRequest_active.png")));
            btnFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnRequest.png")));

            setScannerFocus();
        } else {
            tabRequest.setSelectedIndex(1);
            btnFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnRequest_active.png")));
            btnCreateReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnRequest.png")));

            repopulateFilterComboBox();
        }
    }

    public void setScannerFocus() {
        fieldCode.requestFocusInWindow();
    }

    public void clearFields() {
        fieldID.resetToPlaceholder();
        fieldTimestamp.resetToPlaceholder();
        
        fieldCode.resetToPlaceholder();
        fieldSelectedCode.setText("None");
        
        GuiUtil.clearField(fieldName, "");
        GuiUtil.clearField(fieldDesc, "");
        comboCondition.clearComboBox();
        tableRequest.clearSelectedRow();

        fieldName.setForeground(Color.BLACK);
        fieldDesc.setForeground(Color.BLACK);
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

        fieldID = new ProjectINSY.java.swing.Form.FormField();
        fieldTimestamp = new ProjectINSY.java.swing.Form.FormField();
        panelMain = new javax.swing.JPanel();
        tableScroll = new javax.swing.JScrollPane();
        tableRequest = new ProjectINSY.java.swing.Table();
        panelTab = new javax.swing.JPanel();
        tabButtons = new javax.swing.JPanel();
        labelFormLocation = new javax.swing.JLabel();
        btnCreateReport = new javax.swing.JButton();
        labelFormLocation1 = new javax.swing.JLabel();
        btnFilter = new javax.swing.JButton();
        tabRequest = new javax.swing.JTabbedPane();
        panelBody = new javax.swing.JPanel();
        labelDesc = new javax.swing.JLabel();
        labelItem = new javax.swing.JLabel();
        labelCode = new javax.swing.JLabel();
        fieldCode = new ProjectINSY.java.swing.Form.FormField();
        imageCode = new javax.swing.JLabel();
        labelCondition = new javax.swing.JLabel();
        comboCondition = new ProjectINSY.java.swing.ComboBoxSuggestion();
        imageCondition = new javax.swing.JLabel();
        panelCRUD = new javax.swing.JPanel();
        labelAdd = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        labelUpdate = new javax.swing.JLabel();
        btnUpdate = new javax.swing.JButton();
        labelClear = new javax.swing.JLabel();
        btnClear = new javax.swing.JButton();
        labelDelete = new javax.swing.JLabel();
        btnDelete = new javax.swing.JButton();
        fieldName = new ProjectINSY.java.swing.Form.FormField();
        jSeparator6 = new javax.swing.JSeparator();
        fieldDesc = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        labelSelectedCode = new javax.swing.JLabel();
        fieldSelectedCode = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        labelScanInfo1 = new javax.swing.JLabel();
        labelScanInfo = new javax.swing.JLabel();
        panelFilter = new javax.swing.JPanel();
        labelFilterItem = new javax.swing.JLabel();
        searchName = new ProjectINSY.java.swing.ComboBoxSuggestion();
        jSeparator2 = new javax.swing.JSeparator();
        labelFilterDesc = new javax.swing.JLabel();
        searchDesc = new ProjectINSY.java.swing.ComboBoxSuggestion();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        labelFilterCondition = new javax.swing.JLabel();
        searchCondition = new ProjectINSY.java.swing.ComboBoxSuggestion();
        panelClearFilter = new javax.swing.JPanel();
        labelCleaFilter = new javax.swing.JLabel();
        btnClearFilter = new javax.swing.JButton();

        fieldID.setBorder(null);
        fieldID.setForeground(new java.awt.Color(0, 0, 0));
        fieldID.setText("formField");
        fieldID.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldID.setPlaceholderColor(new java.awt.Color(153, 153, 153));
        fieldID.setSelectionColor(new java.awt.Color(25, 102, 24));

        fieldTimestamp.setBorder(null);
        fieldTimestamp.setForeground(new java.awt.Color(0, 0, 0));
        fieldTimestamp.setText("formField");
        fieldTimestamp.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldTimestamp.setPlaceholderColor(new java.awt.Color(153, 153, 153));
        fieldTimestamp.setSelectionColor(new java.awt.Color(25, 102, 24));

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
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Code", "Item", "Description", "Condition"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

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
        labelFormLocation.setText("REPORT MANAGEMENT");
        labelFormLocation.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        tabButtons.add(labelFormLocation, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -1, 270, 40));

        btnCreateReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnRequest.png"))); // NOI18N
        btnCreateReport.setBorder(null);
        btnCreateReport.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCreateReport.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnRequest_active.png"))); // NOI18N
        btnCreateReport.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnRequest_active.png"))); // NOI18N
        btnCreateReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateReportActionPerformed(evt);
            }
        });
        tabButtons.add(btnCreateReport, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -4, -1, 50));

        labelFormLocation1.setFont(new java.awt.Font("Bebas", 0, 24)); // NOI18N
        labelFormLocation1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelFormLocation1.setText("REPORT FILTER");
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

        labelDesc.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelDesc.setText("Description");
        panelBody.add(labelDesc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 520, -1, -1));

        labelItem.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelItem.setText("Item Name");
        panelBody.add(labelItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, -1, -1));

        labelCode.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelCode.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCode.setText("Enter or Scan Code:");
        panelBody.add(labelCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 80, 240, -1));

        fieldCode.setBorder(null);
        fieldCode.setForeground(new java.awt.Color(0, 0, 0));
        fieldCode.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldCode.setText("formField");
        fieldCode.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        fieldCode.setPlaceholderColor(new java.awt.Color(153, 153, 153));
        fieldCode.setSelectionColor(new java.awt.Color(25, 102, 24));
        panelBody.add(fieldCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 130, 240, 30));

        imageCode.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldHalf_small.png"))); // NOI18N
        panelBody.add(imageCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, 260, -1));

        labelCondition.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelCondition.setText("Condition");
        panelBody.add(labelCondition, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, -1, -1));

        comboCondition.setBorder(null);
        comboCondition.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "DAMAGED", "IN MAINTENANCE", "LOST/STOLEN", "OBSOLETE", "UNSERVICABLE" }));
        comboCondition.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        panelBody.add(comboCondition, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 510, 30));

        imageCondition.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageCondition.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldLogIn.png"))); // NOI18N
        panelBody.add(imageCondition, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 330, 540, -1));

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

        panelBody.add(panelCRUD, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 660, 560, 110));

        fieldName.setBorder(null);
        fieldName.setForeground(new java.awt.Color(0, 0, 0));
        fieldName.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        fieldName.setText("formField");
        fieldName.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        fieldName.setPlaceholderColor(new java.awt.Color(153, 153, 153));
        fieldName.setSelectionColor(new java.awt.Color(25, 102, 24));
        panelBody.add(fieldName, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 440, 530, 30));
        panelBody.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 480, 530, 10));

        fieldDesc.setEditable(false);
        fieldDesc.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldDesc.setBorder(null);
        fieldDesc.setFocusable(false);
        fieldDesc.setSelectionColor(new java.awt.Color(25, 102, 24));
        panelBody.add(fieldDesc, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 560, 530, -1));
        panelBody.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 600, 530, 10));

        labelSelectedCode.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelSelectedCode.setText("Code: ");
        panelBody.add(labelSelectedCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 210, -1, -1));

        fieldSelectedCode.setEditable(false);
        fieldSelectedCode.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        fieldSelectedCode.setText("None");
        fieldSelectedCode.setBorder(null);
        fieldSelectedCode.setFocusable(false);
        fieldSelectedCode.setSelectionColor(new java.awt.Color(25, 102, 24));
        panelBody.add(fieldSelectedCode, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 210, 460, 30));

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnSearch.png"))); // NOI18N
        btnSearch.setBorder(null);
        btnSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearch.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnSearch_pressed.png"))); // NOI18N
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        panelBody.add(btnSearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 130, -1, -1));

        labelScanInfo1.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 18)); // NOI18N
        labelScanInfo1.setText("When scanning a new barcode, ensure");
        panelBody.add(labelScanInfo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));

        labelScanInfo.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 18)); // NOI18N
        labelScanInfo.setText("that the field below is selected and empty.");
        panelBody.add(labelScanInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, -1, -1));

        tabRequest.addTab("tab1", panelBody);

        panelFilter.setBackground(new java.awt.Color(255, 255, 255));

        labelFilterItem.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelFilterItem.setText("Item Name");

        searchName.setBorder(null);
        searchName.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        searchName.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                searchNameItemStateChanged(evt);
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

        labelFilterCondition.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelFilterCondition.setText("Condition");

        searchCondition.setBorder(null);
        searchCondition.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        searchCondition.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                searchConditionItemStateChanged(evt);
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
            .addComponent(panelClearFilter, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(panelFilterLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addComponent(labelFilterItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(labelFilterDesc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator3)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFilterLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(panelFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(labelFilterCondition, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 538, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelFilterLayout.createSequentialGroup()
                        .addGroup(panelFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(searchName, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFilterLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(searchCondition, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );
        panelFilterLayout.setVerticalGroup(
            panelFilterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFilterLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(labelFilterItem)
                .addGap(6, 6, 6)
                .addComponent(searchName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(labelFilterDesc)
                .addGap(6, 6, 6)
                .addComponent(searchDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(labelFilterCondition)
                .addGap(6, 6, 6)
                .addComponent(searchCondition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(panelClearFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(453, Short.MAX_VALUE))
        );

        tabRequest.addTab("tab2", panelFilter);

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

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        String report_code = fieldSelectedCode.getText();
        String report_name = fieldName.getText();
        String report_desc = fieldDesc.getText();
        String report_condition = comboCondition.getSelectedItem().toString();

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            if (DatabaseUtil.recordExists(conn, Main.TB_ITEM_REPORT, "report_code", report_code)) {
                return;
            }
            String query = "INSERT INTO " + Main.TB_ITEM_REPORT + " (report_code, report_condition)\n"
                    + "VALUES (?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, report_code);
            pst.setString(2, report_condition);

            // HISTORY : REPORT-ADD
            String history_desc = "";
            history_desc += createHistoryDesc(report_name, "Name");
            history_desc += createHistoryDesc(report_desc, "Description");
            history_desc += createHistoryDesc(report_condition, "Condition");

            pst.executeUpdate();

            insertHistory(DatabaseUtil.HistoryFrame.REPORT, DatabaseUtil.HistoryType.ADD, report_code, report_code, history_desc, "");

            JOptionPane.showMessageDialog(this, "Report Added!", "Success", JOptionPane.INFORMATION_MESSAGE);

            clearFields();
            refreshItemTable();
        } catch (SQLException e) {
            MessageUtil.paneDatabaseError(e);
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        int report_id = Integer.parseInt(fieldID.getText());
        String report_code = fieldSelectedCode.getText();
        String report_condition = comboCondition.getSelectedItem().toString();

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            String query = "UPDATE " + Main.TB_ITEM_REPORT + " SET report_condition = ? WHERE report_id = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, report_condition);
            pst.setInt(2, report_id);

            // HISTORY : REPORT-UPDATE
            String history_desc = "";

            String old_condition = getColumnValueByInt(Main.TB_ITEM_REPORT, "report_condition", "report_id", report_id);

            history_desc += createHistoryDesc(old_condition, report_condition, "Condition");

            insertHistory(DatabaseUtil.HistoryFrame.REPORT, DatabaseUtil.HistoryType.UPDATE, report_code, report_code, history_desc, "");

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Report Updated!", "Success", JOptionPane.INFORMATION_MESSAGE);

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
        int report_id = Integer.parseInt(fieldID.getText());
        String report_code = fieldSelectedCode.getText();
        String report_name = fieldName.getText();

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            int warnUser = JOptionPane.showConfirmDialog(
                    null,
                    "Confirm Delete?",
                    "Warning: Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (warnUser == JOptionPane.YES_OPTION) {
                String query = "DELETE FROM " + Main.TB_ITEM_REPORT + " WHERE report_id = ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setInt(1, report_id);

                // HISTORY : REQUEST-DELETE
                String history_desc = createHistoryDesc(report_name, "Name");

                insertHistory(DatabaseUtil.HistoryFrame.REPORT, DatabaseUtil.HistoryType.DELETE, report_code, report_code, history_desc, "");

                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Report Deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);

                clearFields();
                refreshItemTable();
            }
        } catch (SQLException e) {
            paneDatabaseError(e);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnClearFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearFilterActionPerformed
        searchName.setSelectedIndex(0);
        searchDesc.setSelectedIndex(0);
        searchCondition.setSelectedIndex(0);
    }//GEN-LAST:event_btnClearFilterActionPerformed

    private void searchNameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_searchNameItemStateChanged
        if (isUpdatingComboBoxes) {
            return;
        }

        disableUpdatingComboBoxes();
        searchDesc.repopulateAssociatedComboBox(searchName, "stock_name", "SELECT s.stock_desc FROM "
                + Main.TB_ITEM_REPORT
                + " r JOIN "
                + Main.TB_ITEM_STOCK
                + " s ON r.report_code = s.stock_code");
        searchCondition.repopulateAssociatedComboBox(searchName, "stock_name", "SELECT r.report_condition FROM "
                + Main.TB_ITEM_REPORT
                + " r JOIN "
                + Main.TB_ITEM_STOCK
                + " s ON r.report_code = s.stock_code");
        enableUpdatingComboBoxes();

        refreshItemTable();
    }//GEN-LAST:event_searchNameItemStateChanged

    private void searchDescItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_searchDescItemStateChanged
        if (isUpdatingComboBoxes) {
            return;
        }

        refreshItemTable();
    }//GEN-LAST:event_searchDescItemStateChanged

    private void searchConditionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_searchConditionItemStateChanged
        if (isUpdatingComboBoxes) {
            return;
        }

        refreshItemTable();
    }//GEN-LAST:event_searchConditionItemStateChanged

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        searchItem();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterActionPerformed
        switchReportForm(btnFilter);
    }//GEN-LAST:event_btnFilterActionPerformed

    private void btnCreateReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateReportActionPerformed
        switchReportForm(btnCreateReport);
    }//GEN-LAST:event_btnCreateReportActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnClearFilter;
    private javax.swing.JButton btnCreateReport;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFilter;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdate;
    private ProjectINSY.java.swing.ComboBoxSuggestion comboCondition;
    private ProjectINSY.java.swing.Form.FormField fieldCode;
    private javax.swing.JTextField fieldDesc;
    private ProjectINSY.java.swing.Form.FormField fieldID;
    private ProjectINSY.java.swing.Form.FormField fieldName;
    private javax.swing.JTextField fieldSelectedCode;
    private ProjectINSY.java.swing.Form.FormField fieldTimestamp;
    private javax.swing.JLabel imageCode;
    private javax.swing.JLabel imageCondition;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JLabel labelAdd;
    private javax.swing.JLabel labelCleaFilter;
    private javax.swing.JLabel labelClear;
    private javax.swing.JLabel labelCode;
    private javax.swing.JLabel labelCondition;
    private javax.swing.JLabel labelDelete;
    private javax.swing.JLabel labelDesc;
    private javax.swing.JLabel labelFilterCondition;
    private javax.swing.JLabel labelFilterDesc;
    private javax.swing.JLabel labelFilterItem;
    private javax.swing.JLabel labelFormLocation;
    private javax.swing.JLabel labelFormLocation1;
    private javax.swing.JLabel labelItem;
    private javax.swing.JLabel labelScanInfo;
    private javax.swing.JLabel labelScanInfo1;
    private javax.swing.JLabel labelSelectedCode;
    private javax.swing.JLabel labelUpdate;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelCRUD;
    private javax.swing.JPanel panelClearFilter;
    private javax.swing.JPanel panelFilter;
    private javax.swing.JPanel panelMain;
    private javax.swing.JPanel panelTab;
    private ProjectINSY.java.swing.ComboBoxSuggestion searchCondition;
    private ProjectINSY.java.swing.ComboBoxSuggestion searchDesc;
    private ProjectINSY.java.swing.ComboBoxSuggestion searchName;
    private javax.swing.JPanel tabButtons;
    private javax.swing.JTabbedPane tabRequest;
    private ProjectINSY.java.swing.Table tableRequest;
    private javax.swing.JScrollPane tableScroll;
    // End of variables declaration//GEN-END:variables
}
