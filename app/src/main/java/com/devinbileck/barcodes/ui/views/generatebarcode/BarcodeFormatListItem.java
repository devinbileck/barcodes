package com.devinbileck.barcodes.ui.views.generatebarcode;

import java.util.Objects;

import com.google.zxing.BarcodeFormat;

public record BarcodeFormatListItem(BarcodeFormat barcodeFormat) {
    @Override
    public String toString() {
        return barcodeFormat.toString();
    }

    @Override
    public boolean equals(Object otherObject) {
        if (!(otherObject instanceof BarcodeFormatListItem)) {
            return false;
        }
        return Objects.equals(this.toString(), otherObject.toString());
    }
}
