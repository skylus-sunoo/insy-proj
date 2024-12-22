package ProjectINSY.java;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
import ProjectINSY.java.ui.*;
import ProjectINSY.java.ui.component.MenuLayout;
import static ProjectINSY.java.util.GuiUtil.setForm;
import static ProjectINSY.java.util.SessionUtil.isLoggedIn;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    public static String DB_NAME = "db_cvsu_" + BRANCH_CAMPUS.toLowerCase() + "_inventory";
    public static String TB_USER = "tb_user";
    public static String TB_CATALOG_CATEGORY = "tb_catalog_category";
    public static String TB_CATALOG_ITEM = "tb_catalog_item";
    public static String TB_ITEM_BATCH = "tb_item_batch";
    public static String TB_ITEM_STOCK = "tb_item_stock";
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
    private final ItemManagement ItemManagement = new ItemManagement();
    private final ItemCatalog ItemCatalog = new ItemCatalog();
    private final ItemHistory ItemHistory = new ItemHistory();

    public LogIn getLogIn() {
        return LogIn;
    }

    public ItemStock getItemStock() {
        return ItemStock;
    }

    public ItemManagement getItemManagement() {
        return ItemManagement;
    }

    public ItemCatalog getItemCatalog() {
        return ItemCatalog;
    }

    public ItemHistory getItemHistory() {
        return ItemHistory;
    }
    //</editor-fold>

    public static String filterMaxNumber = "999999999";
    public static String filterMinNumber = "1";
    public static String filterMaxDate = "2100-01-01";
    public static String filterMinDate = "2006-04-18";
    public final static String validDatePattern = "^(\\d{4})-(\\d{2})-(\\d{2})$";

    private final MigLayout layout;
    private final MenuLayout menu = new MenuLayout(this);
    private final Animator animator;

//    public static void setTextHeader(){
//        System.out.println(userSessionEmail);
//    }
    /**
     * Creates new form Dashboard
     */
    private Main() {
        initComponents();

//        String basePath = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath())
//                .getParentFile()
//                .getParent();
//
//        File batFile = new File(basePath, "start_xampp.bat");
//
//        if (!batFile.exists()) {
//            System.out.println("Batch file not found: " + batFile.getAbsolutePath());
//        }
//
//        try {
//            Runtime.getRuntime().exec("cmd /c start /B /MIN " + batFile.getAbsolutePath());
//        } catch (IOException ex) {
//            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//        }
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        layout = new MigLayout("fill", "0[fill]0", "0[fill]0");
        panelMain.setLayer(menu, JLayeredPane.POPUP_LAYER);
        panelMain.setLayout(layout);
        panelMain.add(menu, "pos -1366 70 100% 100%", 0);
        TimingTarget target = new TimingTargetAdapter() {
            @Override
            public void timingEvent(float fraction) {
                float x = (fraction * 1366);
                float alpha;
                if (menu.isShow()) {
                    x = -x;
                    alpha = 0.5f - (fraction / 2);
                } else {
                    x -= 1366;
                    alpha = fraction / 2;
                }
                layout.setComponentConstraints(menu, "pos " + (int) x + " 70 100% 100%");
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

        setForm(panelForm, LogIn);
    }

    public void showMenu() {
        if (!animator.isRunning()) {
            if (!menu.isShow()) {
                menu.setVisible(true);
            }
            animator.start();
        }
    }

    public void setDefaultForm() {
        setForm(panelForm, ItemStock);
        ItemStock.repopulateComboBox();
        ItemStock.refreshTableInventory();
    }

    public void setLogInForm() {
        setForm(panelForm, LogIn);
    }

    public void setItemForm(JComponent com) {
        setForm(panelForm, com);

        switch (com) {
            case ItemStock form -> {
                form.repopulateComboBox();
                form.refreshTableInventory();
            }
            case ItemManagement form -> {
                form.repopulateNameComboBox();
                form.refreshTableInventory();
            }
            case ItemCatalog form -> {
                form.refreshTableCategory();
                form.repopulateCategoryComboBox();
                form.refreshTableItem();
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
        imageBg = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cavite State University Inventory System");
        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(1366, 768));
        setMinimumSize(new java.awt.Dimension(1366, 768));
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(1366, 768));
        setResizable(false);
        getContentPane().setLayout(null);

        panelBody.setBackground(new java.awt.Color(255, 255, 255));
        panelBody.setMinimumSize(new java.awt.Dimension(800, 600));
        panelBody.setName(""); // NOI18N
        panelBody.setOpaque(false);
        panelBody.setPreferredSize(new java.awt.Dimension(800, 600));
        panelBody.setLayout(null);
        panelBody.add(header);
        header.setBounds(0, 0, 1370, 90);

        panelForm.setOpaque(false);
        panelForm.setLayout(new java.awt.BorderLayout());
        panelBody.add(panelForm);
        panelForm.setBounds(0, 0, 1370, 770);

        imageBg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/images/bg.png"))); // NOI18N
        panelBody.add(imageBg);
        imageBg.setBounds(0, 0, 1370, 770);

        panelMain.add(panelBody);
        panelBody.setBounds(0, 0, 1370, 720);

        getContentPane().add(panelMain);
        panelMain.setBounds(0, 0, 1370, 770);

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
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ProjectINSY.java.ui.component.Header header;
    private javax.swing.JLabel imageBg;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelForm;
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
