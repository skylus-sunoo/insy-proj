/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ProjectINSY.java.ui;

import ProjectINSY.java.Main;
import ProjectINSY.java.util.BarcodeUtil;
import static ProjectINSY.java.util.BarcodeUtil.validateBarcode;
import ProjectINSY.java.util.DatabaseUtil;
import ProjectINSY.java.util.DatabaseUtil.HistoryFrame;
import ProjectINSY.java.util.DatabaseUtil.HistoryType;
import static ProjectINSY.java.util.DatabaseUtil.createHistoryDesc;
import static ProjectINSY.java.util.DatabaseUtil.getColumnValueByInt;
import static ProjectINSY.java.util.DatabaseUtil.getColumnValueByString;
import static ProjectINSY.java.util.DatabaseUtil.insertHistory;
import static ProjectINSY.java.util.DatabaseUtil.prepareQueryWithParameters;
import ProjectINSY.java.util.GuiUtil;
import static ProjectINSY.java.util.GuiUtil.resetBtnEnability;
import static ProjectINSY.java.util.GuiUtil.setDefaultField;
import static ProjectINSY.java.util.GuiUtil.setTransparentFrame;
import static ProjectINSY.java.util.MessageUtil.paneDatabaseError;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author admin
 */
public class ItemTrackerScan extends javax.swing.JPanel implements Runnable, ThreadFactory {

    private WebcamPanel panel = null;
    private Webcam webcam = null;
    private boolean isWebcamRunning = false;  // Flag to track the webcam state
    private Thread captureThread = null;      // The thread that runs the webcam capture

    private final int WEBCAM_HEIGHT = 800;
    private final int WEBCAM_WIDTH = 600;

    private String selectedCode = null;
    private int selectedCodeID = -1;

    private final String PLACEHOLDER_FULL_CODE = "Silang-00-000000";
    private final String PLACEHOLDER_LOCATION = "Enter Location";
    private final String PLACEHOLDER_HOLDER = "Enter Holder";

    private boolean isUpdating = false;

    /**
     * Creates new form ItemTrackerScan
     */
    public ItemTrackerScan() {
        initComponents();

        setTransparentFrame(btnClear, btnWebcamControl, btnSearch, btnUpdate);
        fieldCode.getDocument().addDocumentListener(new FieldChangeListener());
        fieldName.getDocument().addDocumentListener(new FieldChangeListener());
        fieldDesc.getDocument().addDocumentListener(new FieldChangeListener());
        fieldLocation.getDocument().addDocumentListener(new FieldChangeListener());
        fieldHolder.getDocument().addDocumentListener(new FieldChangeListener());
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
            if (isUpdating) {
                return;
            }

            try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
                if (DatabaseUtil.recordExists(conn, Main.TB_ITEM_STOCK, "stock_code", fieldCode.getText())) {
                    isUpdating = true;

                    SwingUtilities.invokeLater(() -> {
                        searchItem();

                        isUpdating = false;
                    });
                }
            } catch (SQLException e) {
                paneDatabaseError(e);
            }

            btnUpdate.setEnabled(!fieldName.getText().isEmpty()
                    && !fieldLocation.getText().isEmpty()
                    && !fieldLocation.getText().equals(PLACEHOLDER_LOCATION)
                    && !fieldHolder.getText().isEmpty()
                    && !fieldHolder.getText().equals(PLACEHOLDER_HOLDER)
            );
        }
    }

    public void repopulateSuggestions() {
//        fieldLocation.repopulateSuggestions("stock_location", "SELECT DISTINCT stock_location FROM " + Main.TB_ITEM_STOCK);
//        fieldHolder.repopulateSuggestions("stock_holder", "SELECT DISTINCT stock_holder FROM " + Main.TB_ITEM_STOCK);
    }

    public void setScannerFocus() {
        fieldCode.requestFocusInWindow();
    }

    public void searchItem() {
        String codeText = fieldCode.getText();
        fieldSelectedCode.setText("None");

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            if (codeText.isEmpty() || !DatabaseUtil.recordExists(conn, Main.TB_ITEM_STOCK, "stock_code", fieldCode.getText())) {
                return;
            }
        } catch (SQLException e) {
            paneDatabaseError(e);
        }

        selectedCodeID = Integer.parseInt(getColumnValueByString(Main.TB_ITEM_STOCK, "stock_id", "stock_code", codeText));
        selectedCode = codeText;
        fieldSelectedCode.setText(selectedCode);

        String name = getColumnValueByInt(Main.TB_ITEM_STOCK, "stock_name", "stock_id", selectedCodeID);
        String desc = getColumnValueByInt(Main.TB_ITEM_STOCK, "stock_desc", "stock_id", selectedCodeID);
        String location = getColumnValueByInt(Main.TB_ITEM_STOCK, "stock_location", "stock_id", selectedCodeID);
        String holder = getColumnValueByInt(Main.TB_ITEM_STOCK, "stock_holder", "stock_id", selectedCodeID);
        String batch = getColumnValueByInt(Main.TB_ITEM_STOCK, "stock_batch", "stock_id", selectedCodeID);

        String query = "SELECT COUNT(*) FROM " + Main.TB_ITEM_STOCK + " WHERE stock_batch = ?";

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME); PreparedStatement pst = prepareQueryWithParameters(conn, query, batch); ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                int num = rs.getInt("COUNT(*)");
                String numStr = "Item";
                if (num > 1) {
                    numStr = "Items";
                }
//                radioScanBatch.setText("Batch (" + num + " " + numStr + ")");
            }
        } catch (SQLException e) {
            paneDatabaseError(e);
        }

        if (!codeText.isEmpty() && !codeText.equals(fieldLastCode.getText())) {
            String current_barcode = validateBarcode(selectedCode);
            if (current_barcode == null) {
                return;
            }
            ImageIcon barcodeIcon = BarcodeUtil.generateBarcode(current_barcode);
            imgBarcode.setIcon(barcodeIcon);
            fieldName.setText(name);
            fieldDesc.setText(desc);
            fieldLocation.setText(location);
//            fieldLocation.requestFocusInWindow();
            fieldHolder.setText(holder);
            fieldBatch.setText(batch);

            fieldLocation.setForeground(Color.BLACK);
            fieldHolder.setForeground(Color.BLACK);
//            SoundUtil.playSound(SoundUtil.SOUND_SCANNED);

            fieldLastCode.setText(codeText);
        }
    }

    private void clearFields() {
        selectedCode = null;
        selectedCodeID = -1;

        fieldSelectedCode.setText("None");
        GuiUtil.clearField(fieldCode, PLACEHOLDER_FULL_CODE);

//        GuiUtil.clearField(fieldName, "");
//        GuiUtil.clearField(fieldDesc, "");
        fieldName.setText("");
        fieldDesc.setText("");
        GuiUtil.clearField(fieldLocation, PLACEHOLDER_LOCATION);
        GuiUtil.clearField(fieldHolder, PLACEHOLDER_HOLDER);

        GuiUtil.clearField(fieldBatch, "");
        GuiUtil.clearField(fieldLastCode, "");

        GuiUtil.resetIcon(imgBarcode);
//        radioScanBatch.setText("Batch (0 Item)");

        setUpdateDeleteEnable();
    }

    public void setUpdateDeleteEnable() {
        resetBtnEnability(fieldName, btnUpdate);
    }

    //<editor-fold defaultstate="collapsed" desc="Webcam Initialization">
    private void initWebcam() {
        List<Webcam> webcams = Webcam.getWebcams();
        if (webcams.isEmpty()) {
            System.out.println("No webcams found!");
            return;
        }

        webcam = webcams.get(0);
        Dimension size = WebcamResolution.VGA.getSize();
        webcam.setViewSize(size);

        panel = new WebcamPanel(webcam);
        panel.setPreferredSize(size);
        panel.setFPSDisplayed(true);

        panelCamera.add(panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, WEBCAM_HEIGHT, WEBCAM_WIDTH));
        panelCamera.revalidate();
        panelCamera.repaint();
    }

    private void startWebcam() {
        if (webcam == null) {
            initWebcam();
        }

        if (!isWebcamRunning) {
            if (webcam != null && !webcam.isOpen()) {
                webcam.open();
            }
            isWebcamRunning = true;

            if (captureThread != null && captureThread.isAlive()) {
                captureThread.interrupt();
            }
            captureThread = new Thread(this);
            captureThread.setDaemon(true);
            captureThread.start();
        }
    }

    private void stopWebcam() {
        if (webcam != null && webcam.isOpen()) {
            webcam.close();
        }
        webcam = null;
        isWebcamRunning = false;

        if (captureThread != null && captureThread.isAlive()) {
            captureThread.interrupt();
        }
    }

    private void toggleWebcam() {
        if (isWebcamRunning) {
            stopWebcam();
        } else {
            startWebcam();
        }
    }

    @Override
    public void run() {
        while (isWebcamRunning) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
//                Logger.getLogger(ItemTracker.class.getName()).log(Level.SEVERE, null, ex);
                break;
            }

            if (webcam.isOpen()) {
                Result result = null;
                BufferedImage image = null;

                image = webcam.getImage();
                if (image == null) {
                    continue;
                }

                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                try {
                    result = new MultiFormatReader().decode(bitmap);
                } catch (NotFoundException ex) {
//                    Logger.getLogger(ItemTracker.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (result != null) {
                    selectedCodeID = Integer.parseInt(getColumnValueByString(Main.TB_ITEM_STOCK, "stock_id", "stock_code", result.getText()));
                    selectedCode = result.getText();

                    searchItem();

                    if (radioAutoclose.isSelected()) {
                        stopWebcam();
                    }
                }
            }
        }
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, "My Thread");
        t.setDaemon(true);

        return t;
    }
    //</editor-fold>

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fieldBatch = new javax.swing.JTextField();
        fieldLastCode = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        panelCameraControls = new javax.swing.JPanel();
        labelCam = new javax.swing.JLabel();
        btnWebcamControl = new javax.swing.JToggleButton();
        radioAutoclose = new ProjectINSY.java.swing.RadioButtonCustom();
        panelCamera = new javax.swing.JPanel();
        panelFields = new javax.swing.JPanel();
        panelBarcode = new javax.swing.JPanel();
        imgBarcode = new javax.swing.JLabel();
        labelUpdate = new javax.swing.JLabel();
        btnUpdate = new javax.swing.JButton();
        panelScan = new javax.swing.JPanel();
        labelScan = new javax.swing.JLabel();
        labelSelectedCode = new javax.swing.JLabel();
        fieldSelectedCode = new javax.swing.JTextField();
        labelClear = new javax.swing.JLabel();
        btnClear = new javax.swing.JButton();
        panelCode = new javax.swing.JPanel();
        labelCode = new javax.swing.JLabel();
        fieldCode = new javax.swing.JTextField();
        imageCode = new javax.swing.JLabel();
        labelScanInfo = new javax.swing.JLabel();
        labelScanInfo1 = new javax.swing.JLabel();
        labelName = new javax.swing.JLabel();
        fieldName = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        labelDesc = new javax.swing.JLabel();
        fieldDesc = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        labelLocation = new javax.swing.JLabel();
        fieldLocation = new ProjectINSY.java.swing.Form.FormFieldSuggestion();
        imageLocation = new javax.swing.JLabel();
        labelHolder = new javax.swing.JLabel();
        fieldHolder = new ProjectINSY.java.swing.Form.FormFieldSuggestion();
        imageHolder = new javax.swing.JLabel();

        fieldBatch.setText("jTextField1");

        fieldLastCode.setText("jTextField1");

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnSearch.png"))); // NOI18N
        btnSearch.setBorder(null);
        btnSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearch.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnSearch_pressed.png"))); // NOI18N
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        panelCameraControls.setBackground(new java.awt.Color(255, 255, 255));
        panelCameraControls.setLayout(null);

        labelCam.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelCam.setForeground(new java.awt.Color(255, 255, 255));
        labelCam.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/iconCam.png"))); // NOI18N
        labelCam.setText("Start Webcam");
        labelCam.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelCameraControls.add(labelCam);
        labelCam.setBounds(40, 0, 300, 50);

        btnWebcamControl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnCam.png"))); // NOI18N
        btnWebcamControl.setBorder(null);
        btnWebcamControl.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnWebcamControl.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnCam_pressed.png"))); // NOI18N
        btnWebcamControl.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnCam_pressed.png"))); // NOI18N
        btnWebcamControl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWebcamControlActionPerformed(evt);
            }
        });
        panelCameraControls.add(btnWebcamControl);
        btnWebcamControl.setBounds(40, 0, 300, 49);

        radioAutoclose.setBackground(new java.awt.Color(25, 102, 24));
        radioAutoclose.setText("Auto-close webcam when barcode is processed");
        radioAutoclose.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        panelCameraControls.add(radioAutoclose);
        radioAutoclose.setBounds(390, 10, 402, 27);

        panelCamera.setBackground(new java.awt.Color(0, 0, 0));
        panelCamera.setMaximumSize(new java.awt.Dimension(800, 600));
        panelCamera.setMinimumSize(new java.awt.Dimension(800, 600));
        panelCamera.setPreferredSize(new java.awt.Dimension(800, 600));
        panelCamera.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1836, 850));

        panelFields.setBackground(new java.awt.Color(255, 255, 255));
        panelFields.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 102, 24), 2));
        panelFields.setLayout(null);

        panelBarcode.setBackground(new java.awt.Color(255, 255, 255));
        panelBarcode.setLayout(null);

        imgBarcode.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        panelBarcode.add(imgBarcode);
        imgBarcode.setBounds(0, 10, 250, 90);

        panelFields.add(panelBarcode);
        panelBarcode.setBounds(120, 650, 250, 120);

        labelUpdate.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelUpdate.setForeground(new java.awt.Color(255, 255, 255));
        labelUpdate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelUpdate.setText("Update");
        labelUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelFields.add(labelUpdate);
        labelUpdate.setBounds(370, 790, 130, 30);

        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnPrint.png"))); // NOI18N
        btnUpdate.setBorder(null);
        btnUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnUpdate.setEnabled(false);
        btnUpdate.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnPrint_pressed.png"))); // NOI18N
        btnUpdate.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnPrint_pressed.png"))); // NOI18N
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });
        panelFields.add(btnUpdate);
        btnUpdate.setBounds(360, 780, 150, 50);

        panelScan.setBackground(new java.awt.Color(25, 102, 24));

        labelScan.setBackground(new java.awt.Color(25, 102, 24));
        labelScan.setFont(new java.awt.Font("Bebas", 0, 64)); // NOI18N
        labelScan.setForeground(new java.awt.Color(255, 255, 255));
        labelScan.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelScan.setText("change location");
        labelScan.setOpaque(true);

        javax.swing.GroupLayout panelScanLayout = new javax.swing.GroupLayout(panelScan);
        panelScan.setLayout(panelScanLayout);
        panelScanLayout.setHorizontalGroup(
            panelScanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 910, Short.MAX_VALUE)
            .addGroup(panelScanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelScanLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(labelScan)
                    .addGap(0, 0, Short.MAX_VALUE)))
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

        panelFields.add(panelScan);
        panelScan.setBounds(0, 10, 910, 80);

        labelSelectedCode.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelSelectedCode.setText("Code: ");
        panelFields.add(labelSelectedCode);
        labelSelectedCode.setBounds(110, 100, 100, 40);

        fieldSelectedCode.setEditable(false);
        fieldSelectedCode.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldSelectedCode.setText("None");
        fieldSelectedCode.setBorder(null);
        fieldSelectedCode.setFocusable(false);
        fieldSelectedCode.setSelectionColor(new java.awt.Color(25, 102, 24));
        panelFields.add(fieldSelectedCode);
        fieldSelectedCode.setBounds(210, 110, 460, 30);

        labelClear.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelClear.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelClear.setText("Clear");
        labelClear.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelFields.add(labelClear);
        labelClear.setBounds(690, 110, 80, 23);

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
        btnClear.setBounds(680, 100, 100, 40);

        panelCode.setBackground(new java.awt.Color(255, 255, 255));
        panelCode.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 102, 24), 2));
        panelCode.setLayout(null);

        labelCode.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 64)); // NOI18N
        labelCode.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelCode.setText("Enter or Scan Code:");
        panelCode.add(labelCode);
        labelCode.setBounds(10, 10, 880, 90);

        fieldCode.setFont(new java.awt.Font("Bahnschrift", 1, 64)); // NOI18N
        fieldCode.setForeground(new java.awt.Color(153, 153, 153));
        fieldCode.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldCode.setText("Silang-00-000000");
        fieldCode.setBorder(null);
        fieldCode.setSelectionColor(new java.awt.Color(25, 102, 24));
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
        panelCode.add(fieldCode);
        fieldCode.setBounds(120, 130, 640, 100);

        imageCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldTextArea.png"))); // NOI18N
        panelCode.add(imageCode);
        imageCode.setBounds(110, 110, 665, 130);

        labelScanInfo.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelScanInfo.setText("that the field above is selected and empty.");
        panelCode.add(labelScanInfo);
        labelScanInfo.setBounds(20, 310, 660, 45);

        labelScanInfo1.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelScanInfo1.setText("When scanning a new barcode, ensure");
        panelCode.add(labelScanInfo1);
        labelScanInfo1.setBounds(20, 260, 660, 45);

        labelName.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelName.setText("Customer Name");
        panelCode.add(labelName);
        labelName.setBounds(100, 380, 270, 40);

        fieldName.setEditable(false);
        fieldName.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldName.setBorder(null);
        fieldName.setFocusable(false);
        fieldName.setSelectionColor(new java.awt.Color(25, 102, 24));
        panelCode.add(fieldName);
        fieldName.setBounds(100, 430, 660, 30);
        panelCode.add(jSeparator2);
        jSeparator2.setBounds(100, 470, 660, 10);

        labelDesc.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelDesc.setText("Quantity");
        panelCode.add(labelDesc);
        labelDesc.setBounds(100, 490, 200, 40);

        fieldDesc.setEditable(false);
        fieldDesc.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldDesc.setBorder(null);
        fieldDesc.setFocusable(false);
        fieldDesc.setSelectionColor(new java.awt.Color(25, 102, 24));
        panelCode.add(fieldDesc);
        fieldDesc.setBounds(100, 540, 660, 30);
        panelCode.add(jSeparator1);
        jSeparator1.setBounds(100, 580, 660, 10);

        labelLocation.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelLocation.setText("Customer Name");
        panelCode.add(labelLocation);
        labelLocation.setBounds(100, 630, 260, 30);

        fieldLocation.setBorder(null);
        fieldLocation.setForeground(new java.awt.Color(153, 153, 153));
        fieldLocation.setText("Enter Customer Name");
        fieldLocation.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldLocation.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldLocationFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldLocationFocusLost(evt);
            }
        });
        panelCode.add(fieldLocation);
        fieldLocation.setBounds(110, 680, 640, 50);

        imageLocation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldFull.png"))); // NOI18N
        panelCode.add(imageLocation);
        imageLocation.setBounds(100, 670, 665, 70);

        labelHolder.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelHolder.setText("Channel");
        panelCode.add(labelHolder);
        labelHolder.setBounds(430, 750, 173, 30);

        fieldHolder.setBorder(null);
        fieldHolder.setForeground(new java.awt.Color(153, 153, 153));
        fieldHolder.setText("Enter Channel");
        fieldHolder.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldHolder.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldHolderFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldHolderFocusLost(evt);
            }
        });
        panelCode.add(fieldHolder);
        fieldHolder.setBounds(440, 800, 310, 50);

        imageHolder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldHalf.png"))); // NOI18N
        panelCode.add(imageHolder);
        imageHolder.setBounds(430, 790, 340, 70);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelCode, javax.swing.GroupLayout.PREFERRED_SIZE, 901, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panelFields, javax.swing.GroupLayout.PREFERRED_SIZE, 905, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelFields, javax.swing.GroupLayout.DEFAULT_SIZE, 884, Short.MAX_VALUE)
                    .addComponent(panelCode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void fieldLocationFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldLocationFocusGained
        setDefaultField(fieldLocation, PLACEHOLDER_LOCATION, GuiUtil.FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_fieldLocationFocusGained

    private void fieldLocationFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldLocationFocusLost
        setDefaultField(fieldLocation, PLACEHOLDER_LOCATION, GuiUtil.FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_fieldLocationFocusLost

    private void fieldHolderFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldHolderFocusGained
        setDefaultField(fieldHolder, PLACEHOLDER_HOLDER, GuiUtil.FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_fieldHolderFocusGained

    private void fieldHolderFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldHolderFocusLost
        setDefaultField(fieldHolder, PLACEHOLDER_HOLDER, GuiUtil.FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_fieldHolderFocusLost

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        String stock_location = fieldLocation.getText();
        String stock_holder = fieldHolder.getText();

        try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
            int warnUser = JOptionPane.showConfirmDialog(
                    null,
                    "Confirm Update?",
                    "Warning: Stock Update",
                    JOptionPane.YES_NO_OPTION
            );

            if (warnUser == JOptionPane.YES_OPTION) {
                String query = "UPDATE " + Main.TB_ITEM_STOCK + " SET stock_location = ?, stock_holder = ?";
                int scanTypeID = selectedCodeID;
                String selectedCodeEnd = selectedCode;

//                if (radioScanSingle.isSelected()) {
                if (true) {
                    query += " WHERE stock_id = ?";
                } else {
                    query += " WHERE stock_batch = ?";
                    scanTypeID = Integer.parseInt(fieldBatch.getText());

                    PreparedStatement pst = conn.prepareStatement("SELECT stock_code FROM " + Main.TB_ITEM_STOCK + " WHERE stock_batch = " + scanTypeID + " ORDER BY stock_code DESC LIMIT 1");
                    ResultSet rs = pst.executeQuery();

                    if (rs.next()) {
                        selectedCodeEnd = rs.getString("stock_code");
                    }
                }
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, stock_location);
                pst.setString(2, stock_holder);
                pst.setInt(3, scanTypeID);

                // HISTORY : TRACKER-UPDATE
                String history_desc = "";
                String old_location = getColumnValueByInt(Main.TB_ITEM_STOCK, "stock_location", "stock_id", selectedCodeID);
                String old_holder = getColumnValueByInt(Main.TB_ITEM_STOCK, "stock_holder", "stock_id", selectedCodeID);

                history_desc += createHistoryDesc(old_location, stock_location, "Location");
                history_desc += createHistoryDesc(old_holder, stock_holder, "Holder");

                insertHistory(HistoryFrame.TRACKER, HistoryType.UPDATE, selectedCode, selectedCodeEnd, history_desc, stock_holder);

                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Stock Updated!", "Success", JOptionPane.INFORMATION_MESSAGE);

                clearFields();
            }

        } catch (SQLException e) {
            paneDatabaseError(e);
        }

        repopulateSuggestions();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnWebcamControlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWebcamControlActionPerformed
        toggleWebcam();
        labelCam.setText(isWebcamRunning ? "Stop Webcam" : "Start Webcam");
    }//GEN-LAST:event_btnWebcamControlActionPerformed

    private void fieldCodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldCodeFocusGained
        setDefaultField(fieldCode, PLACEHOLDER_FULL_CODE, GuiUtil.FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_fieldCodeFocusGained

    private void fieldCodeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldCodeFocusLost
        setDefaultField(fieldCode, PLACEHOLDER_FULL_CODE, GuiUtil.FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_fieldCodeFocusLost

    private void fieldCodeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldCodeKeyTyped
        char c = evt.getKeyChar();
        String currentCode = fieldCode.getText();
        int dashesCount = 0;

        for (int i = 0; i < currentCode.length(); i++) {
            if (currentCode.charAt(i) == '-') {
                dashesCount++;
            }
        }

        if (dashesCount > 1 && c == '-') {
            evt.consume();
        } else if (currentCode.contains("Silang") && !Character.isDigit(c) && c != '-' && c != KeyEvent.VK_BACK_SPACE) {
            evt.consume();
        }

        if (c == KeyEvent.VK_BACK_SPACE) {
            if (currentCode.isEmpty()) {
                clearFields();
                btnClear.requestFocusInWindow();
            } else if (currentCode.equals("Silang-")) {
                clearFields();
                fieldCode.setForeground(Color.BLACK);
                fieldCode.setText("Silang-");
            }
        }
    }//GEN-LAST:event_fieldCodeKeyTyped

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        searchItem();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearFields();
    }//GEN-LAST:event_btnClearActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JToggleButton btnWebcamControl;
    private javax.swing.JTextField fieldBatch;
    private javax.swing.JTextField fieldCode;
    private javax.swing.JTextField fieldDesc;
    private ProjectINSY.java.swing.Form.FormFieldSuggestion fieldHolder;
    private javax.swing.JTextField fieldLastCode;
    private ProjectINSY.java.swing.Form.FormFieldSuggestion fieldLocation;
    private javax.swing.JTextField fieldName;
    private javax.swing.JTextField fieldSelectedCode;
    private javax.swing.JLabel imageCode;
    private javax.swing.JLabel imageHolder;
    private javax.swing.JLabel imageLocation;
    private javax.swing.JLabel imgBarcode;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel labelCam;
    private javax.swing.JLabel labelClear;
    private javax.swing.JLabel labelCode;
    private javax.swing.JLabel labelDesc;
    private javax.swing.JLabel labelHolder;
    private javax.swing.JLabel labelLocation;
    private javax.swing.JLabel labelName;
    private javax.swing.JLabel labelScan;
    private javax.swing.JLabel labelScanInfo;
    private javax.swing.JLabel labelScanInfo1;
    private javax.swing.JLabel labelSelectedCode;
    private javax.swing.JLabel labelUpdate;
    private javax.swing.JPanel panelBarcode;
    private javax.swing.JPanel panelCamera;
    private javax.swing.JPanel panelCameraControls;
    private javax.swing.JPanel panelCode;
    private javax.swing.JPanel panelFields;
    private javax.swing.JPanel panelScan;
    private ProjectINSY.java.swing.RadioButtonCustom radioAutoclose;
    // End of variables declaration//GEN-END:variables
}
