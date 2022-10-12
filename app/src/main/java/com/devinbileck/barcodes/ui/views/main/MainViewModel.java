package com.devinbileck.barcodes.ui.views.main;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.springframework.stereotype.Component;

@Component
public class MainViewModel {
    private final ObservableList<String> scannedContent = FXCollections.observableArrayList();

    public ObservableList<String> getScannedContent() {
        return scannedContent;
    }

    public void addScannedContent(@NotNull final String content) {
        Objects.requireNonNull(content, "Content must not be null");
        scannedContent.add(content);
    }

    public void clearScannedContent() {
        scannedContent.clear();
    }
}
