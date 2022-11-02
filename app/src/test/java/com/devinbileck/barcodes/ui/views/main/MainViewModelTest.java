package com.devinbileck.barcodes.ui.views.main;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.zxing.BarcodeFormat;

class MainViewModelTest {
    private final MainViewModel mainViewModel = new MainViewModel();

    @Test
    void uponInitialization_barcodeListIsEmpty() {
        assertTrue(mainViewModel.getBarcodes().isEmpty());
    }

    @Test
    void addingNullBarcode_exceptionIsThrown() {
        //noinspection ConstantConditions
        assertThrows(NullPointerException.class, () -> mainViewModel.addBarcode(null));
    }

    @Test
    void addingBarcode_contentIsPersisted() {
        List<BarcodeListItem> barcodesToAdd =
                List.of(new BarcodeListItem(BarcodeFormat.QR_CODE, "first content", Instant.now().toEpochMilli(), null),
                        new BarcodeListItem(BarcodeFormat.QR_CODE, "second content", Instant.now().toEpochMilli(),
                                null),
                        new BarcodeListItem(BarcodeFormat.QR_CODE, "third content", Instant.now().toEpochMilli(),
                                null));

        barcodesToAdd.forEach(mainViewModel::addBarcode);

        List<BarcodeListItem> barcodes = mainViewModel.getBarcodes();
        assertThat(barcodes).hasSameElementsAs(barcodesToAdd);
    }

    @Test
    void clearingBarcodes_barcodesAreCleared() {
        mainViewModel.addBarcode(
                new BarcodeListItem(BarcodeFormat.QR_CODE, "content", Instant.now().toEpochMilli(), null));

        mainViewModel.clearBarcodes();

        assertThat(mainViewModel.getBarcodes()).isEmpty();
    }
}
