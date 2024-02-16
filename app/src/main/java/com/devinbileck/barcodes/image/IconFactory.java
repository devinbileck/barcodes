package com.devinbileck.barcodes.image;

import javafx.scene.image.Image;

import javax.validation.constraints.NotNull;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class IconFactory {
    private static final Map<ICON, Image> images = new EnumMap<>(ICON.class);

    public enum ICON {
        APP,
        COPY,
        TRASH
    }

    public static Image getImage(@NotNull final ICON icon) {
        switch (icon) {
            case APP -> images.putIfAbsent(
                    icon,
                    new Image(
                            Objects.requireNonNull(
                                    IconFactory.class
                                            .getClassLoader()
                                            .getResourceAsStream("icons/qrcode_scan.png"))));
            case COPY -> images.putIfAbsent(
                    icon,
                    new Image(
                            Objects.requireNonNull(
                                    IconFactory.class
                                            .getClassLoader()
                                            .getResourceAsStream("icons/copy.png"))));
            case TRASH -> images.putIfAbsent(
                    icon,
                    new Image(
                            Objects.requireNonNull(
                                    IconFactory.class
                                            .getClassLoader()
                                            .getResourceAsStream("icons/trash.png"))));
        }
        return images.get(icon);
    }
}
