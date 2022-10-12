package com.devinbileck.barcodes.barcode;

import static com.devinbileck.barcodes.image.ImageUtil.isImagesIdentical;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

import com.google.zxing.WriterException;

class BarcodeGeneratorTest {
    @Test
    void whenGeneratingQrCodeWithEmptyText_exceptionIsThrown() {
        assertThrows(
                IllegalArgumentException.class, () -> BarcodeGenerator.generateQRCodeImage(""));
    }

    @Test
    void whenGeneratingQrCodeWithNegativeSize_exceptionIsThrown() {
        assertThrows(
                IllegalArgumentException.class,
                () -> BarcodeGenerator.generateQRCodeImage("test", -1, -1));
    }

    @Test
    void whenGeneratingQrCodeWithoutSize_validImageIsGenerated()
            throws WriterException, IOException {
        BufferedImage expectedImage =
                ImageIO.read(
                        new File(
                                Objects.requireNonNull(
                                                getClass()
                                                        .getClassLoader()
                                                        .getResource("Test_QR.png"))
                                        .getFile()));
        BufferedImage actualImage = BarcodeGenerator.generateQRCodeImage("test");
        assertThat(isImagesIdentical(expectedImage, actualImage)).isTrue();
    }

    @Test
    void whenGeneratingQrCodeWithCustomSize_validImageIsGenerated()
            throws WriterException, IOException {
        BufferedImage expectedImage =
                ImageIO.read(
                        new File(
                                Objects.requireNonNull(
                                                getClass()
                                                        .getClassLoader()
                                                        .getResource("Test_QR_Custom_Size.png"))
                                        .getFile()));
        BufferedImage actualImage = BarcodeGenerator.generateQRCodeImage("test", 1012, 1012);
        assertThat(isImagesIdentical(expectedImage, actualImage)).isTrue();
    }
}
