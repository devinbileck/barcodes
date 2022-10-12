package com.devinbileck.barcodes.tests.ui.views;

import org.junit.jupiter.api.BeforeAll;
import org.testfx.api.FxToolkit;

import com.devinbileck.barcodes.ui.UiApplication;

public abstract class ViewIT {
    @BeforeAll
    static void init() throws Exception {
        if (Boolean.getBoolean("headless")) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("java.awt.headless", "true");
        }

        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(UiApplication.class);
    }
}
