package com.devinbileck.barcodes.components.elements;

import static org.testfx.assertions.api.Assertions.assertThat;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javafx.scene.Node;

import org.testfx.util.WaitForAsyncUtils;

import com.devinbileck.barcodes.components.views.View;

public abstract class Element<T extends Node> {
    private final Class<T> type;
    protected final View view;
    protected final String query;

    protected Element(Class<T> type, View view, String query) {
        this.type = type;
        this.view = view;
        this.query = query;
    }

    public boolean isVisible() {
        try {
            return view.getWindow().lookup(query).queryAs(type).isVisible();
        } catch (NoSuchElementException ignored) {
            return false;
        }
    }

    public boolean isDisabled() {
        return view.getWindow().lookup(query).queryAs(type).isDisabled();
    }

    public double getWidth() {
        return view.getWindow().lookup(query).queryAs(type).getLayoutBounds().getWidth();
    }

    public double getHeight() {
        return view.getWindow().lookup(query).queryAs(type).getLayoutBounds().getHeight();
    }

    public void assertIsVisible() {
        assertThat(isVisible())
                .describedAs(
                        "%s \"%s\" on view \"%s\" is visible",
                        type.getName(), query, view.getStageTitle())
                .isTrue();
    }

    public void assertIsInvisible() {
        assertThat(isVisible())
                .describedAs(
                        "%s \"%s\" on view \"%s\" is invisible",
                        type.getName(), query, view.getStageTitle())
                .isFalse();
    }

    public void assertIsEnabled() {
        assertThat(isDisabled())
                .describedAs(
                        "%s \"%s\" on view \"%s\" is enabled",
                        type.getName(), query, view.getStageTitle())
                .isFalse();
    }

    public void assertIsDisabled() {
        assertThat(isDisabled())
                .describedAs(
                        "%s \"%s\" on view \"%s\" is disabled",
                        type.getName(), query, view.getStageTitle())
                .isTrue();
    }

    public void assertShown() {
        assertThat(getWidth())
                .describedAs(
                        "%s \"%s\" on view \"%s\" is shown (width > 0)",
                        type.getName(), query, view.getStageTitle())
                .isPositive();
        assertThat(getHeight())
                .describedAs(
                        "%s \"%s\" on view \"%s\" is shown (height > 0)",
                        type.getName(), query, view.getStageTitle())
                .isPositive();
    }

    public void assertNotShown() {
        assertThat(getWidth())
                .describedAs(
                        "%s \"%s\" on view \"%s\" is not shown (width == 0)",
                        type.getName(), query, view.getStageTitle())
                .isZero();
        assertThat(getHeight())
                .describedAs(
                        "%s \"%s\" on view \"%s\" is not shown (height == 0)",
                        type.getName(), query, view.getStageTitle())
                .isZero();
    }

    public void waitUntilVisible(int timeoutSeconds) throws TimeoutException {
        WaitForAsyncUtils.waitFor(timeoutSeconds, TimeUnit.SECONDS, this::isVisible);
    }

    public void waitUntilInvisible(int timeoutSeconds) throws TimeoutException {
        WaitForAsyncUtils.waitFor(timeoutSeconds, TimeUnit.SECONDS, () -> !isVisible());
    }

    public void waitUntilEnabled(int timeoutSeconds) throws TimeoutException {
        WaitForAsyncUtils.waitFor(timeoutSeconds, TimeUnit.SECONDS, () -> !isDisabled());
    }

    public void waitUntilDisabled(int timeoutSeconds) throws TimeoutException {
        WaitForAsyncUtils.waitFor(timeoutSeconds, TimeUnit.SECONDS, this::isDisabled);
    }

    public void waitUntilShown(int timeoutSeconds) throws TimeoutException {
        WaitForAsyncUtils.waitFor(timeoutSeconds, TimeUnit.SECONDS, () -> getHeight() > 0);
    }

    public void waitUntilNotShown(int timeoutSeconds) throws TimeoutException {
        WaitForAsyncUtils.waitFor(timeoutSeconds, TimeUnit.SECONDS, () -> getHeight() == 0);
    }
}
