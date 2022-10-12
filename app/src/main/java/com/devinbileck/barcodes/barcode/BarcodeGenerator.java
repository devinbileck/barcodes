package com.devinbileck.barcodes.barcode;

import java.awt.image.BufferedImage;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class BarcodeGenerator {
    private BarcodeGenerator() {}

    public static BufferedImage generateQRCodeImage(String barcodeText) throws WriterException {
        return generateBarcodeImage(barcodeText, BarcodeFormat.QR_CODE, 0, 0);
    }

    public static BufferedImage generateQRCodeImage(String barcodeText, int width, int height)
            throws WriterException {
        return generateBarcodeImage(barcodeText, BarcodeFormat.QR_CODE, width, height);
    }

    private static BufferedImage generateBarcodeImage(
            String barcodeText, BarcodeFormat barcodeFormat, int width, int height)
            throws WriterException {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, barcodeFormat, width, height);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }
}
