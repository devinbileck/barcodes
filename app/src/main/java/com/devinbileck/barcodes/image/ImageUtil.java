package com.devinbileck.barcodes.image;

import org.apache.commons.text.WordUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ImageUtil {
    private ImageUtil() {
        // this class cannot be instantiated
    }

    /**
     * Compares two images pixel by pixel.
     *
     * @param imgA the first image.
     * @param imgB the second image.
     * @return whether the images are identical or not.
     */
    public static boolean isImagesIdentical(final BufferedImage imgA, final BufferedImage imgB) {
        if (imgA.getWidth() != imgB.getWidth() || imgA.getHeight() != imgB.getHeight()) {
            return false;
        }

        int width = imgA.getWidth();
        int height = imgA.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Generate an image consisting of a black background with white text overlay.
     *
     * @param text the {@link String} to show wrapped and centered within the image.
     * @param width the width of the overall image.
     * @param height the height of the overall image.
     * @return the generated {@link BufferedImage}.
     */
    public static BufferedImage generateTextImage(final String text, final int width, final int height) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();

        Rectangle rect = new Rectangle(width, height);
        drawString(graphics, text, rect, getFont());

        return bufferedImage;
    }

    /**
     * Draw a {@link String} wrapped and centered in the middle of a {@link Rectangle}.
     *
     * @param graphics The {@link Graphics} instance.
     * @param text The {@link String} to draw.
     * @param rect The {@link Rectangle} to center the text in.
     * @param font The {@link Font} to use to draw the text.
     */
    private static void drawString(final Graphics graphics, final String text, final Rectangle rect, final Font font) {
        final FontMetrics metrics = graphics.getFontMetrics(font);
        final int wrapLength = rect.width / metrics.charWidth('a');
        final String wrappedText = WordUtils.wrap(text, wrapLength);
        final int lineCount = wrappedText.split("\n").length;

        graphics.setFont(font);

        // For the initial Y coordinate of the text, add the ascent since in Java 2d 0 is top of the screen
        int y = rect.y + ((rect.height - metrics.getHeight() * lineCount) / 2) + metrics.getAscent();

        for (String line : wrappedText.split("\n")) {
            int x = rect.x + (rect.width - metrics.stringWidth(line)) / 2;
            graphics.drawString(line, x, y);
            y += metrics.getHeight();
        }
    }

    /**
     * Get a consistent font for generating text content.
     *
     * @return A {@link Font} to use for generating text content.
     */
    private static Font getFont() {
        InputStream stream = Objects.requireNonNull(
                ClassLoader.getSystemClassLoader().getResourceAsStream("fonts/free-sans.ttf"));
        try {
            return Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(24f);
        } catch (IOException | FontFormatException ignored) {
            // Too bad, just use the font defined above
            return new Font("Courier New", Font.PLAIN, 24);
        }
    }
}
