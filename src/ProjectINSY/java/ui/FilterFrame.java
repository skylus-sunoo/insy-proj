/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ProjectINSY.java.ui;

import ProjectINSY.java.Main;
import ProjectINSY.java.model.Filter;
import ProjectINSY.java.util.GuiUtil;
import static ProjectINSY.java.util.GuiUtil.enforceDigits;
import static ProjectINSY.java.util.GuiUtil.setDefaultField;
import static ProjectINSY.java.util.GuiUtil.setTransparentFrame;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author admin
 */
public final class FilterFrame extends javax.swing.JFrame {

    public final static Filter filterCategory = new Filter(Filter.FilterOrder.START, "stock_category", Filter.FilterComparator.EQUAL, "- - Select Category - -");
    public final static Filter filterName = new Filter(Filter.FilterOrder.NOT_START, "stock_name", Filter.FilterComparator.EQUAL, "- - Select Name - -");
    public final static Filter filterDesc = new Filter(Filter.FilterOrder.NOT_START, "stock_desc", Filter.FilterComparator.EQUAL, "- - Select Description - -");
    public final static Filter filterQuantityStart = new Filter(Filter.FilterOrder.START, "stock_quantity", Filter.FilterComparator.GREATER_THAN, null);
    public final static Filter filterQuantityEnd = new Filter(Filter.FilterOrder.NOT_START, "stock_quantity", Filter.FilterComparator.LESSER_THAN, null);
    public final static Filter filterPriceStart = new Filter(Filter.FilterOrder.NOT_START, "stock_price", Filter.FilterComparator.GREATER_THAN, null);
    public final static Filter filterPriceEnd = new Filter(Filter.FilterOrder.NOT_START, "stock_price", Filter.FilterComparator.LESSER_THAN, null);
    public final static Filter filterDateStart = new Filter(Filter.FilterOrder.NOT_START, "stock_dod", Filter.FilterComparator.GREATER_THAN_DATE, null);
    public final static Filter filterDateEnd = new Filter(Filter.FilterOrder.NOT_START, "stock_dod", Filter.FilterComparator.LESSER_THAN_DATE, null);
    public final static Filter filterHolder = new Filter(Filter.FilterOrder.NOT_START, "stock_user", Filter.FilterComparator.EQUAL, "- - Select Holder - -");

    public static ItemManagement ItemManagement;

    /**
     * Creates new form FilterFrame
     *
     * @param ItemManagement
     */
    public FilterFrame(ItemManagement ItemManagement) {
        FilterFrame.ItemManagement = ItemManagement;
        setAlwaysOnTop(true);

        initComponents();
        getContentPane().setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));
        setBackground(new Color(1.0f, 1.0f, 1.0f, 0.0f));

        setTransparentFrame(btnClose);

        initMoving(FilterFrame.this, panelBody);
        repopulateComboBox();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date parsedDate = dateFormat.parse(Main.filterMinDate);
            dateStart.setSelectedDate(parsedDate);

            parsedDate = dateFormat.parse(Main.filterMaxDate);
            dateEnd.setSelectedDate(parsedDate);
        } catch (ParseException ex) {
            Logger.getLogger(FilterFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        refreshTableInventory();

        searchDateStart.getDocument().addDocumentListener(new FieldChangeListener());
        searchDateEnd.getDocument().addDocumentListener(new FieldChangeListener());
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
            refreshTableFromDate();

            resetSearchQuery();
            refreshTableInventory();
        }
    }

    public void refreshTableFromDate() {
        String date_filter = searchDateEnd.getText();
        if (date_filter.matches(Main.validDatePattern)) {
            try {
                LocalDate.parse(date_filter);
                filterDateEnd.createFilter(searchDateEnd);

            } catch (DateTimeParseException e) {
            }
        }
        date_filter = searchDateStart.getText();
        if (date_filter.matches(Main.validDatePattern)) {
            try {
                LocalDate.parse(date_filter);
                filterDateStart.createFilter(searchDateStart);

            } catch (DateTimeParseException e) {
            }
        }
    }

    public void refreshTableInventory() {
        refreshTableFromDate();
        resetSearchQuery();
        ItemManagement.refreshTableInventory();

//        System.out.println(ItemManagement.currentSearchQuery);
    }

    public void repopulateComboBox() {
        GuiUtil.repopulateComboBox(searchCategory, "stock_category", "SELECT stock_category FROM " + Main.TB_ITEM_STOCK);
        searchCategory.insertItemAt(filterCategory.getDefaultString(), 0);
        searchCategory.setSelectedIndex(0);

        GuiUtil.repopulateComboBox(searchName, "stock_name", "SELECT stock_name FROM " + Main.TB_ITEM_STOCK);
        searchName.insertItemAt(filterName.getDefaultString(), 0);
        searchName.setSelectedIndex(0);

        GuiUtil.repopulateComboBox(searchDesc, "stock_desc", "SELECT stock_desc FROM " + Main.TB_ITEM_STOCK);
        searchDesc.insertItemAt(filterDesc.getDefaultString(), 0);
        searchDesc.setSelectedIndex(0);

        GuiUtil.repopulateComboBox(searchHolder, "stock_user", "SELECT stock_user FROM " + Main.TB_ITEM_STOCK);
        searchHolder.insertItemAt(filterHolder.getDefaultString(), 0);
        searchHolder.setSelectedIndex(0);
    }

    private void resetSearchQuery() {
        
        // also edit 'ItemManagement.radioBatchesActionPerformed' 
        // when editing this method
        if (radioBatches.isSelected()) {
            ItemManagement.groupByBatches = true;
            ItemManagement.currentSearchQuery
                    = "SELECT "
                    + "    stock_id, "
                    + "    stock_batch, "
                    + "    stock_category, "
                    + "    stock_name, "
                    + "    stock_desc, "
                    + "    (stock_price * COUNT(*)) AS stock_price, "
                    + "    stock_dod, "
                    + "    stock_user, "
                    + "    CONCAT( "
                    + "        SUBSTRING_INDEX(MIN(stock_code), '-', 1), '-', "
                    + "        SUBSTRING_INDEX(MIN(stock_code), '-', 2), '-', "
                    + "        RIGHT(MIN(stock_code), LOCATE('-', REVERSE(MIN(stock_code))) - 1), "
                    + "        CASE "
                    + "            WHEN MIN(stock_code) = MAX(stock_code) THEN '' "
                    + "            ELSE CONCAT('-', RIGHT(MAX(stock_code), LOCATE('-', REVERSE(MAX(stock_code))) - 1)) "
                    + "        END "
                    + "    ) AS stock_code, "
                    + "    COUNT(*) AS stock_quantity, "
                    + "    COUNT(stock_name) AS name_count "
                    + "FROM " + Main.TB_ITEM_STOCK + " "
                    + "WHERE "
                    + filterCategory.getFilterSQL()
                    + filterName.getFilterSQL()
                    + filterDesc.getFilterSQL()
                    + filterDateStart.getFilterSQL()
                    + filterDateEnd.getFilterSQL()
                    + filterHolder.getFilterSQL()
                    + " GROUP BY stock_batch"
                    + " HAVING "
                    + filterQuantityStart.getFilterSQL()
                    + filterQuantityEnd.getFilterSQL()
                    + filterPriceStart.getFilterSQL()
                    + filterPriceEnd.getFilterSQL()
                    + " ORDER BY stock_id ASC";
        } else {
            ItemManagement.groupByBatches = false;
            ItemManagement.currentSearchQuery = "SELECT *, 1 AS stock_quantity FROM " + Main.TB_ITEM_STOCK
                    + " WHERE "
                    + filterCategory.getFilterSQL()
                    + filterName.getFilterSQL()
                    + filterDesc.getFilterSQL()
                    + filterDateStart.getFilterSQL()
                    + filterDateEnd.getFilterSQL()
                    + filterHolder.getFilterSQL()
                    + " HAVING "
                    + filterQuantityStart.getFilterSQL()
                    + filterQuantityEnd.getFilterSQL()
                    + filterPriceStart.getFilterSQL()
                    + filterPriceEnd.getFilterSQL()
                    + " ORDER BY stock_id ASC";
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dateStart = new ProjectINSY.java.swing.Date.DateChooser();
        dateEnd = new ProjectINSY.java.swing.Date.DateChooser();
        panelBody = new javax.swing.JPanel();
        labelFilterCategory = new javax.swing.JLabel();
        searchCategory = new ProjectINSY.java.swing.ComboBoxSuggestion();
        labelFilterName = new javax.swing.JLabel();
        searchName = new ProjectINSY.java.swing.ComboBoxSuggestion();
        labelFilterDesc = new javax.swing.JLabel();
        searchDesc = new ProjectINSY.java.swing.ComboBoxSuggestion();
        labelFilterPrice = new javax.swing.JLabel();
        labelFilterPriceFrom = new javax.swing.JLabel();
        searchPriceStart = new ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion();
        labelFilterPriceTo = new javax.swing.JLabel();
        searchPriceEnd = new ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion();
        labelFilterQuantity = new javax.swing.JLabel();
        labelFilterQuantityFrom = new javax.swing.JLabel();
        searchQuantityStart = new ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion();
        labelFilterQuantityTo = new javax.swing.JLabel();
        searchQuantityEnd = new ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion();
        labelFilterHolder = new javax.swing.JLabel();
        searchHolder = new ProjectINSY.java.swing.ComboBoxSuggestion();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        labelFilterTitle = new javax.swing.JLabel();
        btnClose = new javax.swing.JButton();
        labelFilterDateFrom = new javax.swing.JLabel();
        searchDateStart = new ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion();
        labelFilterDate = new javax.swing.JLabel();
        labelFilterDateTo = new javax.swing.JLabel();
        searchDateEnd = new ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion();
        jSeparator6 = new javax.swing.JSeparator();
        radioBatches = new ProjectINSY.java.swing.RadioButtonCustom();

        dateStart.setForeground(new java.awt.Color(25, 102, 24));
        dateStart.setDateFormat("yyyy-MM-dd");
        dateStart.setTextRefernce(searchDateStart);

        dateEnd.setForeground(new java.awt.Color(25, 102, 24));
        dateEnd.setDateFormat("yyyy-MM-dd");
        dateEnd.setTextRefernce(searchDateEnd);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 102, 24), 2));
        panelBody.setMaximumSize(new java.awt.Dimension(666, 300));
        panelBody.setMinimumSize(new java.awt.Dimension(666, 300));

        labelFilterCategory.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 18)); // NOI18N
        labelFilterCategory.setText("Category");

        searchCategory.setBorder(null);
        searchCategory.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        searchCategory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchCategoryActionPerformed(evt);
            }
        });

        labelFilterName.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 18)); // NOI18N
        labelFilterName.setText("Name");

        searchName.setBorder(null);
        searchName.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        searchName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchNameActionPerformed(evt);
            }
        });

        labelFilterDesc.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 18)); // NOI18N
        labelFilterDesc.setText("Description");

        searchDesc.setBorder(null);
        searchDesc.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        searchDesc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchDescActionPerformed(evt);
            }
        });

        labelFilterPrice.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 18)); // NOI18N
        labelFilterPrice.setText("Price Range");

        labelFilterPriceFrom.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        labelFilterPriceFrom.setText("From");

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

        labelFilterPriceTo.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        labelFilterPriceTo.setText("To");

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

        labelFilterQuantity.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 18)); // NOI18N
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

        labelFilterHolder.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 18)); // NOI18N
        labelFilterHolder.setText("Holder");

        searchHolder.setBorder(null);
        searchHolder.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        searchHolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchHolderActionPerformed(evt);
            }
        });

        labelFilterTitle.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelFilterTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelFilterTitle.setText("Item Filters");

        btnClose.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnClose.png"))); // NOI18N
        btnClose.setBorder(null);
        btnClose.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnClose.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnClose_pressed.png"))); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        labelFilterDateFrom.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        labelFilterDateFrom.setText("From");

        searchDateStart.setEditable(true);
        searchDateStart.setBorder(null);
        searchDateStart.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N

        labelFilterDate.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 18)); // NOI18N
        labelFilterDate.setText("Date Range");

        labelFilterDateTo.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        labelFilterDateTo.setText("To");

        searchDateEnd.setEditable(true);
        searchDateEnd.setBorder(null);
        searchDateEnd.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N

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

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(labelFilterDesc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(searchDesc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(labelFilterCategory, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(searchCategory, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(labelFilterName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(searchName, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                    .addComponent(jSeparator2)))
                            .addComponent(jSeparator3)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(panelBodyLayout.createSequentialGroup()
                                            .addComponent(labelFilterPriceFrom)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(searchPriceStart, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(labelFilterPriceTo)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(searchPriceEnd, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
                                        .addComponent(labelFilterPrice, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(labelFilterDate, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addComponent(labelFilterDateFrom)
                                        .addGap(6, 6, 6)
                                        .addComponent(searchDateStart, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(20, 20, 20)
                                        .addComponent(labelFilterDateTo)
                                        .addGap(5, 5, 5)
                                        .addComponent(searchDateEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE)
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(panelBodyLayout.createSequentialGroup()
                                            .addComponent(labelFilterQuantityFrom)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(searchQuantityStart, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(labelFilterQuantityTo)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(searchQuantityEnd, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE))
                                        .addComponent(labelFilterQuantity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jSeparator5))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(labelFilterHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(searchHolder, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                        .addComponent(jSeparator7)))))
                        .addContainerGap())
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(labelFilterTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                        .addComponent(radioBatches, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnClose))))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnClose)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(labelFilterTitle)
                            .addComponent(radioBatches, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(labelFilterCategory)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(labelFilterName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelFilterDesc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(labelFilterPrice)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(searchPriceStart, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labelFilterPriceTo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(searchPriceEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(labelFilterPriceFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(labelFilterQuantity)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(searchQuantityStart, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labelFilterQuantityTo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(searchQuantityEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(labelFilterQuantityFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(labelFilterHolder)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addComponent(labelFilterDate)
                        .addGap(8, 8, 8)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(labelFilterDateFrom, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
                            .addComponent(searchDateStart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(labelFilterDateTo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(searchDateEnd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(6, 6, 6)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBody, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBody, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        ItemManagement.showFilterFrame(true);
//        refreshTableInventory();
//        System.out.println(ItemManagement.currentSearchQuery);
        dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void searchCategoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchCategoryActionPerformed
        Object selectedItem = searchCategory.getSelectedItem();
        if (selectedItem != null && selectedItem.toString() != null) {
            String selectedCategory = searchCategory.getSelectedItem().toString();

            if (selectedCategory.equals(filterCategory.getDefaultString())) {
                GuiUtil.repopulateComboBox(searchName, "stock_name", "SELECT stock_name FROM " + Main.TB_ITEM_STOCK);
            } else {
                GuiUtil.repopulateComboBox(searchName, "stock_name", "SELECT stock_name FROM " + Main.TB_ITEM_STOCK + " WHERE stock_category = '" + selectedCategory + "'");
            }

            searchName.insertItemAt(filterName.getDefaultString(), 0);
            searchName.setSelectedIndex(0);

            if (selectedCategory.equals(filterCategory.getDefaultString())) {
                GuiUtil.repopulateComboBox(searchDesc, "stock_desc", "SELECT stock_desc FROM " + Main.TB_ITEM_STOCK);
            } else {
                GuiUtil.repopulateComboBox(searchDesc, "stock_desc", "SELECT stock_desc FROM " + Main.TB_ITEM_STOCK + " WHERE stock_category = '" + selectedCategory + "'");
            }

            searchDesc.insertItemAt(filterDesc.getDefaultString(), 0);
            searchDesc.setSelectedIndex(0);

            filterCategory.createFilter(searchCategory);
            resetSearchQuery();
            refreshTableInventory();
        }
    }//GEN-LAST:event_searchCategoryActionPerformed

    private void searchNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchNameActionPerformed
        Object selectedItem = searchName.getSelectedItem();
        if (selectedItem != null && selectedItem.toString() != null) {
            String selectedName = searchName.getSelectedItem().toString();

            if (selectedName.equals(filterName.getDefaultString())) {
                GuiUtil.repopulateComboBox(searchDesc, "stock_desc", "SELECT stock_desc FROM " + Main.TB_ITEM_STOCK);
            } else {
                GuiUtil.repopulateComboBox(searchDesc, "stock_desc", "SELECT stock_desc FROM " + Main.TB_ITEM_STOCK + " WHERE stock_name = '" + selectedName + "'");
            }

            searchDesc.insertItemAt(filterDesc.getDefaultString(), 0);
            searchDesc.setSelectedIndex(0);

            filterName.createFilter(searchName);
            resetSearchQuery();
            refreshTableInventory();
        }
    }//GEN-LAST:event_searchNameActionPerformed

    private void searchDescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchDescActionPerformed
        filterDesc.createFilter(searchDesc);
        resetSearchQuery();
        refreshTableInventory();
    }//GEN-LAST:event_searchDescActionPerformed

    private void searchQuantityStartFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchQuantityStartFocusLost
        setDefaultField(searchQuantityStart, Main.filterMinNumber, GuiUtil.FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_searchQuantityStartFocusLost

    private void searchQuantityStartKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchQuantityStartKeyReleased
        filterQuantityStart.createFilter(searchQuantityStart);
        resetSearchQuery();
        refreshTableInventory();
    }//GEN-LAST:event_searchQuantityStartKeyReleased

    private void searchQuantityStartKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchQuantityStartKeyTyped
        enforceDigits(evt);
    }//GEN-LAST:event_searchQuantityStartKeyTyped

    private void searchQuantityEndFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchQuantityEndFocusLost
        setDefaultField(searchQuantityEnd, Main.filterMaxNumber, GuiUtil.FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_searchQuantityEndFocusLost

    private void searchQuantityEndKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchQuantityEndKeyReleased
        filterQuantityEnd.createFilter(searchQuantityEnd);
        resetSearchQuery();
        refreshTableInventory();
    }//GEN-LAST:event_searchQuantityEndKeyReleased

    private void searchQuantityEndKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchQuantityEndKeyTyped
        enforceDigits(evt);
    }//GEN-LAST:event_searchQuantityEndKeyTyped

    private void searchQuantityStartFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchQuantityStartFocusGained
        setDefaultField(searchQuantityStart, Main.filterMinNumber, GuiUtil.FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_searchQuantityStartFocusGained

    private void searchQuantityEndFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchQuantityEndFocusGained
        setDefaultField(searchQuantityEnd, Main.filterMaxNumber, GuiUtil.FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_searchQuantityEndFocusGained

    private void searchPriceStartFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchPriceStartFocusGained
        setDefaultField(searchPriceStart, Main.filterMaxNumber, GuiUtil.FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_searchPriceStartFocusGained

    private void searchPriceStartFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchPriceStartFocusLost
        setDefaultField(searchPriceStart, Main.filterMinNumber, GuiUtil.FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_searchPriceStartFocusLost

    private void searchPriceStartKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchPriceStartKeyReleased
        filterPriceStart.createFilter(searchPriceStart);
        resetSearchQuery();
        refreshTableInventory();
    }//GEN-LAST:event_searchPriceStartKeyReleased

    private void searchPriceStartKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchPriceStartKeyTyped
        enforceDigits(evt);
    }//GEN-LAST:event_searchPriceStartKeyTyped

    private void searchPriceEndFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchPriceEndFocusGained
        setDefaultField(searchPriceEnd, Main.filterMaxNumber, GuiUtil.FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_searchPriceEndFocusGained

    private void searchPriceEndFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_searchPriceEndFocusLost
        setDefaultField(searchPriceEnd, Main.filterMinNumber, GuiUtil.FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_searchPriceEndFocusLost

    private void searchPriceEndKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchPriceEndKeyReleased
        filterPriceEnd.createFilter(searchPriceEnd);
        resetSearchQuery();
        refreshTableInventory();
    }//GEN-LAST:event_searchPriceEndKeyReleased

    private void searchPriceEndKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchPriceEndKeyTyped
        enforceDigits(evt);
    }//GEN-LAST:event_searchPriceEndKeyTyped

    private void searchHolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchHolderActionPerformed
        filterHolder.createFilter(searchHolder);
        resetSearchQuery();
        refreshTableInventory();
    }//GEN-LAST:event_searchHolderActionPerformed

    private void radioBatchesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioBatchesActionPerformed
        resetSearchQuery();
        refreshTableInventory();
    }//GEN-LAST:event_radioBatchesActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FilterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FilterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FilterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FilterFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FilterFrame(ItemManagement).setVisible(true);
            }
        });
    }

    public static void initMoving(JFrame frame, JPanel panel) {
        int[] x = {0};
        int[] y = {0};

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                x[0] = me.getX();
                y[0] = me.getY();
            }
        });

        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent me) {
                int newX = me.getXOnScreen() - x[0];
                int newY = me.getYOnScreen() - y[0];

                int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
                int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

                int frameWidth = frame.getWidth();
                int frameHeight = frame.getHeight();

                newX = Math.max(0, Math.min(newX, screenWidth - frameWidth));
                newY = Math.max(0, Math.min(newY, screenHeight - frameHeight));
                frame.setLocation(newX, newY);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private ProjectINSY.java.swing.Date.DateChooser dateEnd;
    private ProjectINSY.java.swing.Date.DateChooser dateStart;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JLabel labelFilterCategory;
    private javax.swing.JLabel labelFilterDate;
    private javax.swing.JLabel labelFilterDateFrom;
    private javax.swing.JLabel labelFilterDateTo;
    private javax.swing.JLabel labelFilterDesc;
    private javax.swing.JLabel labelFilterHolder;
    private javax.swing.JLabel labelFilterName;
    private javax.swing.JLabel labelFilterPrice;
    private javax.swing.JLabel labelFilterPriceFrom;
    private javax.swing.JLabel labelFilterPriceTo;
    private javax.swing.JLabel labelFilterQuantity;
    private javax.swing.JLabel labelFilterQuantityFrom;
    private javax.swing.JLabel labelFilterQuantityTo;
    private javax.swing.JLabel labelFilterTitle;
    private javax.swing.JPanel panelBody;
    public static ProjectINSY.java.swing.RadioButtonCustom radioBatches;
    private ProjectINSY.java.swing.ComboBoxSuggestion searchCategory;
    private ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion searchDateEnd;
    private ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion searchDateStart;
    private ProjectINSY.java.swing.ComboBoxSuggestion searchDesc;
    private ProjectINSY.java.swing.ComboBoxSuggestion searchHolder;
    private ProjectINSY.java.swing.ComboBoxSuggestion searchName;
    private ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion searchPriceEnd;
    private ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion searchPriceStart;
    private ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion searchQuantityEnd;
    private ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion searchQuantityStart;
    // End of variables declaration//GEN-END:variables
}
