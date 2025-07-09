package ProjectINSY.java;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
import ProjectINSY.java.ui.*;
import ProjectINSY.java.ui.component.MenuLayout;
import static ProjectINSY.java.util.GuiUtil.setForm;
import static ProjectINSY.java.util.SessionUtil.isLoggedIn;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.prefs.Preferences;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.TimingTargetAdapter;

/**
 *
 * @author admin
 */
public class Main extends javax.swing.JFrame {

    //<editor-fold defaultstate="collapsed" desc="Instance">
    private static Main instance;

    public static Main getInstance() {
        if (instance == null) {
            instance = new Main();
        }
        return instance;
    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Database">
    public static String BRANCH_CAMPUS = "Silang";
//    public static String DB_NAME = "db_cvsu_" + BRANCH_CAMPUS.toLowerCase() + "_inventory";";
    public static String DB_NAME = "db_demo_paulash";
    public static String TB_USER = "tb_user";
    public static String TB_CATALOG_ITEM = "tb_catalog_item";
    public static String TB_INVENTORY_BALANCE = "tb_inventory_balance";
    public static String TB_INVENTORY_TRANSACTION = "tb_inventory_transaction";
    
    public static String TB_CATALOG_CATEGORY = "tb_catalog_category";
//    public static String TB_CATALOG_ITEM = "tb_catalog_item";
    public static String TB_ITEM_BATCH = "tb_item_batch";
    public static String TB_ITEM_STOCK = "tb_item_stock";
    public static String TB_ITEM_HISTORY = "tb_item_history";
    public static String TB_ITEM_REQUEST = "tb_item_request";
    public static String TB_ITEM_REPORT = "tb_item_report";
    public static String TB_ITEM_TRANSACTION = "tb_item_out";
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Session">
    private static int userSessionID = -1;
    private static String userSessionEmail = null;

    public static int getUserSessionID() {
        return userSessionID;
    }

    public static void setUserSessionID(int userSessionID) {
        Main.userSessionID = userSessionID;
    }

    public static String getUserSessionEmail() {
        return userSessionEmail;
    }

    public static void setUserSessionEmail(String userSessionEmail) {
        Main.userSessionEmail = userSessionEmail;
//        labelHeader.setText(userSessionEmail);
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="UI Forms">
    private final LogIn LogIn = new LogIn(this);
    private final ItemStock ItemStock = new ItemStock();
    private final ItemRequest ItemRequest = new ItemRequest();
    private final ItemReport ItemReport = new ItemReport();
    private final ItemManagement ItemManagement = new ItemManagement();
    private final ItemCatalog ItemCatalog = new ItemCatalog();
    private final ItemTracker ItemTracker = new ItemTracker();
    private final ItemHistory ItemHistory = new ItemHistory();
    private final ItemTransaction ItemTransaction = new ItemTransaction();
    private final ItemOut ItemOut = new ItemOut();

    public LogIn getLogIn() {
        return LogIn;
    }

    public ItemStock getItemStock() {
        return ItemStock;
    }

    public ItemTransaction getItemTransaction() {
        return ItemTransaction;
    }

    public ItemRequest getItemRequest() {
        return ItemRequest;
    }

    public ItemReport getItemReport() {
        return ItemReport;
    }

    public ItemManagement getItemManagement() {
        return ItemManagement;
    }

    public ItemCatalog getItemCatalog() {
        return ItemCatalog;
    }

    public ItemTracker getItemTracker() {
        return ItemTracker;
    }

    public ItemHistory getItemHistory() {
        return ItemHistory;
    }

    public ItemOut getItemOut() {
        return ItemOut;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Color Scheme">
    public static Color accentGreen = new Color(25, 102, 24);
    public static Color accentViolet = new Color(84, 34, 102);
    //</editor-fold>

    public static String filterMaxNumber = "999999999";
    public static String filterMinNumber = "1";
    public final static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public final static Date date = new Date();
    public static String filterMaxDate = dateFormat.format(date);
    public static String filterMinDate = "2006-04-18";
    public final static String validDatePattern = "^(\\d{4})-(\\d{2})-(\\d{2})$";

    private final MigLayout layout;
    private final MenuLayout menu = new MenuLayout(this);
    private final Animator animator;

    //<editor-fold defaultstate="collapsed" desc="Preferences">
//    private final Preferences PREFS = Preferences.userNodeForPackage(Main.class);
//
//    private final String PREF_LOG_IN = "PREF_LOG_IN";
//    private final String PREF_DEFAULT_PRINT_FORMAT = "PREF_DEFAULT_PRINT_FORMAT";
//    private final String PREF_DEFAULT_LOCATION = "PREF_DEFAULT_LOCATION";
//    private final String PREF_DEFAULT_PAGE_VIEW = "PREF_DEFAULT_PAGE_VIEW";
//
//    private final boolean ACTIVE_PREF_LOGIN = PREFS.getBoolean(PREF_LOG_IN, false);
//    private final String ACTIVE_PREF_BARCODE_EXPORT_FORMAT = PREFS.get(PREF_DEFAULT_PRINT_FORMAT, "PDF");
//    private final String ACTIVE_PREF_DEFAULT_LOCATION = PREFS.get(PREF_DEFAULT_LOCATION, "Supply Room");
//    private final String ACTIVE_PREF_DEFAULT_PAGE_VIEW = PREFS.get(PREF_DEFAULT_PAGE_VIEW, "Item Stock");
//
//    private void initPreferences() {
//        System.out.println(PREF_LOG_IN + ": " + ACTIVE_PREF_LOGIN);
//        System.out.println(PREF_DEFAULT_PRINT_FORMAT + ": " + ACTIVE_PREF_BARCODE_EXPORT_FORMAT);
//        System.out.println(PREF_DEFAULT_LOCATION + ": " + ACTIVE_PREF_DEFAULT_LOCATION);
//        System.out.println(PREF_DEFAULT_PAGE_VIEW + ": " + ACTIVE_PREF_DEFAULT_PAGE_VIEW);
//    }
    //</editor-fold>

    /**
     * Creates new form Dashboard
     */
    private Main() {
//        initPreferences();
        initComponents();
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        layout = new MigLayout("fill", "0[fill]0", "0[fill]0");
        panelMain.setLayer(menu, JLayeredPane.POPUP_LAYER);
        panelMain.setLayout(layout);
        panelMain.add(menu, "pos -1920 100 100% 100%", 0);
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                float x = (fraction * 1920);
                float alpha;
                if (menu.isShow()) {
                    x = -x;
                    alpha = 0.5f - (fraction / 2);
                } else {
                    x -= 1920;
                    alpha = fraction / 2;
                }
                layout.setComponentConstraints(menu, "pos " + (int) x + " 100 100% 100%");
                if (alpha < 0) {
                    alpha = 0;
                }
                menu.setAlpha(alpha);
                panelMain.revalidate();
            }

            @Override
            public void end() {
                menu.setShow(!menu.isShow());
                if (!menu.isShow()) {
                    menu.setVisible(false);
                }
            }

        };
        animator = new Animator(200, target);
        menu.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isLeftMouseButton(me)) {
                    if (!animator.isRunning()) {
                        if (menu.isShow()) {
                            animator.start();
                        }
                    }
                }
            }
        });
        header.addEventMenu((ActionEvent ae) -> {
            if (isLoggedIn()) {
                showMenu();
            }
        });
        LogIn.addEventMenu((ActionEvent ae) -> {
            if (isLoggedIn()) {
                showMenu();
            }
        });

        setLogInForm();
    }

    public void showMenu() {
        if (!animator.isRunning()) {
            if (!menu.isShow()) {
                menu.setVisible(true);
            }
            animator.start();
        }
    }

    public void showHeaderMenu(boolean show) {
        header.showHeaderMenu(show);
    }

    public void setDefaultForm() {
        setItemForm(ItemStock);
    }

    public void setLogInForm() {
        setForm(panelForm, LogIn);
        panelFormBlur.setVisible(false);
    }

    public void setItemForm(JComponent com) {
        setForm(panelForm, com);
        panelFormBlur.setVisible(true);

        switch (com) {
            case ItemStock form -> {
                form.repopulateFilterComboBox();
            }
            case ItemRequest form -> {
                form.repopulateFilterComboBox();
            }
            case ItemReport form -> {
                form.repopulateFilterComboBox();
            }
            case ItemManagement form -> {
                form.repopulateComboBox();
                form.repopulateFilterComboBox();
            }
            case ItemCatalog form -> {
                form.repopulateComboBox();
                form.refreshItemTable();
            }
            case ItemTracker form -> {
                form.ItemTrackerOut.repopulateFilterComboBox();
                form.ItemTrackerScan.repopulateSuggestions();
                form.ItemTrackerScan.setScannerFocus();
            }
            case ItemHistory form -> {
                form.repopulateFilterComboBox();
            }
            case ItemTransaction form -> {
                form.repopulateFilterComboBox();
            }
            case ItemOut form -> {
                form.setScannerFocus();
                form.repopulateSuggestions();
            }
            default -> {
            }
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

        panelMain = new javax.swing.JLayeredPane();
        panelBody = new javax.swing.JPanel();
        header = new ProjectINSY.java.ui.component.Header();
        panelForm = new javax.swing.JPanel();
        panelFormBlur = new ProjectINSY.java.ui.panel.GradientPanel();
        imageBg = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cavite State University Inventory System");
        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(1920, 1080));
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(null);

        panelMain.setMaximumSize(new java.awt.Dimension(1920, 1080));
        panelMain.setMinimumSize(new java.awt.Dimension(1920, 1080));

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setMaximumSize(new java.awt.Dimension(1920, 1080));
        panelBody.setMinimumSize(new java.awt.Dimension(1920, 1080));
        panelBody.setName(""); // NOI18N
        panelBody.setOpaque(false);
        panelBody.setPreferredSize(new java.awt.Dimension(1920, 1080));
        panelBody.setLayout(null);
        panelBody.add(header);
        header.setBounds(0, 0, 1920, 120);

        panelForm.setBackground(new java.awt.Color(241, 239, 241));
        panelForm.setMaximumSize(new java.awt.Dimension(1840, 900));
        panelForm.setMinimumSize(new java.awt.Dimension(1840, 900));
        panelForm.setPreferredSize(new java.awt.Dimension(1840, 900));
        panelForm.setLayout(new java.awt.BorderLayout());
        panelBody.add(panelForm);
        panelForm.setBounds(40, 140, 1840, 900);

        panelFormBlur.setColorEnd(new java.awt.Color(241, 239, 241));
        panelFormBlur.setColorStart(new java.awt.Color(241, 239, 241));
        panelFormBlur.setShadowIntensity(255);

        javax.swing.GroupLayout panelFormBlurLayout = new javax.swing.GroupLayout(panelFormBlur);
        panelFormBlur.setLayout(panelFormBlurLayout);
        panelFormBlurLayout.setHorizontalGroup(
            panelFormBlurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1880, Short.MAX_VALUE)
        );
        panelFormBlurLayout.setVerticalGroup(
            panelFormBlurLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 940, Short.MAX_VALUE)
        );

        panelBody.add(panelFormBlur);
        panelFormBlur.setBounds(20, 120, 1880, 940);

        imageBg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/images/bg.png"))); // NOI18N
        panelBody.add(imageBg);
        imageBg.setBounds(0, 0, 1920, 1080);

        panelMain.add(panelBody);
        panelBody.setBounds(0, 0, 1920, 1080);

        getContentPane().add(panelMain);
        panelMain.setBounds(0, 0, 1920, 1080);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public void addEventMenuLogIn(ActionListener event) {
        LogIn.addEventMenu(event);
    }

    public void addEventMenu(ActionListener event) {
        header.addEventMenu(event);
    }

    public void initMoving(JFrame frame) {
        header.initMoving(frame);
    }

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
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ProjectINSY.java.ui.component.Header header;
    private javax.swing.JLabel imageBg;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelForm;
    private ProjectINSY.java.ui.panel.GradientPanel panelFormBlur;
    private javax.swing.JLayeredPane panelMain;
    // End of variables declaration//GEN-END:variables

}

//        DateFormat df = new SimpleDateFormat("yy");
//        String currentYear = df.format(Calendar.getInstance().getTime());
//
//        Random r = new Random();
//
//        String code = BRANCH_CAMPUS + "-" + currentYear + "-";
//
//        UUID uuid = UUID.randomUUID();
//        String uuidAsString = uuid.toString();
//
//        System.out.println("Your UUID is: " + uuidAsString);
//        System.out.println(code);
//
//        String item_name = "Ball";
//        String item_uom = "UNIT";
//        if (!isAlreadyInColumn(tableProduct, product_name, 3)) {
//        String query = "INSERT INTO " + Main.TB_ITEM_CATALOG + " (item_name, item_uom) VALUES (?, ?)";
//
//        try (Connection conn = Utils.getConnection(Main.DB_NAME);) {
//            PreparedStatement pst = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
//            pst.setString(1, item_name);
//            pst.setString(2, item_uom);
//
//            pst.executeUpdate();
//
//            ResultSet generatedKeys = pst.getGeneratedKeys();
//            if (generatedKeys.next()) {
//                int id = generatedKeys.getInt(1);
//
////                    Queries.addSupplyHistoryEntry(Queries.EnumHistory.CATALOG_PRODUCT_ADD, "[" + id + "] " + product_full_name + "  -  (Retail: " + product_retail_price + ")");
//            }
//
////                clearProductFields();
////                refreshTableProduct();
//            JOptionPane.showMessageDialog(Main.this, "Item Added!", "Success", JOptionPane.INFORMATION_MESSAGE);
//        } catch (SQLException e) {
//            Utils.paneDatabaseError(e);
//        }
//        } else {
//            JOptionPane.showMessageDialog(this, "This product already exists!", "Error", JOptionPane.ERROR_MESSAGE);
//        }
