package com.devinbileck.barcodes.barcode;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static org.assertj.core.api.Assertions.assertThat;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

import com.google.zxing.BarcodeFormat;

class BarcodeProcessorTest {
    @Test
    void whenNoBarcodeInImage_thenEmptyResult() {
        BufferedImage image = new BufferedImage(640, 480, TYPE_INT_RGB);
        Optional<BarcodeResult> result = BarcodeProcessor.process(image);
        assertThat(result).isEmpty();
    }

    @Test
    void whenValidBarcodeInImage_thenValidResult() throws IOException {
        BufferedImage image = ImageIO.read(
                new File(Objects.requireNonNull(getClass().getClassLoader().getResource("Test_QR.png")).getFile()));
        Optional<BarcodeResult> result = BarcodeProcessor.process(image);
        assertThat(result).isNotEmpty();
        assertThat(result.get()).extracting(BarcodeResult::format, BarcodeResult::text, BarcodeResult::image)
                .containsExactly(BarcodeFormat.QR_CODE, "test", image);
    }
}
