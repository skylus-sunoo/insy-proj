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
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.PdfException;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

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

    public enum FileType {
        PDF, PNG
    }

    public static void generateFileFromBarcodes(List<BufferedImage> bufferedImages, FileType FileType, String fileName) throws IOException {
        String outputFilePath = "C:\\Users\\admin\\Documents\\" + fileName + ".";
        int barcodeWidth = 615;
        int barcodeHeight = 213;
        int spacing = 20;
        int totalHeight = (barcodeHeight + spacing) * bufferedImages.size() - spacing;

        if (bufferedImages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No barcodes to process!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (FileType == FileType.PNG) {
            outputFilePath += "png";

            BufferedImage combinedImage = new BufferedImage(barcodeWidth, totalHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics g = combinedImage.getGraphics();

            int y = 0;
            for (BufferedImage barcode : bufferedImages) {
                g.drawImage(barcode, 0, y, barcodeWidth, barcodeHeight, null);
                y += barcodeHeight + spacing;
            }

            g.dispose();

            ImageIO.write(combinedImage, "png", new File(outputFilePath));
        } else if (FileType == FileType.PDF) {
            outputFilePath += "pdf";

            PdfWriter writer = new PdfWriter(outputFilePath);
            PdfDocument pdf = new PdfDocument(writer);

            String pageSizeSTR = "A4";
            PageSize pageSize = new PageSize(PageSize.A4);

            try (Document document = new Document(pdf, pageSize)) {
                if (pageSizeSTR.equals("A4")) {
                    float x = 50;
                    float y = 750;
                    for (BufferedImage bufferedImage : bufferedImages) {
                        ImageData imageData = ImageDataFactory.create(bufferedImage, null);
                        Image image = new Image(imageData);

                        image.scaleToFit(150, 150);
                        image.setFixedPosition(x, y);
                        document.add(image);

                        y -= 77;

                        if (y < 50) {
                            y = 750;
                            x += 180;
                            if (x > 500) {
                                pdf.addNewPage();
                                x = 50;
                            }
                        }
                    }
                } else if (pageSizeSTR.equals("B9")) {
                    float pageHeight = pageSize.getHeight();
                    float x = 10;
                    float y = pageHeight - 50;

                    int barcodeCount = 0;

                    for (BufferedImage bufferedImage : bufferedImages) {
                        ImageData imageData = ImageDataFactory.create(bufferedImage, null);
                        Image image = new Image(imageData);

                        image.scaleToFit(106, 106);
                        image.setFixedPosition(x, y);
                        document.add(image);

                        barcodeCount++;

                        y -= 50;

                        if (barcodeCount % 3 == 0) {
                            pdf.addNewPage();
                            y = pageHeight - 50;
                            x = 10;
                        }
                    }
                }
            }
        }

        File outputFile = new File(outputFilePath);
        File parentDirectory = outputFile.getParentFile();
        if (Desktop.isDesktopSupported() && parentDirectory.exists()) {
            Desktop.getDesktop().open(parentDirectory);
        } else {
            System.out.println("Could not open File Explorer. Ensure Desktop is supported and the folder exists.");
        }
        
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            File File = new File(outputFilePath);
            if (File.exists()) {
                desktop.open(File);
            }
        }

        JOptionPane.showMessageDialog(null, "File created successfully: " + outputFilePath, "Print Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
