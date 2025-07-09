/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProjectINSY.java;

import ProjectINSY.java.ui.ItemManagement;
import ProjectINSY.java.util.BarcodeUtil;
import ProjectINSY.java.util.DatabaseUtil;
import static ProjectINSY.java.util.DatabaseUtil.getColumnValueByInt;
import ProjectINSY.java.util.MessageUtil;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;

/**
 *
 * @author admin
 */
public class test {

    public static void main(String args[]) {
        List<BufferedImage> barcodeImages = new ArrayList<>();

        for (int i = 1; i < 25; i++) {
            try (Connection conn = DatabaseUtil.getConnection(Main.DB_NAME);) {
                if (DatabaseUtil.recordExists(conn, Main.TB_CATALOG_ITEM, "item_id", String.valueOf(i))) {
                    String code = getColumnValueByInt(Main.TB_CATALOG_ITEM, "item_code", "item_id", i);
                    ImageIcon barcode = BarcodeUtil.generateBarcode(code);

                    BufferedImage bufferedImage = (BufferedImage) barcode.getImage();
                    barcodeImages.add(bufferedImage);

//                    System.out.println(code);
                }
            } catch (SQLException e) {
                MessageUtil.paneDatabaseError(e);
            }
        }

        try {
            BarcodeUtil.generateFileFromBarcodes(barcodeImages, BarcodeUtil.FileType.PDF, "output2");
        } catch (IOException ex) {
            Logger.getLogger(ItemManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
