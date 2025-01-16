package ProjectINSY.java.ui.component;

import ProjectINSY.java.Main;
import ProjectINSY.java.event.EventMenuSelected;
import ProjectINSY.java.model.Model_Menu;
import static ProjectINSY.java.util.GuiUtil.setTransparentFrame;
import static ProjectINSY.java.util.SessionUtil.removeUserSession;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Path2D;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Menu extends javax.swing.JPanel {

    private int menuIndex = 0;
    private final JComponent[] pointers;

    public int getMenuIndex() {
        return menuIndex;
    }

    public void setMenuIndex(int menuIndex) {
        this.menuIndex = menuIndex;
    }

    private Main main;

    public Menu(Main main) {
        this.main = main;

        initComponents();

        btnItemStock.setForeground(new Color(255, 255, 255));
        btnItemRequest.setForeground(new Color(255, 255, 255));
        btnItemReport.setForeground(new Color(255, 255, 255));
        btnItemManagement.setForeground(new Color(255, 255, 255));
        btnItemCatalog.setForeground(new Color(255, 255, 255));
        btnItemTracker.setForeground(new Color(255, 255, 255));
        btnItemHistory.setForeground(new Color(255, 255, 255));
        btnLogOut.setForeground(new Color(255, 255, 255));

        setTransparentFrame(btnItemStock, btnItemRequest, btnItemReport, btnItemManagement, btnItemCatalog, btnItemTracker, btnItemHistory, btnLogOut);

        pointers = new JComponent[]{pointerItemStock, pointerItemRequest, pointerItemReport, pointerItemManagement, pointerItemCatalog, pointerItemTracker, pointerItemHistory};
        refreshPointers();
    }

    public Menu() {
        initComponents();

        setTransparentFrame(btnItemStock, btnItemRequest, btnItemReport, btnItemManagement, btnItemCatalog, btnItemTracker, btnItemHistory, btnLogOut);

        pointers = new JComponent[]{pointerItemStock, pointerItemRequest, pointerItemReport, pointerItemManagement, pointerItemCatalog, pointerItemTracker, pointerItemHistory};
        refreshPointers();
    }

    private void refreshPointers() {
        for (int i = 0; i < pointers.length; i++) {
            pointers[i].setVisible(i == menuIndex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelBody = new javax.swing.JPanel();
        imageLogo = new javax.swing.JLabel();
        separatorLogo = new javax.swing.JSeparator();
        btnItemStock = new javax.swing.JButton();
        btnItemRequest = new javax.swing.JButton();
        btnItemManagement = new javax.swing.JButton();
        btnItemCatalog = new javax.swing.JButton();
        btnItemTracker = new javax.swing.JButton();
        btnItemHistory = new javax.swing.JButton();
        pointerItemStock = new javax.swing.JLabel();
        pointerItemRequest = new javax.swing.JLabel();
        pointerItemManagement = new javax.swing.JLabel();
        pointerItemCatalog = new javax.swing.JLabel();
        pointerItemTracker = new javax.swing.JLabel();
        pointerItemHistory = new javax.swing.JLabel();
        btnLogOut = new javax.swing.JButton();
        btnItemReport = new javax.swing.JButton();
        pointerItemReport = new javax.swing.JLabel();
        separatorLogo1 = new javax.swing.JSeparator();
        separatorLogo2 = new javax.swing.JSeparator();
        separatorLogo3 = new javax.swing.JSeparator();
        separatorLogo4 = new javax.swing.JSeparator();

        setBackground(new java.awt.Color(249, 215, 30));

        panelBody.setBackground(new java.awt.Color(25, 102, 24));
        panelBody.setPreferredSize(new java.awt.Dimension(333, 628));

        imageLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imageLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/images/cvsu-silang-logo_alt.png"))); // NOI18N

        separatorLogo.setBackground(new java.awt.Color(0, 0, 0));
        separatorLogo.setForeground(new java.awt.Color(0, 0, 0));

        btnItemStock.setFont(new java.awt.Font("Bebas", 0, 36)); // NOI18N
        btnItemStock.setForeground(new java.awt.Color(25, 102, 24));
        btnItemStock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/iconItemStock.png"))); // NOI18N
        btnItemStock.setText("ITEM STOCK");
        btnItemStock.setBorder(null);
        btnItemStock.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnItemStock.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnItemStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemStockActionPerformed(evt);
            }
        });

        btnItemRequest.setFont(new java.awt.Font("Bebas", 0, 36)); // NOI18N
        btnItemRequest.setForeground(new java.awt.Color(25, 102, 24));
        btnItemRequest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/iconItemRequest.png"))); // NOI18N
        btnItemRequest.setText("ITEM Request");
        btnItemRequest.setBorder(null);
        btnItemRequest.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnItemRequest.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnItemRequest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemRequestActionPerformed(evt);
            }
        });

        btnItemManagement.setFont(new java.awt.Font("Bebas", 0, 36)); // NOI18N
        btnItemManagement.setForeground(new java.awt.Color(25, 102, 24));
        btnItemManagement.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/iconItemManagement.png"))); // NOI18N
        btnItemManagement.setText("ITEM MANAGEMENT");
        btnItemManagement.setBorder(null);
        btnItemManagement.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnItemManagement.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnItemManagement.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemManagementActionPerformed(evt);
            }
        });

        btnItemCatalog.setFont(new java.awt.Font("Bebas", 0, 36)); // NOI18N
        btnItemCatalog.setForeground(new java.awt.Color(25, 102, 24));
        btnItemCatalog.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/iconItemCatalog.png"))); // NOI18N
        btnItemCatalog.setText("ITEM Catalog");
        btnItemCatalog.setBorder(null);
        btnItemCatalog.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnItemCatalog.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnItemCatalog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemCatalogActionPerformed(evt);
            }
        });

        btnItemTracker.setFont(new java.awt.Font("Bebas", 0, 36)); // NOI18N
        btnItemTracker.setForeground(new java.awt.Color(25, 102, 24));
        btnItemTracker.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/iconItemTracker.png"))); // NOI18N
        btnItemTracker.setText("ITEM Tracker");
        btnItemTracker.setBorder(null);
        btnItemTracker.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnItemTracker.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnItemTracker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemTrackerActionPerformed(evt);
            }
        });

        btnItemHistory.setFont(new java.awt.Font("Bebas", 0, 36)); // NOI18N
        btnItemHistory.setForeground(new java.awt.Color(25, 102, 24));
        btnItemHistory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/iconItemHistory.png"))); // NOI18N
        btnItemHistory.setText("ITEM HISTORY");
        btnItemHistory.setBorder(null);
        btnItemHistory.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnItemHistory.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnItemHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemHistoryActionPerformed(evt);
            }
        });

        pointerItemStock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/menuPointer.png"))); // NOI18N

        pointerItemRequest.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/menuPointer.png"))); // NOI18N

        pointerItemManagement.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/menuPointer.png"))); // NOI18N

        pointerItemCatalog.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/menuPointer.png"))); // NOI18N

        pointerItemTracker.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/menuPointer.png"))); // NOI18N

        pointerItemHistory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/menuPointer.png"))); // NOI18N

        btnLogOut.setFont(new java.awt.Font("Bebas", 0, 36)); // NOI18N
        btnLogOut.setForeground(new java.awt.Color(25, 102, 24));
        btnLogOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnLogOut.png"))); // NOI18N
        btnLogOut.setText("LOG OUT");
        btnLogOut.setBorder(null);
        btnLogOut.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogOut.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnLogOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogOutActionPerformed(evt);
            }
        });

        btnItemReport.setFont(new java.awt.Font("Bebas", 0, 36)); // NOI18N
        btnItemReport.setForeground(new java.awt.Color(25, 102, 24));
        btnItemReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/iconItemReport.png"))); // NOI18N
        btnItemReport.setText("ITEM Report");
        btnItemReport.setBorder(null);
        btnItemReport.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnItemReport.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnItemReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemReportActionPerformed(evt);
            }
        });

        pointerItemReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/menuPointer.png"))); // NOI18N

        separatorLogo1.setBackground(new java.awt.Color(0, 0, 0));
        separatorLogo1.setForeground(new java.awt.Color(0, 0, 0));

        separatorLogo2.setBackground(new java.awt.Color(0, 0, 0));
        separatorLogo2.setForeground(new java.awt.Color(0, 0, 0));

        separatorLogo3.setBackground(new java.awt.Color(0, 0, 0));
        separatorLogo3.setForeground(new java.awt.Color(0, 0, 0));

        separatorLogo4.setBackground(new java.awt.Color(0, 0, 0));
        separatorLogo4.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(imageLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(separatorLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addComponent(btnLogOut, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addComponent(btnItemReport)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(pointerItemReport))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                                        .addComponent(btnItemRequest)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(pointerItemRequest))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                                        .addComponent(btnItemStock)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(pointerItemStock))
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addComponent(btnItemTracker, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(pointerItemTracker))
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addComponent(btnItemHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(pointerItemHistory))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                                        .addComponent(btnItemManagement, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(pointerItemManagement))
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addComponent(btnItemCatalog, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(pointerItemCatalog)))))))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(separatorLogo3, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(separatorLogo1, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(separatorLogo2, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(separatorLogo4, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addComponent(imageLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(separatorLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnItemStock, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pointerItemStock, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(separatorLogo1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnItemRequest, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pointerItemRequest, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnItemReport, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pointerItemReport, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(separatorLogo2, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnItemManagement, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pointerItemManagement, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnItemCatalog, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pointerItemCatalog, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(separatorLogo3, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnItemTracker, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pointerItemTracker, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addComponent(separatorLogo4, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnItemHistory, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pointerItemHistory, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(btnLogOut)
                .addGap(112, 112, 112))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBody, javax.swing.GroupLayout.DEFAULT_SIZE, 755, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnItemStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemStockActionPerformed
        setMenuIndex(0);
        main.setItemForm(main.getItemStock());
        refreshPointers();
    }//GEN-LAST:event_btnItemStockActionPerformed

    private void btnItemManagementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemManagementActionPerformed
        setMenuIndex(3);
        main.setItemForm(main.getItemManagement());
        refreshPointers();
    }//GEN-LAST:event_btnItemManagementActionPerformed

    private void btnItemCatalogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemCatalogActionPerformed
        setMenuIndex(4);
        main.setItemForm(main.getItemCatalog());
        refreshPointers();
    }//GEN-LAST:event_btnItemCatalogActionPerformed

    private void btnItemTrackerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemTrackerActionPerformed
        setMenuIndex(5);
        main.setItemForm(main.getItemTracker());
        refreshPointers();
    }//GEN-LAST:event_btnItemTrackerActionPerformed

    private void btnLogOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogOutActionPerformed
        int warnUser = JOptionPane.showConfirmDialog(
                null,
                "Are you sure you want to log out?",
                "Log Out Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (warnUser == JOptionPane.YES_OPTION) {
            setMenuIndex(0);
            refreshPointers();

            removeUserSession();
            main.showMenu();
            main.showHeaderMenu(false);
            main.setLogInForm();
        }
    }//GEN-LAST:event_btnLogOutActionPerformed

    private void btnItemHistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemHistoryActionPerformed
        setMenuIndex(6);
        main.setItemForm(main.getItemHistory());
        refreshPointers();
    }//GEN-LAST:event_btnItemHistoryActionPerformed

    private void btnItemRequestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemRequestActionPerformed
        setMenuIndex(1);
        main.setItemForm(main.getItemRequest());
        refreshPointers();
    }//GEN-LAST:event_btnItemRequestActionPerformed

    private void btnItemReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemReportActionPerformed
        setMenuIndex(2);
        main.setItemForm(main.getItemReport());
        refreshPointers();
    }//GEN-LAST:event_btnItemReportActionPerformed

    @Override
    protected void paintChildren(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint g = new GradientPaint(0, 0, Color.decode("#e74c3c"), 0, getHeight(), Color.decode("#000000"));
        int height = 140;
        Path2D.Float f = new Path2D.Float();
        f.moveTo(0, 0);
        f.curveTo(0, 0, 0, 70, 100, 70);
        f.curveTo(100, 70, getWidth(), 70, getWidth(), height);
        f.lineTo(getWidth(), getHeight());
        f.lineTo(0, getHeight());
        g2.setColor(new Color(60, 60, 60));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setPaint(g);
        g2.fill(f);
        super.paintChildren(grphcs);
    }

    private int x;
    private int y;

    public void initMoving(JFrame fram) {
        imageLogo.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                x = me.getX();
                y = me.getY();
            }

        });
        imageLogo.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent me) {
                fram.setLocation(me.getXOnScreen() - x, me.getYOnScreen() - y);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnItemCatalog;
    private javax.swing.JButton btnItemHistory;
    private javax.swing.JButton btnItemManagement;
    private javax.swing.JButton btnItemReport;
    private javax.swing.JButton btnItemRequest;
    private javax.swing.JButton btnItemStock;
    private javax.swing.JButton btnItemTracker;
    private javax.swing.JButton btnLogOut;
    private javax.swing.JLabel imageLogo;
    private javax.swing.JPanel panelBody;
    private javax.swing.JLabel pointerItemCatalog;
    private javax.swing.JLabel pointerItemHistory;
    private javax.swing.JLabel pointerItemManagement;
    private javax.swing.JLabel pointerItemReport;
    private javax.swing.JLabel pointerItemRequest;
    private javax.swing.JLabel pointerItemStock;
    private javax.swing.JLabel pointerItemTracker;
    private javax.swing.JSeparator separatorLogo;
    private javax.swing.JSeparator separatorLogo1;
    private javax.swing.JSeparator separatorLogo2;
    private javax.swing.JSeparator separatorLogo3;
    private javax.swing.JSeparator separatorLogo4;
    // End of variables declaration//GEN-END:variables
}
