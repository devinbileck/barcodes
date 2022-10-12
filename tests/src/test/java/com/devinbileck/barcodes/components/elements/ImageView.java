package com.devinbileck.barcodes.components.elements;

import static org.testfx.assertions.api.Assertions.assertThat;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javafx.scene.image.Image;

import org.testfx.util.WaitForAsyncUtils;

import com.devinbileck.barcodes.components.views.View;

public class ImageView extends Element<javafx.scene.image.ImageView> {
    public ImageView(View view, String query) {
        super(javafx.scene.image.ImageView.class, view, query);
    }

    public Image getImage() {
        return view.getWindow()
                .lookup(query)
                .queryAs(javafx.scene.image.ImageView.class)
                .getImage();
    }

    public void assertIsNull() {
        assertThat(getImage())
                .describedAs("ImageView \"%s\" on view \"%s\" is null", query, view.getStageTitle())
                .isNull();
    }

    public void assertIsNotNull() {
        assertThat(getImage())
                .describedAs(
                        "ImageView \"%s\" on view \"%s\" is not null", query, view.getStageTitle())
                .isNotNull();
    }

    public void waitUntilNull(int timeoutSeconds) throws TimeoutException {
        WaitForAsyncUtils.waitFor(
                timeoutSeconds, TimeUnit.SECONDS, () -> Objects.isNull(getImage()));
    }

    public void waitUntilNotNull(int timeoutSeconds) throws TimeoutException {
        WaitForAsyncUtils.waitFor(
                timeoutSeconds, TimeUnit.SECONDS, () -> !Objects.isNull(getImage()));
    }
}
