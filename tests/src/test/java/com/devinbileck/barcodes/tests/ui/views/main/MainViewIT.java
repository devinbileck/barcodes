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
import org.testfx.framework.junit5.ApplicationExtension;

import com.devinbileck.barcodes.components.views.main.MainView;
import com.devinbileck.barcodes.components.views.scanbarcode.ScanBarcodeView;
import com.devinbileck.barcodes.tests.ui.views.ViewIT;

@ExtendWith(ApplicationExtension.class)
class MainViewIT extends ViewIT {

    @BeforeEach
    void setup() throws ExecutionException, InterruptedException, TimeoutException {
        MainView.getInstance().listView.clearItems();
    }

    @Test
    void fieldsDisplayCorrectText() {
        MainView.getInstance().scanBarcodeButton.assertHasText("Scan Barcode");
        MainView.getInstance().clearScannedContentButton.assertHasText("Clear Scanned Content");
    }

    @Test
    void whenNoContent_clearButtonIsDisabled() {
        MainView.getInstance().listView.assertEmpty();
        MainView.getInstance().clearScannedContentButton.assertIsDisabled();
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
        MainView.getInstance().listView.addItem("Scanned content");
        MainView.getInstance().listView.assertNotEmpty();
        MainView.getInstance().clearScannedContentButton.assertIsEnabled();
        MainView.getInstance().clearScannedContentButton.click();
        MainView.getInstance().listView.assertEmpty();
        MainView.getInstance().clearScannedContentButton.assertIsDisabled();
    }

    @Test
    @Disabled("Not able to interact with context menus")
    void rightClickListViewItem_showsContextMenu()
            throws ExecutionException, InterruptedException, TimeoutException {
        MainView.getInstance().listView.addItem("Scanned content");
        MainView.getInstance().listView.rightClickCell(0);
        MainView.getInstance().listView.assertContextMenuShown(0, "Copy", "Delete");
        MainView.getInstance().listView.closeContextMenu(0);
    }

    @Test
    @Disabled("Not able to interact with context menus")
    void rightClickListViewItemAndSelectCopy_contentCopiedToClipboard()
            throws ExecutionException, InterruptedException, TimeoutException {
        runInFxThread(() -> Clipboard.getSystemClipboard().clear());
        MainView.getInstance().listView.addItem("Scanned content");
        MainView.getInstance().listView.rightClickCellAndSelectMenuItem(0, 0);
        runInFxThread(
                () ->
                        assertThat(Clipboard.getSystemClipboard().getContent(DataFormat.PLAIN_TEXT))
                                .isEqualTo("Scanned content"));
    }

    @Test
    @Disabled("Not able to interact with context menus")
    void rightClickListViewItemAndSelectDelete_itemIsDeleted()
            throws ExecutionException, InterruptedException, TimeoutException {
        MainView.getInstance().listView.addItem("Scanned content");
        MainView.getInstance().listView.rightClickCellAndSelectMenuItem(0, 1);
        MainView.getInstance().listView.assertEmpty();
    }
}
