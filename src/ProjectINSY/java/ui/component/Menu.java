package ProjectINSY.java.ui.component;

import ProjectINSY.java.Main;
import ProjectINSY.java.event.EventMenuSelected;
import ProjectINSY.java.model.Model_Menu;
import ProjectINSY.java.ui.ItemTransaction;
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
        btnItemTransaction.setForeground(new Color(255, 255, 255));
        btnItemReport.setForeground(new Color(255, 255, 255));
        btnItemManagement.setForeground(new Color(255, 255, 255));
        btnItemCatalog.setForeground(new Color(255, 255, 255));
        btnItemTracker.setForeground(new Color(255, 255, 255));
        btnItemHistory.setForeground(new Color(255, 255, 255));
        btnLogOut.setForeground(new Color(255, 255, 255));

        setTransparentFrame(btnItemStock, btnItemTransaction, btnItemReport, btnItemManagement, btnItemCatalog, btnItemTracker, btnItemHistory, btnLogOut);

        pointers = new JComponent[]{pointerItemStock, pointerItemTransaction, pointerItemReport, pointerItemManagement, pointerItemCatalog, pointerItemTracker, pointerItemHistory};
        refreshPointers();
    }

    public Menu() {
        initComponents();

        setTransparentFrame(btnItemStock, btnItemTransaction, btnItemReport, btnItemManagement, btnItemCatalog, btnItemTracker, btnItemHistory, btnLogOut);

        pointers = new JComponent[]{pointerItemStock, pointerItemTransaction, pointerItemReport, pointerItemManagement, pointerItemCatalog, pointerItemTracker, pointerItemHistory};
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
        btnItemManagement = new javax.swing.JButton();
        btnItemCatalog = new javax.swing.JButton();
        btnItemTracker = new javax.swing.JButton();
        btnItemHistory = new javax.swing.JButton();
        pointerItemStock = new javax.swing.JLabel();
        pointerItemManagement = new javax.swing.JLabel();
        pointerItemCatalog = new javax.swing.JLabel();
        pointerItemTracker = new javax.swing.JLabel();
        pointerItemHistory = new javax.swing.JLabel();
        btnLogOut = new javax.swing.JButton();
        btnItemReport = new javax.swing.JButton();
        pointerItemReport = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnItemTransaction = new javax.swing.JButton();
        pointerItemTransaction = new javax.swing.JLabel();

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
        btnItemStock.setText("browse Stock");
        btnItemStock.setBorder(null);
        btnItemStock.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnItemStock.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnItemStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemStockActionPerformed(evt);
            }
        });

        btnItemManagement.setFont(new java.awt.Font("Bebas", 0, 36)); // NOI18N
        btnItemManagement.setForeground(new java.awt.Color(25, 102, 24));
        btnItemManagement.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/iconItemManagement.png"))); // NOI18N
        btnItemManagement.setText("Add Stock");
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
        btnItemCatalog.setText("catalog");
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
        btnItemTracker.setText("Out");
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
        btnItemHistory.setText("STOCK history");
        btnItemHistory.setBorder(null);
        btnItemHistory.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnItemHistory.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnItemHistory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemHistoryActionPerformed(evt);
            }
        });

        pointerItemStock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/menuPointer.png"))); // NOI18N

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

        jLabel1.setFont(new java.awt.Font("Bebas", 0, 42)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("INVENTORY");

        jLabel2.setFont(new java.awt.Font("Bebas", 0, 42)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Transaction");

        btnItemTransaction.setFont(new java.awt.Font("Bebas", 0, 36)); // NOI18N
        btnItemTransaction.setForeground(new java.awt.Color(25, 102, 24));
        btnItemTransaction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/iconItemRequest.png"))); // NOI18N
        btnItemTransaction.setText("transactions");
        btnItemTransaction.setBorder(null);
        btnItemTransaction.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnItemTransaction.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnItemTransaction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnItemTransactionActionPerformed(evt);
            }
        });

        pointerItemTransaction.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/menuPointer.png"))); // NOI18N

        javax.swing.GroupLayout panelBodyLayout = new javax.swing.GroupLayout(panelBody);
        panelBody.setLayout(panelBodyLayout);
        panelBodyLayout.setHorizontalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(imageLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(separatorLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel1)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(btnItemStock)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pointerItemStock))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBodyLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pointerItemReport))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(btnItemCatalog)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(pointerItemCatalog))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(btnItemHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(pointerItemHistory))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addComponent(btnItemManagement)
                                        .addGap(111, 111, 111)
                                        .addComponent(pointerItemManagement))
                                    .addComponent(btnItemReport))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(panelBodyLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelBodyLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addComponent(btnLogOut, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addComponent(btnItemTransaction)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(pointerItemTransaction))
                                    .addGroup(panelBodyLayout.createSequentialGroup()
                                        .addComponent(btnItemTracker, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(pointerItemTracker)))))))
                .addContainerGap())
        );
        panelBodyLayout.setVerticalGroup(
            panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBodyLayout.createSequentialGroup()
                .addComponent(imageLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(separatorLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnItemStock, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pointerItemStock, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnItemManagement, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pointerItemManagement, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnItemCatalog, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pointerItemCatalog, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pointerItemReport, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnItemReport, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnItemHistory, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pointerItemHistory, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(55, 55, 55)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnItemTracker, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pointerItemTracker, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelBodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pointerItemTransaction, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnItemTransaction))
                .addGap(68, 68, 68)
                .addComponent(btnLogOut)
                .addContainerGap(75, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBody, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelBody, javax.swing.GroupLayout.DEFAULT_SIZE, 855, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnItemTransactionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemTransactionActionPerformed
        setMenuIndex(1);
        main.setItemForm(main.getItemTransaction());
        refreshPointers();
    }//GEN-LAST:event_btnItemTransactionActionPerformed

    private void btnItemReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemReportActionPerformed
        setMenuIndex(2);
        main.setItemForm(main.getItemReport());
        refreshPointers();
    }//GEN-LAST:event_btnItemReportActionPerformed

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

    private void btnItemTrackerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemTrackerActionPerformed
        setMenuIndex(5);
        main.setItemForm(main.getItemTracker());
        refreshPointers();
    }//GEN-LAST:event_btnItemTrackerActionPerformed

    private void btnItemCatalogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemCatalogActionPerformed
        setMenuIndex(4);
        main.setItemForm(main.getItemCatalog());
        refreshPointers();
    }//GEN-LAST:event_btnItemCatalogActionPerformed

    private void btnItemManagementActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemManagementActionPerformed
        setMenuIndex(3);
        main.setItemForm(main.getItemManagement());
        refreshPointers();
    }//GEN-LAST:event_btnItemManagementActionPerformed

    private void btnItemStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnItemStockActionPerformed
        setMenuIndex(0);
        main.setItemForm(main.getItemStock());
        refreshPointers();
    }//GEN-LAST:event_btnItemStockActionPerformed

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
    private javax.swing.JButton btnItemStock;
    private javax.swing.JButton btnItemTracker;
    private javax.swing.JButton btnItemTransaction;
    private javax.swing.JButton btnLogOut;
    private javax.swing.JLabel imageLogo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel panelBody;
    private javax.swing.JLabel pointerItemCatalog;
    private javax.swing.JLabel pointerItemHistory;
    private javax.swing.JLabel pointerItemManagement;
    private javax.swing.JLabel pointerItemReport;
    private javax.swing.JLabel pointerItemStock;
    private javax.swing.JLabel pointerItemTracker;
    private javax.swing.JLabel pointerItemTransaction;
    private javax.swing.JSeparator separatorLogo;
    // End of variables declaration//GEN-END:variables
}
