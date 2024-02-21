package com.devinbileck.barcodes.tests.ui.views.main;

import static com.devinbileck.barcodes.test.utils.JavaFxThreadUtil.runInFxThread;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.devinbileck.barcodes.components.views.generatebarcode.GenerateBarcodeView;
import com.devinbileck.barcodes.components.views.main.MainView;
import com.devinbileck.barcodes.components.views.scanbarcode.ScanBarcodeView;
import com.devinbileck.barcodes.extensions.UiApplicationExtension;

@ExtendWith(UiApplicationExtension.class)
class MainViewIT {
    @BeforeEach
    void setup() throws ExecutionException, InterruptedException, TimeoutException {
        MainView.getInstance().listView.clearItems();
    }

    @Test
    void fieldsDisplayCorrectText() {
        MainView.getInstance().generateBarcodeButton.assertHasText("Generate Barcode");
        MainView.getInstance().scanBarcodeButton.assertHasText("Scan Barcode");
        MainView.getInstance().clearBarcodesButton.assertHasText("Clear Barcodes");
    }

    @Test
    void whenNoContent_clearButtonIsDisabled() {
        MainView.getInstance().listView.assertEmpty();
        MainView.getInstance().clearBarcodesButton.assertIsDisabled();
    }

    @Test
    void clickingGenerateButton_displaysGenerateDialog() {
        MainView.getInstance().generateBarcodeButton.click();
        GenerateBarcodeView.getInstance().assertIsShowing();
        GenerateBarcodeView.getInstance().closeButton.click();
    }

    @Test
    void clickingScanButton_displaysScanDialog() {
        MainView.getInstance().scanBarcodeButton.click();
        ScanBarcodeView.getInstance().assertIsShowing();
        ScanBarcodeView.getInstance().closeButton.click();
    }

    @Test
    void clickingClearButton_clearsScannedContentListView()
            throws ExecutionException, InterruptedException, TimeoutException {
        MainView.getInstance().listView.addItem("Content");
        MainView.getInstance().listView.assertNotEmpty();
        MainView.getInstance().clearBarcodesButton.assertIsEnabled();
        MainView.getInstance().clearBarcodesButton.click();
        MainView.getInstance().listView.assertEmpty();
        MainView.getInstance().clearBarcodesButton.assertIsDisabled();
    }

    @Test
    @Disabled("Not able to interact with context menus")
    void rightClickListViewItem_showsContextMenu()
            throws ExecutionException, InterruptedException, TimeoutException {
        MainView.getInstance().listView.addItem("Content");
        MainView.getInstance().listView.rightClickCell(0);
        MainView.getInstance().listView.assertContextMenuShown(0, "Copy", "Delete");
        MainView.getInstance().listView.closeContextMenu(0);
    }

    @Test
    @Disabled("Not able to interact with context menus")
    void rightClickListViewItemAndSelectCopy_contentCopiedToClipboard()
            throws ExecutionException, InterruptedException, TimeoutException {
        runInFxThread(() -> Clipboard.getSystemClipboard().clear());
        MainView.getInstance().listView.addItem("Content");
        MainView.getInstance().listView.rightClickCellAndSelectMenuItem(0, 0);
        runInFxThread(
                () ->
                        assertThat(Clipboard.getSystemClipboard().getContent(DataFormat.PLAIN_TEXT))
                                .isEqualTo("Content"));
    }

    @Test
    @Disabled("Not able to interact with context menus")
    void rightClickListViewItemAndSelectDelete_itemIsDeleted()
            throws ExecutionException, InterruptedException, TimeoutException {
        MainView.getInstance().listView.addItem("Content");
        MainView.getInstance().listView.rightClickCellAndSelectMenuItem(0, 1);
        MainView.getInstance().listView.assertEmpty();
    }
}
