package com.devinbileck.barcodes.image;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javafx.scene.image.Image;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class IconFactoryTest {
    @Test
    void whenGettingNullIcon_thenExceptionThrown() {
        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> IconFactory.getImage(null));
    }

    @ParameterizedTest
    @EnumSource(IconFactory.ICON.class)
    void whenGettingAnIcon_thenAnImageIsReturned(IconFactory.ICON icon) {
        assertThat(IconFactory.getImage(icon)).isInstanceOf(Image.class);
    }
}
