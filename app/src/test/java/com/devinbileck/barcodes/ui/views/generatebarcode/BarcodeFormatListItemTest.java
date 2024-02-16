package com.devinbileck.barcodes.ui.views.generatebarcode;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

import com.google.zxing.BarcodeFormat;

class BarcodeFormatListItemTest {
    @Test
    void testToStringFormat() {
        BarcodeFormatListItem barcodeFormatListItem = new BarcodeFormatListItem(BarcodeFormat.QR_CODE);
        assertThat(barcodeFormatListItem).hasToString("QR_CODE");
    }

    @Test
    void testEqualsWithEqualObjects() {
        BarcodeFormatListItem barcodeFormatListItem = new BarcodeFormatListItem(BarcodeFormat.QR_CODE);
        BarcodeFormatListItem otherObject = new BarcodeFormatListItem(BarcodeFormat.QR_CODE);
        assertThat(barcodeFormatListItem.equals(otherObject)).isTrue();
    }

    @Test
    void testEqualsWithDifferentObjectType() {
        BarcodeFormatListItem barcodeFormatListItem = new BarcodeFormatListItem(BarcodeFormat.QR_CODE);
        assertThat(barcodeFormatListItem.equals(new Object())).isFalse();
    }

    @Test
    void testEqualsWithNonEqualObjects() {
        BarcodeFormatListItem barcodeFormatListItem = new BarcodeFormatListItem(BarcodeFormat.QR_CODE);
        BarcodeFormatListItem otherObject = new BarcodeFormatListItem(BarcodeFormat.UPC_A);
        assertThat(barcodeFormatListItem.equals(otherObject)).isFalse();
    }
}
