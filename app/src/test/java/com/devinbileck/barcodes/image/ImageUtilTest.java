package com.devinbileck.barcodes.image;

import static com.devinbileck.barcodes.image.ImageUtil.generateTextImage;
import static com.devinbileck.barcodes.image.ImageUtil.isImagesIdentical;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ImageUtilTest {
    @Test
    void whenComparingIdenticalImages_thenResultIsTrue() {
        BufferedImage image1 = createImage(640, 480, Color.RED);
        BufferedImage image2 = createImage(640, 480, Color.RED);
        assertThat(ImageUtil.isImagesIdentical(image1, image2)).isTrue();
    }

    @Test
    void whenComparingSimilarImagesWithDifferingDimensions_thenResultIsFalse() {
        BufferedImage image1 = createImage(640, 480, Color.RED);
        BufferedImage image2 = createImage(1024, 720, Color.RED);
        assertThat(ImageUtil.isImagesIdentical(image1, image2)).isFalse();
    }

    @Test
    void whenComparingImagesWithDifferingPixels_thenResultIsFalse() {
        BufferedImage image1 = createImage(640, 480, Color.RED);
        BufferedImage image2 = createImage(640, 480, Color.BLACK);
        assertThat(ImageUtil.isImagesIdentical(image1, image2)).isFalse();
    }

    @Test
    void whenGeneratingTextImageWithNegativeSize_exceptionIsThrown() {
        assertThrows(IllegalArgumentException.class, () -> generateTextImage("test", -1, -1));
    }

    @ParameterizedTest
    @CsvSource({
        "'', 640, 480, Blank_Image.png",
        "Short Text, 640, 480, Short_Text_Image.png",
        "This is long text for testing purposes to ensure the image is generated correctly, 640,"
                + " 480, Long_Text_Image.png",
    })
    void whenGeneratingTextImage_imageIsGenerated(
            String text, int width, int height, String resourceName) throws IOException {
        BufferedImage expectedImage =
                ImageIO.read(
                        new File(
                                Objects.requireNonNull(
                                                getClass()
                                                        .getClassLoader()
                                                        .getResource(resourceName))
                                        .getFile()));
        BufferedImage actualImage = generateTextImage(text, width, height);
        assertThat(isImagesIdentical(expectedImage, actualImage)).isTrue();
    }

    private BufferedImage createImage(int width, int height, Color color) {
        BufferedImage img = new BufferedImage(width, height, TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(color);
        g2d.fillRect(0, 0, img.getWidth(), img.getHeight());
        g2d.dispose();
        return img;
    }
}
