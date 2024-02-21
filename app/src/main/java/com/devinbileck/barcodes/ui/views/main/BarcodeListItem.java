package com.devinbileck.barcodes.ui.views.main;

import java.awt.image.BufferedImage;
import java.util.Objects;

import com.google.zxing.BarcodeFormat;

public record BarcodeListItem(BarcodeFormat format, String content, long timestamp, BufferedImage image) {
    @Override
    public String toString() {
        return String.format("""
                BarcodeListItem {
                    format : %s,
                    content : "%s",
                    timestamp : %s,
                    image : %s
                }""", format, content, timestamp, image);
    }

    @Override
    public boolean equals(Object otherObject) {
        if (!(otherObject instanceof BarcodeListItem otherBarcodeListItem)) {
            return false;
        }
        return Objects.equals(this.format, otherBarcodeListItem.format) && Objects.equals(this.content,
                otherBarcodeListItem.content) && Objects.equals(this.timestamp, otherBarcodeListItem.timestamp)
                && Objects.equals(this.image, otherBarcodeListItem.image);
    }
}
