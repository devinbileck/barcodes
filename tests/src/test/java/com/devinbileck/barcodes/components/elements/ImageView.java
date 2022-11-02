package com.devinbileck.barcodes.components.elements;

import static org.testfx.assertions.api.Assertions.assertThat;

import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.testfx.util.WaitForAsyncUtils;

import com.devinbileck.barcodes.components.views.View;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class ImageView extends Element<javafx.scene.image.ImageView> {
    public ImageView(View view, String query) {
        super(javafx.scene.image.ImageView.class, view, query);
    }

    public BufferedImage getImage() {
        Image fxImage = view.getWindow().lookup(query).queryAs(javafx.scene.image.ImageView.class).getImage();
        if (fxImage == null) {
            return null;
        }
        return SwingFXUtils.fromFXImage(fxImage, null);
    }

    public void assertIsNull() {
        assertThat(getImage()).describedAs("ImageView \"%s\" on view \"%s\" is null", query, view.getStageTitle())
                .isNull();
    }

    public void assertIsNotNull() {
        assertThat(getImage()).describedAs("ImageView \"%s\" on view \"%s\" is not null", query, view.getStageTitle())
                .isNotNull();
    }

    public void waitUntilNull(int timeoutSeconds) throws TimeoutException {
        WaitForAsyncUtils.waitFor(timeoutSeconds, TimeUnit.SECONDS, () -> Objects.isNull(getImage()));
    }

    public void waitUntilNotNull(int timeoutSeconds) throws TimeoutException {
        WaitForAsyncUtils.waitFor(timeoutSeconds, TimeUnit.SECONDS, () -> !Objects.isNull(getImage()));
    }
}
