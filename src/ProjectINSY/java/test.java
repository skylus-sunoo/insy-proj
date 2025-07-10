/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProjectINSY.java;

import ProjectINSY.java.util.BarcodeUtil;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

/**
 *
 * @author admin
 */
public class test {

    public static void main(String args[]) {
        String barcodeCode = "2507109472029";
        int amount = 50;

        List<BufferedImage> barcodeImages = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            ImageIcon barcodeIcon = BarcodeUtil.generateBarcode(barcodeCode);
            BufferedImage bufferedImage = (BufferedImage) barcodeIcon.getImage();
            barcodeImages.add(bufferedImage);
        }

        String fileName = "barcode_test";

        try {
            BarcodeUtil.generateFileFromBarcodes(barcodeImages, BarcodeUtil.FileType.PDF, fileName);
        } catch (IOException ex) {
        }
    }
}
