package com.devinbileck.barcodes.barcode;

import java.awt.image.BufferedImage;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

public class BarcodeGenerator {
    private BarcodeGenerator() {
    }

    public static BarcodeResult generateQRCodeImage(@NotNull final String barcodeText) throws WriterException {
        return generateBarcodeImage(barcodeText, BarcodeFormat.QR_CODE, 0, 0);
    }

    public static BarcodeResult generateQRCodeImage(@NotNull final String barcodeText, final int width,
            final int height) throws WriterException {
        return generateBarcodeImage(barcodeText, BarcodeFormat.QR_CODE, width, height);
    }

    public static BarcodeResult generateBarcodeImage(@NotNull final String barcodeText,
            @NotNull final BarcodeFormat barcodeFormat, final int width, final int height) throws WriterException {
        Objects.requireNonNull(barcodeText);
        Objects.requireNonNull(barcodeFormat);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(barcodeText, barcodeFormat, width, height);
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
        return new BarcodeResult(barcodeFormat, barcodeText, image);
    }
}
