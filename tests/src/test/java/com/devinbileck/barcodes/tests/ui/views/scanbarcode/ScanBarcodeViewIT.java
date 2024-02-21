package com.devinbileck.barcodes.tests.ui.views.scanbarcode;

import static com.devinbileck.barcodes.webcam.WebcamDiscoveryService.SIMULATED_WEBCAM_GET_IMAGE_DELAY_MILLIS;

import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.devinbileck.barcodes.components.views.main.MainView;
import com.devinbileck.barcodes.components.views.scanbarcode.ScanBarcodeView;
import com.devinbileck.barcodes.extensions.UiApplicationExtension;

@ExtendWith(UiApplicationExtension.class)
class ScanBarcodeViewIT {
    @Nested
    class NoWebcamsDiscovered {
        @BeforeEach
        void setup() throws TimeoutException {
            System.setProperty("simulation.enabled", "true");
            System.setProperty("simulation.fakeWebcams", "false");

            if (ScanBarcodeView.getInstance().isShowing()) {
                ScanBarcodeView.getInstance().closeButton.click();
                ScanBarcodeView.getInstance().waitUntilNotShowing(10);
            }

            MainView.getInstance().scanBarcodeButton.click();
            ScanBarcodeView.getInstance().waitUntilShowing(10);
        }

        @AfterEach
        void teardown() throws TimeoutException {
            if (ScanBarcodeView.getInstance().isShowing()) {
                ScanBarcodeView.getInstance().closeButton.click();
                ScanBarcodeView.getInstance().waitUntilNotShowing(10);
            }
        }

        @Test
        void fieldsDisplayCorrectText() {
            ScanBarcodeView.getInstance().webcamComboBox.assertHasPromptText("Choose Camera");
            ScanBarcodeView.getInstance().closeButton.assertHasText("Close");
        }

        @Test
        void webcamComboboxIsEmptyAndDisabled() {
            ScanBarcodeView.getInstance().webcamComboBox.assertIsEmpty();
            ScanBarcodeView.getInstance().webcamComboBox.assertIsDisabled();
        }
    }

    @Nested
    class WebcamsDiscovered {
        @BeforeEach
        void setup() throws TimeoutException {
            System.setProperty("simulation.enabled", "true");
            System.setProperty("simulation.fakeWebcams", "true");

            if (ScanBarcodeView.getInstance().isShowing()) {
                ScanBarcodeView.getInstance().closeButton.click();
                ScanBarcodeView.getInstance().waitUntilNotShowing(10);
            }

            MainView.getInstance().scanBarcodeButton.click();
            ScanBarcodeView.getInstance().waitUntilShowing(10);
        }

        @AfterEach
        void teardown() throws TimeoutException {
            if (ScanBarcodeView.getInstance().isShowing()) {
                ScanBarcodeView.getInstance().closeButton.click();
                ScanBarcodeView.getInstance().waitUntilNotShowing(10);
            }
        }

        @Test
        void fieldsDisplayCorrectText() {
            ScanBarcodeView.getInstance().webcamComboBox.assertHasPromptText("Choose Camera");
            ScanBarcodeView.getInstance().closeButton.assertHasText("Close");
        }

        @Test
        void webcamComboboxIsPopulatedAndEnabled() {
            ScanBarcodeView.getInstance().webcamComboBox.assertIsNotEmpty();
            ScanBarcodeView.getInstance().webcamComboBox.assertIsEnabled();
        }

        @Test
        void imageViewDisplaysImageFromSelectedFunctionalWebcam() throws TimeoutException {
            ScanBarcodeView.getInstance().webcamComboBox.selectIndex(0);
            ScanBarcodeView.getInstance().webcamImageView.waitUntilNotNull(1);
            ScanBarcodeView.getInstance().webcamImageView.assertShown();
            ScanBarcodeView.getInstance().webcamStatusLabel.assertTextIsEqualTo("");
            ScanBarcodeView.getInstance().waitUntilNotShowing(15);
            MainView.getInstance().listView.assertNotEmpty();
        }

        @Test
        void imageViewDisplaysErrorFromSelectedDisconnectedWebcam() throws TimeoutException {
            ScanBarcodeView.getInstance().webcamComboBox.selectIndex(1);
            ScanBarcodeView.getInstance().webcamImageView.waitUntilNotNull(1);
            ScanBarcodeView.getInstance().webcamImageView.assertShown();
            ScanBarcodeView.getInstance()
                    .webcamImageView
                    .waitUntilNull(
                            (int) (SIMULATED_WEBCAM_GET_IMAGE_DELAY_MILLIS / 1000) + 1);
            ScanBarcodeView.getInstance().webcamImageView.waitUntilNotShown(1);
            ScanBarcodeView.getInstance()
                    .webcamStatusLabel
                    .assertTextIsEqualTo("Failed to get image from webcam. Is it disconnected?");
        }

        @Test
        void imageViewDisplaysErrorFromSelectedNonOpeningWebcam() {
            ScanBarcodeView.getInstance().webcamComboBox.selectIndex(2);
            ScanBarcodeView.getInstance().webcamImageView.assertIsNull();
            ScanBarcodeView.getInstance().webcamImageView.assertNotShown();
            ScanBarcodeView.getInstance()
                    .webcamStatusLabel
                    .assertTextIsEqualTo("Failed to load webcam");
        }

        @Test
        void dialogClosesWhenClosingDialogWithWebcamSelected() {
            ScanBarcodeView.getInstance().webcamComboBox.selectIndex(0);
            ScanBarcodeView.getInstance().closeButton.click();
            MainView.getInstance().listView.assertEmpty();
        }
    }
}
