package com.devinbileck.barcodes.barcode;

import java.awt.image.BufferedImage;

import com.google.zxing.BarcodeFormat;

public record BarcodeResult(BarcodeFormat format, String text, BufferedImage image) {
    @Override
    public String toString() {
        return String.format("""
                BarcodeResult {
                    format : %s
                    text : "%s",
                    image : %s
                }""", format, text, image);
    }
}
