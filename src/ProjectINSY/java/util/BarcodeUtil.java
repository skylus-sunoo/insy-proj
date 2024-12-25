/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProjectINSY.java.util;

/**
 *
 * @author admin
 */
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

public class BarcodeUtil {

    public static String validateBarcode(String barcodeValue) {
        if (barcodeValue.contains("-")) {
            String[] parts = barcodeValue.split("-");
            return parts[0] + "-" + parts[1] + "-" + parts[2];
        }
        return null;
    }

    public static ImageIcon generateBarcode(String barcodeValue) {
        try {
            Map<EncodeHintType, String> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            int barcodeWidth = 200;
            int barcodeHeight = 50;

            BitMatrix bitMatrix = new MultiFormatWriter().encode(barcodeValue, BarcodeFormat.CODE_128, barcodeWidth, barcodeHeight, hints);

            int totalHeight = barcodeHeight + 20; 

            BufferedImage combinedImage = new BufferedImage(barcodeWidth, totalHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = combinedImage.createGraphics();

            g2d.setBackground(Color.WHITE);
            g2d.clearRect(0, 0, barcodeWidth, totalHeight);  

            for (int x = 0; x < barcodeWidth; x++) {
                for (int y = 0; y < barcodeHeight; y++) {
                    combinedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0x000000 : 0xFFFFFF); 
                }
            }

            g2d.setColor(Color.BLACK); 
            g2d.setFont(new Font("Arial", Font.PLAIN, 14)); 

            FontMetrics fontMetrics = g2d.getFontMetrics();
            int labelWidth = fontMetrics.stringWidth(barcodeValue); 
            int xPosition = (barcodeWidth - labelWidth) / 2; 

            g2d.drawString(barcodeValue, xPosition, barcodeHeight + 15); 

            g2d.dispose();

            return new ImageIcon(combinedImage);

        } catch (WriterException e) {
            return null;
        }
    }

//    public static ImageIcon generateBarcode(String barcodeValue) {
//        try {
//            int barcodeWidth = 300;
//            int barcodeHeight = 100;
//            
//            Map<EncodeHintType, String> hints = new HashMap<>();
//            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//
//            BitMatrix bitMatrix = new MultiFormatWriter().encode(barcodeValue, BarcodeFormat.CODE_128, barcodeWidth, barcodeHeight, hints);
//
//            BufferedImage bufferedImage = new BufferedImage(barcodeWidth, barcodeHeight, BufferedImage.TYPE_INT_RGB);
//            for (int x = 0; x < barcodeWidth; x++) {
//                for (int y = 0; y < barcodeHeight; y++) {
//                    bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0x000000 : 0xFFFFFF);
//                }
//            }
//
//            return new ImageIcon(bufferedImage);
//
//        } catch (WriterException e) {
//            e.printStackTrace(System.out);
//            return null;
//        }
//    }
}
