package com.devinbileck.barcodes.tests.ui.views.generatebarcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import com.devinbileck.barcodes.barcode.BarcodeGenerator;
import com.devinbileck.barcodes.components.views.generatebarcode.GenerateBarcodeView;
import com.devinbileck.barcodes.components.views.main.MainView;
import com.devinbileck.barcodes.image.ImageUtil;
import com.devinbileck.barcodes.tests.ui.views.ViewIT;
import com.devinbileck.barcodes.ui.views.generatebarcode.BarcodeFormatListItem;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

@ExtendWith(ApplicationExtension.class)
class GenerateBarcodeViewIT extends ViewIT {
    @BeforeEach
    void setup() throws TimeoutException {
        if (GenerateBarcodeView.getInstance().isShowing()) {
            GenerateBarcodeView.getInstance().closeButton.click();
            GenerateBarcodeView.getInstance().waitUntilNotShowing(10);
        }

        MainView.getInstance().generateBarcodeButton.click();
        GenerateBarcodeView.getInstance().waitUntilShowing(10);
    }

    @AfterEach
    void teardown() throws TimeoutException {
        if (GenerateBarcodeView.getInstance().isShowing()) {
            GenerateBarcodeView.getInstance().closeButton.click();
            GenerateBarcodeView.getInstance().waitUntilNotShowing(10);
        }
    }

    @Test
    void fieldsDisplayCorrectText() {
        GenerateBarcodeView.getInstance().contentTextArea.assertHasPromptText("Enter Content");
        GenerateBarcodeView.getInstance().barcodeTypeComboBox.assertContainsExactlyItemsInOrder(
                Arrays.stream(BarcodeFormat.values()).map(BarcodeFormatListItem::new).collect(Collectors.toList()));
        assertThat(GenerateBarcodeView.getInstance().barcodeTypeComboBox.getSelectedText()).isEqualTo("Select Format");
        GenerateBarcodeView.getInstance().generateButton.assertHasText("Generate");
        GenerateBarcodeView.getInstance().closeButton.assertHasText("Close");
    }

    @Test
    void whenNoContent_generateButtonIsDisabled() {
        GenerateBarcodeView.getInstance().contentTextArea.assertIsEmpty();
        GenerateBarcodeView.getInstance().generateButton.assertIsDisabled();
    }

    @Test
    void whenGenerateButtonClicked_thenBarcodeImageGenerated() throws WriterException {
        GenerateBarcodeView.getInstance().contentTextArea.setText("This is a test");
        GenerateBarcodeView.getInstance().barcodeTypeComboBox.selectItem(
                new BarcodeFormatListItem(BarcodeFormat.QR_CODE));
        GenerateBarcodeView.getInstance().generateButton.click();
        int imageViewWidth = (int) GenerateBarcodeView.getInstance().imageView.getWidth();
        int imageViewHeight = (int) GenerateBarcodeView.getInstance().imageView.getHeight();
        BufferedImage expectedImage =
                BarcodeGenerator.generateQRCodeImage("This is a test", imageViewWidth, imageViewHeight).image();
        BufferedImage actualImage = GenerateBarcodeView.getInstance().imageView.getImage();
        assertThat(actualImage).describedAs("Generated image is not null").isNotNull();
        assertThat(ImageUtil.isImagesIdentical(expectedImage, actualImage)).describedAs(
                "Generated image is as expected").isTrue();
    }

    @Test
    void whenReopeningView_allFieldsAreResetToDefault() {
        GenerateBarcodeView.getInstance().contentTextArea.setText("This is a test");
        GenerateBarcodeView.getInstance().barcodeTypeComboBox.selectItem(
                new BarcodeFormatListItem(BarcodeFormat.QR_CODE));
        GenerateBarcodeView.getInstance().generateButton.click();

        GenerateBarcodeView.getInstance().closeButton.click();
        MainView.getInstance().generateBarcodeButton.click();

        GenerateBarcodeView.getInstance().contentTextArea.assertIsEmpty();
        assertThat(GenerateBarcodeView.getInstance().barcodeTypeComboBox.getSelectedText()).isEqualTo("Select Format");
        GenerateBarcodeView.getInstance().generateButton.assertIsDisabled();
        GenerateBarcodeView.getInstance().imageView.assertIsNull();
    }
}
