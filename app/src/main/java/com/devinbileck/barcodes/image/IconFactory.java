package com.devinbileck.barcodes.image;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import javafx.scene.image.Image;

public class IconFactory {
    private static final Map<ICON, Image> images = new EnumMap<>(ICON.class);

    public enum ICON {
        APP,
        COPY,
        TRASH
    }

    static {
        images.put(
                ICON.APP,
                new Image(
                        Objects.requireNonNull(
                                IconFactory.class
                                        .getClassLoader()
                                        .getResourceAsStream("icons/qrcode_scan.png"))));
        images.put(
                ICON.COPY,
                new Image(
                        Objects.requireNonNull(
                                IconFactory.class
                                        .getClassLoader()
                                        .getResourceAsStream("icons/copy.png"))));
        images.put(
                ICON.TRASH,
                new Image(
                        Objects.requireNonNull(
                                IconFactory.class
                                        .getClassLoader()
                                        .getResourceAsStream("icons/trash.png"))));
    }

    public static Image getImage(@NotNull final ICON icon) {
        return images.get(icon);
    }
}
