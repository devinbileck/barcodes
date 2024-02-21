package com.devinbileck.barcodes.extensions;

import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testfx.api.FxToolkit;
import org.testfx.util.WaitForAsyncUtils;

import com.devinbileck.barcodes.ui.UiApplication;

public class UiApplicationExtension implements BeforeAllCallback, BeforeEachCallback, AfterEachCallback {
    @Override
    public void beforeAll(ExtensionContext context) {
        if (Boolean.getBoolean("headless")) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("java.awt.headless", "true");
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) throws TimeoutException {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(UiApplication.class);
    }

    @Override
    public void afterEach(ExtensionContext context) {
        WaitForAsyncUtils.waitForFxEvents();
    }
}
