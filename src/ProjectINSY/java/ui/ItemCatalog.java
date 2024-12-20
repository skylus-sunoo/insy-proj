/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ProjectINSY.java.ui;

import ProjectINSY.java.Main;
import ProjectINSY.java.util.DatabaseUtil;
import ProjectINSY.java.util.GuiUtil;
import ProjectINSY.java.util.GuiUtil.FieldFocus;
import static ProjectINSY.java.util.GuiUtil.resetBtnEnability;
import static ProjectINSY.java.util.GuiUtil.setDefaultField;
import static ProjectINSY.java.util.GuiUtil.setScrollBarCustom;
import static ProjectINSY.java.util.GuiUtil.setTransparentFrame;
import ProjectINSY.java.util.MessageUtil;
import static ProjectINSY.java.util.MessageUtil.paneDatabaseError;
import ProjectINSY.java.util.TableUtil;
import static ProjectINSY.java.util.TableUtil.defaultTable;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class ItemCatalog extends javax.swing.JPanel {

    public String currentSearchQuery = "SELECT * FROM " + Main.TB_ITEM_STOCK;

    private final String PLACEHOLDER_CATEGORY = "Enter Category";
    private final String PLACEHOLDER_NAME = "Enter Name";

    /**
     * Creates new form LogIn
     */
    public ItemCatalog() {
        initComponents();
        setScrollBarCustom(scrollCategory);
        setScrollBarCustom(tableScroll);

        setTransparentFrame(ItemCatalog.this, fieldCategoryName, fieldName);
        setTransparentFrame(btnAddCategory, btnUpdateCategory, btnDeleteCategory, btnAddItem, btnUpdateItem, btnDeleteItem);

        fieldCategoryName.getDocument().addDocumentListener(new ItemCatalog.FieldChangeListener());
        defaultTable(tableCategory);
        tableCategory.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableCategory.getSelectedRow();
                if (selectedRow >= 0) {
                    selectTableCategory(selectedRow);
                }
            }
        });

        fieldName.getDocument().addDocumentListener(new ItemCatalog.FieldChangeListener());
        defaultTable(tableItem);
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
    }

    public void selectTableCategory(int selectedRow) {
        String[] tableRow = TableUtil.selectTableRow(tableCategory, selectedRow);
        TableUtil.linkFieldsToTable(tableRow, fieldCategoryName);

        fieldCategoryID.setText(fieldCategoryName.getText());

        setUpdateDeleteEnableCategory();
    }

    public void selectTableItem(int selectedRow) {
        String[] tableRow = TableUtil.selectTableRow(tableItem, selectedRow);
        TableUtil.linkFieldsToTable(tableRow, fieldName, comboCategory, comboUOM);

        fieldItemID.setText(fieldName.getText());

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
            btnAddCategory.setEnabled(!fieldCategoryName.getText().trim().isEmpty()
                    && !fieldCategoryName.getText().trim().equals(PLACEHOLDER_CATEGORY));

            btnAddItem.setEnabled(!fieldName.getText().trim().isEmpty()
                    && !fieldName.getText().trim().equals(PLACEHOLDER_NAME));
        }
    }

    public void refreshTableCategory() {
        TableUtil.refreshTable(tableCategory, "SELECT * FROM " + Main.TB_CATALOG_CATEGORY, TableUtil.TableEnum.CATALOG_CATEGORY);
    }

    public void setUpdateDeleteEnableCategory() {
        resetBtnEnability(fieldCategoryID, btnUpdateCategory, btnDeleteCategory);
    }

    public void clearCategoryFields() {
        GuiUtil.clearField(fieldCategoryID, "");
        GuiUtil.clearField(fieldCategoryName, PLACEHOLDER_CATEGORY);
        TableUtil.clearSelectedTableRow(tableCategory);
        setUpdateDeleteEnableCategory();
    }

    public void refreshTableItem() {
        TableUtil.refreshTable(tableItem, "SELECT * FROM " + Main.TB_CATALOG_ITEM, TableUtil.TableEnum.CATALOG_ITEM);
    }

    public void setUpdateDeleteEnableItem() {
        resetBtnEnability(fieldItemID, btnUpdateItem, btnDeleteItem);
    }

    public void clearItemFields() {
        GuiUtil.clearField(fieldItemID, "");
        GuiUtil.clearComboBox(comboCategory);
        GuiUtil.clearField(fieldName, PLACEHOLDER_NAME);
        TableUtil.clearSelectedTableRow(tableItem);
        setUpdateDeleteEnableItem();
    }

    public void repopulateCategoryComboBox() {
        GuiUtil.repopulateComboBox(comboCategory, "category_name", "SELECT category_name FROM " + Main.TB_CATALOG_CATEGORY);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fieldCategoryID = new javax.swing.JTextField();
        fieldItemID = new javax.swing.JTextField();
        panelBlur = new ProjectINSY.java.ui.panel.GradientPanel();
        panelCategory = new javax.swing.JPanel();
        panelFields = new javax.swing.JPanel();
        labelCategoryName = new javax.swing.JLabel();
        fieldCategoryName = new javax.swing.JTextField();
        imageCategoryName = new javax.swing.JLabel();
        labelAddCategory = new javax.swing.JLabel();
        btnAddCategory = new javax.swing.JButton();
        labelUpdateCategory = new javax.swing.JLabel();
        btnUpdateCategory = new javax.swing.JButton();
        labelDeleteCategory = new javax.swing.JLabel();
        btnDeleteCategory = new javax.swing.JButton();
        scrollCategory = new javax.swing.JScrollPane();
        tableCategory = new ProjectINSY.java.swing.Table();
        labelCatalogCategory = new javax.swing.JLabel();
        panelItem = new javax.swing.JPanel();
        labelCatalogItem = new javax.swing.JLabel();
        tableScroll = new javax.swing.JScrollPane();
        tableItem = new ProjectINSY.java.swing.Table();
        panelItemFields = new javax.swing.JPanel();
        labelCategory = new javax.swing.JLabel();
        comboCategory = new ProjectINSY.java.swing.ComboBoxSuggestion();
        imageCategory = new javax.swing.JLabel();
        labelName = new javax.swing.JLabel();
        fieldName = new javax.swing.JTextField();
        imageName = new javax.swing.JLabel();
        labelAddItem = new javax.swing.JLabel();
        btnAddItem = new javax.swing.JButton();
        labelUpdateItem = new javax.swing.JLabel();
        btnUpdateItem = new javax.swing.JButton();
        labelDeleteItem = new javax.swing.JLabel();
        btnDeleteItem = new javax.swing.JButton();
        labelUOM = new javax.swing.JLabel();
        comboUOM = new ProjectINSY.java.swing.ComboBoxSuggestion();
        imageUOM = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(1366, 768));
        setMinimumSize(new java.awt.Dimension(1366, 768));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(1366, 768));

        panelBlur.setColorEnd(new java.awt.Color(241, 239, 241));
        panelBlur.setColorStart(new java.awt.Color(241, 239, 241));
        panelBlur.setMaximumSize(new java.awt.Dimension(1326, 669));
        panelBlur.setMinimumSize(new java.awt.Dimension(1326, 669));
        panelBlur.setName(""); // NOI18N
        panelBlur.setShadowIntensity(255);

        panelCategory.setBackground(new java.awt.Color(255, 255, 255));
        panelCategory.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 102, 24), 2));

        panelFields.setBackground(new java.awt.Color(255, 255, 255));
        panelFields.setLayout(null);

        labelCategoryName.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 18)); // NOI18N
        labelCategoryName.setText("Category");
        panelFields.add(labelCategoryName);
        labelCategoryName.setBounds(0, 0, 173, 30);

        fieldCategoryName.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        fieldCategoryName.setForeground(new java.awt.Color(153, 153, 153));
        fieldCategoryName.setText("Enter Category");
        fieldCategoryName.setBorder(null);
        fieldCategoryName.setSelectionColor(new java.awt.Color(25, 102, 24));
        fieldCategoryName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldCategoryNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldCategoryNameFocusLost(evt);
            }
        });
        panelFields.add(fieldCategoryName);
        fieldCategoryName.setBounds(10, 40, 360, 20);

        imageCategoryName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldLogIn.png"))); // NOI18N
        panelFields.add(imageCategoryName);
        imageCategoryName.setBounds(0, 30, 377, 40);

        labelAddCategory.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelAddCategory.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelAddCategory.setText("Add");
        labelAddCategory.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelFields.add(labelAddCategory);
        labelAddCategory.setBounds(10, 110, 80, 23);

        btnAddCategory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn.png"))); // NOI18N
        btnAddCategory.setBorder(null);
        btnAddCategory.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddCategory.setEnabled(false);
        btnAddCategory.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_pressed.png"))); // NOI18N
        btnAddCategory.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_pressed.png"))); // NOI18N
        btnAddCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddCategoryActionPerformed(evt);
            }
        });
        panelFields.add(btnAddCategory);
        btnAddCategory.setBounds(0, 100, 100, 40);

        labelUpdateCategory.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelUpdateCategory.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelUpdateCategory.setText("Update");
        labelUpdateCategory.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelFields.add(labelUpdateCategory);
        labelUpdateCategory.setBounds(150, 110, 80, 23);

        btnUpdateCategory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn.png"))); // NOI18N
        btnUpdateCategory.setBorder(null);
        btnUpdateCategory.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdateCategory.setEnabled(false);
        btnUpdateCategory.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_pressed.png"))); // NOI18N
        btnUpdateCategory.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_pressed.png"))); // NOI18N
        btnUpdateCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateCategoryActionPerformed(evt);
            }
        });
        panelFields.add(btnUpdateCategory);
        btnUpdateCategory.setBounds(140, 100, 100, 40);

        labelDeleteCategory.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelDeleteCategory.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelDeleteCategory.setText("Delete");
        labelDeleteCategory.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelFields.add(labelDeleteCategory);
        labelDeleteCategory.setBounds(290, 110, 80, 23);

        btnDeleteCategory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_red.png"))); // NOI18N
        btnDeleteCategory.setBorder(null);
        btnDeleteCategory.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDeleteCategory.setEnabled(false);
        btnDeleteCategory.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_pressed.png"))); // NOI18N
        btnDeleteCategory.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_pressed.png"))); // NOI18N
        btnDeleteCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteCategoryActionPerformed(evt);
            }
        });
        panelFields.add(btnDeleteCategory);
        btnDeleteCategory.setBounds(280, 100, 100, 40);

        scrollCategory.setBorder(null);

        tableCategory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Category List"
            }
        ));
        tableCategory.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        tableCategory.setSelectionBackground(new java.awt.Color(25, 102, 24));
        scrollCategory.setViewportView(tableCategory);

        labelCatalogCategory.setBackground(new java.awt.Color(25, 102, 24));
        labelCatalogCategory.setFont(new java.awt.Font("Bebas", 0, 48)); // NOI18N
        labelCatalogCategory.setForeground(new java.awt.Color(255, 255, 255));
        labelCatalogCategory.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCatalogCategory.setText("CATEGORIES CATALOG");
        labelCatalogCategory.setOpaque(true);

        javax.swing.GroupLayout panelCategoryLayout = new javax.swing.GroupLayout(panelCategory);
        panelCategory.setLayout(panelCategoryLayout);
        panelCategoryLayout.setHorizontalGroup(
            panelCategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCategoryLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(panelFields, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
            .addGroup(panelCategoryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(labelCatalogCategory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelCategoryLayout.setVerticalGroup(
            panelCategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCategoryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelCatalogCategory)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelFields, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelItem.setBackground(new java.awt.Color(255, 255, 255));
        panelItem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 102, 24), 2));

        labelCatalogItem.setBackground(new java.awt.Color(25, 102, 24));
        labelCatalogItem.setFont(new java.awt.Font("Bebas", 0, 48)); // NOI18N
        labelCatalogItem.setForeground(new java.awt.Color(255, 255, 255));
        labelCatalogItem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCatalogItem.setText("ITEMS CATALOG");
        labelCatalogItem.setOpaque(true);

        tableScroll.setBorder(null);

        tableItem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Item", "Category", "Unit of Measure"
            }
        ));
        tableItem.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        tableItem.setGridColor(new java.awt.Color(255, 255, 255));
        tableItem.setSelectionBackground(new java.awt.Color(25, 102, 24));
        tableScroll.setViewportView(tableItem);

        panelItemFields.setBackground(new java.awt.Color(255, 255, 255));
        panelItemFields.setLayout(null);

        labelCategory.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 18)); // NOI18N
        labelCategory.setText("Category");
        panelItemFields.add(labelCategory);
        labelCategory.setBounds(0, 70, 173, 30);

        comboCategory.setBorder(null);
        comboCategory.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        panelItemFields.add(comboCategory);
        comboCategory.setBounds(10, 110, 360, 24);

        imageCategory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldLogIn.png"))); // NOI18N
        panelItemFields.add(imageCategory);
        imageCategory.setBounds(0, 100, 377, 40);

        labelName.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 18)); // NOI18N
        labelName.setText("Name");
        panelItemFields.add(labelName);
        labelName.setBounds(0, 0, 173, 30);

        fieldName.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        fieldName.setForeground(new java.awt.Color(153, 153, 153));
        fieldName.setText("Enter Name");
        fieldName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        fieldName.setSelectionColor(new java.awt.Color(25, 102, 24));
        fieldName.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldNameFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldNameFocusLost(evt);
            }
        });
        panelItemFields.add(fieldName);
        fieldName.setBounds(10, 40, 360, 20);

        imageName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldLogIn.png"))); // NOI18N
        panelItemFields.add(imageName);
        imageName.setBounds(0, 30, 377, 40);

        labelAddItem.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelAddItem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelAddItem.setText("Add");
        labelAddItem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelItemFields.add(labelAddItem);
        labelAddItem.setBounds(450, 110, 80, 23);

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
        btnAddItem.setBounds(440, 100, 100, 40);

        labelUpdateItem.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelUpdateItem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelUpdateItem.setText("Update");
        labelUpdateItem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelItemFields.add(labelUpdateItem);
        labelUpdateItem.setBounds(590, 110, 80, 23);

        btnUpdateItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn.png"))); // NOI18N
        btnUpdateItem.setBorder(null);
        btnUpdateItem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdateItem.setEnabled(false);
        btnUpdateItem.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_pressed.png"))); // NOI18N
        btnUpdateItem.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_pressed.png"))); // NOI18N
        btnUpdateItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateItemActionPerformed(evt);
            }
        });
        panelItemFields.add(btnUpdateItem);
        btnUpdateItem.setBounds(580, 100, 100, 40);

        labelDeleteItem.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelDeleteItem.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelDeleteItem.setText("Delete");
        labelDeleteItem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelItemFields.add(labelDeleteItem);
        labelDeleteItem.setBounds(730, 110, 80, 23);

        btnDeleteItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_red.png"))); // NOI18N
        btnDeleteItem.setBorder(null);
        btnDeleteItem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDeleteItem.setEnabled(false);
        btnDeleteItem.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_pressed.png"))); // NOI18N
        btnDeleteItem.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btn_pressed.png"))); // NOI18N
        btnDeleteItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteItemActionPerformed(evt);
            }
        });
        panelItemFields.add(btnDeleteItem);
        btnDeleteItem.setBounds(720, 100, 100, 40);

        labelUOM.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 18)); // NOI18N
        labelUOM.setText("Unit of Measure");
        panelItemFields.add(labelUOM);
        labelUOM.setBounds(440, 0, 173, 30);

        comboUOM.setBorder(null);
        comboUOM.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        panelItemFields.add(comboUOM);
        comboUOM.setBounds(450, 40, 360, 24);

        imageUOM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldLogIn.png"))); // NOI18N
        panelItemFields.add(imageUOM);
        imageUOM.setBounds(440, 30, 377, 40);

        javax.swing.GroupLayout panelItemLayout = new javax.swing.GroupLayout(panelItem);
        panelItem.setLayout(panelItemLayout);
        panelItemLayout.setHorizontalGroup(
            panelItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelItemLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(panelItemFields, javax.swing.GroupLayout.PREFERRED_SIZE, 819, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelItemLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tableScroll)
                .addContainerGap())
            .addComponent(labelCatalogItem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelItemLayout.setVerticalGroup(
            panelItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelItemLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelCatalogItem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelItemFields, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelBlurLayout = new javax.swing.GroupLayout(panelBlur);
        panelBlur.setLayout(panelBlurLayout);
        panelBlurLayout.setHorizontalGroup(
            panelBlurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBlurLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(panelCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        panelBlurLayout.setVerticalGroup(
            panelBlurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBlurLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(panelBlurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelCategory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(873, Short.MAX_VALUE)
                .addComponent(panelBlur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(93, Short.MAX_VALUE)
                .addComponent(panelBlur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void fieldNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldNameFocusGained
        setDefaultField(fieldName, PLACEHOLDER_NAME, FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_fieldNameFocusGained

    private void fieldNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldNameFocusLost
        setDefaultField(fieldName, PLACEHOLDER_NAME, FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_fieldNameFocusLost

    private void btnAddCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddCategoryActionPerformed
        String category_name = fieldCategoryName.getText();

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            if (!DatabaseUtil.recordExists(conn, Main.TB_CATALOG_CATEGORY, "category_name", category_name)) {
                String query = "INSERT INTO " + Main.TB_CATALOG_CATEGORY + " (category_name)\n"
                        + "VALUES (?)";
                PreparedStatement pst = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setString(1, category_name);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Category Added!", "Success", JOptionPane.INFORMATION_MESSAGE);

                clearCategoryFields();
                refreshTableCategory();
                repopulateCategoryComboBox();
            }
        } catch (SQLException e) {
            MessageUtil.paneDatabaseError(e);
        }
    }//GEN-LAST:event_btnAddCategoryActionPerformed

    private void fieldCategoryNameFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldCategoryNameFocusGained
        setDefaultField(fieldCategoryName, PLACEHOLDER_CATEGORY, FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_fieldCategoryNameFocusGained

    private void fieldCategoryNameFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldCategoryNameFocusLost
        setDefaultField(fieldCategoryName, PLACEHOLDER_CATEGORY, FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_fieldCategoryNameFocusLost

    private void btnUpdateCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateCategoryActionPerformed
        String category_name_original = fieldCategoryID.getText();
        String category_name = fieldCategoryName.getText();

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            if (!DatabaseUtil.recordExists(conn, Main.TB_CATALOG_CATEGORY, "category_name", category_name)) {
                int warnUser = JOptionPane.showConfirmDialog(
                        null,
                        "Updating this Category's name will also update the corresponding category name in other related tables. Do you want to proceed?",
                        "Warning: Category Update",
                        JOptionPane.YES_NO_OPTION
                );

                if (warnUser == JOptionPane.YES_OPTION) {
                    String query = "UPDATE " + Main.TB_CATALOG_CATEGORY + " SET category_name = ? WHERE category_name = ?";
                    PreparedStatement pst = conn.prepareStatement(query);
                    pst.setString(1, category_name);
                    pst.setString(2, category_name_original);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Category Updated!", "Success", JOptionPane.INFORMATION_MESSAGE);

                    clearCategoryFields();
                    refreshTableCategory();
                    repopulateCategoryComboBox();
                }
            } else {
                JOptionPane.showMessageDialog(this, "This category already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            paneDatabaseError(e);
        }
    }//GEN-LAST:event_btnUpdateCategoryActionPerformed

    private void btnDeleteCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteCategoryActionPerformed
        String category_name = fieldCategoryID.getText();

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            int warnUser = JOptionPane.showConfirmDialog(
                    null,
                    "Confirm Delete?",
                    "Warning: Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (warnUser == JOptionPane.YES_OPTION) {
                String query = "DELETE FROM " + Main.TB_CATALOG_CATEGORY + " WHERE category_name = ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, category_name);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Category Deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);

                clearCategoryFields();
                refreshTableCategory();
                repopulateCategoryComboBox();
            }
        } catch (SQLException e) {
            paneDatabaseError(e);
        }
    }//GEN-LAST:event_btnDeleteCategoryActionPerformed

    private void btnAddItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddItemActionPerformed
        String item_category = comboCategory.getSelectedItem().toString();
        String item_name = fieldName.getText();
        String item_uom = comboUOM.getSelectedItem().toString();

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            if (!DatabaseUtil.recordExists(conn, Main.TB_CATALOG_ITEM, "item_name", item_name)) {
                String query = "INSERT INTO " + Main.TB_CATALOG_ITEM + " (item_category, item_name, item_uom)\n"
                        + "VALUES (?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
                pst.setString(1, item_category);
                pst.setString(2, item_name);
                pst.setString(3, item_uom);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Item Added!", "Success", JOptionPane.INFORMATION_MESSAGE);

                clearItemFields();
                refreshTableItem();
            }
        } catch (SQLException e) {
            MessageUtil.paneDatabaseError(e);
        }
    }//GEN-LAST:event_btnAddItemActionPerformed

    private void btnUpdateItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateItemActionPerformed
        String item_name_original = fieldItemID.getText();
        String item_name = fieldName.getText();
        String item_category = comboCategory.getSelectedItem().toString();
        String item_uom = comboUOM.getSelectedItem().toString();

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
//            if (!DatabaseUtil.recordExists(conn, Main.TB_CATALOG_ITEM, "item_name", item_name)) {
            int warnUser = JOptionPane.showConfirmDialog(
                    null,
                    "Updating this Item's name will also update the corresponding item name in other related tables. Do you want to proceed?",
                    "Warning: Item Update",
                    JOptionPane.YES_NO_OPTION
            );

            if (warnUser == JOptionPane.YES_OPTION) {
                String query = "UPDATE " + Main.TB_CATALOG_ITEM + " SET item_category = ?, item_name = ?, item_uom = ? WHERE item_name = ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, item_category);
                pst.setString(2, item_name);
                pst.setString(3, item_uom);
                pst.setString(4, item_name_original);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Item Updated!", "Success", JOptionPane.INFORMATION_MESSAGE);

                clearItemFields();
                refreshTableItem();
            }
//            } else {
//                JOptionPane.showMessageDialog(this, "This item already exists!", "Error", JOptionPane.ERROR_MESSAGE);
//            }
        } catch (SQLException e) {
            paneDatabaseError(e);
        }
    }//GEN-LAST:event_btnUpdateItemActionPerformed

    private void btnDeleteItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteItemActionPerformed
        String item_name = fieldItemID.getText();

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            int warnUser = JOptionPane.showConfirmDialog(
                    null,
                    "Confirm Delete?",
                    "Warning: Delete",
                    JOptionPane.YES_NO_OPTION
            );

            if (warnUser == JOptionPane.YES_OPTION) {
                String query = "DELETE FROM " + Main.TB_CATALOG_ITEM + " WHERE item_name = ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, item_name);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Item Deleted!", "Success", JOptionPane.INFORMATION_MESSAGE);

                clearItemFields();
                refreshTableItem();
            }
        } catch (SQLException e) {
            paneDatabaseError(e);
        }
    }//GEN-LAST:event_btnDeleteItemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddCategory;
    private javax.swing.JButton btnAddItem;
    private javax.swing.JButton btnDeleteCategory;
    private javax.swing.JButton btnDeleteItem;
    private javax.swing.JButton btnUpdateCategory;
    private javax.swing.JButton btnUpdateItem;
    private ProjectINSY.java.swing.ComboBoxSuggestion comboCategory;
    private ProjectINSY.java.swing.ComboBoxSuggestion comboUOM;
    private javax.swing.JTextField fieldCategoryID;
    private javax.swing.JTextField fieldCategoryName;
    private javax.swing.JTextField fieldItemID;
    private javax.swing.JTextField fieldName;
    private javax.swing.JLabel imageCategory;
    private javax.swing.JLabel imageCategoryName;
    private javax.swing.JLabel imageName;
    private javax.swing.JLabel imageUOM;
    private javax.swing.JLabel labelAddCategory;
    private javax.swing.JLabel labelAddItem;
    private javax.swing.JLabel labelCatalogCategory;
    private javax.swing.JLabel labelCatalogItem;
    private javax.swing.JLabel labelCategory;
    private javax.swing.JLabel labelCategoryName;
    private javax.swing.JLabel labelDeleteCategory;
    private javax.swing.JLabel labelDeleteItem;
    private javax.swing.JLabel labelName;
    private javax.swing.JLabel labelUOM;
    private javax.swing.JLabel labelUpdateCategory;
    private javax.swing.JLabel labelUpdateItem;
    private ProjectINSY.java.ui.panel.GradientPanel panelBlur;
    private javax.swing.JPanel panelCategory;
    private javax.swing.JPanel panelFields;
    private javax.swing.JPanel panelItem;
    private javax.swing.JPanel panelItemFields;
    private javax.swing.JScrollPane scrollCategory;
    private ProjectINSY.java.swing.Table tableCategory;
    private ProjectINSY.java.swing.Table tableItem;
    private javax.swing.JScrollPane tableScroll;
    // End of variables declaration//GEN-END:variables
}
