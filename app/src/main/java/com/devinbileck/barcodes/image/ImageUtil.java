package com.devinbileck.barcodes.image;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtil {
    private ImageUtil() {}

    /**
     * Compares two images pixel by pixel.
     *
     * @param imgA the first image.
     * @param imgB the second image.
     * @return whether the images are both the same or not.
     */
    public static boolean isImagesIdentical(BufferedImage imgA, BufferedImage imgB) {
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

    public static BufferedImage generateTextImage(String text, int width, int height) {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();

        Rectangle rect = new Rectangle(width, height);
        drawCenteredString(graphics, text, rect, new Font("Courier New", Font.PLAIN, 16));

        return bufferedImage;
    }

    /**
     * Draw a {@link String} centered in the middle of a {@link Rectangle}.
     *
     * @param g The ${@link Graphics} instance.
     * @param text The ${@link String} to draw.
     * @param rect The ${@link Rectangle} to center the text in.
     * @param font The ${@link Font} to use to draw the text.
     */
    private static void drawCenteredString(Graphics g, String text, Rectangle rect, Font font) {
        FontMetrics metrics = g.getFontMetrics(font);
        int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
        // For the Y coordinate of the text, add the ascent since in Java 2d 0 is top of the screen
        int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
        g.setFont(font);
        g.drawString(text, x, y);
    }
}
