package com.devinbileck.barcodes.ui.views.main;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.stereotype.Component;

@Component
public class MainViewModel {
    private final ObservableList<BarcodeListItem> barcodes = FXCollections.observableArrayList();

    public ObservableList<BarcodeListItem> getBarcodes() {
        return barcodes;
    }

    public void addBarcode(@NotNull final BarcodeListItem barcode) {
        Objects.requireNonNull(barcode, "barcode must not be null");
        this.barcodes.add(barcode);
    }

    public void clearBarcodes() {
        barcodes.clear();
    }
}
