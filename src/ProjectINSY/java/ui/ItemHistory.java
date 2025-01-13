/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ProjectINSY.java.ui;

import ProjectINSY.java.Main;
import ProjectINSY.java.model.Filter;
import ProjectINSY.java.model.Filter.FilterComparator;
import ProjectINSY.java.model.Filter.FilterOrder;
import ProjectINSY.java.util.GuiUtil;
import static ProjectINSY.java.util.GuiUtil.enforceDigits;
import static ProjectINSY.java.util.GuiUtil.setDefaultField;
import static ProjectINSY.java.util.GuiUtil.setScrollBarCustom;
import ProjectINSY.java.util.TableUtil;
import ProjectINSY.java.util.TableUtil.EnumAlignment;
import static ProjectINSY.java.util.TableUtil.defaultTable;
import static ProjectINSY.java.util.TableUtil.fieldHasValue;
import static ProjectINSY.java.util.TableUtil.fixedColumnAll;
import static ProjectINSY.java.util.TableUtil.getComboSelected;
import static ProjectINSY.java.util.TableUtil.getFieldString;
import static ProjectINSY.java.util.TableUtil.isDefaultComboItem;
import static ProjectINSY.java.util.TableUtil.resetDefaultComboItem;
import static ProjectINSY.java.util.TableUtil.setColumnHorizontalAligment;
import static ProjectINSY.java.util.TableUtil.sorterNumbers;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author admin
 */
public class ItemHistory extends javax.swing.JPanel {

    public String currentSearchQuery = "SELECT * FROM " + Main.TB_ITEM_HISTORY, filterWHERE = "";

    /**
     * Creates new form LogIn
     */
    public ItemHistory() {
        initComponents();

        setScrollBarCustom(tableScroll);

        defaultTable(tableHistory);
        tableHistory.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableHistory.getColumnModel().getColumn(0).setPreferredWidth(200);
        tableHistory.getColumnModel().getColumn(1).setPreferredWidth(200);
        tableHistory.getColumnModel().getColumn(2).setPreferredWidth(250);
        tableHistory.getColumnModel().getColumn(3).setPreferredWidth(974);
        tableHistory.getColumnModel().getColumn(4).setPreferredWidth(200);

//        setColumnHorizontalAligment(tableInventory, 2, EnumAlignment.LEFT);
//        fixedColumnAll(tableInventory);
//        sorterNumbers(tableInventory, 2);
        String[] parts = getFieldString(searchTimestampStart).split("-");
        String month_start = parts[0] + "-" + parts[1] + "-01";
        searchTimestampStart.setText(month_start);
        searchTimestampStart.getDocument().addDocumentListener(new FieldChangeListener());
        searchTimestampEnd.getDocument().addDocumentListener(new FieldChangeListener());
    }

    public void refreshTableInventory() {
        TableUtil.refreshTable(tableHistory, currentSearchQuery, TableUtil.TableEnum.ITEM_HISTORY);
    }

    public void repopulateComboBox() {
        GuiUtil.repopulateComboBox(searchType, "history_frame_type", "SELECT CONCAT(history_frame,'-', history_type) AS history_frame_type FROM " + Main.TB_ITEM_HISTORY);
        resetDefaultComboItem(searchType);

        GuiUtil.repopulateComboBox(searchHolder, "history_user", "SELECT history_user FROM " + Main.TB_ITEM_HISTORY);
        resetDefaultComboItem(searchHolder);
    }

    public void resetSearchQuery() {
        filterWHERE = "";
        if (!isDefaultComboItem(searchType)) {
            String[] parts = getComboSelected(searchType).split("-");
            if (parts.length == 2) {
                filterWHERE += "AND history_frame = '" + parts[0] + "' AND history_type = '" + parts[1] + "' ";
            }
        }
        if (!isDefaultComboItem(searchHolder)) {
            filterWHERE += "AND history_user = '" + getComboSelected(searchHolder) + "' ";
        }
        if (fieldHasValue(searchTimestampStart)) {
            filterWHERE += "AND history_timestamp >= '" + getFieldString(searchTimestampStart) + " 00:00:00' ";
        }
        if (fieldHasValue(searchTimestampEnd)) {
            filterWHERE += "AND history_timestamp <= '" + getFieldString(searchTimestampEnd) + " 23:59:59' ";
        }

        currentSearchQuery = "SELECT *, CONCAT(history_frame,'-', history_type) AS history_frame_type FROM "
                + Main.TB_ITEM_HISTORY + " WHERE 1 "
                + filterWHERE
                + "ORDER BY history_timestamp DESC";

        refreshTableInventory();
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
            resetSearchQuery();
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
        panelMain = new javax.swing.JPanel();
        tableScroll = new javax.swing.JScrollPane();
        tableHistory = new ProjectINSY.java.swing.Table();
        panelSearch = new javax.swing.JPanel();
        searchType = new ProjectINSY.java.swing.ComboBoxSuggestion();
        labelFilterType = new javax.swing.JLabel();
        searchHolder = new ProjectINSY.java.swing.ComboBoxSuggestion();
        labelFilterHolder = new javax.swing.JLabel();
        labelFilterTimestamp = new javax.swing.JLabel();
        searchTimestampStart = new ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion();
        searchTimestampEnd = new ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion();
        labelFilterTimestampFrom = new javax.swing.JLabel();
        labelFilterTimestampTo = new javax.swing.JLabel();

        dateStart.setForeground(new java.awt.Color(25, 102, 24));
        dateStart.setDateFormat("yyyy-MM-dd");
        dateStart.setTextRefernce(searchTimestampStart);

        dateEnd.setForeground(new java.awt.Color(25, 102, 24));
        dateEnd.setDateFormat("yyyy-MM-dd");
        dateEnd.setTextRefernce(searchTimestampEnd);

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

        tableHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Timestamp", "Type", "Item/s", "Description", "Latest Holder"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableHistory.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        tableHistory.setGridColor(new java.awt.Color(255, 255, 255));
        tableHistory.setSelectionBackground(new java.awt.Color(25, 102, 24));
        tableScroll.setViewportView(tableHistory);

        panelSearch.setBackground(new java.awt.Color(255, 255, 255));
        panelSearch.setMaximumSize(new java.awt.Dimension(1277, 71));
        panelSearch.setMinimumSize(new java.awt.Dimension(1277, 71));

        searchType.setBorder(null);
        searchType.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        searchType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTypeActionPerformed(evt);
            }
        });

        labelFilterType.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelFilterType.setText("Type");

        searchHolder.setBorder(null);
        searchHolder.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        searchHolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchHolderActionPerformed(evt);
            }
        });

        labelFilterHolder.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 24)); // NOI18N
        labelFilterHolder.setText("Latest Holder");

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
                .addGap(293, 293, 293)
                .addGroup(panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelFilterType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchType, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 319, Short.MAX_VALUE)
                .addGroup(panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(labelFilterHolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(searchHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        panelSearchLayout.setVerticalGroup(
            panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSearchLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSearchLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(searchHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(labelFilterHolder)
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSearchLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelFilterType)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
                .addComponent(panelSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tableScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 801, Short.MAX_VALUE)
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

    private void searchTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTypeActionPerformed
        resetSearchQuery();
    }//GEN-LAST:event_searchTypeActionPerformed

    private void searchHolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchHolderActionPerformed
        resetSearchQuery();
    }//GEN-LAST:event_searchHolderActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ProjectINSY.java.swing.Date.DateChooser dateEnd;
    private ProjectINSY.java.swing.Date.DateChooser dateStart;
    private javax.swing.JLabel labelFilterHolder;
    private javax.swing.JLabel labelFilterTimestamp;
    private javax.swing.JLabel labelFilterTimestampFrom;
    private javax.swing.JLabel labelFilterTimestampTo;
    private javax.swing.JLabel labelFilterType;
    private javax.swing.JPanel panelMain;
    private javax.swing.JPanel panelSearch;
    private ProjectINSY.java.swing.ComboBoxSuggestion searchHolder;
    private ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion searchTimestampEnd;
    private ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion searchTimestampStart;
    private ProjectINSY.java.swing.ComboBoxSuggestion searchType;
    private ProjectINSY.java.swing.Table tableHistory;
    private javax.swing.JScrollPane tableScroll;
    // End of variables declaration//GEN-END:variables
}
