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
import static ProjectINSY.java.util.GuiUtil.enforceCharacterAmount;
import static ProjectINSY.java.util.GuiUtil.enforceDigits;
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
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
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

    private final String PLACEHOLDER_CODE_YEAR = "00";
    private final String PLACEHOLDER_CODE_ID = "0000";
    private final String PLACEHOLDER_LOCATION = "Enter Location";
    private final String PLACEHOLDER_HOLDER = "Enter Holder";

    /**
     * Creates new form ItemTrackerLocation
     */
    public ItemTrackerScan() {
        initComponents();

        setTransparentFrame(btnWebcamControl, btnSearch, btnUpdate);
        fieldCodeID.getDocument().addDocumentListener(new FieldChangeListener());
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
            if (!fieldCodeID.getText().isEmpty()) {
                selectedCodeID = Integer.parseInt(fieldCodeID.getText());
                if (!fieldCodeYear.getText().isEmpty()) {
                    selectedCode = getColumnValueByInt(Main.TB_ITEM_STOCK, "stock_code", "stock_id", selectedCodeID);
                }
            } else {
                selectedCodeID = -1;
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
        GuiUtil.repopulateSuggestions(fieldLocation, "stock_location", "SELECT DISTINCT stock_location FROM " + Main.TB_ITEM_STOCK);
        GuiUtil.repopulateSuggestions(fieldHolder, "stock_holder", "SELECT DISTINCT stock_holder FROM " + Main.TB_ITEM_STOCK);
    }

    public void searchItem() {
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
                radioScanBatch.setText("Batch (" + num + " " + numStr + ")");
            }
        } catch (SQLException e) {
            paneDatabaseError(e);
        }

        if (!name.isEmpty() && !name.equals(fieldName.getText())) {
            String current_barcode = validateBarcode(selectedCode);
            ImageIcon barcodeIcon = BarcodeUtil.generateBarcode(current_barcode);
            imgBarcode.setIcon(barcodeIcon);
//            SoundUtil.playSound(SoundUtil.SOUND_SCANNED);

            fieldName.setText(name);
            fieldDesc.setText(desc);
            fieldLocation.setText(location);
            fieldHolder.setText(holder);
            fieldBatch.setText(batch);

            fieldLocation.setForeground(Color.BLACK);
            fieldHolder.setForeground(Color.BLACK);
        }
    }

    private void clearFields() {
        selectedCode = null;
        selectedCodeID = -1;

        GuiUtil.clearField(fieldCodeYear, PLACEHOLDER_CODE_YEAR);
        GuiUtil.clearField(fieldCodeID, PLACEHOLDER_CODE_ID);

//        GuiUtil.clearField(fieldName, "");
//        GuiUtil.clearField(fieldDesc, "");
        fieldName.setText("");
        fieldDesc.setText("");
        GuiUtil.clearField(fieldLocation, PLACEHOLDER_LOCATION);
        GuiUtil.clearField(fieldHolder, PLACEHOLDER_HOLDER);

        GuiUtil.resetIcon(imgBarcode);
        radioScanBatch.setText("Batch (0 Item)");

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
                    selectedCode = result.getText();
                    if (selectedCode.contains("-")) {
                        String[] parts = selectedCode.split("-");
                        fieldCodeYear.setText(parts[1]);
                        fieldCodeID.setText(parts[2]);

                        fieldCodeYear.setForeground(Color.BLACK);
                        fieldCodeID.setForeground(Color.BLACK);
                    }

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
        panelFields = new javax.swing.JPanel();
        labelScanType = new javax.swing.JLabel();
        radioScanSingle = new ProjectINSY.java.swing.RadioButtonCustom();
        radioScanBatch = new ProjectINSY.java.swing.RadioButtonCustom();
        labelName = new javax.swing.JLabel();
        fieldName = new javax.swing.JTextField();
        fieldLocation = new ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion();
        imageLocation = new javax.swing.JLabel();
        labelLocation = new javax.swing.JLabel();
        fieldDesc = new javax.swing.JTextField();
        labelHolder = new javax.swing.JLabel();
        fieldHolder = new ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion();
        panelBarcode = new javax.swing.JPanel();
        imgBarcode = new javax.swing.JLabel();
        imageHolder1 = new javax.swing.JLabel();
        labelDesc = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        labelUpdate = new javax.swing.JLabel();
        btnUpdate = new javax.swing.JButton();
        panelScan = new javax.swing.JPanel();
        labelScan = new javax.swing.JLabel();
        panelCode = new javax.swing.JPanel();
        panelCamera = new javax.swing.JPanel();
        panelCameraControls = new javax.swing.JPanel();
        labelCam = new javax.swing.JLabel();
        btnWebcamControl = new javax.swing.JToggleButton();
        radioAutoclose = new ProjectINSY.java.swing.RadioButtonCustom();
        labelCode = new javax.swing.JLabel();
        fieldCodeSilang = new javax.swing.JLabel();
        fieldCodeYear = new javax.swing.JTextField();
        fieldCodeSilang1 = new javax.swing.JLabel();
        fieldCodeID = new javax.swing.JTextField();
        imageCode = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();

        fieldBatch.setText("jTextField1");

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1836, 850));

        panelFields.setBackground(new java.awt.Color(255, 255, 255));
        panelFields.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 102, 24), 2));
        panelFields.setLayout(null);

        labelScanType.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelScanType.setText("Update by:");
        panelFields.add(labelScanType);
        labelScanType.setBounds(110, 140, 180, 45);

        radioScanSingle.setBackground(new java.awt.Color(25, 102, 24));
        radioScanSingle.setSelected(true);
        radioScanSingle.setText("Selected Item Only");
        radioScanSingle.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        radioScanSingle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioScanSingleActionPerformed(evt);
            }
        });
        panelFields.add(radioScanSingle);
        radioScanSingle.setBounds(290, 150, 180, 30);

        radioScanBatch.setBackground(new java.awt.Color(25, 102, 24));
        radioScanBatch.setText("Batch (0 Item)");
        radioScanBatch.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        radioScanBatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioScanBatchActionPerformed(evt);
            }
        });
        panelFields.add(radioScanBatch);
        radioScanBatch.setBounds(490, 150, 290, 30);

        labelName.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelName.setText("Name");
        panelFields.add(labelName);
        labelName.setBounds(110, 230, 173, 40);

        fieldName.setEditable(false);
        fieldName.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldName.setBorder(null);
        fieldName.setFocusable(false);
        fieldName.setSelectionColor(new java.awt.Color(25, 102, 24));
        panelFields.add(fieldName);
        fieldName.setBounds(110, 280, 660, 30);

        fieldLocation.setBorder(null);
        fieldLocation.setForeground(new java.awt.Color(153, 153, 153));
        fieldLocation.setText("Enter Location");
        fieldLocation.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldLocation.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldLocationFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldLocationFocusLost(evt);
            }
        });
        panelFields.add(fieldLocation);
        fieldLocation.setBounds(120, 530, 640, 50);

        imageLocation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldFull.png"))); // NOI18N
        panelFields.add(imageLocation);
        imageLocation.setBounds(110, 520, 665, 70);

        labelLocation.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelLocation.setText("Location");
        panelFields.add(labelLocation);
        labelLocation.setBounds(110, 480, 173, 30);

        fieldDesc.setEditable(false);
        fieldDesc.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldDesc.setBorder(null);
        fieldDesc.setFocusable(false);
        fieldDesc.setSelectionColor(new java.awt.Color(25, 102, 24));
        panelFields.add(fieldDesc);
        fieldDesc.setBounds(110, 390, 660, 30);

        labelHolder.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelHolder.setText("Holder");
        panelFields.add(labelHolder);
        labelHolder.setBounds(440, 620, 173, 30);

        fieldHolder.setBorder(null);
        fieldHolder.setForeground(new java.awt.Color(153, 153, 153));
        fieldHolder.setText("Enter Holder");
        fieldHolder.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldHolder.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldHolderFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldHolderFocusLost(evt);
            }
        });
        panelFields.add(fieldHolder);
        fieldHolder.setBounds(450, 670, 310, 50);

        panelBarcode.setBackground(new java.awt.Color(255, 255, 255));
        panelBarcode.setLayout(null);

        imgBarcode.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        panelBarcode.add(imgBarcode);
        imgBarcode.setBounds(0, 40, 250, 90);

        panelFields.add(panelBarcode);
        panelBarcode.setBounds(120, 590, 250, 90);

        imageHolder1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldHalf.png"))); // NOI18N
        panelFields.add(imageHolder1);
        imageHolder1.setBounds(440, 660, 340, 70);

        labelDesc.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelDesc.setText("Description");
        panelFields.add(labelDesc);
        labelDesc.setBounds(110, 340, 200, 40);
        panelFields.add(jSeparator1);
        jSeparator1.setBounds(110, 430, 660, 10);
        panelFields.add(jSeparator2);
        jSeparator2.setBounds(110, 320, 660, 10);

        labelUpdate.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        labelUpdate.setForeground(new java.awt.Color(255, 255, 255));
        labelUpdate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelUpdate.setText("Update");
        labelUpdate.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelFields.add(labelUpdate);
        labelUpdate.setBounds(370, 760, 130, 30);

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
        btnUpdate.setBounds(360, 750, 150, 50);

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

        panelCode.setBackground(new java.awt.Color(255, 255, 255));
        panelCode.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(25, 102, 24), 2));
        panelCode.setLayout(null);

        panelCamera.setBackground(new java.awt.Color(0, 0, 0));
        panelCamera.setMaximumSize(new java.awt.Dimension(800, 600));
        panelCamera.setMinimumSize(new java.awt.Dimension(800, 600));
        panelCamera.setPreferredSize(new java.awt.Dimension(800, 600));
        panelCamera.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        panelCode.add(panelCamera);
        panelCamera.setBounds(40, 190, 800, 600);

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

        panelCode.add(panelCameraControls);
        panelCameraControls.setBounds(40, 110, 800, 50);

        labelCode.setFont(new java.awt.Font("Aaux ProThin OSF", 1, 36)); // NOI18N
        labelCode.setText("Enter or Scan Code:");
        panelCode.add(labelCode);
        labelCode.setBounds(30, 40, 320, 30);

        fieldCodeSilang.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldCodeSilang.setText("-");
        panelCode.add(fieldCodeSilang);
        fieldCodeSilang.setBounds(550, 40, 12, 30);

        fieldCodeYear.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldCodeYear.setForeground(new java.awt.Color(153, 153, 153));
        fieldCodeYear.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldCodeYear.setText("00");
        fieldCodeYear.setBorder(null);
        fieldCodeYear.setSelectionColor(new java.awt.Color(25, 102, 24));
        fieldCodeYear.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldCodeYearFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldCodeYearFocusLost(evt);
            }
        });
        fieldCodeYear.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                fieldCodeYearKeyTyped(evt);
            }
        });
        panelCode.add(fieldCodeYear);
        fieldCodeYear.setBounds(500, 40, 40, 30);

        fieldCodeSilang1.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldCodeSilang1.setText("Silang  -");
        panelCode.add(fieldCodeSilang1);
        fieldCodeSilang1.setBounds(400, 40, 100, 30);

        fieldCodeID.setFont(new java.awt.Font("Bahnschrift", 0, 24)); // NOI18N
        fieldCodeID.setForeground(new java.awt.Color(153, 153, 153));
        fieldCodeID.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        fieldCodeID.setText("0000");
        fieldCodeID.setBorder(null);
        fieldCodeID.setSelectionColor(new java.awt.Color(25, 102, 24));
        fieldCodeID.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                fieldCodeIDFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                fieldCodeIDFocusLost(evt);
            }
        });
        fieldCodeID.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                fieldCodeIDKeyTyped(evt);
            }
        });
        panelCode.add(fieldCodeID);
        fieldCodeID.setBounds(570, 40, 70, 30);

        imageCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/fieldHalf.png"))); // NOI18N
        panelCode.add(imageCode);
        imageCode.setBounds(370, 20, 333, 70);

        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnSearch.png"))); // NOI18N
        btnSearch.setBorder(null);
        btnSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearch.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/ProjectINSY/resources/interface/btnSearch_pressed.png"))); // NOI18N
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });
        panelCode.add(btnSearch);
        btnSearch.setBounds(730, 50, 140, 25);

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(panelFields, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 844, Short.MAX_VALUE)
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

                if (radioScanSingle.isSelected()) {
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

    private void fieldCodeYearFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldCodeYearFocusGained
        setDefaultField(fieldCodeYear, PLACEHOLDER_CODE_YEAR, GuiUtil.FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_fieldCodeYearFocusGained

    private void fieldCodeYearFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldCodeYearFocusLost
        setDefaultField(fieldCodeYear, PLACEHOLDER_CODE_YEAR, GuiUtil.FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_fieldCodeYearFocusLost

    private void fieldCodeYearKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldCodeYearKeyTyped
        enforceDigits(evt);
        enforceCharacterAmount(evt, 2);
    }//GEN-LAST:event_fieldCodeYearKeyTyped

    private void fieldCodeIDFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldCodeIDFocusGained
        setDefaultField(fieldCodeID, PLACEHOLDER_CODE_ID, GuiUtil.FieldFocus.GAINED, Color.BLACK);
    }//GEN-LAST:event_fieldCodeIDFocusGained

    private void fieldCodeIDFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fieldCodeIDFocusLost
        setDefaultField(fieldCodeID, PLACEHOLDER_CODE_ID, GuiUtil.FieldFocus.LOST, Color.BLACK);
    }//GEN-LAST:event_fieldCodeIDFocusLost

    private void fieldCodeIDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_fieldCodeIDKeyTyped
        enforceDigits(evt);
    }//GEN-LAST:event_fieldCodeIDKeyTyped

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        searchItem();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void radioScanSingleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioScanSingleActionPerformed
        if (radioScanSingle.isSelected()) {
            radioScanBatch.setSelected(false);
        } else {
            radioScanSingle.setSelected(true);
        }
    }//GEN-LAST:event_radioScanSingleActionPerformed

    private void radioScanBatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioScanBatchActionPerformed
        if (radioScanBatch.isSelected()) {
            radioScanSingle.setSelected(false);
        } else {
            radioScanBatch.setSelected(true);
        }
    }//GEN-LAST:event_radioScanBatchActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JToggleButton btnWebcamControl;
    private javax.swing.JTextField fieldBatch;
    private javax.swing.JTextField fieldCodeID;
    private javax.swing.JLabel fieldCodeSilang;
    private javax.swing.JLabel fieldCodeSilang1;
    private javax.swing.JTextField fieldCodeYear;
    private javax.swing.JTextField fieldDesc;
    private ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion fieldHolder;
    private ProjectINSY.java.swing.TextFieldSuggestion.TextFieldSuggestion fieldLocation;
    private javax.swing.JTextField fieldName;
    private javax.swing.JLabel imageCode;
    private javax.swing.JLabel imageHolder1;
    private javax.swing.JLabel imageLocation;
    private javax.swing.JLabel imgBarcode;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel labelCam;
    private javax.swing.JLabel labelCode;
    private javax.swing.JLabel labelDesc;
    private javax.swing.JLabel labelHolder;
    private javax.swing.JLabel labelLocation;
    private javax.swing.JLabel labelName;
    private javax.swing.JLabel labelScan;
    private javax.swing.JLabel labelScanType;
    private javax.swing.JLabel labelUpdate;
    private javax.swing.JPanel panelBarcode;
    private javax.swing.JPanel panelCamera;
    private javax.swing.JPanel panelCameraControls;
    private javax.swing.JPanel panelCode;
    private javax.swing.JPanel panelFields;
    private javax.swing.JPanel panelScan;
    private ProjectINSY.java.swing.RadioButtonCustom radioAutoclose;
    private ProjectINSY.java.swing.RadioButtonCustom radioScanBatch;
    private ProjectINSY.java.swing.RadioButtonCustom radioScanSingle;
    // End of variables declaration//GEN-END:variables
}
