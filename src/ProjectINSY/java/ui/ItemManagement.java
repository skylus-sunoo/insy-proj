/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ProjectINSY.java.ui;

import ProjectINSY.java.Main;
import ProjectINSY.java.util.BarcodeUtil;
import static ProjectINSY.java.util.BarcodeUtil.validateBarcode;
import ProjectINSY.java.util.DatabaseUtil;
import static ProjectINSY.java.util.DatabaseUtil.generateNewBatch;
import static ProjectINSY.java.util.DatabaseUtil.getConnection;
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
import static ProjectINSY.java.util.TableUtil.defaultTable;
import static ProjectINSY.java.util.TableUtil.floatFormatDecimal;
import static ProjectINSY.java.util.TableUtil.setColumnHorizontalAligment;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author admin
 */
public class ItemManagement extends javax.swing.JPanel {

    public String currentSearchQuery = "SELECT stock_id, stock_batch, "
            + "stock_category, "
            + "stock_name, "
            + "stock_desc, "
            + "(stock_price * COUNT(*)) AS stock_price, "
            + "stock_dod, "
            + "stock_user, "
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
            + "GROUP BY stock_batch ORDER BY stock_id ASC";

    private final String PLACEHOLDER_CODE = "Enter Code (XXXX)";
    private final String PLACEHOLDER_DESC = "Enter Description";
    private final String PLACEHOLDER_PRICE = "Enter Price";
    private final String PLACEHOLDER_DOD = "Enter Delivery Date";
    private final String PLACEHOLDER_QTY = "1";
    private final String PLACEHOLDER_HOLDER = "Enter Holder";
    private String current_barcode = null;
    private ImageIcon barcodeIcon;

    public static boolean groupByBatches = true;

    /**
     * Creates new form LogIn
     */
    public ItemManagement() {
        initComponents();

        setScrollBarCustom(tableScroll);

        setTransparentFrame(ItemManagement.this, fieldDesc, fieldPrice, fieldDOD, fieldQuantity, fieldHolder);
        setTransparentFrame(btnDOD, btnAdd, btnUpdate, btnClear, btnDelete);

//        tableInventory.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        fieldPrice.getDocument().addDocumentListener(new ItemManagement.FieldChangeListener());
        fieldQuantity.getDocument().addDocumentListener(new ItemManagement.FieldChangeListener());
        fieldHolder.getDocument().addDocumentListener(new ItemManagement.FieldChangeListener());
        defaultTable(tableInventory);
        tableInventory.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableInventory.getSelectedRow();
                if (selectedRow >= 0) {
                    selectTableStock(selectedRow);
                }
            }
        });

        setColumnHorizontalAligment(tableInventory, 3, TableUtil.EnumAlignment.LEFT);
        floatFormatDecimal(tableInventory, 3);
        setColumnHorizontalAligment(tableInventory, 4, TableUtil.EnumAlignment.LEFT);
//        sorterNumbers(tableInventory, 3);
//        sorterNumbers(tableInventory, 4);
    }

    public void selectTableStock(int selectedRow) {
        String[] tableRow = TableUtil.selectTableRow(tableInventory, selectedRow);
        TableUtil.linkFieldsToTable(tableRow, fieldID, comboName, fieldDesc, fieldPrice, fieldQuantity, fieldDOD, fieldHolder);

        Float actual_price = Float.valueOf(fieldPrice.getText());
        Float quantity = Float.valueOf(fieldQuantity.getText());
        fieldQuantity.setText("");
        setDefaultField(fieldQuantity, PLACEHOLDER_QTY, FieldFocus.LOST, Color.BLACK);

        fieldPrice.setText(String.valueOf(actual_price / quantity) + "0");

        current_barcode = validateBarcode(fieldID.getText());
        barcodeIcon = BarcodeUtil.generateBarcode(current_barcode);
        imgBarcode.setIcon(barcodeIcon);

        if (fieldDesc.getText().isEmpty()) {
            setDefaultField(fieldDesc, PLACEHOLDER_DESC, FieldFocus.LOST, Color.BLACK);
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
            btnAdd.setEnabled(!fieldPrice.getText().trim().isEmpty()
                    && !fieldPrice.getText().trim().equals(PLACEHOLDER_PRICE)
                    && !fieldQuantity.getText().trim().isEmpty()
                    && !fieldHolder.getText().trim().isEmpty()
                    && !fieldHolder.getText().trim().equals(PLACEHOLDER_HOLDER));
        }
    }

    public void refreshTableInventory() {
        currentSearchQuery = cleanSpaces(currentSearchQuery);
        TableUtil.refreshTable(tableInventory, currentSearchQuery, TableUtil.TableEnum.STOCK_DELIVERY);
//        System.out.println(currentSearchQuery);

        String query = "SELECT DISTINCT stock_user FROM " + Main.TB_ITEM_STOCK;

        fieldHolder.clearItemSuggestion();
        try (Connection conn = getConnection(Main.DB_NAME); PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                fieldHolder.addItemSuggestion(rs.getString("stock_user"));
            }
        } catch (SQLException e) {
            paneDatabaseError(e);
        }
    }

    public void setUpdateDeleteEnable() {
        resetBtnEnability(fieldID, btnUpdate, btnDelete);
    }

    public void clearFields() {
        current_barcode = null;
        barcodeIcon = null;
        GuiUtil.resetIcon(imgBarcode);

        GuiUtil.clearField(fieldID, "");
        GuiUtil.clearField(fieldID2, "");
        GuiUtil.clearField(fieldCode, PLACEHOLDER_CODE);
        GuiUtil.clearComboBox(comboName);
        GuiUtil.clearField(fieldDesc, PLACEHOLDER_DESC);
        GuiUtil.clearField(fieldPrice, PLACEHOLDER_PRICE);
        GuiUtil.clearField(fieldQuantity, PLACEHOLDER_QTY);
        GuiUtil.clearField(fieldHolder, PLACEHOLDER_HOLDER);
        GuiUtil.clearFieldDate(fieldDOD);
        fieldDOD.setForeground(Color.BLACK);
        TableUtil.clearSelectedTableRow(tableInventory);
        setUpdateDeleteEnable();
    }

    public void repopulateNameComboBox() {
        GuiUtil.repopulateComboBox(comboName, "item_name", "SELECT item_name FROM " + Main.TB_CATALOG_ITEM);
    }

    public static boolean isGroupedByBatches() {
        return groupByBatches;
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
        dateDOD = new ProjectINSY.java.swing.Date.DateChooser();
        fieldID2 = new javax.swing.JTextField();
        panelMain = new javax.swing.JPanel();
        panelFields = new javax.swing.JPanel();
        labelName = new javax.swing.JLabel();
        comboName = new ProjectINSY.java.swing.ComboBoxSuggestion();
        imageName = new javax.swing.JLabel();
        labelDesc = new javax.swing.JLabel();
        fieldDesc = new javax.swing.JTextField();
        imageDesc = new javax.swing.JLabel();
        labelPrice = new javax.swing.JLabel();
        fieldPrice = new javax.swing.JTextField();
        imagePrice = new javax.swing.JLabel();
        labelDOD = new javax.swing.JLabel();
        fieldDOD = new javax.swing.JTextField();
        btnDOD = new javax.swing.JButton();
        imageDOD = new javax.swing.JLabel();
        fieldQuantity = new javax.swing.JTextField();
        labelQuantity = new javax.swing.JLabel();
        imageQuantity = new javax.swing.JLabel();
        labelHolder = new javax.swing.JLabel();
        fieldHolder = new ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion();
        imageHolder = new javax.swing.JLabel();
        labelAdd = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        labelUpdate = new javax.swing.JLabel();
        btnUpdate = new javax.swing.JButton();
        labelClear = new javax.swing.JLabel();
        btnClear = new javax.swing.JButton();
        labelDelete = new javax.swing.JLabel();
        btnDelete = new javax.swing.JButton();
        labelCode = new javax.swing.JLabel();
        fieldCode = new ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion();
        imageCode = new javax.swing.JLabel();
        infoCode = new javax.swing.JLabel();
        infoCode1 = new javax.swing.JLabel();
        panelBarcode = new javax.swing.JPanel();
        imgBarcode = new javax.swing.JLabel();
        labelPrint = new javax.swing.JLabel();
        btnPrint = new javax.swing.JButton();
        tableScroll = new javax.swing.JScrollPane();
        tableInventory = new ProjectINSY.java.swing.Table();
        btnFilter = new javax.swing.JButton();

        dateDOD.setForeground(new java.awt.Color(25, 102, 24));
        dateDOD.setDateFormat("yyyy-MM-dd");
        dateDOD.setTextRefernce(fieldDOD);

        setMaximumSize(new java.awt.Dimension(1840, 900));
        setMinimumSize(new java.awt.Dimension(1840, 900));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(1840, 900));

        panelMain.setBackground(new java.awt.Color(255, 255, 255));
        panelMain.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 102, 24), 2));

        panelFields.setBackground(new java.awt.Color(255, 255, 255));
        panelFields.setLayout(null);

        labelName.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelName.setText("Name");
        panelFields.add(labelName);
        labelName.setBounds(0, 110, 173, 30);

        comboName.setBorder(null);
        comboName.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        panelFields.add(comboName);
        comboName.setBounds(10, 150, 520, 40);

        imageName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldLogIn.png"))); // NOI18N
        panelFields.add(imageName);
        imageName.setBounds(0, 140, 540, 60);

        labelDesc.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelDesc.setText("Description");
        panelFields.add(labelDesc);
        labelDesc.setBounds(0, 220, 173, 30);

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
        panelFields.add(fieldDesc);
        fieldDesc.setBounds(10, 260, 520, 40);

        imageDesc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldLogIn.png"))); // NOI18N
        panelFields.add(imageDesc);
        imageDesc.setBounds(0, 250, 540, 60);

        labelPrice.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelPrice.setText("Price");
        panelFields.add(labelPrice);
        labelPrice.setBounds(0, 330, 173, 30);

        fieldPrice.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        fieldPrice.setForeground(new java.awt.Color(153, 153, 153));
        fieldPrice.setText("Enter Price");
        fieldPrice.setBorder(null);
        fieldPrice.setSelectionColor(new java.awt.Color(25, 102, 24));
        fieldPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldPriceFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldPriceFocusLost(evt);
            }
        });
        fieldPrice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                fieldPriceKeyTyped(evt);
            }
        });
        panelFields.add(fieldPrice);
        fieldPrice.setBounds(10, 370, 230, 40);

        imagePrice.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldHalf.png"))); // NOI18N
        panelFields.add(imagePrice);
        imagePrice.setBounds(0, 360, 250, 60);

        labelDOD.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelDOD.setText("Delivery Date");
        panelFields.add(labelDOD);
        labelDOD.setBounds(290, 330, 190, 30);

        fieldDOD.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        fieldDOD.setBorder(null);
        fieldDOD.setSelectionColor(new java.awt.Color(25, 102, 24));
        fieldDOD.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldDODFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldDODFocusLost(evt);
            }
        });
        panelFields.add(fieldDOD);
        fieldDOD.setBounds(300, 370, 190, 40);

        btnDOD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnDateSelect.png"))); // NOI18N
        btnDOD.setBorder(null);
        btnDOD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDODActionPerformed(evt);
            }
        });
        panelFields.add(btnDOD);
        btnDOD.setBounds(500, 370, 30, 40);

        imageDOD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldHalf.png"))); // NOI18N
        panelFields.add(imageDOD);
        imageDOD.setBounds(290, 360, 250, 60);

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
        panelFields.add(fieldQuantity);
        fieldQuantity.setBounds(10, 480, 230, 40);

        labelQuantity.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelQuantity.setText("Quantity");
        panelFields.add(labelQuantity);
        labelQuantity.setBounds(0, 440, 173, 30);

        imageQuantity.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldHalf.png"))); // NOI18N
        panelFields.add(imageQuantity);
        imageQuantity.setBounds(0, 470, 250, 60);

        labelHolder.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelHolder.setText("Holder");
        panelFields.add(labelHolder);
        labelHolder.setBounds(290, 440, 173, 30);

        fieldHolder.setBorder(null);
        fieldHolder.setForeground(new java.awt.Color(153, 153, 153));
        fieldHolder.setText("Enter Holder");
        fieldHolder.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        fieldHolder.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldHolderFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldHolderFocusLost(evt);
            }
        });
        panelFields.add(fieldHolder);
        fieldHolder.setBounds(300, 480, 230, 40);

        imageHolder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldHalf.png"))); // NOI18N
        panelFields.add(imageHolder);
        imageHolder.setBounds(290, 470, 250, 60);

        labelAdd.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelAdd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelAdd.setText("Add");
        labelAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelFields.add(labelAdd);
        labelAdd.setBounds(10, 850, 80, 23);

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
        panelFields.add(btnAdd);
        btnAdd.setBounds(0, 840, 100, 40);

        labelUpdate.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelUpdate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelUpdate.setText("Update");
        labelUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelFields.add(labelUpdate);
        labelUpdate.setBounds(160, 850, 80, 23);

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
        panelFields.add(btnUpdate);
        btnUpdate.setBounds(150, 840, 100, 40);

        labelClear.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelClear.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelClear.setText("Clear");
        labelClear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelFields.add(labelClear);
        labelClear.setBounds(300, 850, 80, 23);

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
        panelFields.add(btnClear);
        btnClear.setBounds(290, 840, 100, 40);

        labelDelete.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelDelete.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelDelete.setText("Delete");
        labelDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelFields.add(labelDelete);
        labelDelete.setBounds(450, 850, 80, 23);

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
        panelFields.add(btnDelete);
        btnDelete.setBounds(440, 840, 100, 40);

        labelCode.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelCode.setText("Custom Code (Silang-YY-XXXX) - Optional");
        panelFields.add(labelCode);
        labelCode.setBounds(0, 10, 540, 30);

        fieldCode.setBorder(null);
        fieldCode.setForeground(new java.awt.Color(153, 153, 153));
        fieldCode.setText("Enter Code (XXXX)");
        fieldCode.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        fieldCode.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldCodeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldCodeFocusLost(evt);
            }
        });
        fieldCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                fieldCodeKeyTyped(evt);
            }
        });
        panelFields.add(fieldCode);
        fieldCode.setBounds(9, 50, 230, 40);

        imageCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldHalf.png"))); // NOI18N
        panelFields.add(imageCode);
        imageCode.setBounds(0, 40, 250, 60);

        infoCode.setFont(new java.awt.Font("Bahnschrift", 2, 14)); // NOI18N
        infoCode.setText("'Adding' Stocks");
        panelFields.add(infoCode);
        infoCode.setBounds(260, 70, 110, 20);

        infoCode1.setFont(new java.awt.Font("Bahnschrift", 2, 14)); // NOI18N
        infoCode1.setText("Only works for");
        panelFields.add(infoCode1);
        infoCode1.setBounds(260, 50, 110, 20);

        panelBarcode.setBackground(new java.awt.Color(255, 255, 255));
        panelBarcode.setLayout(null);

        imgBarcode.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        panelBarcode.add(imgBarcode);
        imgBarcode.setBounds(0, 0, 250, 190);

        labelPrint.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelPrint.setForeground(new java.awt.Color(255, 255, 255));
        labelPrint.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/iconPrint.png"))); // NOI18N
        labelPrint.setText("Print");
        labelPrint.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelBarcode.add(labelPrint);
        labelPrint.setBounds(340, 50, 150, 50);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnPrint.png"))); // NOI18N
        btnPrint.setBorder(null);
        btnPrint.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });
        panelBarcode.add(btnPrint);
        btnPrint.setBounds(340, 50, 150, 50);

        panelFields.add(panelBarcode);
        panelBarcode.setBounds(0, 540, 540, 190);

        tableScroll.setBorder(null);

        tableInventory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "", "Name", "Description", "Price", "Quantity", "Delivery Date", "Holder"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Float.class, java.lang.Float.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tableInventory.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        tableInventory.setGridColor(new java.awt.Color(255, 255, 255));
        tableInventory.setSelectionBackground(new java.awt.Color(25, 102, 24));
        tableScroll.setViewportView(tableInventory);

        btnFilter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnSearch.png"))); // NOI18N
        btnFilter.setToolTipText("");
        btnFilter.setBorder(null);
        btnFilter.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnFilter.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnSearch_pressed.png"))); // NOI18N
        btnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilterActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelMainLayout = new javax.swing.GroupLayout(panelMain);
        panelMain.setLayout(panelMainLayout);
        panelMainLayout.setHorizontalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMainLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(panelFields, javax.swing.GroupLayout.PREFERRED_SIZE, 545, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnFilter)
                    .addComponent(tableScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 1262, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        panelMainLayout.setVerticalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelMainLayout.createSequentialGroup()
                        .addComponent(btnFilter)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tableScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 853, Short.MAX_VALUE))
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

    private void fieldDescFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldDescFocusGained
        setDefaultField(fieldDesc, PLACEHOLDER_DESC, FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_fieldDescFocusGained

    private void fieldDescFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldDescFocusLost
        setDefaultField(fieldDesc, PLACEHOLDER_DESC, FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_fieldDescFocusLost

    private void fieldPriceFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldPriceFocusGained
        setDefaultField(fieldPrice, PLACEHOLDER_PRICE, FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_fieldPriceFocusGained

    private void fieldPriceFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldPriceFocusLost
        setDefaultField(fieldPrice, PLACEHOLDER_PRICE, FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_fieldPriceFocusLost

    private void fieldDODFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldDODFocusGained
        setDefaultField(fieldDOD, PLACEHOLDER_DOD, FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_fieldDODFocusGained

    private void fieldDODFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldDODFocusLost
        setDefaultField(fieldDOD, PLACEHOLDER_DOD, FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_fieldDODFocusLost

    private void fieldPriceKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldPriceKeyTyped
        enforceDigits(evt);
    }//GEN-LAST:event_fieldPriceKeyTyped

    private void fieldQuantityKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldQuantityKeyTyped
        enforceDigits(evt);
    }//GEN-LAST:event_fieldQuantityKeyTyped

    private void fieldQuantityFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldQuantityFocusLost
        setDefaultField(fieldQuantity, PLACEHOLDER_QTY, FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_fieldQuantityFocusLost

    private void fieldQuantityFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldQuantityFocusGained
        setDefaultField(fieldQuantity, PLACEHOLDER_QTY, FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_fieldQuantityFocusGained

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        String stock_name = comboName.getSelectedItem().toString();
        String stock_desc = fieldDesc.getText().equals(PLACEHOLDER_DESC) ? "" : fieldDesc.getText();
        String stock_price = fieldPrice.getText();
        int stock_quantity = Integer.parseInt(fieldQuantity.getText());
        String stock_deliveryDate = fieldDOD.getText();
        String stock_holder = fieldHolder.getText();

        String stock_category = DatabaseUtil.getCategoryByItem(stock_name);
        int stock_batch = generateNewBatch();

        int stock_custom_code = 0;
        boolean hasCustomCode = false;
        if (!fieldCode.getText().equals(PLACEHOLDER_CODE)) {
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
                System.out.println("Valid date: " + stock_deliveryDate);
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
                String query = "INSERT INTO " + Main.TB_ITEM_STOCK + " (stock_id, stock_category, stock_name, stock_desc, stock_price, stock_dod, stock_user, stock_batch)\n"
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
                        pst.setString(7, stock_holder);
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
                String query = "INSERT INTO " + Main.TB_ITEM_STOCK + " (stock_category, stock_name, stock_desc, stock_price, stock_dod, stock_user, stock_batch)\n"
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
                for (int i = 0; i < stock_quantity; i++) {
                    pst.setString(1, stock_category);
                    pst.setString(2, stock_name);
                    pst.setString(3, stock_desc);
                    pst.setString(4, stock_price);
                    pst.setString(5, stock_deliveryDate);
                    pst.setString(6, stock_holder);
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
            JOptionPane.showMessageDialog(this, "(" + stock_quantity + ") Stock/s Added!", "Success", JOptionPane.INFORMATION_MESSAGE);

            clearFields();
            refreshTableInventory();
        } catch (SQLException e) {
            MessageUtil.paneDatabaseError(e);
        } catch (ParseException ex) {
            Logger.getLogger(ItemManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnDODActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDODActionPerformed
        dateDOD.showPopup();
    }//GEN-LAST:event_btnDODActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        if (!fieldQuantity.getText().equals(PLACEHOLDER_QTY)) {
            fieldQuantity.setText("");
            setDefaultField(fieldQuantity, PLACEHOLDER_QTY, FieldFocus.LOST, Color.BLACK);
            JOptionPane.showMessageDialog(this, "Stock quantity cannot be updated! \n\nPlease just use 'Add' or 'Delete' to update the new stock quantity.", "Update Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
//        if (!fieldCode.getText().equals(PLACEHOLDER_CODE)) {
//            fieldCode.setText("");
//            setDefaultField(fieldCode, PLACEHOLDER_CODE, FieldFocus.LOST, Color.BLACK);
//            JOptionPane.showMessageDialog(this, "Stock code are not allowed to be updated.", "Update Failed", JOptionPane.ERROR_MESSAGE);
//            return;
//        }

        int stock_id = Integer.parseInt(fieldID.getText());
        int stock_batch_end = Integer.parseInt(fieldID2.getText());
        String stock_name = comboName.getSelectedItem().toString();
        String stock_desc = fieldDesc.getText().equals(PLACEHOLDER_DESC) ? "" : fieldDesc.getText();
        String stock_price = fieldPrice.getText();
        String stock_deliveryDate = fieldDOD.getText();
        String stock_holder = fieldHolder.getText();

        String stock_category = DatabaseUtil.getCategoryByItem(stock_name);

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            int warnUser = JOptionPane.showConfirmDialog(
                    null,
                    "Confirm Update?",
                    "Warning: Stock Update",
                    JOptionPane.YES_NO_OPTION
            );

            if (warnUser == JOptionPane.YES_OPTION) {
                String query = "UPDATE " + Main.TB_ITEM_STOCK + " SET stock_category = ?, stock_name = ?, stock_desc = ?, stock_price = ?, stock_dod = ?, stock_user = ? WHERE stock_id >= ? && stock_id <= ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, stock_category);
                pst.setString(2, stock_name);
                pst.setString(3, stock_desc);
                pst.setString(4, stock_price);
                pst.setString(5, stock_deliveryDate);
                pst.setString(6, stock_holder);
                pst.setInt(7, stock_id);
                pst.setInt(8, stock_batch_end);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Stock Updated!", "Success", JOptionPane.INFORMATION_MESSAGE);

                clearFields();
                refreshTableInventory();
            }

        } catch (SQLException e) {
            paneDatabaseError(e);
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

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
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Stock Deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);

                clearFields();
                refreshTableInventory();
            }
        } catch (SQLException e) {
            paneDatabaseError(e);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void fieldHolderFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldHolderFocusGained
        setDefaultField(fieldHolder, PLACEHOLDER_HOLDER, FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_fieldHolderFocusGained

    private void fieldHolderFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldHolderFocusLost
        setDefaultField(fieldHolder, PLACEHOLDER_HOLDER, FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_fieldHolderFocusLost

    private void fieldCodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldCodeFocusGained
        setDefaultField(fieldCode, PLACEHOLDER_CODE, FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_fieldCodeFocusGained

    private void fieldCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldCodeFocusLost
        setDefaultField(fieldCode, PLACEHOLDER_CODE, FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_fieldCodeFocusLost

    private void fieldCodeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldCodeKeyTyped
        enforceDigits(evt);
    }//GEN-LAST:event_fieldCodeKeyTyped

    private void btnFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterActionPerformed
        new FilterFrame(this).setVisible(true);
        showFilterFrame(false);
    }//GEN-LAST:event_btnFilterActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearFields();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        if (current_barcode != null) {
            try {
                BufferedImage bufferedImage = (BufferedImage) barcodeIcon.getImage();

                ImageIO.write(bufferedImage, "PNG", new File(current_barcode + ".png"));
            } catch (IOException e) {
            }
        } else {
            JOptionPane.showMessageDialog(this, "No Barcode Selected", "Print Failed", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnPrintActionPerformed

    public void showFilterFrame(boolean bool) {
        btnFilter.setEnabled(bool);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDOD;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFilter;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnUpdate;
    private ProjectINSY.java.swing.ComboBoxSuggestion comboName;
    private ProjectINSY.java.swing.Date.DateChooser dateDOD;
    private ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion fieldCode;
    private javax.swing.JTextField fieldDOD;
    private javax.swing.JTextField fieldDesc;
    private ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion fieldHolder;
    private javax.swing.JTextField fieldID;
    private javax.swing.JTextField fieldID2;
    private javax.swing.JTextField fieldPrice;
    private javax.swing.JTextField fieldQuantity;
    private javax.swing.JLabel imageCode;
    private javax.swing.JLabel imageDOD;
    private javax.swing.JLabel imageDesc;
    private javax.swing.JLabel imageHolder;
    private javax.swing.JLabel imageName;
    private javax.swing.JLabel imagePrice;
    private javax.swing.JLabel imageQuantity;
    private javax.swing.JLabel imgBarcode;
    private javax.swing.JLabel infoCode;
    private javax.swing.JLabel infoCode1;
    private javax.swing.JLabel labelAdd;
    private javax.swing.JLabel labelClear;
    private javax.swing.JLabel labelCode;
    private javax.swing.JLabel labelDOD;
    private javax.swing.JLabel labelDelete;
    private javax.swing.JLabel labelDesc;
    private javax.swing.JLabel labelHolder;
    private javax.swing.JLabel labelName;
    private javax.swing.JLabel labelPrice;
    private javax.swing.JLabel labelPrint;
    private javax.swing.JLabel labelQuantity;
    private javax.swing.JLabel labelUpdate;
    private javax.swing.JPanel panelBarcode;
    private javax.swing.JPanel panelFields;
    private javax.swing.JPanel panelMain;
    private ProjectINSY.java.swing.Table tableInventory;
    private javax.swing.JScrollPane tableScroll;
    // End of variables declaration//GEN-END:variables
}
