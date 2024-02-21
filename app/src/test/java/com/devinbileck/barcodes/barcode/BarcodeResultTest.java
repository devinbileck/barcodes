package com.devinbileck.barcodes.barcode;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

import com.google.zxing.BarcodeFormat;

class BarcodeResultTest {
    @Test
    void testToStringFormat() {
        BarcodeResult barcodeResult = new BarcodeResult(BarcodeFormat.QR_CODE, "", null);
        assertThat(barcodeResult).hasToString("""
                BarcodeResult {
                    format : QR_CODE
                    text : "",
                    image : null
                }""");
    }
}
