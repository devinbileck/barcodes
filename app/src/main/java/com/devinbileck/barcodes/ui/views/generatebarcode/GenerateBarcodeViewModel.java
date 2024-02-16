package com.devinbileck.barcodes.ui.views.generatebarcode;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.stereotype.Component;

import com.google.zxing.BarcodeFormat;

import com.devinbileck.barcodes.barcode.BarcodeResult;

@Component
public class GenerateBarcodeViewModel {
    private final ObservableList<BarcodeFormatListItem> availableBarcodeFormats;
    private final ObjectProperty<BarcodeFormatListItem> specifiedFormat = new SimpleObjectProperty<>();
    private final ObjectProperty<String> specifiedContent = new SimpleObjectProperty<>();
    private final ObjectProperty<Optional<BarcodeResult>> generatedResult =
            new SimpleObjectProperty<>(Optional.empty());
    private final SimpleBooleanProperty canGenerate = new SimpleBooleanProperty();

    public GenerateBarcodeViewModel() {
        availableBarcodeFormats = FXCollections.observableArrayList(
                Arrays.stream(BarcodeFormat.values()).map(BarcodeFormatListItem::new).toList());
        specifiedContent.setValue("");
        canGenerate.bind(
                Bindings.createBooleanBinding(() -> !specifiedContent.get().isEmpty() && specifiedFormat.get() != null,
                        specifiedContent, specifiedFormat));
    }

    public SimpleBooleanProperty canGenerate() {
        return canGenerate;
    }

    public ObservableList<BarcodeFormatListItem> getAvailableBarcodeFormats() {
        return availableBarcodeFormats;
    }

    public ObjectProperty<BarcodeFormatListItem> getSpecifiedFormat() {
        return specifiedFormat;
    }

    public void setSpecifiedFormat(final BarcodeFormatListItem barcodeFormatListItem) {
        specifiedFormat.setValue(barcodeFormatListItem);
    }

    public ObjectProperty<String> getSpecifiedContent() {
        return specifiedContent;
    }

    public void setSpecifiedContent(final String specifiedContent) {
        this.specifiedContent.setValue(specifiedContent);
    }

    public ObjectProperty<Optional<BarcodeResult>> getGeneratedResult() {
        return generatedResult;
    }

    public void setGeneratedResult(@NotNull final Optional<BarcodeResult> barcodeResult) {
        Objects.requireNonNull(barcodeResult);
        generatedResult.setValue(barcodeResult);
    }
}
