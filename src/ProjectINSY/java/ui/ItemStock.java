/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ProjectINSY.java.ui;

import ProjectINSY.java.Main;
import ProjectINSY.java.util.GuiUtil;
import static ProjectINSY.java.util.GuiUtil.enforceDigits;
import static ProjectINSY.java.util.GuiUtil.setScrollBarCustom;
import ProjectINSY.java.util.TableUtil;
import ProjectINSY.java.util.TableUtil.EnumAlignment;
import static ProjectINSY.java.util.TableUtil.defaultTable;
import static ProjectINSY.java.util.TableUtil.fixedColumnAll;
import static ProjectINSY.java.util.TableUtil.setColumnHorizontalAligment;
import static ProjectINSY.java.util.TableUtil.sorterNumbers;
import java.util.Comparator;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author admin
 */
public class ItemStock extends javax.swing.JPanel {

    private class Filter {

        private String value;

        public Filter(String initialValue) {
            this.value = initialValue;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private final Filter filterCategory = new Filter("stock_category IS NOT NULL ");
    private final Filter filterName = new Filter("&& stock_name IS NOT NULL");
    private final Filter filterQuantityStart = new Filter("stock_quantity >= 1 ");
    private final Filter filterQuantityEnd = new Filter("&& stock_quantity <= 9999 ");

    public String currentSearchQuery = "SELECT stock_category, stock_name, COUNT(stock_name) AS stock_quantity FROM "
            + Main.TB_ITEM_STOCK + " WHERE "
            + filterCategory.getValue() + filterName.getValue()
            + "GROUP BY stock_category, stock_name HAVING "
            + filterQuantityStart.getValue() + filterQuantityEnd.getValue();

    /**
     * Creates new form LogIn
     */
    public ItemStock() {
        initComponents();
        btnSearch.setVisible(false);
        setScrollBarCustom(tableScroll);

        defaultTable(tableInventory);

        setColumnHorizontalAligment(tableInventory, 2, EnumAlignment.LEFT);
        fixedColumnAll(tableInventory);
        sorterNumbers(tableInventory, 2);

//        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher((KeyEvent e) -> {
//            if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_ENTER) {
//                refreshTableInventory();
//                return true;
//            }
//            return false;
//        });
    }

    public void refreshTableInventory() {
        TableUtil.refreshTable(tableInventory, currentSearchQuery, TableUtil.TableEnum.STOCK_DISTINCT);
    }

    public void repopulateComboBox() {
        GuiUtil.repopulateComboBox(searchCategory, "stock_category", "SELECT stock_category FROM " + Main.TB_ITEM_STOCK);
        searchCategory.insertItemAt("- - Select Category - -", 0);
        searchCategory.setSelectedIndex(0);

        GuiUtil.repopulateComboBox(searchName, "stock_name", "SELECT stock_name FROM " + Main.TB_ITEM_STOCK);
        searchName.insertItemAt("- - Select Name - -", 0);
        searchName.setSelectedIndex(0);
    }

    private void resetSearchQuery() {
        currentSearchQuery = "SELECT stock_category, stock_name, COUNT(stock_name) AS stock_quantity FROM "
                + Main.TB_ITEM_STOCK + " WHERE "
                + filterCategory.getValue() + filterName.getValue()
                + "GROUP BY stock_category, stock_name HAVING "
                + filterQuantityStart.getValue() + filterQuantityEnd.getValue();
    }

    private enum EnumFilterType {
        EQUAL, EQUAL_OR_GREATER, EQUAL_OR_LESSER
    }

    private void createFilter(Object inputComponent, String defaultString, String column, Filter filter, EnumFilterType filterType) {
        String selectedValue = null;

        if (inputComponent instanceof JComboBox) {
            JComboBox<?> comboBox = (JComboBox<?>) inputComponent;
            Object selectedItem = comboBox.getSelectedItem();
            if (selectedItem != null) {
                selectedValue = selectedItem.toString();
            }
        } else if (inputComponent instanceof JTextField textField) {
            selectedValue = textField.getText();
        } else {
            throw new IllegalArgumentException("Unsupported input component type. Must be JComboBox or JTextField.");
        }

        String type = "=";

        if (filterType != null) {
            switch (filterType) {
                case EQUAL ->
                    type = "=";
                case EQUAL_OR_GREATER ->
                    type = ">=";
                case EQUAL_OR_LESSER ->
                    type = "<=";
                default -> {
                }
            }
        }

        if (selectedValue == null || selectedValue.trim().isEmpty() || selectedValue.equals(defaultString)) {
            if (filter == filterCategory || filter == filterQuantityStart) {
                filter.setValue(column + " IS NOT NULL ");
            } else {
                filter.setValue("&& " + column + " IS NOT NULL ");
            }
        } else {
            if (filter == filterCategory || filter == filterQuantityStart) {
                filter.setValue(column + " " + type + " '" + selectedValue + "' ");
            } else {
                filter.setValue("&& " + column + " " + type + " '" + selectedValue + "' ");
            }
        }
        resetSearchQuery();
        refreshTableInventory();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelBlur = new ProjectINSY.java.ui.panel.GradientPanel();
        panelMain = new javax.swing.JPanel();
        tableScroll = new javax.swing.JScrollPane();
        tableInventory = new ProjectINSY.java.swing.Table();
        panelSearch = new javax.swing.JPanel();
        searchCategory = new ProjectINSY.java.swing.ComboBoxSuggestion();
        labelFilterCategory = new javax.swing.JLabel();
        searchName = new ProjectINSY.java.swing.ComboBoxSuggestion();
        labelFilterName = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        labelFilterQuantity = new javax.swing.JLabel();
        searchQuantityStart = new ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion();
        searchQuantityEnd = new ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion();
        labelFilterQuantityFrom = new javax.swing.JLabel();
        labelFilterQuantityTo = new javax.swing.JLabel();

        setMaximumSize(new java.awt.Dimension(1366, 768));
        setMinimumSize(new java.awt.Dimension(1366, 768));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(1366, 768));

        panelBlur.setColorEnd(new java.awt.Color(241, 239, 241));
        panelBlur.setColorStart(new java.awt.Color(241, 239, 241));
        panelBlur.setMaximumSize(new java.awt.Dimension(1326, 669));
        panelBlur.setMinimumSize(new java.awt.Dimension(1326, 669));
        panelBlur.setShadowIntensity(255);

        panelMain.setBackground(new java.awt.Color(255, 255, 255));
        panelMain.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 102, 24), 2));

        tableScroll.setBorder(null);

        tableInventory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Category", "Name", "Quantity"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class
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
        tableInventory.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        tableInventory.setGridColor(new java.awt.Color(255, 255, 255));
        tableInventory.setSelectionBackground(new java.awt.Color(25, 102, 24));
        tableScroll.setViewportView(tableInventory);

        panelSearch.setBackground(new java.awt.Color(255, 255, 255));
        panelSearch.setMaximumSize(new java.awt.Dimension(1277, 71));
        panelSearch.setMinimumSize(new java.awt.Dimension(1277, 71));

        searchCategory.setBorder(null);
        searchCategory.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        searchCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchCategoryActionPerformed(evt);
            }
        });

        labelFilterCategory.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 18)); // NOI18N
        labelFilterCategory.setText("Category");

        searchName.setBorder(null);
        searchName.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        searchName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchNameActionPerformed(evt);
            }
        });

        labelFilterName.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 18)); // NOI18N
        labelFilterName.setText("Name");

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnSearch.png"))); // NOI18N
        btnSearch.setBorder(null);
        btnSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearch.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnSearch_pressed.png"))); // NOI18N
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        labelFilterQuantity.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 18)); // NOI18N
        labelFilterQuantity.setText("Quantity Range");

        searchQuantityStart.setBorder(null);
        searchQuantityStart.setText("0");
        searchQuantityStart.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        searchQuantityStart.addFocusListener(new java.awt.event.FocusAdapter() {
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

        searchQuantityEnd.setBorder(null);
        searchQuantityEnd.setText("9999");
        searchQuantityEnd.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        searchQuantityEnd.addFocusListener(new java.awt.event.FocusAdapter() {
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

        labelFilterQuantityFrom.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        labelFilterQuantityFrom.setText("From");

        labelFilterQuantityTo.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        labelFilterQuantityTo.setText("To");

        javax.swing.GroupLayout panelSearchLayout = new javax.swing.GroupLayout(panelSearch);
        panelSearch.setLayout(panelSearchLayout);
        panelSearchLayout.setHorizontalGroup(
            panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSearchLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelFilterCategory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchCategory, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE))
                .addGap(122, 122, 122)
                .addGroup(panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelFilterName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchName, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 127, Short.MAX_VALUE)
                .addGroup(panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelSearchLayout.createSequentialGroup()
                        .addComponent(labelFilterQuantityFrom)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchQuantityStart, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(labelFilterQuantityTo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchQuantityEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21))
                    .addComponent(labelFilterQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(97, 97, 97)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelSearchLayout.setVerticalGroup(
            panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSearchLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSearchLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(btnSearch))
                    .addGroup(panelSearchLayout.createSequentialGroup()
                        .addComponent(labelFilterCategory)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(searchName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(searchCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelSearchLayout.createSequentialGroup()
                            .addComponent(labelFilterName)
                            .addGap(30, 30, 30))
                        .addGroup(panelSearchLayout.createSequentialGroup()
                            .addComponent(labelFilterQuantity)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(searchQuantityStart, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(labelFilterQuantityTo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(searchQuantityEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(labelFilterQuantityFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelMainLayout = new javax.swing.GroupLayout(panelMain);
        panelMain.setLayout(panelMainLayout);
        panelMainLayout.setHorizontalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tableScroll)
                    .addComponent(panelSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelMainLayout.setVerticalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 541, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelBlurLayout = new javax.swing.GroupLayout(panelBlur);
        panelBlur.setLayout(panelBlurLayout);
        panelBlurLayout.setHorizontalGroup(
            panelBlurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBlurLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(panelMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        panelBlurLayout.setVerticalGroup(
            panelBlurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBlurLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(panelMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addComponent(panelBlur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(84, Short.MAX_VALUE)
                .addComponent(panelBlur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        refreshTableInventory();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void searchNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchNameActionPerformed
        createFilter(searchName, "- - Select Name - -", "stock_name", filterName, EnumFilterType.EQUAL);
    }//GEN-LAST:event_searchNameActionPerformed

    private void searchCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchCategoryActionPerformed
        Object selectedItem = searchCategory.getSelectedItem();
        if (selectedItem != null && selectedItem.toString() != null) {
            String selectedCategory = searchCategory.getSelectedItem().toString();
            
            if (selectedCategory.equals("- - Select Category - -")) {
                GuiUtil.repopulateComboBox(searchName, "stock_name", "SELECT stock_name FROM " + Main.TB_ITEM_STOCK);
            } else {
                GuiUtil.repopulateComboBox(searchName, "stock_name", "SELECT stock_name FROM " + Main.TB_ITEM_STOCK + " WHERE stock_category = '" + selectedCategory + "'");
            }

            searchName.insertItemAt("- - Select Name - -", 0);
            searchName.setSelectedIndex(0);

            createFilter(searchCategory, "- - Select Category - -", "stock_category", filterCategory, EnumFilterType.EQUAL);
        }
    }//GEN-LAST:event_searchCategoryActionPerformed

    private void searchQuantityStartKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchQuantityStartKeyReleased
        createFilter(searchQuantityStart, null, "stock_quantity", filterQuantityStart, EnumFilterType.EQUAL_OR_GREATER);
    }//GEN-LAST:event_searchQuantityStartKeyReleased

    private void searchQuantityEndKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchQuantityEndKeyReleased
        createFilter(searchQuantityEnd, null, "stock_quantity", filterQuantityEnd, EnumFilterType.EQUAL_OR_LESSER);
    }//GEN-LAST:event_searchQuantityEndKeyReleased

    private void searchQuantityStartKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchQuantityStartKeyTyped
        enforceDigits(evt);
    }//GEN-LAST:event_searchQuantityStartKeyTyped

    private void searchQuantityEndKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchQuantityEndKeyTyped
        enforceDigits(evt);
    }//GEN-LAST:event_searchQuantityEndKeyTyped

    private void searchQuantityStartFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchQuantityStartFocusLost
        if (searchQuantityStart.getText().isEmpty()) {
            searchQuantityStart.setText("1");
        }
    }//GEN-LAST:event_searchQuantityStartFocusLost

    private void searchQuantityEndFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchQuantityEndFocusLost
        if (searchQuantityEnd.getText().isEmpty()) {
            searchQuantityEnd.setText("9999");
        }
    }//GEN-LAST:event_searchQuantityEndFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSearch;
    private javax.swing.JLabel labelFilterCategory;
    private javax.swing.JLabel labelFilterName;
    private javax.swing.JLabel labelFilterQuantity;
    private javax.swing.JLabel labelFilterQuantityFrom;
    private javax.swing.JLabel labelFilterQuantityTo;
    private ProjectINSY.java.ui.panel.GradientPanel panelBlur;
    private javax.swing.JPanel panelMain;
    private javax.swing.JPanel panelSearch;
    private ProjectINSY.java.swing.ComboBoxSuggestion searchCategory;
    private ProjectINSY.java.swing.ComboBoxSuggestion searchName;
    private ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion searchQuantityEnd;
    private ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion searchQuantityStart;
    private ProjectINSY.java.swing.Table tableInventory;
    private javax.swing.JScrollPane tableScroll;
    // End of variables declaration//GEN-END:variables
}
