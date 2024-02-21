package com.devinbileck.barcodes.ui.views.main;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.awt.image.BufferedImage;
import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.google.zxing.BarcodeFormat;

class BarcodeListItemTest {
    @Test
    void testToStringFormat() {
        long timestamp = Instant.now().toEpochMilli();
        BarcodeListItem barcodeListItem = new BarcodeListItem(
                BarcodeFormat.QR_CODE, "first content", timestamp, null);
        assertThat(barcodeListItem).hasToString(String.format("""
                BarcodeListItem {
                    format : QR_CODE,
                    content : "first content",
                    timestamp : %s,
                    image : null
                }""", timestamp));
    }

    @Test
    void testEqualsWithEqualObjects() {
        long timestamp = Instant.now().toEpochMilli();
        BarcodeListItem barcodeListItem = new BarcodeListItem(
                BarcodeFormat.QR_CODE, "first content", timestamp, null);
        BarcodeListItem otherObject = new BarcodeListItem(
                BarcodeFormat.QR_CODE, "first content", timestamp, null);
        assertThat(barcodeListItem.equals(otherObject)).isTrue();
    }

    @Test
    void testEqualsWithDifferentObjectType() {
        BarcodeListItem barcodeListItem = new BarcodeListItem(
                BarcodeFormat.QR_CODE, "first content", Instant.now().toEpochMilli(), null);
        assertThat(barcodeListItem.equals(new Object())).isFalse();
    }

    @Test
    void testEqualsWithNonEqualFormat() {
        long timestamp = Instant.now().toEpochMilli();
        BarcodeListItem barcodeListItem = new BarcodeListItem(
                BarcodeFormat.UPC_A, "first content", timestamp, null);
        BarcodeListItem otherObject = new BarcodeListItem(
                BarcodeFormat.QR_CODE, "first content", timestamp, null);
        assertThat(barcodeListItem.equals(otherObject)).isFalse();
    }

    @Test
    void testEqualsWithNonEqualContent() {
        long timestamp = Instant.now().toEpochMilli();
        BarcodeListItem barcodeListItem = new BarcodeListItem(
                BarcodeFormat.QR_CODE, "first content", timestamp, null);
        BarcodeListItem otherObject = new BarcodeListItem(
                BarcodeFormat.QR_CODE, "second content", timestamp, null);
        assertThat(barcodeListItem.equals(otherObject)).isFalse();
    }

    @Test
    void testEqualsWithNonEqualTimestamp() {
        long timestamp = Instant.now().toEpochMilli();
        BarcodeListItem barcodeListItem = new BarcodeListItem(
                BarcodeFormat.QR_CODE, "first content", timestamp, null);
        BarcodeListItem otherObject = new BarcodeListItem(
                BarcodeFormat.QR_CODE, "first content", timestamp + 1, null);
        assertThat(barcodeListItem.equals(otherObject)).isFalse();
    }

    @Test
    void testEqualsWithNonEqualImage() {
        long timestamp = Instant.now().toEpochMilli();
        BufferedImage image = new BufferedImage(640, 480, TYPE_INT_RGB);
        BarcodeListItem barcodeListItem = new BarcodeListItem(
                BarcodeFormat.QR_CODE, "first content", timestamp, image);
        BarcodeListItem otherObject = new BarcodeListItem(
                BarcodeFormat.QR_CODE, "first content", timestamp, null);
        assertThat(barcodeListItem.equals(otherObject)).isFalse();
    }
}
