package com.devinbileck.barcodes.test.extensions;

import javafx.application.Platform;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.devinbileck.barcodes.test.utils.JavaFxThreadUtil;

/**
 * This JUnit 5 extension is used to start a JavaFX thread before executing tests, so that you can
 * use {@link Platform#runLater} and similar constructs in your tests.
 *
 * <p>However, this extension will not execute tests on the JavaFX thread. To do so, use {@link
 * JavaFxThreadUtil#runInFxThread(Runnable)}.
 */
public class JavaFxToolkitExtension implements BeforeAllCallback {
    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException ignored) {
            // The JavaFX runtime is already running
        }
    }
}
