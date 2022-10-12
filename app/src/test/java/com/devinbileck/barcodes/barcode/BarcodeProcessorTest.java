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

class BarcodeProcessorTest {
    @Test
    void whenNoBarcodeInImage_thenEmptyResult() {
        BufferedImage image = new BufferedImage(640, 480, TYPE_INT_RGB);
        Optional<String> result = BarcodeProcessor.process(image);
        assertThat(result).isEmpty();
    }

    @Test
    void whenValidBarcodeInImage_thenValidResult() throws IOException {
        BufferedImage image =
                ImageIO.read(
                        new File(
                                Objects.requireNonNull(
                                                getClass()
                                                        .getClassLoader()
                                                        .getResource("Test_QR.png"))
                                        .getFile()));
        Optional<String> result = BarcodeProcessor.process(image);
        assertThat(result).isNotEmpty().get().isEqualTo("test");
    }
}
