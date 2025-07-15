/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ProjectINSY.java.ui;

import ProjectINSY.java.Main;
import ProjectINSY.java.model.ItemPanel;
import static ProjectINSY.java.util.GuiUtil.setScrollBarCustom;
import ProjectINSY.java.util.TableUtil;
import static ProjectINSY.java.util.GuiUtil.fieldHasValue;
import static ProjectINSY.java.util.GuiUtil.getComboSelected;
import static ProjectINSY.java.util.GuiUtil.getFieldString;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;

/**
 *
 * @author admin
 */
public class ItemTransaction extends ItemPanel {

    /**
     * Creates new form LogIn
     */
    public ItemTransaction() {
        initComponents();

        setScrollBarCustom(scrollTransaction);
        setScrollBarCustom(scrollBreakdown);

        tableTransaction.setDefaultTable();
        tableTransaction.setPriceColumn(3);
        tableBreakdown.setDefaultTable();
        tableBreakdown.setIntegerColumn(1);
        tableBreakdown.setPriceColumn(2);
        tableBreakdown.setPriceColumn(3);

        String[] parts = getFieldString(searchTimestampStart).split("-");
        String month_start = parts[0] + "-" + parts[1] + "-01";
        searchTimestampStart.setText(month_start);
        searchTimestampStart.getDocument().addDocumentListener(new FieldChangeListener());
        searchTimestampEnd.getDocument().addDocumentListener(new FieldChangeListener());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = formatter.parse(month_start);

            dateStart.setSelectedDate(date);
        } catch (ParseException e) {
            e.printStackTrace(System.out);
        }

        tableTransaction.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tableTransaction.getSelectedRow();
                if (selectedRow >= 0) {
                    selectTableTransaction(selectedRow);
                }
            }
        });
    }

    public void selectTableTransaction(int selectedRow) {
        String[] tableRow = TableUtil.selectTableRow(tableTransaction, selectedRow);
        TableUtil.linkFieldsToTable(tableRow, fieldTimestamp, fieldCustomer, fieldChannel, fieldTotal);

        TableUtil.refreshTable(tableBreakdown, "SELECT c.name, s.quantity, s.unit_price, s.total_price FROM "
                + Main.TB_SALES_ITEMS
                + " s JOIN "
                + Main.TB_CATALOG_ITEM
                + " c ON s.item_id = c.item_id JOIN "
                + Main.TB_SALES
                + " sales ON s.sale_id = sales.sale_id"
                + " WHERE sales.created_at = '"
                + fieldTimestamp.getText() + "'", TableUtil.TableEnum.SALES_ITEMS);
    }

    public void clearFields() {
        fieldTimestamp.setText("");
        fieldCustomer.setText("");
        fieldChannel.setText("");
        fieldTotal.setText("");
    }

    //<editor-fold defaultstate="collapsed" desc="Item Panel">
    @Override
    public void refreshItemTable() {
        filterWHERE = "";
//        if (!searchName.isDefaultComboItem()) {
//            filterWHERE += "AND out_name = '" + getComboSelected(searchName) + "' ";
//        }
        if (!searchChannel.isDefaultComboItem()) {
            filterWHERE += "AND channel = '" + getComboSelected(searchChannel) + "' ";
        }
        if (!searchCustomer.isDefaultComboItem()) {
            filterWHERE += "AND customer_name = '" + getComboSelected(searchCustomer) + "' ";
        }
        if (fieldHasValue(searchTimestampStart)) {
            filterWHERE += "AND created_at >= '" + getFieldString(searchTimestampStart) + " 00:00:00' ";
        }
        if (fieldHasValue(searchTimestampEnd)) {
            filterWHERE += "AND created_at <= '" + getFieldString(searchTimestampEnd) + " 23:59:59' ";
        }

        currentSearchQuery = "SELECT * FROM "
                + Main.TB_SALES + " WHERE 1 "
                + filterWHERE
                + "ORDER BY created_at DESC";

        TableUtil.refreshTable(tableTransaction, currentSearchQuery, TableUtil.TableEnum.SALES_TRANSACTION);
    }

    @Override
    public void repopulateFilterComboBox() {
        disableUpdatingComboBoxes();
//        searchName.repopulateComboBox("SELECT name FROM " + Main.TB_SALES);
        searchChannel.repopulateComboBox("SELECT channel FROM " + Main.TB_SALES);
        searchCustomer.repopulateComboBox("SELECT customer_name FROM " + Main.TB_SALES);
//        searchCustomer.repopulateAssociatedComboBox(searchChannel, "out_channel", "SELECT out_customer FROM " + Main.TB_ITEM_TRANSACTION);
        enableUpdatingComboBoxes();

        refreshItemTable();
    }

    @Override
    public void repopulateComboBox() {
    }
    //</editor-fold>

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
            refreshItemTable();
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
        separatorChannel = new javax.swing.JSeparator();
        labelCustomer = new javax.swing.JLabel();
        fieldCustomer = new javax.swing.JLabel();
        separatorTotal = new javax.swing.JSeparator();
        separatorCustomer = new javax.swing.JSeparator();
        fieldChannel = new javax.swing.JLabel();
        labelTotal = new javax.swing.JLabel();
        labelChannel = new javax.swing.JLabel();
        fieldTotal = new javax.swing.JLabel();
        fieldTimestamp = new javax.swing.JLabel();
        labelTimestamp = new javax.swing.JLabel();
        separatorTimestamp = new javax.swing.JSeparator();
        panelMain = new javax.swing.JPanel();
        panelSales = new javax.swing.JPanel();
        scrollTransaction = new javax.swing.JScrollPane();
        tableTransaction = new ProjectINSY.java.swing.Table();
        panelSearch = new javax.swing.JPanel();
        searchChannel = new ProjectINSY.java.swing.ComboBoxSuggestion();
        labelFilterChannel = new javax.swing.JLabel();
        labelFilterTimestamp = new javax.swing.JLabel();
        searchTimestampStart = new ProjectINSY.java.swing.Form.FormFieldSuggestion();
        searchTimestampEnd = new ProjectINSY.java.swing.Form.FormFieldSuggestion();
        labelFilterTimestampFrom = new javax.swing.JLabel();
        labelFilterTimestampTo = new javax.swing.JLabel();
        labelFilterCustomer = new javax.swing.JLabel();
        searchCustomer = new ProjectINSY.java.swing.ComboBoxSuggestion();
        panelExport = new javax.swing.JPanel();
        labelExport = new javax.swing.JLabel();
        btnExport = new javax.swing.JButton();
        panelSalesInfo = new javax.swing.JPanel();
        panelScan = new javax.swing.JPanel();
        labelScan = new javax.swing.JLabel();
        scrollBreakdown = new javax.swing.JScrollPane();
        tableBreakdown = new ProjectINSY.java.swing.Table();

        dateStart.setForeground(new java.awt.Color(25, 102, 24));
        dateStart.setDateFormat("yyyy-MM-dd");
        dateStart.setTextRefernce(searchTimestampStart);

        dateEnd.setForeground(new java.awt.Color(25, 102, 24));
        dateEnd.setDateFormat("yyyy-MM-dd");
        dateEnd.setTextRefernce(searchTimestampEnd);

        labelCustomer.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        labelCustomer.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        labelCustomer.setText("Customer");
        labelCustomer.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        fieldCustomer.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldCustomer.setText("No Sale Selected");
        fieldCustomer.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        fieldChannel.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldChannel.setText("No Sale Selected");
        fieldChannel.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        labelTotal.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        labelTotal.setText("Total Amount");
        labelTotal.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        labelChannel.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        labelChannel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        labelChannel.setText("Channel");
        labelChannel.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        fieldTotal.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldTotal.setText("No Sale Selected");
        fieldTotal.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        fieldTimestamp.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldTimestamp.setText("No Sale Selected");
        fieldTimestamp.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        labelTimestamp.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        labelTimestamp.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        labelTimestamp.setText("Timestamp");
        labelTimestamp.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        setMaximumSize(new java.awt.Dimension(1840, 900));
        setMinimumSize(new java.awt.Dimension(1840, 900));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(1840, 900));

        panelMain.setBackground(new java.awt.Color(255, 255, 255));
        panelMain.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 102, 24), 2));
        panelMain.setMaximumSize(new java.awt.Dimension(1840, 900));
        panelMain.setMinimumSize(new java.awt.Dimension(1840, 900));
        panelMain.setPreferredSize(new java.awt.Dimension(1840, 900));

        panelSales.setBackground(new java.awt.Color(255, 255, 255));
        panelSales.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 102, 24), 2));

        scrollTransaction.setBorder(null);

        tableTransaction.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Timestamp", "Customer", "Channel", "Total Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class
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
        tableTransaction.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        tableTransaction.setGridColor(new java.awt.Color(255, 255, 255));
        tableTransaction.setSelectionBackground(new java.awt.Color(25, 102, 24));
        scrollTransaction.setViewportView(tableTransaction);

        panelSearch.setBackground(new java.awt.Color(255, 255, 255));
        panelSearch.setMaximumSize(new java.awt.Dimension(1277, 71));
        panelSearch.setMinimumSize(new java.awt.Dimension(1277, 71));

        searchChannel.setBorder(null);
        searchChannel.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        searchChannel.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                searchChannelItemStateChanged(evt);
            }
        });

        labelFilterChannel.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelFilterChannel.setText("Channel");

        labelFilterTimestamp.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelFilterTimestamp.setText("Date Range");

        searchTimestampStart.setBorder(null);
        searchTimestampStart.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N

        searchTimestampEnd.setBorder(null);
        searchTimestampEnd.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N

        labelFilterTimestampFrom.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        labelFilterTimestampFrom.setText("From");

        labelFilterTimestampTo.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        labelFilterTimestampTo.setText("To");

        labelFilterCustomer.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelFilterCustomer.setText("Customer");

        searchCustomer.setBorder(null);
        searchCustomer.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        searchCustomer.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                searchCustomerItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout panelSearchLayout = new javax.swing.GroupLayout(panelSearch);
        panelSearch.setLayout(panelSearchLayout);
        panelSearchLayout.setHorizontalGroup(
            panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSearchLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelSearchLayout.createSequentialGroup()
                        .addComponent(labelFilterTimestampFrom)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchTimestampStart, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(labelFilterTimestampTo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchTimestampEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(labelFilterTimestamp, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelFilterCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelFilterChannel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchChannel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelSearchLayout.setVerticalGroup(
            panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSearchLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSearchLayout.createSequentialGroup()
                        .addComponent(labelFilterChannel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchChannel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelSearchLayout.createSequentialGroup()
                        .addComponent(labelFilterCustomer)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelSearchLayout.createSequentialGroup()
                        .addComponent(labelFilterTimestamp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(searchTimestampStart, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(labelFilterTimestampTo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(searchTimestampEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(labelFilterTimestampFrom, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelExport.setBackground(new java.awt.Color(255, 255, 255));
        panelExport.setLayout(null);

        labelExport.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelExport.setForeground(new java.awt.Color(255, 255, 255));
        labelExport.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/iconPrint.png"))); // NOI18N
        labelExport.setText("Export");
        labelExport.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelExport.add(labelExport);
        labelExport.setBounds(0, 0, 140, 50);

        btnExport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnPrint.png"))); // NOI18N
        btnExport.setBorder(null);
        btnExport.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExport.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnPrint_pressed.png"))); // NOI18N
        btnExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportActionPerformed(evt);
            }
        });
        panelExport.add(btnExport);
        btnExport.setBounds(0, 0, 150, 49);

        javax.swing.GroupLayout panelSalesLayout = new javax.swing.GroupLayout(panelSales);
        panelSales.setLayout(panelSalesLayout);
        panelSalesLayout.setHorizontalGroup(
            panelSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSalesLayout.createSequentialGroup()
                .addContainerGap(874, Short.MAX_VALUE)
                .addComponent(panelExport, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panelSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelSalesLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(panelSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(scrollTransaction, javax.swing.GroupLayout.PREFERRED_SIZE, 1026, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(panelSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        panelSalesLayout.setVerticalGroup(
            panelSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSalesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelExport, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panelSalesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelSalesLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(panelSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(scrollTransaction, javax.swing.GroupLayout.PREFERRED_SIZE, 706, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(85, Short.MAX_VALUE)))
        );

        panelSalesInfo.setBackground(new java.awt.Color(255, 255, 255));
        panelSalesInfo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 102, 24), 2));

        panelScan.setBackground(new java.awt.Color(25, 102, 24));

        labelScan.setBackground(new java.awt.Color(25, 102, 24));
        labelScan.setFont(new java.awt.Font("Bebas", 0, 64)); // NOI18N
        labelScan.setForeground(new java.awt.Color(255, 255, 255));
        labelScan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelScan.setText("SALE BREAKDOWN");
        labelScan.setOpaque(true);

        javax.swing.GroupLayout panelScanLayout = new javax.swing.GroupLayout(panelScan);
        panelScan.setLayout(panelScanLayout);
        panelScanLayout.setHorizontalGroup(
            panelScanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(panelScanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelScanLayout.createSequentialGroup()
                    .addGap(0, 192, Short.MAX_VALUE)
                    .addComponent(labelScan)
                    .addGap(0, 193, Short.MAX_VALUE)))
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

        scrollBreakdown.setBorder(null);

        tableBreakdown.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item", "Quantity", "Unit Price", "Total Price"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Float.class, java.lang.Float.class
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
        tableBreakdown.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        tableBreakdown.setSelectionBackground(new java.awt.Color(25, 102, 24));
        scrollBreakdown.setViewportView(tableBreakdown);

        javax.swing.GroupLayout panelSalesInfoLayout = new javax.swing.GroupLayout(panelSalesInfo);
        panelSalesInfo.setLayout(panelSalesInfoLayout);
        panelSalesInfoLayout.setHorizontalGroup(
            panelSalesInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelScan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSalesInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollBreakdown, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelSalesInfoLayout.setVerticalGroup(
            panelSalesInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSalesInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelScan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollBreakdown, javax.swing.GroupLayout.DEFAULT_SIZE, 782, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout panelMainLayout = new javax.swing.GroupLayout(panelMain);
        panelMain.setLayout(panelMainLayout);
        panelMainLayout.setHorizontalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMainLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelSales, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelSalesInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelMainLayout.setVerticalGroup(
            panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelSales, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelSalesInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void searchCustomerItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_searchCustomerItemStateChanged
        refreshItemTable();
    }//GEN-LAST:event_searchCustomerItemStateChanged

    private void searchChannelItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_searchChannelItemStateChanged
//        if (isUpdatingComboBoxes) {
//            return;
//        }
//
//        disableUpdatingComboBoxes();
//        searchCustomer.repopulateAssociatedComboBox(searchChannel, "out_channel", "SELECT out_customer FROM " + Main.TB_ITEM_TRANSACTION);
//        enableUpdatingComboBoxes();

        refreshItemTable();
    }//GEN-LAST:event_searchChannelItemStateChanged

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
        exportSQLToCSV(getCurrentSearchQuery(), "ItemSales");
    }//GEN-LAST:event_btnExportActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExport;
    private ProjectINSY.java.swing.Date.DateChooser dateEnd;
    private ProjectINSY.java.swing.Date.DateChooser dateStart;
    private javax.swing.JLabel fieldChannel;
    private javax.swing.JLabel fieldCustomer;
    private javax.swing.JLabel fieldTimestamp;
    private javax.swing.JLabel fieldTotal;
    private javax.swing.JLabel labelChannel;
    private javax.swing.JLabel labelCustomer;
    private javax.swing.JLabel labelExport;
    private javax.swing.JLabel labelFilterChannel;
    private javax.swing.JLabel labelFilterCustomer;
    private javax.swing.JLabel labelFilterTimestamp;
    private javax.swing.JLabel labelFilterTimestampFrom;
    private javax.swing.JLabel labelFilterTimestampTo;
    private javax.swing.JLabel labelScan;
    private javax.swing.JLabel labelTimestamp;
    private javax.swing.JLabel labelTotal;
    private javax.swing.JPanel panelExport;
    private javax.swing.JPanel panelMain;
    private javax.swing.JPanel panelSales;
    private javax.swing.JPanel panelSalesInfo;
    private javax.swing.JPanel panelScan;
    private javax.swing.JPanel panelSearch;
    private javax.swing.JScrollPane scrollBreakdown;
    private javax.swing.JScrollPane scrollTransaction;
    private ProjectINSY.java.swing.ComboBoxSuggestion searchChannel;
    private ProjectINSY.java.swing.ComboBoxSuggestion searchCustomer;
    private ProjectINSY.java.swing.Form.FormFieldSuggestion searchTimestampEnd;
    private ProjectINSY.java.swing.Form.FormFieldSuggestion searchTimestampStart;
    private javax.swing.JSeparator separatorChannel;
    private javax.swing.JSeparator separatorCustomer;
    private javax.swing.JSeparator separatorTimestamp;
    private javax.swing.JSeparator separatorTotal;
    private ProjectINSY.java.swing.Table tableBreakdown;
    private ProjectINSY.java.swing.Table tableTransaction;
    // End of variables declaration//GEN-END:variables
}
