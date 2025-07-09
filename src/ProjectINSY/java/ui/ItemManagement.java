/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ProjectINSY.java.ui;

import ProjectINSY.java.Main;
import ProjectINSY.java.model.ItemPanel;
import ProjectINSY.java.swing.Form.FormField.FieldType;
import ProjectINSY.java.swing.Table.EnumAlignment;
import ProjectINSY.java.util.BarcodeUtil;
import static ProjectINSY.java.util.BarcodeUtil.validateBarcode;
import ProjectINSY.java.util.DatabaseUtil;
import static ProjectINSY.java.util.DatabaseUtil.createHistoryDesc;
import static ProjectINSY.java.util.DatabaseUtil.generateNewBatch;
import static ProjectINSY.java.util.DatabaseUtil.getColumnFromLastRow;
import static ProjectINSY.java.util.DatabaseUtil.getColumnValueByInt;
import static ProjectINSY.java.util.DatabaseUtil.getConnection;
import static ProjectINSY.java.util.DatabaseUtil.insertHistory;
import ProjectINSY.java.util.GuiUtil;
import ProjectINSY.java.util.GuiUtil.FieldFocus;
import static ProjectINSY.java.util.GuiUtil.cleanSpaces;
import static ProjectINSY.java.util.GuiUtil.enforceDigits;
import static ProjectINSY.java.util.GuiUtil.resetBtnEnability;
import static ProjectINSY.java.util.GuiUtil.setDefaultField;
import static ProjectINSY.java.util.GuiUtil.setTransparentFrame;
import ProjectINSY.java.util.MessageUtil;
import static ProjectINSY.java.util.MessageUtil.paneDatabaseError;
import ProjectINSY.java.util.TableUtil;
import static ProjectINSY.java.util.TableUtil.floatFormatDecimal;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import static ProjectINSY.java.util.GuiUtil.setScrollBarCustom;
import static ProjectINSY.java.util.GuiUtil.fieldHasValue;
import static ProjectINSY.java.util.TableUtil.floatRoundOff;
import static ProjectINSY.java.util.GuiUtil.getComboSelected;
import static ProjectINSY.java.util.GuiUtil.getFieldString;
import com.itextpdf.kernel.geom.PageSize;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JTable;

/**
 *
 * @author admin
 */
public class ItemManagement extends ItemPanel {

    private String current_barcode = null;
    private ImageIcon barcodeIcon;
    private int batchQuantity = -1;

    /**
     * Creates new form LogIn
     */
    public ItemManagement() {
        initComponents();

        setScrollBarCustom(tableScroll);
        setScrollBarCustom(scrollMain);

        setTransparentFrame(ItemManagement.this, fieldBenefactor, fieldPrice, fieldDOD, fieldQuantity, fieldBenefactor);
        setTransparentFrame(btnDOD, btnAdd, btnUpdate, btnClear, btnDelete, btnClearFilter);

        tableInventory.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableInventory.setColumnWidth(new int[]{250, 250, 350, 175, 75, 150, 242});

        fieldCode.setForm(PLACEHOLDER_ID_CODE, FieldType.INT);
        fieldDesc.setForm(PLACEHOLDER_DESC, FieldType.STRING);
        fieldPrice.setForm(PLACEHOLDER_PRICE, FieldType.FLOAT);
        fieldQuantity.setForm(PLACEHOLDER_QTY, FieldType.INT);
        fieldDOD.setForm(PLACEHOLDER_DOD, FieldType.DATE);
        fieldBenefactor.setForm(PLACEHOLDER_BENEFACTOR, FieldType.STRING);
        
        fieldID.setForm(null, FieldType.INT);
        fieldID2.setForm(null, FieldType.INT);
        fieldQuantitySelected.setForm(null, FieldType.INT);

        fieldPrice.getDocument().addDocumentListener(new FieldChangeListener());
        fieldQuantity.getDocument().addDocumentListener(new FieldChangeListener());
        fieldBenefactor.getDocument().addDocumentListener(new FieldChangeListener());
        searchDateStart.getDocument().addDocumentListener(new FieldChangeListener());
        searchDateEnd.getDocument().addDocumentListener(new FieldChangeListener());
        tableInventory.setDefaultTable();
        tableInventory.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableInventory.getSelectedRow();
                if (selectedRow >= 0) {
                    selectTableStock(selectedRow);
                }
            }
        });

        tableInventory.setColumnHorizontalAligment(3, EnumAlignment.LEFT);
        floatFormatDecimal(tableInventory, 3);
        tableInventory.setIntegerColumn(4);
        searchDateStart.setText(Main.filterMinDate);
//        sorterNumbers(tableInventory, 3);
//        sorterNumbers(tableInventory, 4);
    }

    //<editor-fold defaultstate="collapsed" desc="Item Panel">
    @Override
    public void refreshItemTable() {
        filterWHERE = " ";
        if (!searchCategory.isDefaultComboItem()) {
            filterWHERE += "AND stock_category = '" + getComboSelected(searchCategory) + "' ";
        }
        if (!searchName.isDefaultComboItem()) {
            filterWHERE += "AND stock_name = '" + getComboSelected(searchName) + "' ";
        }
        if (!searchDesc.isDefaultComboItem()) {
            filterWHERE += "AND stock_desc = '" + getComboSelected(searchDesc) + "' ";
        }
        if (!searchBenefactor.isDefaultComboItem()) {
            filterWHERE += "AND stock_benefactor = '" + getComboSelected(searchBenefactor) + "' ";
        }
        if (fieldHasValue(searchDateStart)) {
            filterWHERE += "AND stock_dod >= '" + getFieldString(searchDateStart) + "' ";
        }
        if (fieldHasValue(searchDateEnd)) {
            filterWHERE += "AND stock_dod <= '" + getFieldString(searchDateEnd) + "' ";
        }

        filterHAVING = " ";
        if (fieldHasValue(searchPriceStart)) {
            filterHAVING += "AND stock_price >= '" + getFieldString(searchPriceStart) + "' ";
        }
        if (fieldHasValue(searchPriceEnd)) {
            filterHAVING += "AND stock_price <= '" + getFieldString(searchPriceEnd) + "' ";
        }
        if (fieldHasValue(searchQuantityStart)) {
            filterHAVING += "AND stock_quantity >= '" + getFieldString(searchQuantityStart) + "' ";
        }
        if (fieldHasValue(searchQuantityEnd)) {
            filterHAVING += "AND stock_quantity <= '" + getFieldString(searchQuantityEnd) + "' ";
        }

        if (radioBatches.isSelected()) {
            currentSearchQuery = "SELECT stock_timestamp, stock_id, stock_batch, "
                    + "stock_category, "
                    + "stock_name, "
                    + "stock_desc, "
                    + "stock_price, "
                    + "(stock_price * COUNT(*)) AS stock_price_batch, "
                    + "stock_dod, "
                    + "stock_benefactor, "
                    + "CONCAT( "
                    + "    SUBSTRING_INDEX(MIN(stock_code), '-', 1), '-', "
                    + "    SUBSTRING_INDEX(MIN(stock_code), '-', 2), '-', "
                    + "    RIGHT(MIN(stock_code), LOCATE('-', REVERSE(MIN(stock_code))) - 1), "
                    + "    CASE "
                    + "        WHEN MIN(stock_code) = MAX(stock_code) THEN '' "
                    + "        ELSE CONCAT('-', RIGHT(MAX(stock_code), LOCATE('-', REVERSE(MAX(stock_code))) - 1)) "
                    + "    END "
                    + ") AS stock_code, "
                    + "COUNT(*) AS stock_quantity "
                    + "FROM " + Main.TB_ITEM_STOCK + " "
                    + "WHERE 1 "
                    + filterWHERE
                    + "GROUP BY stock_batch "
                    + "HAVING 1 "
                    + filterHAVING
                    + "ORDER BY stock_timestamp DESC";
        } else {
            currentSearchQuery = "SELECT *, 1 AS stock_quantity FROM "
                    + Main.TB_ITEM_STOCK + " "
                    + "WHERE 1 "
                    + filterWHERE
                    + "HAVING 1 "
                    + filterHAVING
                    + " ORDER BY stock_timestamp DESC";
        }

        currentSearchQuery = cleanSpaces(currentSearchQuery);
        TableUtil.refreshTable(tableInventory, currentSearchQuery, TableUtil.TableEnum.STOCK_DELIVERY);

        String query = "SELECT DISTINCT stock_benefactor FROM " + Main.TB_ITEM_STOCK;

        fieldBenefactor.clearItemSuggestion();
        try (Connection conn = getConnection(Main.DB_NAME); PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                fieldBenefactor.addItemSuggestion(rs.getString("stock_benefactor"));
            }
        } catch (SQLException e) {
            paneDatabaseError(e);
        }
    }

    @Override
    public void repopulateFilterComboBox() {
        disableUpdatingComboBoxes();
        searchCategory.repopulateComboBox("SELECT stock_category FROM " + Main.TB_ITEM_STOCK);
        searchName.repopulateComboBox("SELECT stock_name FROM " + Main.TB_ITEM_STOCK);
        searchDesc.repopulateComboBox("SELECT stock_desc FROM " + Main.TB_ITEM_STOCK);
        searchBenefactor.repopulateComboBox("SELECT stock_benefactor FROM " + Main.TB_ITEM_STOCK);
        enableUpdatingComboBoxes();

        refreshItemTable();
    }

    @Override
    public void repopulateComboBox() {
        comboName.repopulateComboBox("SELECT item_name FROM " + Main.TB_CATALOG_ITEM);
        comboRequest.repopulateComboBox("SELECT request_timestamp FROM " + Main.TB_ITEM_REQUEST + " WHERE request_status = 'PENDING'");

        for (int i = 1; i < comboRequest.getItemCount(); i++) {
            String request_timestamp = comboRequest.getItemAt(i).toString();
            String request_name = DatabaseUtil.getColumnValueByString(Main.TB_ITEM_REQUEST, "request_name", "request_timestamp", request_timestamp);
            String request_item = DatabaseUtil.getColumnValueByString(Main.TB_ITEM_REQUEST, "request_item", "request_timestamp", request_timestamp);
            String request_desc = DatabaseUtil.getColumnValueByString(Main.TB_ITEM_REQUEST, "request_desc", "request_timestamp", request_timestamp);
            if (request_desc.isEmpty()) {
                request_desc = "No Desc";
            }
            String request_quantity = DatabaseUtil.getColumnValueByString(Main.TB_ITEM_REQUEST, "request_quantity", "request_timestamp", request_timestamp);
            comboRequest.removeItemAt(i);
            comboRequest.insertItemAt(request_timestamp + "  :  " + request_name + "  :  " + request_item + "  :  " + request_desc + "  :  " + request_quantity, i);
        }
    }
    //</editor-fold>

    public void selectTableStock(int selectedRow) {
        String[] tableRow = TableUtil.selectTableRow(tableInventory, selectedRow);
        TableUtil.linkFieldsToTable(tableRow, fieldID, comboName, fieldDesc, fieldPrice, fieldQuantitySelected, fieldDOD, fieldBenefactor);

        batchQuantity = Integer.parseInt(fieldQuantitySelected.getText());
        Float actual_price;
        if (isGroupedByBatches()) {
            String[] parts = fieldPrice.getText().split(" / ");
            actual_price = Float.valueOf(parts[0]);
        } else {
            actual_price = Float.valueOf(fieldPrice.getText());
        }
        fieldQuantity.resetToPlaceholder();
//        setDefaultField(fieldQuantity, PLACEHOLDER_QTY, FieldFocus.LOST, Color.BLACK);

        fieldPrice.setText(floatRoundOff(actual_price));

        current_barcode = validateBarcode(fieldID.getText());
        barcodeIcon = BarcodeUtil.generateBarcode(current_barcode);
        imgBarcode.setIcon(barcodeIcon);

        if (fieldBenefactor.getText().isEmpty()) {
            setDefaultField(fieldBenefactor, PLACEHOLDER_DESC, FieldFocus.LOST, Color.BLACK);
        }

        if (fieldID.getText().contains("-")) {
            String[] parts = fieldID.getText().split("-");
            fieldID.setText(parts[2]);
            if (parts.length == 4) {
                fieldID2.setText(parts[3]);
            } else {
                fieldID2.setText(parts[2]);
            }
        }
        setUpdateDeleteEnable();
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
            btnAdd.setEnabled(fieldPrice.isValidText() && !fieldQuantity.getText().trim().isEmpty() && fieldBenefactor.isValidText());
            if (!fieldID2.getText().isEmpty()) {
                btnUpdate.setEnabled(fieldQuantity.isPlaceholder());
            }

            String date_filter = searchDateEnd.getText();
            if (date_filter.matches(Main.validDatePattern)) {
                try {
                    LocalDate.parse(date_filter);
                } catch (DateTimeParseException e) {
                }
            }
            date_filter = searchDateStart.getText();
            if (date_filter.matches(Main.validDatePattern)) {
                try {
                    LocalDate.parse(date_filter);
                } catch (DateTimeParseException e) {
                }
            }
            refreshItemTable();
        }
    }

    public void setUpdateDeleteEnable() {
        resetBtnEnability(fieldID, btnUpdate, btnDelete);
    }

    public void clearFields() {
        current_barcode = null;
        barcodeIcon = null;
        batchQuantity = -1;
        GuiUtil.resetIcon(imgBarcode);

        fieldID.resetToPlaceholder();
        fieldID2.resetToPlaceholder();
        fieldCode.resetToPlaceholder();
        comboName.clearComboBox();
        
        fieldDesc.resetToPlaceholder();
        fieldPrice.resetToPlaceholder();
        fieldQuantity.resetToPlaceholder();
        fieldQuantitySelected.resetToPlaceholder();
        fieldBenefactor.resetToPlaceholder();
        fieldDOD.resetToPlaceholder();
        
        comboRequest.clearComboBox();
        tableInventory.clearSelectedRow();
        setUpdateDeleteEnable();
    }

    public static boolean isGroupedByBatches() {
        return radioBatches.isSelected();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateDOD = new ProjectINSY.java.swing.Date.DateChooser();
        dateStart = new ProjectINSY.java.swing.Date.DateChooser();
        dateEnd = new ProjectINSY.java.swing.Date.DateChooser();
        btnExport = new javax.swing.JButton();
        fieldID = new ProjectINSY.java.swing.Form.FormField();
        fieldID2 = new ProjectINSY.java.swing.Form.FormField();
        fieldQuantitySelected = new ProjectINSY.java.swing.Form.FormField();
        panelMain = new javax.swing.JPanel();
        scrollMain = new javax.swing.JScrollPane();
        panelBody = new javax.swing.JPanel();
        panelFields = new javax.swing.JPanel();
        panelCode = new javax.swing.JPanel();
        labelCode = new javax.swing.JLabel();
        fieldCode = new ProjectINSY.java.swing.Form.FormField();
        imageCode = new javax.swing.JLabel();
        infoCode1 = new javax.swing.JLabel();
        infoCode = new javax.swing.JLabel();
        panelInformation = new javax.swing.JPanel();
        labelName = new javax.swing.JLabel();
        comboName = new ProjectINSY.java.swing.ComboBoxSuggestion();
        imageName = new javax.swing.JLabel();
        labelDesc = new javax.swing.JLabel();
        fieldDesc = new ProjectINSY.java.swing.Form.FormField();
        imageDesc = new javax.swing.JLabel();
        labelPrice = new javax.swing.JLabel();
        fieldPrice = new ProjectINSY.java.swing.Form.FormField();
        imagePrice = new javax.swing.JLabel();
        labelDOD = new javax.swing.JLabel();
        fieldDOD = new ProjectINSY.java.swing.Form.FormField();
        btnDOD = new javax.swing.JButton();
        imageDOD = new javax.swing.JLabel();
        fieldQuantity = new ProjectINSY.java.swing.Form.FormField();
        labelQuantity = new javax.swing.JLabel();
        imageQuantity = new javax.swing.JLabel();
        labelBenefactor = new javax.swing.JLabel();
        fieldBenefactor = new ProjectINSY.java.swing.Form.FormFieldSuggestion();
        imageBenefactor = new javax.swing.JLabel();
        panelBarcode = new javax.swing.JPanel();
        imgBarcode = new javax.swing.JLabel();
        labelPrint = new javax.swing.JLabel();
        btnPrint = new javax.swing.JButton();
        panelCRUD = new javax.swing.JPanel();
        labelAdd = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        labelUpdate = new javax.swing.JLabel();
        btnUpdate = new javax.swing.JButton();
        labelClear = new javax.swing.JLabel();
        btnClear = new javax.swing.JButton();
        labelDelete = new javax.swing.JLabel();
        btnDelete = new javax.swing.JButton();
        comboRequest = new ProjectINSY.java.swing.ComboBoxSuggestion();
        imageRequest = new javax.swing.JLabel();
        labelRequest = new javax.swing.JLabel();
        tableScroll = new javax.swing.JScrollPane();
        tableInventory = new ProjectINSY.java.swing.Table();
        panelFilters = new javax.swing.JPanel();
        panelFilterTitle = new javax.swing.JPanel();
        labelFilterTitle = new javax.swing.JLabel();
        labelFilterCategory = new javax.swing.JLabel();
        searchCategory = new ProjectINSY.java.swing.ComboBoxSuggestion();
        labelFilterName = new javax.swing.JLabel();
        searchName = new ProjectINSY.java.swing.ComboBoxSuggestion();
        labelFilterDesc = new javax.swing.JLabel();
        searchDesc = new ProjectINSY.java.swing.ComboBoxSuggestion();
        labelFilterPrice = new javax.swing.JLabel();
        labelFilterPriceFrom = new javax.swing.JLabel();
        searchPriceStart = new ProjectINSY.java.swing.Form.FormFieldSuggestion();
        labelFilterPriceTo = new javax.swing.JLabel();
        searchPriceEnd = new ProjectINSY.java.swing.Form.FormFieldSuggestion();
        labelFilterQuantity = new javax.swing.JLabel();
        labelFilterQuantityFrom = new javax.swing.JLabel();
        searchQuantityStart = new ProjectINSY.java.swing.Form.FormFieldSuggestion();
        labelFilterQuantityTo = new javax.swing.JLabel();
        searchQuantityEnd = new ProjectINSY.java.swing.Form.FormFieldSuggestion();
        labelFilterBenefactor = new javax.swing.JLabel();
        searchBenefactor = new ProjectINSY.java.swing.ComboBoxSuggestion();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        labelFilterDateFrom = new javax.swing.JLabel();
        searchDateStart = new ProjectINSY.java.swing.Form.FormFieldSuggestion();
        labelFilterDate = new javax.swing.JLabel();
        labelFilterDateTo = new javax.swing.JLabel();
        searchDateEnd = new ProjectINSY.java.swing.Form.FormFieldSuggestion();
        jSeparator6 = new javax.swing.JSeparator();
        radioBatches = new ProjectINSY.java.swing.RadioButtonCustom();
        labelCleaFilter = new javax.swing.JLabel();
        btnClearFilter = new javax.swing.JButton();

        dateDOD.setForeground(new java.awt.Color(25, 102, 24));
        dateDOD.setDateFormat("yyyy-MM-dd");
        dateDOD.setTextRefernce(fieldDOD);

        dateStart.setForeground(new java.awt.Color(25, 102, 24));
        dateStart.setDateFormat("yyyy-MM-dd");
        dateStart.setTextRefernce(searchDateStart);

        dateEnd.setForeground(new java.awt.Color(25, 102, 24));
        dateEnd.setDateFormat("yyyy-MM-dd");
        dateEnd.setTextRefernce(searchDateEnd);

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

        fieldID2.setBorder(null);
        fieldID2.setForeground(new java.awt.Color(0, 0, 0));
        fieldID2.setText("formField");
        fieldID2.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldID2.setPlaceholderColor(new java.awt.Color(153, 153, 153));
        fieldID2.setSelectionColor(new java.awt.Color(25, 102, 24));

        fieldQuantitySelected.setBorder(null);
        fieldQuantitySelected.setForeground(new java.awt.Color(0, 0, 0));
        fieldQuantitySelected.setText("formField");
        fieldQuantitySelected.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldQuantitySelected.setPlaceholderColor(new java.awt.Color(153, 153, 153));
        fieldQuantitySelected.setSelectionColor(new java.awt.Color(25, 102, 24));

        setMaximumSize(new java.awt.Dimension(1840, 900));
        setMinimumSize(new java.awt.Dimension(1840, 900));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(1840, 900));

        panelMain.setBackground(new java.awt.Color(255, 255, 255));
        panelMain.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 102, 24), 2));

        scrollMain.setBackground(new java.awt.Color(255, 255, 255));
        scrollMain.setBorder(null);

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setPreferredSize(new java.awt.Dimension(1825, 1298));

        panelFields.setBackground(new java.awt.Color(255, 255, 255));
        panelFields.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 102, 24), 2));
        panelFields.setLayout(null);

        panelCode.setBackground(new java.awt.Color(255, 255, 255));
        panelCode.setLayout(null);

        labelCode.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelCode.setText("Custom Code (Silang-YY-XXXX) - Optional");
        panelCode.add(labelCode);
        labelCode.setBounds(0, 0, 670, 60);

        fieldCode.setBorder(null);
        fieldCode.setForeground(new java.awt.Color(0, 0, 0));
        fieldCode.setText("formField");
        fieldCode.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldCode.setPlaceholderColor(new java.awt.Color(153, 153, 153));
        fieldCode.setSelectionColor(new java.awt.Color(25, 102, 24));
        panelCode.add(fieldCode);
        fieldCode.setBounds(730, 20, 310, 30);

        imageCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldHalf.png"))); // NOI18N
        panelCode.add(imageCode);
        imageCode.setBounds(720, 0, 333, 70);

        infoCode1.setFont(new java.awt.Font("Bahnschrift", 2, 18)); // NOI18N
        infoCode1.setText("Only works for");
        panelCode.add(infoCode1);
        infoCode1.setBounds(1060, 10, 130, 30);

        infoCode.setFont(new java.awt.Font("Bahnschrift", 2, 18)); // NOI18N
        infoCode.setText("'Adding' Stocks");
        panelCode.add(infoCode);
        infoCode.setBounds(1060, 37, 130, 23);

        panelFields.add(panelCode);
        panelCode.setBounds(10, 10, 1610, 70);

        panelInformation.setBackground(new java.awt.Color(255, 255, 255));
        panelInformation.setLayout(null);

        labelName.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelName.setText("Name");
        panelInformation.add(labelName);
        labelName.setBounds(0, 0, 100, 60);

        comboName.setBorder(null);
        comboName.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        panelInformation.add(comboName);
        comboName.setBounds(150, 10, 640, 50);

        imageName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldFull.png"))); // NOI18N
        panelInformation.add(imageName);
        imageName.setBounds(140, 0, 665, 70);

        labelDesc.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelDesc.setText("Description");
        panelInformation.add(labelDesc);
        labelDesc.setBounds(910, 0, 180, 60);

        fieldDesc.setBorder(null);
        fieldDesc.setForeground(new java.awt.Color(0, 0, 0));
        fieldDesc.setText("formField");
        fieldDesc.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldDesc.setPlaceholderColor(new java.awt.Color(153, 153, 153));
        fieldDesc.setSelectionColor(new java.awt.Color(25, 102, 24));
        panelInformation.add(fieldDesc);
        fieldDesc.setBounds(1130, 20, 640, 30);

        imageDesc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldFull.png"))); // NOI18N
        panelInformation.add(imageDesc);
        imageDesc.setBounds(1120, 0, 665, 70);

        labelPrice.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelPrice.setText("Price");
        panelInformation.add(labelPrice);
        labelPrice.setBounds(0, 80, 90, 60);

        fieldPrice.setBorder(null);
        fieldPrice.setForeground(new java.awt.Color(0, 0, 0));
        fieldPrice.setText("formField");
        fieldPrice.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldPrice.setPlaceholderColor(new java.awt.Color(153, 153, 153));
        fieldPrice.setSelectionColor(new java.awt.Color(25, 102, 24));
        panelInformation.add(fieldPrice);
        fieldPrice.setBounds(10, 160, 310, 30);

        imagePrice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldHalf.png"))); // NOI18N
        panelInformation.add(imagePrice);
        imagePrice.setBounds(0, 140, 333, 70);

        labelDOD.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelDOD.setText("Delivery Date");
        panelInformation.add(labelDOD);
        labelDOD.setBounds(470, 80, 210, 60);

        fieldDOD.setBorder(null);
        fieldDOD.setForeground(new java.awt.Color(0, 0, 0));
        fieldDOD.setText("formField");
        fieldDOD.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldDOD.setPlaceholderColor(new java.awt.Color(153, 153, 153));
        fieldDOD.setSelectionColor(new java.awt.Color(25, 102, 24));
        panelInformation.add(fieldDOD);
        fieldDOD.setBounds(480, 160, 270, 30);

        btnDOD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnDateSelect.png"))); // NOI18N
        btnDOD.setBorder(null);
        btnDOD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDODActionPerformed(evt);
            }
        });
        panelInformation.add(btnDOD);
        btnDOD.setBounds(760, 150, 30, 50);

        imageDOD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldHalf.png"))); // NOI18N
        panelInformation.add(imageDOD);
        imageDOD.setBounds(470, 140, 333, 70);

        fieldQuantity.setBorder(null);
        fieldQuantity.setForeground(new java.awt.Color(0, 0, 0));
        fieldQuantity.setText("formField");
        fieldQuantity.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldQuantity.setPlaceholderColor(new java.awt.Color(153, 153, 153));
        fieldQuantity.setSelectionColor(new java.awt.Color(25, 102, 24));
        panelInformation.add(fieldQuantity);
        fieldQuantity.setBounds(980, 160, 310, 30);

        labelQuantity.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelQuantity.setText("Quantity");
        panelInformation.add(labelQuantity);
        labelQuantity.setBounds(970, 90, 173, 50);

        imageQuantity.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldHalf.png"))); // NOI18N
        panelInformation.add(imageQuantity);
        imageQuantity.setBounds(970, 140, 333, 70);

        labelBenefactor.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelBenefactor.setText("Benefactor");
        panelInformation.add(labelBenefactor);
        labelBenefactor.setBounds(1450, 90, 173, 50);

        fieldBenefactor.setBorder(null);
        fieldBenefactor.setForeground(new java.awt.Color(153, 153, 153));
        fieldBenefactor.setText("Enter Benefactor");
        fieldBenefactor.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        panelInformation.add(fieldBenefactor);
        fieldBenefactor.setBounds(1460, 150, 310, 50);

        imageBenefactor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldHalf.png"))); // NOI18N
        panelInformation.add(imageBenefactor);
        imageBenefactor.setBounds(1450, 140, 340, 70);

        panelFields.add(panelInformation);
        panelInformation.setBounds(10, 120, 1790, 210);

        panelBarcode.setBackground(new java.awt.Color(255, 255, 255));
        panelBarcode.setLayout(null);

        imgBarcode.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        panelBarcode.add(imgBarcode);
        imgBarcode.setBounds(0, 0, 200, 80);

        labelPrint.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelPrint.setForeground(new java.awt.Color(255, 255, 255));
        labelPrint.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/iconPrint.png"))); // NOI18N
        labelPrint.setText("Print");
        labelPrint.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelBarcode.add(labelPrint);
        labelPrint.setBounds(230, 10, 150, 50);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnPrint.png"))); // NOI18N
        btnPrint.setBorder(null);
        btnPrint.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPrint.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnPrint_pressed.png"))); // NOI18N
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        panelBarcode.add(btnPrint);
        btnPrint.setBounds(230, 10, 150, 50);

        panelFields.add(panelBarcode);
        panelBarcode.setBounds(1410, 10, 390, 80);

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

        labelUpdate.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelUpdate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelUpdate.setText("Update");
        labelUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelCRUD.add(labelUpdate);
        labelUpdate.setBounds(500, 10, 290, 30);

        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnLong.png"))); // NOI18N
        btnUpdate.setBorder(null);
        btnUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdate.setEnabled(false);
        btnUpdate.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnLong_pressed.png"))); // NOI18N
        btnUpdate.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnLong_pressed.png"))); // NOI18N
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        panelCRUD.add(btnUpdate);
        btnUpdate.setBounds(490, 0, 310, 50);

        labelClear.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelClear.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelClear.setText("Clear");
        labelClear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelCRUD.add(labelClear);
        labelClear.setBounds(1010, 10, 290, 30);

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
        btnClear.setBounds(1000, 0, 310, 50);

        labelDelete.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelDelete.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelDelete.setText("Delete");
        labelDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelCRUD.add(labelDelete);
        labelDelete.setBounds(1490, 10, 290, 30);

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnLong_red.png"))); // NOI18N
        btnDelete.setBorder(null);
        btnDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDelete.setEnabled(false);
        btnDelete.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnLong_red_pressed.png"))); // NOI18N
        btnDelete.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnLong_red_pressed.png"))); // NOI18N
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        panelCRUD.add(btnDelete);
        btnDelete.setBounds(1480, 0, 310, 50);

        panelFields.add(panelCRUD);
        panelCRUD.setBounds(10, 460, 1790, 50);

        comboRequest.setBorder(null);
        comboRequest.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        panelFields.add(comboRequest);
        comboRequest.setBounds(380, 380, 1400, 50);

        imageRequest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldRequest.png"))); // NOI18N
        panelFields.add(imageRequest);
        imageRequest.setBounds(370, 370, 1430, 70);

        labelRequest.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelRequest.setText("Associated Request");
        panelFields.add(labelRequest);
        labelRequest.setBounds(10, 370, 310, 60);

        tableScroll.setBorder(null);

        tableInventory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "", "Name", "Description", "Unit Price / Total Price", "Quantity", "Delivery Date", "Benefactor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Float.class, java.lang.Float.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tableInventory.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        tableInventory.setGridColor(new java.awt.Color(255, 255, 255));
        tableInventory.setSelectionBackground(new java.awt.Color(25, 102, 24));
        tableScroll.setViewportView(tableInventory);

        panelFilters.setBackground(new java.awt.Color(255, 255, 255));
        panelFilters.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 102, 24), 2));
        panelFilters.setLayout(null);

        panelFilterTitle.setBackground(new java.awt.Color(25, 102, 24));

        labelFilterTitle.setBackground(new java.awt.Color(25, 102, 24));
        labelFilterTitle.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        labelFilterTitle.setForeground(new java.awt.Color(255, 255, 255));
        labelFilterTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelFilterTitle.setText("Item Filters");
        labelFilterTitle.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout panelFilterTitleLayout = new javax.swing.GroupLayout(panelFilterTitle);
        panelFilterTitle.setLayout(panelFilterTitleLayout);
        panelFilterTitleLayout.setHorizontalGroup(
            panelFilterTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(labelFilterTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelFilterTitleLayout.setVerticalGroup(
            panelFilterTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFilterTitleLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelFilterTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelFilters.add(panelFilterTitle);
        panelFilterTitle.setBounds(2, 8, 312, 36);

        labelFilterCategory.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelFilterCategory.setText("Category");
        panelFilters.add(labelFilterCategory);
        labelFilterCategory.setBounds(8, 94, 300, 30);

        searchCategory.setBorder(null);
        searchCategory.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        searchCategory.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                searchCategoryItemStateChanged(evt);
            }
        });
        panelFilters.add(searchCategory);
        searchCategory.setBounds(8, 130, 300, 29);

        labelFilterName.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelFilterName.setText("Name");
        panelFilters.add(labelFilterName);
        labelFilterName.setBounds(8, 181, 300, 30);

        searchName.setBorder(null);
        searchName.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        searchName.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                searchNameItemStateChanged(evt);
            }
        });
        panelFilters.add(searchName);
        searchName.setBounds(8, 217, 300, 29);

        labelFilterDesc.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelFilterDesc.setText("Description");
        panelFilters.add(labelFilterDesc);
        labelFilterDesc.setBounds(8, 268, 300, 30);

        searchDesc.setBorder(null);
        searchDesc.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        searchDesc.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                searchDescItemStateChanged(evt);
            }
        });
        panelFilters.add(searchDesc);
        searchDesc.setBounds(8, 304, 300, 29);

        labelFilterPrice.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelFilterPrice.setText("Price Range");
        panelFilters.add(labelFilterPrice);
        labelFilterPrice.setBounds(8, 355, 300, 30);

        labelFilterPriceFrom.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        labelFilterPriceFrom.setText("From");
        panelFilters.add(labelFilterPriceFrom);
        labelFilterPriceFrom.setBounds(8, 391, 34, 24);

        searchPriceStart.setBorder(null);
        searchPriceStart.setForeground(new java.awt.Color(153, 153, 153));
        searchPriceStart.setText("1");
        searchPriceStart.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        searchPriceStart.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchPriceStartFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                searchPriceStartFocusLost(evt);
            }
        });
        searchPriceStart.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchPriceStartKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchPriceStartKeyTyped(evt);
            }
        });
        panelFilters.add(searchPriceStart);
        searchPriceStart.setBounds(48, 391, 100, 24);

        labelFilterPriceTo.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        labelFilterPriceTo.setText("To");
        panelFilters.add(labelFilterPriceTo);
        labelFilterPriceTo.setBounds(166, 391, 15, 24);

        searchPriceEnd.setBorder(null);
        searchPriceEnd.setForeground(new java.awt.Color(153, 153, 153));
        searchPriceEnd.setText("999999999");
        searchPriceEnd.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        searchPriceEnd.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                searchPriceEndFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                searchPriceEndFocusLost(evt);
            }
        });
        searchPriceEnd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchPriceEndKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchPriceEndKeyTyped(evt);
            }
        });
        panelFilters.add(searchPriceEnd);
        searchPriceEnd.setBounds(187, 394, 121, 18);

        labelFilterQuantity.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelFilterQuantity.setText("Quantity Range");
        panelFilters.add(labelFilterQuantity);
        labelFilterQuantity.setBounds(8, 437, 300, 30);

        labelFilterQuantityFrom.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        labelFilterQuantityFrom.setText("From");
        panelFilters.add(labelFilterQuantityFrom);
        labelFilterQuantityFrom.setBounds(8, 473, 34, 24);

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
        panelFilters.add(searchQuantityStart);
        searchQuantityStart.setBounds(48, 473, 100, 24);

        labelFilterQuantityTo.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        labelFilterQuantityTo.setText("To");
        panelFilters.add(labelFilterQuantityTo);
        labelFilterQuantityTo.setBounds(166, 473, 15, 24);

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
        panelFilters.add(searchQuantityEnd);
        searchQuantityEnd.setBounds(187, 476, 121, 18);

        labelFilterBenefactor.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelFilterBenefactor.setText("Benefactor");
        panelFilters.add(labelFilterBenefactor);
        labelFilterBenefactor.setBounds(8, 603, 300, 30);

        searchBenefactor.setBorder(null);
        searchBenefactor.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        searchBenefactor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                searchBenefactorItemStateChanged(evt);
            }
        });
        panelFilters.add(searchBenefactor);
        searchBenefactor.setBounds(8, 639, 300, 29);
        panelFilters.add(jSeparator1);
        jSeparator1.setBounds(8, 165, 300, 10);
        panelFilters.add(jSeparator2);
        jSeparator2.setBounds(8, 252, 300, 10);
        panelFilters.add(jSeparator3);
        jSeparator3.setBounds(8, 339, 300, 10);
        panelFilters.add(jSeparator4);
        jSeparator4.setBounds(8, 421, 300, 10);
        panelFilters.add(jSeparator5);
        jSeparator5.setBounds(8, 503, 300, 10);
        panelFilters.add(jSeparator7);
        jSeparator7.setBounds(8, 674, 300, 10);

        labelFilterDateFrom.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        labelFilterDateFrom.setText("From");
        panelFilters.add(labelFilterDateFrom);
        labelFilterDateFrom.setBounds(8, 557, 34, 24);

        searchDateStart.setBorder(null);
        searchDateStart.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        panelFilters.add(searchDateStart);
        searchDateStart.setBounds(48, 557, 100, 24);

        labelFilterDate.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelFilterDate.setText("Date Range");
        panelFilters.add(labelFilterDate);
        labelFilterDate.setBounds(8, 519, 300, 30);

        labelFilterDateTo.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        labelFilterDateTo.setText("To");
        panelFilters.add(labelFilterDateTo);
        labelFilterDateTo.setBounds(168, 557, 15, 24);

        searchDateEnd.setBorder(null);
        searchDateEnd.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        panelFilters.add(searchDateEnd);
        searchDateEnd.setBounds(188, 557, 121, 24);
        panelFilters.add(jSeparator6);
        jSeparator6.setBounds(8, 587, 300, 10);

        radioBatches.setBackground(new java.awt.Color(25, 102, 24));
        radioBatches.setBorder(null);
        radioBatches.setSelected(true);
        radioBatches.setText("Group by Batches");
        radioBatches.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        radioBatches.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioBatchesActionPerformed(evt);
            }
        });
        panelFilters.add(radioBatches);
        radioBatches.setBounds(8, 53, 300, 35);

        labelCleaFilter.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelCleaFilter.setForeground(new java.awt.Color(255, 255, 255));
        labelCleaFilter.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCleaFilter.setText("Clear Filters");
        labelCleaFilter.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelFilters.add(labelCleaFilter);
        labelCleaFilter.setBounds(0, 700, 320, 50);

        btnClearFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnLong_red.png"))); // NOI18N
        btnClearFilter.setBorder(null);
        btnClearFilter.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnLong_red_pressed.png"))); // NOI18N
        btnClearFilter.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnLong_red_pressed.png"))); // NOI18N
        btnClearFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearFilterActionPerformed(evt);
            }
        });
        panelFilters.add(btnClearFilter);
        btnClearFilter.setBounds(2, 702, 312, 49);

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelFilters, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 1497, Short.MAX_VALUE))
            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelBodyLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(panelFields, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                .addContainerGap(535, Short.MAX_VALUE)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelFilters, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tableScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelBodyLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(panelFields, javax.swing.GroupLayout.PREFERRED_SIZE, 522, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(770, Short.MAX_VALUE)))
        );

        scrollMain.setViewportView(panelBody);

        javax.swing.GroupLayout panelMainLayout = new javax.swing.GroupLayout(panelMain);
        panelMain.setLayout(panelMainLayout);
        panelMainLayout.setHorizontalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollMain)
        );
        panelMainLayout.setVerticalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollMain, javax.swing.GroupLayout.DEFAULT_SIZE, 896, Short.MAX_VALUE)
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

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        if (current_barcode != null) {
            int warnUser = JOptionPane.showConfirmDialog(
                    null,
                    "This item is part of a batch. Do you wish to print barcodes for the entire batch?",
                    "Warning: Batch Print",
                    JOptionPane.YES_NO_OPTION
            );
            List<BufferedImage> barcodeImages = new ArrayList<>();

            int stock_id = Integer.parseInt(fieldID.getText());
            String file_name = getColumnValueByInt(Main.TB_ITEM_STOCK, "stock_code", "stock_id", stock_id);

            if (warnUser == JOptionPane.NO_OPTION) {
                BufferedImage bufferedImage = (BufferedImage) barcodeIcon.getImage();
                barcodeImages.add(bufferedImage);
            } else if (warnUser == JOptionPane.YES_OPTION) {
                file_name += "-BATCH";

                for (int i = 0; i < batchQuantity; i++) {
                    String code = getColumnValueByInt(Main.TB_ITEM_STOCK, "stock_code", "stock_id", stock_id);
                    current_barcode = validateBarcode(code);
                    barcodeIcon = BarcodeUtil.generateBarcode(current_barcode);

                    BufferedImage bufferedImage = (BufferedImage) barcodeIcon.getImage();
                    barcodeImages.add(bufferedImage);

                    stock_id++;
                }
            }

            try {
                BarcodeUtil.generateFileFromBarcodes(barcodeImages, BarcodeUtil.FileType.PDF, file_name);
            } catch (IOException ex) {
                Logger.getLogger(ItemManagement.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No Barcode Selected", "Print Failed", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnPrintActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int stock_id = Integer.parseInt(fieldID.getText());
        int stock_batch_end = Integer.parseInt(fieldID2.getText());

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            int warnUser = JOptionPane.showConfirmDialog(
                    null,
                    "Confirm Delete?",
                    "Warning: Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (warnUser == JOptionPane.YES_OPTION) {
                String query = "DELETE FROM " + Main.TB_ITEM_STOCK + " WHERE stock_id >= ? && stock_id <= ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setInt(1, stock_id);
                pst.setInt(2, stock_batch_end);

                // HISTORY : MANAGEMENT-DELETE
                String stock_code = getColumnValueByInt(Main.TB_ITEM_STOCK, "stock_code", "stock_id", stock_id);
                String stock_code_end = getColumnValueByInt(Main.TB_ITEM_STOCK, "stock_code", "stock_id", stock_batch_end);

                insertHistory(DatabaseUtil.HistoryFrame.MANAGEMENT, DatabaseUtil.HistoryType.DELETE, stock_code, stock_code_end, "", "");

                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Stock Deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);

                clearFields();
                refreshItemTable();
            }
        } catch (SQLException e) {
            paneDatabaseError(e);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearFields();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        if (!fieldQuantity.getText().equals(PLACEHOLDER_QTY)) {
            fieldQuantity.setText("");
            setDefaultField(fieldQuantity, PLACEHOLDER_QTY, FieldFocus.LOST, Color.BLACK);
            JOptionPane.showMessageDialog(this, "Stock quantity cannot be updated! \n\nPlease just use 'Add' or 'Delete' to update the new stock quantity.", "Update Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int stock_id = Integer.parseInt(fieldID.getText());
        int stock_batch_end = Integer.parseInt(fieldID2.getText());
        String stock_name = comboName.getSelectedItem().toString();
        String stock_desc = fieldDesc.getText().equals(PLACEHOLDER_DESC) ? "" : fieldDesc.getText();
        String stock_price = fieldPrice.getText();
        String stock_deliveryDate = fieldDOD.getText();
        String stock_benefactor = fieldBenefactor.getText();

        String stock_category = DatabaseUtil.getColumnValueByString(Main.TB_CATALOG_ITEM, "item_category", "item_name", stock_name);

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            int warnUser = JOptionPane.showConfirmDialog(
                    null,
                    "Confirm Update?",
                    "Warning: Stock Update",
                    JOptionPane.YES_NO_OPTION
            );

            if (warnUser == JOptionPane.YES_OPTION) {
                String query = "UPDATE " + Main.TB_ITEM_STOCK + " SET stock_category = ?, stock_name = ?, stock_desc = ?, stock_price = ?, stock_dod = ?, stock_benefactor = ? WHERE stock_id >= ? && stock_id <= ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, stock_category);
                pst.setString(2, stock_name);
                pst.setString(3, stock_desc);
                pst.setString(4, stock_price);
                pst.setString(5, stock_deliveryDate);
                pst.setString(6, stock_benefactor);
                pst.setInt(7, stock_id);
                pst.setInt(8, stock_batch_end);

                // HISTORY : MANAGEMENT-UPDATE
                String history_desc = "";
                String selectedCode = getColumnValueByInt(Main.TB_ITEM_STOCK, "stock_code", "stock_id", stock_id);
                String selectedCodeEnd = getColumnValueByInt(Main.TB_ITEM_STOCK, "stock_code", "stock_id", stock_batch_end);
                String stock_holder = getColumnValueByInt(Main.TB_ITEM_STOCK, "stock_holder", "stock_id", stock_batch_end);

                String old_name = getColumnValueByInt(Main.TB_ITEM_STOCK, "stock_name", "stock_id", stock_id);
                String old_desc = getColumnValueByInt(Main.TB_ITEM_STOCK, "stock_desc", "stock_id", stock_id);
                Float old_priceFloat = Float.valueOf(getColumnValueByInt(Main.TB_ITEM_STOCK, "stock_price", "stock_id", stock_id));
                String old_price = floatRoundOff(old_priceFloat);
                String old_deliveryDate = getColumnValueByInt(Main.TB_ITEM_STOCK, "stock_dod", "stock_id", stock_id);
                String old_benefactor = getColumnValueByInt(Main.TB_ITEM_STOCK, "stock_benefactor", "stock_id", stock_id);

                history_desc += createHistoryDesc(old_name, stock_name, "Name");
                history_desc += createHistoryDesc(old_desc, stock_desc, "Description");
                history_desc += createHistoryDesc(old_price, stock_price, "Price");
                history_desc += createHistoryDesc(old_deliveryDate, stock_deliveryDate, "DOD");
                history_desc += createHistoryDesc(old_benefactor, stock_benefactor, "Benefactor");

                insertHistory(DatabaseUtil.HistoryFrame.MANAGEMENT, DatabaseUtil.HistoryType.UPDATE, selectedCode, selectedCodeEnd, history_desc, stock_holder);

                pst.executeUpdate();

                for (int i = 0; i < Integer.parseInt(fieldQuantitySelected.getText()); i++) {
                    query = "UPDATE " + Main.TB_ITEM_STOCK + " SET stock_code = ? WHERE stock_id = ?";
                    pst = conn.prepareStatement(query);

                    int id = i + stock_id;
                    String code;

                    // Silang Code Change
                    if (!old_deliveryDate.equals(stock_deliveryDate)) {
                        String parts[] = stock_deliveryDate.split("");
                        code = Main.BRANCH_CAMPUS + "-" + parts[2] + "" + parts[3] + "-" + id;
                    } else {
                        code = getColumnValueByInt(Main.TB_ITEM_STOCK, "stock_code", "stock_id", id);
                    }
                    pst.setString(1, code);
                    pst.setInt(2, id);

                    pst.executeUpdate();

                }

                JOptionPane.showMessageDialog(this, "Stock Updated!", "Success", JOptionPane.INFORMATION_MESSAGE);

                clearFields();
                refreshItemTable();
            }

        } catch (SQLException e) {
            paneDatabaseError(e);
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        String stock_name = comboName.getSelectedItem().toString();
        String stock_desc = fieldDesc.getText().equals(PLACEHOLDER_DESC) ? "" : fieldDesc.getText();
        String stock_price = fieldPrice.getText();
        int stock_quantity = Integer.parseInt(fieldQuantity.getText());
        String stock_deliveryDate = fieldDOD.getText();
        String stock_benefactor = fieldBenefactor.getText();

        String stock_category = DatabaseUtil.getColumnValueByString(Main.TB_CATALOG_ITEM, "item_category", "item_name", stock_name);
        int stock_batch = generateNewBatch();

        int stock_custom_code = 0;
        boolean hasCustomCode = false;
        if (!fieldCode.getText().equals(PLACEHOLDER_ID_CODE)) {
            stock_custom_code = Integer.parseInt(fieldCode.getText());
            hasCustomCode = true;
        }
        if (fieldCode.getText().equals("0")) {
            JOptionPane.showMessageDialog(this, "Custom stock code can't be 0!", "Add Stock Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (stock_deliveryDate.matches(Main.validDatePattern)) {
            try {
                LocalDate.parse(stock_deliveryDate);
            } catch (DateTimeParseException e) {
                JOptionPane.showMessageDialog(this, "Invalid date. The date is not valid.", "Add Stock Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.", "Add Stock Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            if (hasCustomCode) {
                String query = "INSERT INTO " + Main.TB_ITEM_STOCK + " (stock_id, stock_category, stock_name, stock_desc, stock_price, stock_dod, stock_benefactor, stock_batch)\n"
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
                int stock_custom_codeLoop = stock_custom_code;
                boolean hasDuplicateCode = false;
                for (int i = 0; i < stock_quantity; i++) {
                    String stock_custom_codeString = String.valueOf(stock_custom_codeLoop);

                    if (DatabaseUtil.recordExists(conn, Main.TB_ITEM_STOCK, "stock_id", stock_custom_codeString)) {
                        JOptionPane.showMessageDialog(this, "(" + stock_custom_codeLoop + ") Custom stock code can't be used. It already exists!", "Add Stock Failed", JOptionPane.ERROR_MESSAGE);
                        hasDuplicateCode = true;
                        return;
                    }
                    stock_custom_codeLoop++;
                }
                if (!hasDuplicateCode) {
                    for (int i = 0; i < stock_quantity; i++) {
                        pst.setInt(1, stock_custom_code);
                        pst.setString(2, stock_category);
                        pst.setString(3, stock_name);
                        pst.setString(4, stock_desc);
                        pst.setString(5, stock_price);
                        pst.setString(6, stock_deliveryDate);
                        pst.setString(7, stock_benefactor);
                        pst.setInt(8, stock_batch);
                        pst.executeUpdate();

                        ResultSet rs = pst.getGeneratedKeys();
                        if (rs.next()) {
                            int stock_id = stock_custom_code;
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date parsedDate = dateFormat.parse(stock_deliveryDate);

                            String stock_code = "Silang-" + (new SimpleDateFormat("yy").format(parsedDate)) + "-" + stock_id;
                            PreparedStatement codePst = conn.prepareStatement("UPDATE " + Main.TB_ITEM_STOCK + " SET stock_code = ? WHERE stock_id = ?");
                            codePst.setString(1, stock_code);
                            codePst.setInt(2, stock_id);
                            codePst.executeUpdate();
                        }

                        stock_custom_code++;
                    }
                }
            } else {
                String query = "INSERT INTO " + Main.TB_ITEM_STOCK + " (stock_category, stock_name, stock_desc, stock_price, stock_dod, stock_benefactor, stock_batch)\n"
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
                for (int i = 0; i < stock_quantity; i++) {
                    pst.setString(1, stock_category);
                    pst.setString(2, stock_name);
                    pst.setString(3, stock_desc);
                    pst.setString(4, stock_price);
                    pst.setString(5, stock_deliveryDate);
                    pst.setString(6, stock_benefactor);
                    pst.setInt(7, stock_batch);
                    pst.executeUpdate();

                    ResultSet rs = pst.getGeneratedKeys();
                    if (rs.next()) {
                        int stock_id = rs.getInt(1);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date parsedDate = dateFormat.parse(stock_deliveryDate);

                        String stock_code = "Silang-" + (new SimpleDateFormat("yy").format(parsedDate)) + "-" + stock_id;
                        PreparedStatement codePst = conn.prepareStatement("UPDATE " + Main.TB_ITEM_STOCK + " SET stock_code = ? WHERE stock_id = ?");
                        codePst.setString(1, stock_code);
                        codePst.setInt(2, stock_id);
                        codePst.executeUpdate();
                    }
                }
            }

            if (!comboRequest.isDefaultComboItem()) {
                String[] parts = comboRequest.getSelectedItem().toString().split("  :  ");
                String timestamp = parts[0];
                String name = parts[1];

                PreparedStatement pst = conn.prepareStatement("UPDATE " + Main.TB_ITEM_REQUEST + " SET request_status = 'RECEIVED' WHERE request_timestamp = '" + timestamp + "'");

                // HISTORY : REQUEST-UPDATE
                String request_id = "Request-" + DatabaseUtil.getColumnValueByString(Main.TB_ITEM_REQUEST, "request_id", "request_timestamp", timestamp);

                insertHistory(DatabaseUtil.HistoryFrame.REQUEST, DatabaseUtil.HistoryType.UPDATE, request_id, request_id, "; Status: PENDING -> RECEIVED", name);

                pst.executeUpdate();
            }

            // HISTORY : MANAGEMENT-ADD
            String stock_code = getColumnFromLastRow(Main.TB_ITEM_STOCK, "stock_timestamp", "stock_code");

            String stock_code_end = stock_code;

            if (stock_code_end.contains("-")) {
                String[] parts = stock_code_end.split("-");
                int qty = Integer.parseInt(parts[2]) + stock_quantity - 1;
                stock_code_end = parts[0] + "-" + parts[1] + "-" + qty;
            }

            String history_desc = "";

            history_desc += createHistoryDesc(stock_name, "Name");
            if (!stock_desc.isEmpty()) {
                history_desc += createHistoryDesc(stock_desc, "Description");
            }
            history_desc += createHistoryDesc(stock_price, "Price");
            history_desc += createHistoryDesc(String.valueOf(stock_quantity), "Quantity");
            history_desc += createHistoryDesc(stock_deliveryDate, "DOD");
            history_desc += createHistoryDesc(stock_benefactor, "Benefactor");

            insertHistory(DatabaseUtil.HistoryFrame.MANAGEMENT, DatabaseUtil.HistoryType.ADD, stock_code, stock_code_end, history_desc, "N/A");

            JOptionPane.showMessageDialog(this, "(" + stock_quantity + ") Stock/s Added!", "Success", JOptionPane.INFORMATION_MESSAGE);

            clearFields();
            refreshItemTable();
        } catch (SQLException e) {
            MessageUtil.paneDatabaseError(e);
        } catch (ParseException ex) {
            Logger.getLogger(ItemManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnDODActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDODActionPerformed
        dateDOD.showPopup();
    }//GEN-LAST:event_btnDODActionPerformed

    private void searchPriceStartFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchPriceStartFocusGained
        setDefaultField(searchPriceStart, Main.filterMinNumber, GuiUtil.FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_searchPriceStartFocusGained

    private void searchPriceStartFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchPriceStartFocusLost
        setDefaultField(searchPriceStart, Main.filterMinNumber, GuiUtil.FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_searchPriceStartFocusLost

    private void searchPriceStartKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchPriceStartKeyReleased
        refreshItemTable();
    }//GEN-LAST:event_searchPriceStartKeyReleased

    private void searchPriceStartKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchPriceStartKeyTyped
        enforceDigits(evt);
    }//GEN-LAST:event_searchPriceStartKeyTyped

    private void searchPriceEndFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchPriceEndFocusGained
        setDefaultField(searchPriceEnd, Main.filterMaxNumber, GuiUtil.FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_searchPriceEndFocusGained

    private void searchPriceEndFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchPriceEndFocusLost
        setDefaultField(searchPriceEnd, Main.filterMaxNumber, GuiUtil.FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_searchPriceEndFocusLost

    private void searchPriceEndKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchPriceEndKeyReleased
        refreshItemTable();
    }//GEN-LAST:event_searchPriceEndKeyReleased

    private void searchPriceEndKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchPriceEndKeyTyped
        enforceDigits(evt);
    }//GEN-LAST:event_searchPriceEndKeyTyped

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

    private void radioBatchesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioBatchesActionPerformed
        refreshItemTable();
        if (radioBatches.isSelected()) {
            tableInventory.getColumnModel().getColumn(3).setHeaderValue("Unit Price / Total Price");
        } else {
            tableInventory.getColumnModel().getColumn(3).setHeaderValue("Unit Price");
        }
        tableInventory.getTableHeader().repaint();
    }//GEN-LAST:event_radioBatchesActionPerformed

    private void btnClearFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearFilterActionPerformed
        searchCategory.setSelectedIndex(0);
        searchName.setSelectedIndex(0);
        searchDesc.setSelectedIndex(0);
        searchBenefactor.setSelectedIndex(0);

        searchPriceStart.setText("");
        setDefaultField(searchPriceStart, Main.filterMinNumber, GuiUtil.FieldFocus.LOST, Color.BLACK);
        searchPriceEnd.setText("");
        setDefaultField(searchPriceEnd, Main.filterMaxNumber, GuiUtil.FieldFocus.LOST, Color.BLACK);
        searchQuantityStart.setText("");
        setDefaultField(searchQuantityStart, Main.filterMinNumber, GuiUtil.FieldFocus.LOST, Color.BLACK);
        searchQuantityEnd.setText("");
        setDefaultField(searchQuantityEnd, Main.filterMaxNumber, GuiUtil.FieldFocus.LOST, Color.BLACK);
        searchDateStart.setText("");
        setDefaultField(searchDateStart, Main.filterMinDate, GuiUtil.FieldFocus.LOST, Color.BLACK);
        searchDateStart.setForeground(Color.BLACK);
        searchDateEnd.setText("");
        setDefaultField(searchDateEnd, Main.filterMaxDate, GuiUtil.FieldFocus.LOST, Color.BLACK);
        searchDateEnd.setForeground(Color.BLACK);

        refreshItemTable();
    }//GEN-LAST:event_btnClearFilterActionPerformed

    private void searchCategoryItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_searchCategoryItemStateChanged
        if (isUpdatingComboBoxes) {
            return;
        }

        disableUpdatingComboBoxes();
        searchName.repopulateAssociatedComboBox(searchCategory, "stock_category", "SELECT stock_name FROM " + Main.TB_ITEM_STOCK);
        searchDesc.repopulateAssociatedComboBox(searchCategory, "stock_category", "SELECT stock_desc FROM " + Main.TB_ITEM_STOCK);
        searchBenefactor.repopulateAssociatedComboBox(searchCategory, "stock_category", "SELECT stock_benefactor FROM " + Main.TB_ITEM_STOCK);
        enableUpdatingComboBoxes();

        refreshItemTable();
    }//GEN-LAST:event_searchCategoryItemStateChanged

    private void searchNameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_searchNameItemStateChanged
        if (isUpdatingComboBoxes) {
            return;
        }

        disableUpdatingComboBoxes();
        searchDesc.repopulateAssociatedComboBox(searchName, searchCategory, "stock_name", "stock_category", "SELECT stock_desc FROM " + Main.TB_ITEM_STOCK);
        searchBenefactor.repopulateAssociatedComboBox(searchName, searchCategory, "stock_name", "stock_category", "SELECT stock_benefactor FROM " + Main.TB_ITEM_STOCK);
        enableUpdatingComboBoxes();

        refreshItemTable();
    }//GEN-LAST:event_searchNameItemStateChanged

    private void searchDescItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_searchDescItemStateChanged
        if (isUpdatingComboBoxes) {
            return;
        }

        refreshItemTable();
    }//GEN-LAST:event_searchDescItemStateChanged

    private void searchBenefactorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_searchBenefactorItemStateChanged
        if (isUpdatingComboBoxes) {
            return;
        }

        refreshItemTable();
    }//GEN-LAST:event_searchBenefactorItemStateChanged

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
        exportSQLToCSV(currentSearchQuery);
    }//GEN-LAST:event_btnExportActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnClearFilter;
    private javax.swing.JButton btnDOD;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnUpdate;
    private ProjectINSY.java.swing.ComboBoxSuggestion comboName;
    private ProjectINSY.java.swing.ComboBoxSuggestion comboRequest;
    private ProjectINSY.java.swing.Date.DateChooser dateDOD;
    private ProjectINSY.java.swing.Date.DateChooser dateEnd;
    private ProjectINSY.java.swing.Date.DateChooser dateStart;
    private ProjectINSY.java.swing.Form.FormFieldSuggestion fieldBenefactor;
    private ProjectINSY.java.swing.Form.FormField fieldCode;
    private ProjectINSY.java.swing.Form.FormField fieldDOD;
    private ProjectINSY.java.swing.Form.FormField fieldDesc;
    private ProjectINSY.java.swing.Form.FormField fieldID;
    private ProjectINSY.java.swing.Form.FormField fieldID2;
    private ProjectINSY.java.swing.Form.FormField fieldPrice;
    private ProjectINSY.java.swing.Form.FormField fieldQuantity;
    private ProjectINSY.java.swing.Form.FormField fieldQuantitySelected;
    private javax.swing.JLabel imageBenefactor;
    private javax.swing.JLabel imageCode;
    private javax.swing.JLabel imageDOD;
    private javax.swing.JLabel imageDesc;
    private javax.swing.JLabel imageName;
    private javax.swing.JLabel imagePrice;
    private javax.swing.JLabel imageQuantity;
    private javax.swing.JLabel imageRequest;
    private javax.swing.JLabel imgBarcode;
    private javax.swing.JLabel infoCode;
    private javax.swing.JLabel infoCode1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JLabel labelAdd;
    private javax.swing.JLabel labelBenefactor;
    private javax.swing.JLabel labelCleaFilter;
    private javax.swing.JLabel labelClear;
    private javax.swing.JLabel labelCode;
    private javax.swing.JLabel labelDOD;
    private javax.swing.JLabel labelDelete;
    private javax.swing.JLabel labelDesc;
    private javax.swing.JLabel labelFilterBenefactor;
    private javax.swing.JLabel labelFilterCategory;
    private javax.swing.JLabel labelFilterDate;
    private javax.swing.JLabel labelFilterDateFrom;
    private javax.swing.JLabel labelFilterDateTo;
    private javax.swing.JLabel labelFilterDesc;
    private javax.swing.JLabel labelFilterName;
    private javax.swing.JLabel labelFilterPrice;
    private javax.swing.JLabel labelFilterPriceFrom;
    private javax.swing.JLabel labelFilterPriceTo;
    private javax.swing.JLabel labelFilterQuantity;
    private javax.swing.JLabel labelFilterQuantityFrom;
    private javax.swing.JLabel labelFilterQuantityTo;
    private javax.swing.JLabel labelFilterTitle;
    private javax.swing.JLabel labelName;
    private javax.swing.JLabel labelPrice;
    private javax.swing.JLabel labelPrint;
    private javax.swing.JLabel labelQuantity;
    private javax.swing.JLabel labelRequest;
    private javax.swing.JLabel labelUpdate;
    private javax.swing.JPanel panelBarcode;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelCRUD;
    private javax.swing.JPanel panelCode;
    private javax.swing.JPanel panelFields;
    private javax.swing.JPanel panelFilterTitle;
    private javax.swing.JPanel panelFilters;
    private javax.swing.JPanel panelInformation;
    private javax.swing.JPanel panelMain;
    public static ProjectINSY.java.swing.RadioButtonCustom radioBatches;
    private javax.swing.JScrollPane scrollMain;
    private ProjectINSY.java.swing.ComboBoxSuggestion searchBenefactor;
    private ProjectINSY.java.swing.ComboBoxSuggestion searchCategory;
    private ProjectINSY.java.swing.Form.FormFieldSuggestion searchDateEnd;
    private ProjectINSY.java.swing.Form.FormFieldSuggestion searchDateStart;
    private ProjectINSY.java.swing.ComboBoxSuggestion searchDesc;
    private ProjectINSY.java.swing.ComboBoxSuggestion searchName;
    private ProjectINSY.java.swing.Form.FormFieldSuggestion searchPriceEnd;
    private ProjectINSY.java.swing.Form.FormFieldSuggestion searchPriceStart;
    private ProjectINSY.java.swing.Form.FormFieldSuggestion searchQuantityEnd;
    private ProjectINSY.java.swing.Form.FormFieldSuggestion searchQuantityStart;
    private ProjectINSY.java.swing.Table tableInventory;
    private javax.swing.JScrollPane tableScroll;
    // End of variables declaration//GEN-END:variables
}
